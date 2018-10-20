package nz.park.kenneth.wintecdm.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nz.park.kenneth.wintecdm.Structure;
import nz.park.kenneth.wintecdm.database.Data.PreRequisites;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePreRequisites;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;


public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "wintec_dpm.db";
    private static final Integer DB_VERSION = 1;

    public enum Tables {Modules, Pathways, PathwayModules, Students, StudentPathway, PreRequisites}

    private SQLiteDatabase _dbHelper;


    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {

        super(context, DB_NAME, factory, DB_VERSION);

       // context.deleteDatabase(DB_NAME); //for test purpose
        this._dbHelper = getInstance();

    }

    private SQLiteDatabase getInstance() {
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


    private Cursor ExecuteQuery(String query, String... params) {
        return _dbHelper.rawQuery(query, params);
    }


    //without pre-req
    public List<?> GetAllModules() {
        _dbHelper = getReadableDatabase();
        Cursor c = ExecuteQuery("select * from " + Tables.Modules);
        return SelectAllModules(c);
    }


    //Modules
    //implement where params later
    private List<TableModules> SelectAllModules(Cursor c) {

        List<TableModules> _returnList = new ArrayList<TableModules>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex(TableModules.COLUMN_ID)) != null) {

                    int _id = c.getInt(c.getColumnIndex(TableModules.COLUMN_ID));
                    String _name = c.getString(c.getColumnIndex(TableModules.COLUMN_NAME));
                    String _code = c.getString(c.getColumnIndex(TableModules.COLUMN_CODE));
                    int _credits = c.getInt(c.getColumnIndex(TableModules.COLUMN_CREDITS));
                    String _details = c.getString(c.getColumnIndex(TableModules.COLUMN_DETAILS));
                    int _level = c.getInt(c.getColumnIndex(TableModules.COLUMN_LEVEL));
                    int _sem = c.getInt(c.getColumnIndex(TableModules.COLUMN_SEMESTER));
                    String _url = c.getString(c.getColumnIndex(TableModules.COLUMN_URL));

                    //pre-req change
                    Cursor _prereqCursor = ExecuteQuery("select * from " + Tables.PreRequisites + " where " +
                            TablePreRequisites.COLUMN_CODE + "='" + _code + "';");

                    List<TablePreRequisites> _prereqs = null;
                    if (_prereqCursor.moveToFirst()) {

                        _prereqs = new ArrayList<TablePreRequisites>();

                        do {
                            String _prereqCode = _prereqCursor.getString(_prereqCursor.getColumnIndex(TablePreRequisites.COLUMN_PREREQ));
                            int _prereqCombination = _prereqCursor.getInt(_prereqCursor.getColumnIndex(TablePreRequisites.COLUMN_COMBINATION));

                            _prereqs.add(new TablePreRequisites(_prereqCode, _prereqCombination == 1));

                        }
                        while (_prereqCursor.moveToNext());

                    }
                    _prereqCursor.close();

                    _returnList.add(new TableModules(_id, _name, _code, _credits, _details, _sem, _level, _url,_prereqs));

                }
                c.moveToNext();
            }
        }
        _dbHelper.close();

        c.close();
        return _returnList;
    }


    public boolean InsertModule(TableModules module) {
        //if id exists then update

        return true;
    }

    private boolean UpdateModule(TableModules module) {

        return true;
    }

    public boolean DeleteModule(TableModules modules) {

        return true;
    }


    //Students
    //add where

    public List<?> GetAllStudents() {
        _dbHelper = getReadableDatabase();
        Cursor c = ExecuteQuery("select * from " + Tables.Students);
        return SelectStudents(c);
    }

    public List<?> GetStudentByWintecId(int id) {
        _dbHelper = getReadableDatabase();
        Cursor c = ExecuteQuery("select * from " + Tables.Students + " where " + TableStudents.COLUMN_ID_WINTEC + "=?", String.valueOf(id));
        return SelectStudents(c);
    }


    private List<TableStudents> SelectStudents(Cursor c) {

        List<TableStudents> _returnList = new ArrayList<TableStudents>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex(TableStudents.COLUMN_ID)) != null) {

                    int _id = c.getInt(c.getColumnIndex(TableStudents.COLUMN_ID));
                    String _name = c.getString(c.getColumnIndex(TableStudents.COLUMN_NAME));
                    int _wintec_id = c.getInt(c.getColumnIndex(TableStudents.COLUMN_ID_WINTEC));
                    String _degree = c.getString(c.getColumnIndex(TableStudents.COLUMN_DEGREE));
                    String _photo = c.getString(c.getColumnIndex(TableStudents.COLUMN_PHOTO));

                    _returnList.add(new TableStudents(_id, _wintec_id, _name, _degree, _photo));

                }
                c.moveToNext();
            }
        }

        return _returnList;
    }


}
