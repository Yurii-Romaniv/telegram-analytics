package com.example.telegram_analytics;

import static android.provider.DocumentsContract.EXTRA_INITIAL_URI;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.example.telegram_analytics.entities.AnalyticsDTO;
import com.example.telegram_analytics.entities.ParsedChat;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.usersList);
        Button filePickerButton = findViewById(R.id.filePickerButton);


        filePickerButton.setOnClickListener(view -> openFile());
    }


    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");

        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, EXTRA_INITIAL_URI);
        DocumentsContractResultLauncher.launch(intent);
    }


    ActivityResultLauncher<Intent> DocumentsContractResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();

                try {
                    ParsedChat parsedChat = DataExtractor.getParsedChat(MainActivity.this, uri);
                    AnalyticsCollect analyticsCollect = new AnalyticsCollect(parsedChat.getMessages());
                    List<AnalyticsDTO> analyticsDTOs = analyticsCollect.getAnalytics();

                    ArrayAdapter<AnalyticsDTO> adapter = new AnalyticsAdapter(this, R.layout.list_analytics, analyticsDTOs);
                    listView.setAdapter(adapter);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
}


