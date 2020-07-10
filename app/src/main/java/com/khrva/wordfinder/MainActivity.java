package com.khrva.wordfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        var Submit = (Button) findViewById(R.id.Submit);
        var EnterString = (EditText) findViewById(R.id.enterLetters);
        var Language = (Switch) findViewById(R.id.languageSwitch);
        var Result = (TextView) findViewById(R.id.Result);
        var TextBox = (TextView) findViewById(R.id.textBox);



    }
}
