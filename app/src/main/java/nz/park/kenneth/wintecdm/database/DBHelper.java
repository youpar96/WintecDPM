package nz.park.kenneth.wintecdm.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;



public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "wintec_dpm.db";
    private static final Integer DB_VERSION = 7;

    public enum Tables {Modules, Pathways, PathwayModules, Students, StudentPathway}
    private SQLiteDatabase _dbHelper;


    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {

        super(context, DB_NAME, factory, DB_VERSION);

        //context.deleteDatabase(DB_NAME); for test purpose
        this._dbHelper = getInstance();

        SelectAllModules(); //test
    }

    public SQLiteDatabase getInstance() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        new CreateTables(db);
        new PopulateTables(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    //Test Method
    public List<?> SelectAllModules() {

        List<?> _returnList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + Tables.Modules, null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex(TableModules.COLUMN_ID)) != null) {
                    int _id = c.getInt(c.getColumnIndex(TableModules.COLUMN_ID));
                    //_returnList.add(_id);
                }
                c.moveToNext();
            }
        }
        db.close();
        return _returnList;
    }







}
