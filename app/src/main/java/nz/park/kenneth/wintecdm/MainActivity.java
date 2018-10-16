package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    CardView cvStudent, cvManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvStudent = findViewById(R.id.cvStudent);
        cvManager = findViewById(R.id.cvManager);

        cvStudent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), SelectPathway.class);
                i.putExtra("userType", "S");
                startActivity(i);
            }
        });

        cvManager.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // before moving to the activity to select pathway, must check the password
                Intent i = new Intent(getApplicationContext(), AuthorityCheck.class);
                startActivity(i);
            }
        });
    }
}
