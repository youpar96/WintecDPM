package nz.park.kenneth.wintecdm.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ActionPermission {

    public static boolean validatePermissions(String[] permissions, Activity activity, int requestCode){

        // Is necessary to check version because versions before 22 doesn't need to validate permission, only greater than or equal 23
        if(Build.VERSION.SDK_INT >= 23){

            List<String> listPermissions = new ArrayList<>();

            for(String permission: permissions){

                Boolean hasPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;

                if(!hasPermission) listPermissions.add(permission);
            }

            if(listPermissions.isEmpty()) return true;

            String[] newsPermissions = new String[listPermissions.size()];
            listPermissions.toArray(newsPermissions);

            ActivityCompat.requestPermissions(activity, newsPermissions, requestCode);
        }

        return true;
    }
}
