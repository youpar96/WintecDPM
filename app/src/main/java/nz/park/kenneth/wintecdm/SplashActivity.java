package nz.park.kenneth.wintecdm;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.TableClients;
import nz.park.kenneth.wintecdm.database.TableModules;
import nz.park.kenneth.wintecdm.database.TablePathwayModule;
import nz.park.kenneth.wintecdm.database.TablePathways;
import nz.park.kenneth.wintecdm.database.TableStudentPathway;
import nz.park.kenneth.wintecdm.database.TableStudents;

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
    }




    public void moveToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
