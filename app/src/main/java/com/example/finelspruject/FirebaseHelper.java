package com.example.finelspruject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Firebase implementation to replace the SQLite DatabaseHelper for user management
 */
public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    
    // Firebase instances
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;
    
    // Collection names
    private static final String USERS_COLLECTION = "users";
    
    // Singleton instance
    private static FirebaseHelper instance;
    
    /**
     * Get the singleton instance of FirebaseHelper
     */
    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }
    
    /**
     * Private constructor
     */
    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        
        // Disable all verification features that might require SHA-1
        try {
            // Disable reCAPTCHA verification for testing
            FirebaseAuth.getInstance().getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(true);
        } catch (Exception e) {
            Log.e(TAG, "Error disabling verification: " + e.getMessage());
        }
    }
    
    /**
     * Register a new user
     * @param name The user's name
     * @param email The user's email
     * @param username The user's username
     * @param password The user's password
     * @param listener A listener to handle the result
     */
    public void registerUser(String name, String email, String username, String password, 
                             final OnRegistrationListener listener) {
        // For simplicity and to avoid Google Play Services issues, we'll use email/password directly
        // without checking username first
        
        // Create user with email/password
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional user data to Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);
                            userData.put("username", username);
                            
                            try {
                                mFirestore.collection(USERS_COLLECTION)
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        listener.onRegistrationComplete(true, "Registration successful");
                                    })
                                    .addOnFailureListener(e -> {
                                        listener.onRegistrationComplete(false, 
                                            "Registration partial: User created but profile data failed: " + e.getMessage());
                                    });
                            } catch (Exception e) {
                                Log.e(TAG, "Firestore error: " + e.getMessage());
                                listener.onRegistrationComplete(true, 
                                    "Registration partial: User created but profile data failed");
                            }
                        }
                    } else {
                        Exception exception = task.getException();
                        String message = exception != null ? exception.getMessage() : "Unknown error";
                        Log.e(TAG, "Registration failed: " + message, exception);
                        listener.onRegistrationComplete(false, "Registration failed: " + message);
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Firebase Auth error: " + e.getMessage());
            listener.onRegistrationComplete(false, "Registration failed: " + e.getMessage());
        }
    }
    
    /**
     * Validate user credentials for login
     * @param email The user's email
     * @param password The user's password
     * @param listener A listener to handle the result
     */
    public void validateUser(String email, String password, final OnLoginListener listener) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            listener.onLoginComplete(true, "Login successful", user.getUid());
                        } else {
                            listener.onLoginComplete(false, "Login failed: User is null", null);
                        }
                    } else {
                        Exception exception = task.getException();
                        String message = exception != null ? exception.getMessage() : "Unknown error";
                        Log.e(TAG, "Login failed: " + message, exception);
                        listener.onLoginComplete(false, "Login failed: " + message, null);
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Firebase Auth error during login: " + e.getMessage());
            listener.onLoginComplete(false, "Login failed: " + e.getMessage(), null);
        }
    }
    
    /**
     * Check if a username already exists
     * @param username The username to check
     * @param listener A listener to handle the result
     */
    public void checkUserExists(String username, final OnUserExistsCheckListener listener) {
        mFirestore.collection(USERS_COLLECTION)
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean exists = !task.getResult().isEmpty();
                    listener.onUserExistsCheck(exists);
                } else {
                    Log.w(TAG, "Error checking username", task.getException());
                    listener.onUserExistsCheck(false); // Default to false if there's an error
                }
            });
    }
    
    /**
     * Get the user details from Firestore
     * @param uid The user ID
     * @param listener A listener to handle the result
     */
    public void getUserDetails(String uid, final OnUserDetailsListener listener) {
        mFirestore.collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            HashMap<String, String> userDetails = new HashMap<>();
                            userDetails.put("username", document.getString("username"));
                            userDetails.put("email", document.getString("email"));
                            userDetails.put("name", document.getString("name"));
                            listener.onUserDetailsRetrieved(true, userDetails);
                        } else {
                            listener.onUserDetailsRetrieved(false, null);
                        }
                    } else {
                        listener.onUserDetailsRetrieved(false, null);
                    }
                }
            });
    }
    
    /**
     * Get the email address for a given username
     * @param username The username to look up
     * @param listener A listener to handle the result
     */
    /**
     * Get user details by username
     * @param username The username to look up
     * @param listener A listener to handle the result
     */
    public void getUserDetailsByUsername(String username, final OnUserDetailsListener listener) {
        Log.d(TAG, "Getting user details for username: " + username);
        mFirestore.collection(USERS_COLLECTION)
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // Get the first document (should be only one with this username)
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    
                    HashMap<String, String> userDetails = new HashMap<>();
                    userDetails.put("username", document.getString("username"));
                    userDetails.put("email", document.getString("email"));
                    userDetails.put("name", document.getString("name"));
                    
                    listener.onUserDetailsRetrieved(true, userDetails);
                } else {
                    Exception exception = task.getException();
                    String message = exception != null ? exception.getMessage() : "User not found";
                    Log.e(TAG, "Error retrieving user details: " + message);
                    listener.onUserDetailsRetrieved(false, new HashMap<>());
                }
            });
    }
    
    public void getEmailByUsername(String username, final OnEmailRetrievedListener listener) {
        mFirestore.collection(USERS_COLLECTION)
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // Get the first document (should be only one with this username)
                    String email = task.getResult().getDocuments().get(0).getString("email");
                    if (email != null) {
                        listener.onEmailRetrieved(true, email);
                    } else {
                        Log.e(TAG, "Email not found for username: " + username);
                        listener.onEmailRetrieved(false, null);
                    }
                } else {
                    Exception exception = task.getException();
                    String message = exception != null ? exception.getMessage() : "User not found";
                    Log.e(TAG, "Error retrieving email: " + message);
                    listener.onEmailRetrieved(false, null);
                }
            });
    }
    
    /**
     * Sign out the current user
     */
    public void signOut() {
        mAuth.signOut();
    }
    
    /**
     * Get the current signed-in user ID
     * @return The current user ID or null if no user is signed in
     */
    public String getCurrentUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }
    
    // Interfaces for callbacks
    public interface OnRegistrationListener {
        void onRegistrationComplete(boolean success, String message);
    }
    
    public interface OnLoginListener {
        void onLoginComplete(boolean success, String message, String userId);
    }
    
    public interface OnUserExistsCheckListener {
        void onUserExistsCheck(boolean exists);
    }
    
    public interface OnUserDetailsListener {
        void onUserDetailsRetrieved(boolean success, HashMap<String, String> userDetails);
    }
    
    /**
     * Listener interface for email retrieval by username
     */
    public interface OnEmailRetrievedListener {
        void onEmailRetrieved(boolean success, String email);
    }
}
