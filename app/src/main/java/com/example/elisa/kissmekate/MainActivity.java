package com.example.elisa.kissmekate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openLogIn(View view) {
       openNewActivity(LogIn.class);
    }

    public void openNewActivity(Class myclass){
        Intent i = new Intent(this, myclass);
        startActivity(i);
    }

    public void openNewUser(View view) {
        openNewActivity(AddNewUser.class);
    }

    public void openInfo(View view) {
        openNewActivity(Info.class);
    }
}
