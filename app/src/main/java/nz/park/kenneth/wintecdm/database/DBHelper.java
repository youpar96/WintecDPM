package nz.park.kenneth.wintecdm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.database.Structure.TableClients;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathwayModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathways;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;


public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "wintec_dpm.db";
    private static final Integer DB_VERSION = 4;

    private enum Tables {Modules, Pathways, PathwayModules, Students, StudentPathway}


    private static final String PACKAGE = "nz.park.kenneth.wintecdm.database.";
    private SQLiteDatabase _dbHelper;

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {

        super(context, DB_NAME, factory, DB_VERSION);
        this._dbHelper = getInstance();

        SelectAllModules(); //Test stuff
    }

    public SQLiteDatabase getInstance() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this._dbHelper = db;
        createDatabase();
        populateDatabase();
    }


    public void createDatabase() {
        try {
            createModules();
            createPathways();
            createStudents();
            createPathwayModule();
//            createStudentPathway(); later

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public Cursor getAllStudents() {
        return _dbHelper.rawQuery("SELECT * FROM " + Tables.Students, null);
    }

    private void createStudents() {
        String queryStudents =
                "CREATE TABLE IF NOT EXISTS " + Tables.Students +
                        " (" + TableStudents.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        " " + TableStudents.COLUMN_ID_WINTEC + " INTEGER NOT NULL," +
                        " " + TableStudents.COLUMN_NAME + " VARCHAR NOT NULL," +
                        " " + TableStudents.COLUMN_DEGREE + " VARCHAR NOT NULL," +
                        " " + TableStudents.COLUMN_PHOTO + " BLOB)";

        _dbHelper.execSQL(queryStudents);
    }

    private void createModules() {
        String queryModules =
                "CREATE TABLE IF NOT EXISTS " + Tables.Modules.toString() +
                        "(" + TableModules.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        TableModules.COLUMN_NAME + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CODE + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CREDITS + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_PREREQ + " VARCHAR  NULL, " +
                        TableModules.COLUMN_DETAILS + " TEXT, " +
                        TableModules.COLUMN_SEMESTER + " INTEGER NOT NULL," +
                        TableModules.COLUMN_LEVEL + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_URL + " VARCHAR)";

        _dbHelper.execSQL(queryModules);
    }

    private void createPathways() {


        String queryPathways =
                "CREATE TABLE IF NOT EXISTS " + Tables.Pathways.toString() +
                        "(" + TablePathways.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        TablePathways.COLUMN_NAME + " VARCHAR NOT NULL)";

        _dbHelper.execSQL(queryPathways);
    }

    private void createClients() {
        String queryClients =
                "CREATE TABLE IF NOT EXISTS " + TableClients.TABLE_NAME +
                        "(" + TableClients.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        TableClients.COLUMN_NAME + " VARCHAR NOT NULL)";

        _dbHelper.execSQL(queryClients);
    }

    private void createPathwayModule() {
        String queryPathwayModule =
                "CREATE TABLE IF NOT EXISTS " + Tables.PathwayModules.toString() +
                        "(" + TablePathwayModules.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TablePathwayModules.COLUMN_ID_PATHWAY + " INTEGER NOT NULL, " +
                        TablePathwayModules.COLUMN_ID_MODULE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + TablePathwayModules.COLUMN_ID_PATHWAY + ") REFERENCES " + Tables.Pathways + "(" + TablePathways.COLUMN_ID + "), " +
                        "FOREIGN KEY(" + TablePathwayModules.COLUMN_ID_MODULE + ") REFERENCES " + Tables.Modules + "(" + TableModules.COLUMN_ID + "))";

        _dbHelper.execSQL(queryPathwayModule);
    }

    private void createStudentPathway() {
//        String queryStudentPathway =
//                "CREATE TABLE IF NOT EXISTS " + TableStudentPathway.TABLE_NAME +
//                        "(" + TableStudentPathway.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
//                        TableStudentPathway.COLUMN_ID_STUDENT + " INTEGER NOT NULL, " +
//                        TableStudentPathway.COLUMN_ID_PATHWAY + " INTEGER NOT NULL, " +
//                        TableStudentPathway.COLUMN_COMPLETED + " BOOLEAN, " +
//                        "FOREIGN KEY(" + TableStudentPathway.COLUMN_ID_STUDENT + ") REFERENCES " + TableStudents.TABLE_NAME + "(" + TableStudents.COLUMN_ID + "), " +
//                        "FOREIGN KEY(" + TableStudentPathway.COLUMN_ID_PATHWAY + ") REFERENCES " + TablePathways.TABLE_NAME + "(" + TablePathways.COLUMN_ID + "))";
//
//        _dbHelper.execSQL(queryStudentPathway);
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

    public void populateDatabase() {
        try {


            populate(Tables.Modules);
            populate(Tables.Students);
            populate(Tables.Pathways);
            populate(Tables.PathwayModules);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean Validate(String table) {

        boolean _return = false;
        Cursor c = _dbHelper.rawQuery("SELECT * FROM " + table + " Limit 5", null);
        c.moveToFirst();
        _return = c.moveToFirst();
        c.close();
        return _return;
    }

    public void populate(Tables table) {


        String _tableName = table.toString();
        try {

            if (!Validate(_tableName)) {
                Class<?> _class = Class.forName(String.format("%sStructure.Table%s", PACKAGE, _tableName));
                Class _dataClass = Class.forName(String.format("%sData.%s", PACKAGE, _tableName));


                Field _datafield = _dataClass.getDeclaredField("content");
                _datafield.setAccessible(true);


                for (String[] eachModule : (String[][]) _datafield.get(_dataClass.newInstance())) {

                    ContentValues _values = new ContentValues();
                    for (Field f : _class.getDeclaredFields()) {

                        if (f.isAnnotationPresent(FieldOrder.class)) {
                            int index = f.getAnnotation(FieldOrder.class).order();
                            _values.put(f.get(_class.newInstance()).toString(), eachModule[index - 1]);
                            index++;
                        }

                    }


                    long _check = _dbHelper.insertOrThrow(_tableName, null, _values);

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
