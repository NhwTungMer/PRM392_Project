package com.example.prm_project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private ListView weightList;
    private DatabaseReference databaseReference;
    private ArrayList<String> historyList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize views
        weightList = findViewById(R.id.weightList);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("BMIHistory");

        // Initialize history list and adapter
        historyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        weightList.setAdapter(adapter);

        // Load history from Firebase
        loadHistory();
    }

    private void loadHistory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map != null) {

                        String historyItem = "Weight: " + map.get("weight") + ", Height: " + map.get("height") +
                                ", BMI: "+ map.get("bmi")
                               +"\n BMI Category: " +map.get("bmiCategory")+
                                ", Date: " + map.get("date");
                        historyList.add(historyItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
