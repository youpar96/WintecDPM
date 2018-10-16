package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static Context context;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor ed;

    private static final String PREF_NAME = "User";
    private static final String IS_ADMIN = "is_admin";

    public Session(Context context){
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        ed=sp.edit();

        this.context=context;

    }

    public void AddValues(boolean editPrivilege){
        ed.putBoolean(IS_ADMIN, editPrivilege);
        ed.commit();

    }

    public static boolean IsAdmin(){
        return sp.getBoolean(IS_ADMIN, false);

    }

    //Implement later
    public void Clear(){
        ed.clear();
        ed.commit();
    }


}
