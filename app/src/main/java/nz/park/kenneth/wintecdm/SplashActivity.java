package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static int TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToMainActivity();
            }
        }, TIME_OUT);
    }

    public void moveToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
