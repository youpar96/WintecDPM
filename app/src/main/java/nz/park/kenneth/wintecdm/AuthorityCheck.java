package nz.park.kenneth.wintecdm;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AuthorityCheck extends Activity {

    Button btnAuthOk, btnAuthCancel;
    EditText authPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authority_check);

        btnAuthOk = findViewById(R.id.btnAuthOk);
        btnAuthCancel = findViewById(R.id.btnAuthCancel);

        authPassword = findViewById(R.id.authPassword);

        btnAuthOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = authPassword.getText().toString();

                // checking password
                if(CheckAuthority.check(password)){
                    Intent i = new Intent(getApplicationContext(), SelectPathway.class);
                    i.putExtra("userType", "M");
                    startActivity(i);
                    finish();
                }
            }
        });

        btnAuthCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
