package nz.park.kenneth.wintecdm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import nz.park.kenneth.wintecdm.helper.ActionPermission;

public class MainActivity extends AppCompatActivity {

    CardView cvStudent, cvManager;

    // Permissions to access camera and load pictures from phone
    private String[] permissionsNeeded = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Validating permissions to access camera and load pictures from phone
        ActionPermission.validatePermissions(permissionsNeeded, this, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvStudent = findViewById(R.id.cvStudent);
        cvManager = findViewById(R.id.cvManager);

        cvStudent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Change the SelectPathway screen to SelectPathwayActivity
                //Intent i = new Intent(getApplicationContext(), SelectPathway.class);
                // 30.10.2018 - Change Activities to fragments, using NavigationMainActivity for navigation drawer
                // Intent i = new Intent(getApplicationContext(), SelectPathwayActivity.class);
                Intent i = new Intent(getApplicationContext(), NavigationMainActivity.class);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissionResult : grantResults){

            if(permissionResult == PackageManager.PERMISSION_DENIED) {

                alertValidationPermission();
            }
        }
    }

    private void alertValidationPermission() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle("Permission denied");
        builder.setMessage("To use this app is necessary allow the permissions!");
        builder.setCancelable(false);
        /*builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Just use to close the activity
                //finish();

            }
        });*/

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //finish();

                Intent it = new Intent(MainActivity.this, MainActivity.class);
                startActivity(it);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
