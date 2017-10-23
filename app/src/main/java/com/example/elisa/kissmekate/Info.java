package com.example.elisa.kissmekate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

    }

    public void goToAbout(View view) {
        TextView tv = (TextView) findViewById(R.id.about);
        NestedScrollView scroll = (NestedScrollView) findViewById(R.id.scroll);

        scroll.scrollTo(0,tv.getTop());
        Log.d("About","Requested focus!");
    }

    public void goToManual(View view) {
        TextView tv = (TextView) findViewById(R.id.userManual);
        NestedScrollView scroll = (NestedScrollView) findViewById(R.id.scroll);

        scroll.scrollTo(0,tv.getTop());
    }
}
