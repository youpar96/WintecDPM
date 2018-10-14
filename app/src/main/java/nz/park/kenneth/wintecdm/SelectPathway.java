package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

public class SelectPathway extends AppCompatActivity implements View.OnClickListener {

    CardView cvSoftware, cvDatabase, cvNetworking, cvWebDevelopment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pathway);

        Intent i = new Intent(getApplicationContext(), DialogActivity.class);
        startActivity(i);

        cvSoftware = findViewById(R.id.cvSoftware);
        cvDatabase = findViewById(R.id.cvDatabase);
        cvNetworking = findViewById(R.id.cvNetworking);
        cvWebDevelopment = findViewById(R.id.cvWebDevelopment);

        cvSoftware.setOnClickListener(this);
        cvDatabase.setOnClickListener(this);
        cvNetworking.setOnClickListener(this);
        cvWebDevelopment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, DetailPathway.class);
        String pathWay = null;

        switch (view.getId()){
            case R.id.cvSoftware:
                pathWay = "S";
                break;
            case R.id.cvDatabase:
                pathWay = "D";
                break;
            case R.id.cvNetworking:
                pathWay = "N";
                break;
            case R.id.cvWebDevelopment:
                pathWay = "W";
                break;
            default:
                break;
        }

        i.putExtra("pathWay", pathWay);
        startActivity(i);
    }
}
