package com.example.bazededate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditStoryActivity extends AppCompatActivity {
    private EditText editText;
    private FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);

        editText = findViewById(R.id.editStory);
        saveButton = findViewById(R.id.saveButton);

        String extras = getIntent().getExtras().getString("story");

        editText.setText(extras);
        ;

       saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("value", value);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}