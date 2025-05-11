package com.example.finelspruject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText edtBookTitle, edtBookAuthor;
    ImageView imgBookCover;
    Button btnAddBook, btnSelectImage, btnCancel;
    FirebaseLibraryHelper firebaseHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private Bitmap selectedBitmap = null; // Store the selected image as a Bitmap
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // Check for necessary permissions
        checkAndRequestPermissions();
        // Initialize UI components
        edtBookTitle = findViewById(R.id.edtBookTitle);
        edtBookAuthor = findViewById(R.id.edtBookAuthor);
        imgBookCover = findViewById(R.id.imgBookCover);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnCancel = findViewById(R.id.btnCancel);

        firebaseHelper = FirebaseLibraryHelper.getInstance();

        // Get the library ID from the intent
        String libraryId = getIntent().getStringExtra("library_id");
        if (libraryId == null || libraryId.isEmpty()) {
            Toast.makeText(this, "Invalid library ID. Cannot add book.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Handle Add Book button click
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtBookTitle.getText().toString().trim();
                String author = edtBookAuthor.getText().toString().trim();

                if (title.isEmpty() || author.isEmpty()) {
                    Toast.makeText(AddBookActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String imageString = null;
                if (selectedBitmap != null) {
                    imageString = convertBitmapToBase64(selectedBitmap); // Convert the image to a Base64 string
                }

                // Add book to database using Firebase
                firebaseHelper.addBookToLibraryWithImage(libraryId, title, author, imageString, new FirebaseLibraryHelper.OnBookAddedListener() {
                    @Override
                    public void onBookAdded(boolean success, String message) {
                        if (success) {
                            Toast.makeText(AddBookActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to the previous activity
                        } else {
                            Toast.makeText(AddBookActivity.this, "Error adding book: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Handle Select Image button click
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Offer choice to the user: Camera or Gallery
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddBookActivity.this);
                builder.setTitle("Select Image")
                        .setItems(new String[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                            if (which == 0) {
                                // Take a photo
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                                }
                            } else {
                                // Pick from gallery
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                            }
                        })
                        .show();
            }
        });

        // Handle Cancel button click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to the previous activity
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri imageUri = data.getData();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imgBookCover.setImageBitmap(selectedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                selectedBitmap = (Bitmap) extras.get("data"); // Get thumbnail
                imgBookCover.setImageBitmap(selectedBitmap);
            }
        }
    }


    // Convert a Bitmap to a Base64 string
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void checkAndRequestPermissions() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
            }, REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied! The app may not function correctly.", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
