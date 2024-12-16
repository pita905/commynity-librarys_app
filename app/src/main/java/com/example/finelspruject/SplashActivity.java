package com.example.finelspruject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Define the duration for the splash screen (in milliseconds)
    private static final int SPLASH_DURATION = 3000; // 3000ms = 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the parent class's onCreate method
        setContentView(R.layout.activity_splash); // Set the splash screen layout

        // Use a Handler to delay the navigation to the HomeActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to move from SplashActivity to HomeActivity
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent); // Start HomeActivity

                finish(); // Finish SplashActivity to prevent it from appearing when pressing back
            }
        }, SPLASH_DURATION); // Duration of delay (3 seconds)
    }
}
