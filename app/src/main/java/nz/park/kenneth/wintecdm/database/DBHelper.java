package nz.park.kenneth.wintecdm.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "wintec_dpm.db";
    private static Context myContext;
    private static final Integer DB_VERS = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERS);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TableStudents.TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TableStudents.TABLE_NAME, null);
    }

    private void createTableStudents(SQLiteDatabase db)
    {
        String queryStudents =
            "CREATE TABLE IF NOT EXISTS " + TableStudents.TABLE_NAME +
            " (" + TableStudents.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            " " + TableStudents.COLUMN_ID_WINTEC + " INTEGER NOT NULL," +
            " " + TableStudents.COLUMN_NAME + " VARCHAR NOT NULL," +
            " " + TableStudents.COLUMN_DEGREE + " VARCHAR NOT NULL," +
            " " + TableStudents.COLUMN_PHOTO + " BLOB)";

        db.execSQL(queryStudents);
    }
}
