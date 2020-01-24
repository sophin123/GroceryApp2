package com.example.groceryapp.MainAcitivty.mainpage.mainpage.GroceryActivity.NoteActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryapp.R;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText noteeditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        noteeditText = findViewById(R.id.noteEditText);
        btnAdd = findViewById(R.id.btnadd);
        btnViewData = findViewById(R.id.btnview);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = noteeditText.getText().toString();
                if(noteeditText.length() != 0){
                    AddData(newEntry);
                    noteeditText.setText("");
                }else {
                    toastMessage(getResources().getString(R.string.btnAddError));
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }

    public void AddData(String newEntry){
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if(insertData){
            toastMessage("@");
        }else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
