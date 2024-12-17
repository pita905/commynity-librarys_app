package com.example.finelspruject;

import android.content.ContentValues; // To insert data into the database
import android.content.Context;       // To get application context
import android.database.Cursor;       // To query and fetch results from the database
import android.database.sqlite.SQLiteDatabase; // SQLite database management class
import android.database.sqlite.SQLiteOpenHelper; // SQLite helper to manage database creation and updates

import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "finelsproject.db"; // Name of the database file
    private static final int DATABASE_VERSION = 1;                 // Database version (used for upgrades)

    // Table and column names
    private static final String TABLE_USERS = "users";            // Table name for storing users
    private static final String COL_USERNAME = "username";        // Column for storing usernames
    private static final String COL_PASSWORD = "password";        // Column for storing passwords

    // Constructor: Initializes the database helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate: Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the "users" table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USERNAME + " TEXT PRIMARY KEY, " + // Username as the primary key (unique)
                COL_PASSWORD + " TEXT)");             // Password column to store user passwords

        // Insert a default user into the database (e.g., admin user for testing purposes)
        db.execSQL("INSERT INTO " + TABLE_USERS + " VALUES ('admin', 'admin123')");
    }

    // onUpgrade: Called when the database version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old "users" table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Recreate the database by calling onCreate
        onCreate(db);
    }


    // Method to validate a user's credentials during login.
     // username The username entered by the user.
     //password The password entered by the user.
     //return True if the credentials match a user in the database, false otherwise.

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Open the database in read-only mode

        // SQL query to check if a user exists with the provided username and password
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}); // Replace placeholders with actual values

        // Check if the query returned any results (i.e., a match exists)
        boolean isValid = cursor.getCount() > 0;

        cursor.close(); // Close the cursor to release resources
        return isValid; // Return true if credentials are valid, false otherwise
    }


     // Method to register a new user by inserting their credentials into the database.
     //username The desired username.
     //password The desired password.
     //return True if the user was successfully registered, false otherwise.

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Open the database in write mode

        // Create a ContentValues object to store column values
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username); // Insert the username
        contentValues.put(COL_PASSWORD, password); // Insert the password

        // Insert the new user data into the "users" table
        long result = db.insert(TABLE_USERS, null, contentValues);

        // If result is -1, the insertion failed; otherwise, it was successful
        return result != -1;
    }


     //Method to check if a username already exists in the database.
     //username The username to check.
     //return True if the username exists, false otherwise.

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase(); // Open the database in read-only mode

        // SQL query to check if the username already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + COL_USERNAME + "=?", new String[]{username});

        // Check if the query returned any results (i.e., username exists)
        boolean exists = cursor.getCount() > 0;

        cursor.close(); // Close the cursor to release resources
        return exists; // Return true if the username exists, false otherwise
    }


     //Method to log all users in the database (for debugging purposes).
     //Logs the username and password to the Logcat.
     
    public void logAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase(); // Open the database in read-only mode

        // SQL query to select all records from the "users" table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        // Check if the table is empty
        if (cursor.getCount() == 0) {
            android.util.Log.d("Database", "No data found in users table.");
        } else {
            // Iterate through each row and log the username and password
            while (cursor.moveToNext()) {
                android.util.Log.d("Database", "Username: " + cursor.getString(0) +
                        ", Password: " + cursor.getString(1));
            }
        }

        cursor.close(); // Close the cursor to release resources
    }
    public HashMap<String, String> getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> userDetails = new HashMap<>();

        // Query to get user details
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            userDetails.put("username", cursor.getString(cursor.getColumnIndexOrThrow("username")));
            userDetails.put("email", cursor.getString(cursor.getColumnIndexOrThrow("email"))); // Example column
            userDetails.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));   // Example column
        }
        cursor.close();
        db.close();
        return userDetails;
    }

}
