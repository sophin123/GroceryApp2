package com.example.groceryapp.MainAcitivty.mainpage.mainpage.secondactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryapp.R;

public class Main2Activity extends AppCompatActivity {

    DatabaseHelper myDB;
    Button btnAdd, btnView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText = findViewById(R.id.editText);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        myDB = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if(editText.length()!= 0){
                    AddData(newEntry);
                    editText.setText("");
                }else {
                    Toast.makeText(Main2Activity.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();

                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, viewlistcontents.class);
                startActivity(intent);
            }
        });

    }

    private void AddData(String newEntry) {

        boolean insertData = myDB.addData(newEntry);

        if(insertData==true){
            Toast.makeText(this,"Data Successfully Inserted!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :( ", Toast.LENGTH_LONG).show();
        }
    }


}
