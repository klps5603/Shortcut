package com.example.shortcut_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shortcut_demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.createTextShortcutButton.setOnClickListener(this);
        handleShortcut(getIntent());
    }

    private void handleShortcut(Intent intent) {
        if (intent != null) {
            String shortcutId=intent.getStringExtra("shortcutId");
            if (shortcutId != null && shortcutId.equals(getString(R.string.shortcut))) {
                Toast.makeText(this, getString(R.string.openShortcut), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        ShortcutHelper.getInstance(this)
                .setIcon(R.drawable.ic_shortcut_svg)
                .setShortcutInfoId(getString(R.string.shortcut))
                .setShortLabel(getString(R.string.shortcut))
                .build();
    }
}
