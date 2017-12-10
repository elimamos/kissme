package com.example.elisa.kissmekate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Response extends AppCompatActivity {
double[] numbers;
    int index;
boolean isOK;
    private DatabaseManager dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        dbHelper = new DatabaseManager(this);
        dbHelper.open();
        Intent intent = getIntent();
        numbers=  intent.getDoubleArrayExtra("numbers");
        index=intent.getExtras().getInt("index");
        Cursor response = dbHelper.fetchUser(index);
        for(int i =0; i<numbers.length;i++) {
            double percentage =countDifference(response.getDouble(4+i),numbers[i]);
            if(percentage>20){
                Log.d("NUMB","Crossed value! " + i + percentage);
                isOK=false;
                break;
            }else{
                Log.d("NUMB","OK " + i);
                isOK=true;
        }
        }
        TextView info =(TextView) findViewById(R.id.info);
        TextView nameL =(TextView) findViewById(R.id.nameL);
        TextView name =(TextView) findViewById(R.id.name);
        TextView snameL =(TextView) findViewById(R.id.snameL);
        TextView sname =(TextView) findViewById(R.id.surename);
        TextView transL =(TextView) findViewById(R.id.transL);
        TextView trans =(TextView) findViewById(R.id.transcript);
        if(isOK){
            info.setText("Congratulations you logged in as:");
            nameL.setVisibility(View.VISIBLE);
            name.setText(response.getString(1));
            snameL.setVisibility(View.VISIBLE);
            sname.setText(response.getString(2));
            transL.setVisibility(View.VISIBLE);
            trans.setText(response.getString(3));

    }
        else{
            info.setText("Couldn't log user in! Please try again.");
        }
    }

double  countDifference(double user1,double tryingUser){
    double difference =Math.abs(user1-tryingUser);
    double percentage=(difference/user1)*100;
    return percentage;
}


}