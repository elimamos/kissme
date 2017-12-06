package com.example.elisa.kissmekate;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {
double [] numbers;
    private DatabaseManager dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        dbHelper = new DatabaseManager(this);
        dbHelper.open();
    }
    int index;
    public void openCam(View view) {
        EditText et=(EditText)findViewById(R.id.indexInput);
        if(!et.getText().toString().equals("")) {
            index= Integer.parseInt(et.getText().toString());

            Cursor response = dbHelper.fetchUser(index);
            if (response.getCount() == 0) {
                Toast.makeText(this, "No such user registered! Please add user!", Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(this, GetUserLips.class);
                startActivityForResult(i, 11);
            }
        }
        else{
            Toast.makeText(this, "Please fill in transcript number!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 11){
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                numbers = data.getExtras().getDoubleArray("numbers");
                    Log.d("NUMB", Double.toString(numbers[0]));
                Intent i= new Intent(this,Response.class);
                i.putExtra("numbers",numbers);
                i.putExtra("index",index);
                startActivity(i);
            } else {
                Log.d("NUMB", "FAIL");
            }
        }
    }
}
