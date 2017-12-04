package com.example.elisa.kissmekate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewUser extends AppCompatActivity {
    double[] numbers;
    private DatabaseManager dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
        dbHelper = new DatabaseManager(this);
        dbHelper.open();
    }

    public void saveUser(View view) {
        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        EditText surename = (EditText) findViewById(R.id.surnameInput);
        EditText index = (EditText) findViewById(R.id.indexInput);
        String nameImputString = nameInput.getText().toString();
        String surenameString = surename.getText().toString();
        int indexInt = 0;

        if (nameImputString == null || surenameString == null || index.getText().toString() == null) {
            Toast.makeText(this, getResources().getString(R.string.emptyfields), Toast.LENGTH_LONG).show();
            Log.d("SAVE", "EMPTY");
        } else if (nameImputString.equals("") || surenameString.equals("") || index.getText().toString().equals("")) {
            Toast.makeText(this, getResources().getString(R.string.emptyfields), Toast.LENGTH_LONG).show();
            // makeNotification();
            Log.d("SAVE", "EMPTY2");
        } else {
            if (numbers != null) {
                indexInt = Integer.parseInt(index.getText().toString());
                Log.d("SAVE", "Added");

                Cursor response = dbHelper.fetchUser(indexInt);
                if (response.getCount()==0) {

                    dbHelper.insertUser(nameImputString, surenameString, indexInt, numbers);
                    Toast.makeText(this, "User added!", Toast.LENGTH_LONG).show();
                    nameInput.setText("");
                    surename.setText("");
                    index.setText("");
                    numbers=null;
                } else {
                    Toast.makeText(this, "A user with this transcript number already exists!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.noLips), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openCam(View view) {
        Intent i = new Intent(this, GetUserLips.class);
        //startActivityForResult(i,1);
        startActivityForResult(i, 10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 10) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                numbers = data.getExtras().getDoubleArray("numbers");
                //    Log.d("NUMB", Double.toString(numbers[0]));
            } else {
                Log.d("NUMB", "FAIL");
            }
        }
    }

}
