package nz.park.kenneth.wintecdm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DialogActivity extends Activity {

    Button btnDialogOk, btnDialogCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);

        btnDialogCancel = findViewById(R.id.btnDialogCancel);
        btnDialogOk = findViewById(R.id.btnDialogOk);

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }
}
