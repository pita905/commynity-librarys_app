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
    LibraryDatabaseHelper dbHelper;

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

        dbHelper = new LibraryDatabaseHelper(this);

        // Get the library ID from the intent
        int libraryId = getIntent().getIntExtra("library_id", -1);
        if (libraryId == -1) {
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

                // Add book to database
                boolean success = dbHelper.addBookToLibraryWithImage(libraryId, title, author, imageString);
                if (success) {
                    Toast.makeText(AddBookActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the previous activity
                } else {
                    Toast.makeText(AddBookActivity.this, "Error adding book. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Select Image button click
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to select an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Convert the selected image to a Bitmap
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Display the image in the ImageView
                imgBookCover.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
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
