package com.example.elisa.kissmekate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
    }

    public void saveUser(View view) {
        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        EditText surename=(EditText)findViewById(R.id.surnameInput);
        EditText index= (EditText) findViewById(R.id.indexInput);
        String nameImputString=nameInput.getText().toString();
        String surenameString=surename.getText().toString();
        int indexInt=0;

        if(nameImputString==null||surenameString==null||index.getText().toString()==null){
            Toast.makeText(this,getResources().getString(R.string.emptyfields),Toast.LENGTH_LONG);
            makeNotification();

        }
        else if(nameImputString.equals("")||surenameString.equals("")||index.getText().toString().equals("")){
            Toast.makeText(this,getResources().getString(R.string.emptyfields),Toast.LENGTH_LONG);
           // makeNotification();

        }
        else{
            indexInt= Integer.parseInt(index.getText().toString());

            Toast.makeText(this,"User added!",Toast.LENGTH_LONG);

            User newUser = new User(nameImputString,surenameString,indexInt);
            makeNotification();

        }
    }
    private void makeNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.background);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        mBuilder.build();

    }
}
