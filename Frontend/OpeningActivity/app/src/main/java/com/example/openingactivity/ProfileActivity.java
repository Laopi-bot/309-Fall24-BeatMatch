package com.example.openingactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity implements Request {
    // UI elements
    TextView textGetUser, textGetEmail;

    // UI elements
    Button deleteAccountButton, deleteSecurityButton, updateAnswer2, logoutButton;
    Button adminButton;
    ExecutorService executorService;
    TextView textGetResponse;
    EditText inputAnswer1;
    EditText inputAnswer2;


    // Initialize onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Map UI elements to XML elements
        Button openPlaylistsButton = findViewById(R.id.button_playlists);

        // Set button text dynamically
        openPlaylistsButton.setText("Open Playlists");

        // Button click to navigate to PlaylistsActivity
        openPlaylistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, PlaylistsActivity.class);
                startActivity(intent);
            }
        });
        // Map UI elements to XML elements

        textGetUser = findViewById(R.id.text_get_user_ID);
        textGetEmail = findViewById(R.id.text_get_user_email);
        executorService = Executors.newSingleThreadExecutor();
        deleteAccountButton = findViewById(R.id.button_delete_account);
        deleteSecurityButton = findViewById(R.id.button_delete_security);
        textGetResponse = findViewById(R.id.text_get_response);
        updateAnswer2 = findViewById(R.id.button_answer_2_update);
        inputAnswer1 = findViewById(R.id.input_security_answer_1);
        inputAnswer2 = findViewById(R.id.input_security_answer_2);

        adminButton = findViewById(R.id.button_admin);
        adminButton.setVisibility(View.GONE);

        logoutButton = findViewById(R.id.button_profile_logout);




        /* METHOD FOR IF WE CHANGE THE RETURN FROM GET TO BE A JSON OBJECT OF USER INFO - IS CURRENTLY A STRING OF USER ID
        try {
            String result = sendRequest("GET", "/friends/" + user.getUserID(), null);
            JSONObject json = new JSONObject(result);
            int accountStatus = json.getInt("accountStatus");


            if (accountStatus == 3) {
                adminButton = findViewById(R.id.button_admin);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        */

        /**
         * This code is for the admin settings.
         * currently, the page is accessed by a specific users id, (wwinterstein@iastate.edu / verysecurepassword)
         */
        if (user.getUserID() == 72) {
            adminButton.setVisibility(View.VISIBLE);
            adminButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            });
        }



        executorService = Executors.newSingleThreadExecutor();

        // Set the user information in the TextViews
        textGetUser.setText("ID: " + user.getUserID());
        textGetEmail.setText("Email: " + user.getUserEmail());


        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.friends) {
                    Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.events) {
                    Intent intent = new Intent(ProfileActivity.this, EventsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.swiping) {
                    Intent intent = new Intent(ProfileActivity.this, SwipingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.leaderboard) {
                    Intent intent = new Intent(ProfileActivity.this, LeaderboardActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set view back to startup activity
                Intent intent = new Intent(ProfileActivity.this, StartupActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Button to send DELETE request
        deleteSecurityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //sendDeleteRequest(DEL_URL + "/forgetPassword/" + user.getUserEmail()); // old method
                        String result = sendRequest("DELETE", "/forgetPassword/" + user.getUserEmail(), null);
                    }
                });
            }
        });

        // Button to Delete account
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //sendDeleteRequest(DEL_URL + "/users/" + user.getUserID()); // old method
                        String result = sendRequest("DELETE", "/users/delete/" + user.getUserID(), null);
                        // String result2 = sendRequest("DELETE", "/users/" + user.getUserID(), null);
                        // ... rest of delete methods ...

                        Intent intent = new Intent(ProfileActivity.this, StartupActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        // Button to send PUT request
        updateAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer1 = inputAnswer1.getText().toString();
                String answer2 = inputAnswer2.getText().toString();
                String email = user.getUserEmail();

                // Create JSON object for PUT request
                JSONObject json = new JSONObject();
                try {
                    json.put("email", email);
                    json.put("ansSecurityQuestion1", answer1);
                    json.put("ansSecurityQuestion2", answer2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Send PUT request
                        //sendPutRequest(PUT_URL + "/forgetPassword/" + email, json.toString()); // old method
                        String result = sendRequest("PUT", "/forgetPassword/" + email, json.toString());
                    }
                });
            }
        });
    }
}
