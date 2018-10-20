package nz.park.kenneth.wintecdm;

import android.content.Intent;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.database.DBHelper;

public class SplashActivity extends AppCompatActivity {

    private static int TIME_OUT = 3000;
    DBHelper _database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // To delete database
        //getApplicationContext().deleteDatabase(NAME_DATABASE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToMainActivity();
            }
        }, TIME_OUT);
        _database = new DBHelper(getApplicationContext(), null);

        //sample stuff
        //List<?> _students = _database.GetStudentByWintecId(100000);

    }




    public void moveToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
