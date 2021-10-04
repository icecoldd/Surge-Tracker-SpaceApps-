package com.example.surgetracker;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.surgetracker.databinding.ActivityMapsBinding;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // contact api
                try {
                    String urlParameters = "token=" + Config.get("api_token") + "&target=" + ip + "&port=" + port + "&duration=" + duration + "&method=" + method + "&pps=500000";
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                    URL url = new URL("https://api.sleek.to/tests/launch");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Java client");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");

                    try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {

                        wr.write(postData);
                    }

                    StringBuilder content;

                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()))) {

                        String line;
                        content = new StringBuilder();

                        while ((line = br.readLine()) != null) {
                            content.append(line);
                            content.append(System.lineSeparator());
                        }
                    }

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Info - Launch Test");
                    builder.addField("API Response", content.toString(), false);
                    builder.addField("IP", ip, false);
                    builder.addField("Port", port, false);
                    builder.addField("Duration", duration, false);
                    builder.addField("Method", method, false);
                    builder.setColor(Color.RED);
                    event.getChannel().sendMessage(builder.build()).queue();

                } catch (Exception e) {
                    e.printStackTrace();
                    event.getChannel().sendMessage("**Error**: ``Check console for details - make sure you have the correct arguments``").queue();
                }

}