package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.model.Pathway;

public class Profile {

    public static boolean isAdmin = false;
    public static int studentid = 0; //Test
    public static List<TableModules> modules;


    //Test
    public static final String[] pathways = new String[]{"Common", "Networking", "Software", "Database", "Web"};

    static {
        modules = new ArrayList<>();
    }

    public static void Initialize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ProfileFragment.FILE_PREFERENCES
                , context.MODE_PRIVATE);

        if (prefs != null && prefs.contains(ProfileFragment.FILE_PREFERENCES_ID_STUDENT_KEY)) {
            studentid = prefs.getInt(ProfileFragment.FILE_PREFERENCES_ID_STUDENT_KEY,0);

        }
    }


    public static final String PACKAGE = "nz.park.kenneth.wintecdm.database.";  //common package path
    public static final String STRUCTURE = "Structure.Table";

    public static Pathways.PathwayEnum selectedPath= Pathways.PathwayEnum.Networking;

}
