package com.example.ndege.help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ndege.R;
import com.example.ndege.help.models.Help;

public class ViewHelpDetails extends AppCompatActivity {
    TextView helpTitle, helpContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_help_details);
        helpTitle = findViewById(R.id.title_of_help);
        helpContent = findViewById(R.id.content_of_help);
        Help help = HelpActivity.getHelp();
        helpTitle.setText(help.getTitle());
        helpContent.setText(help.getContent());

    }
}
