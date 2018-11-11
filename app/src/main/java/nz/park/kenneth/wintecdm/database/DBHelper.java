package nz.park.kenneth.wintecdm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nz.park.kenneth.wintecdm.Profile;
import nz.park.kenneth.wintecdm.Structure;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Data.PreRequisites;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathwayModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePreRequisites;
import nz.park.kenneth.wintecdm.database.Structure.TableStudentPathway;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;


public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "wintec_dpm.db";
    private static final Integer DB_VERSION = 1;

    public enum Tables {Modules, Pathways, PathwayModules, Students, StudentPathway, PreRequisites}

    private SQLiteDatabase _dbHelper;
    private static final String TAG = "DBHelper";

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {

        super(context, DB_NAME, factory, DB_VERSION);

        //context.deleteDatabase(DB_NAME); //for test purpose
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

        //Test purpose
        db.delete(Tables.Modules.toString(), null, null);
        db.delete(Tables.PathwayModules.toString(), null, null);
        db.delete(Tables.PreRequisites.toString(), null, null);

        onCreate(db);
    }


    private Cursor ExecuteQuery(String query, String... params) {
        _dbHelper = getReadableDatabase();
        return _dbHelper.rawQuery(query, params);
    }


    //Modules
    //stuff to do --format code

    private ArrayList<TableModules> SelectAllModules(Cursor c) {

        ArrayList<TableModules> _returnList = new ArrayList<>();

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

                    boolean _is_completed = false;
                    if (!c.isNull(c.getColumnIndex(TableStudentPathway.COLUMN_IS_COMPLETED))) {
                        int _value = c.getInt(c.getColumnIndex(TableStudentPathway.COLUMN_IS_COMPLETED));
                        _is_completed = _value == 1;

                    }

                    _returnList.add(new TableModules(_id, _name, _code, _credits, _details, _sem, _level, _url, _is_completed));

                }
                c.moveToNext();
            }
        }
        _dbHelper.close();

        c.close();
        return _returnList;
    }


    //stuff to do --implement where params
//    public List<?> GetAllModules() {
//        _dbHelper = getReadableDatabase();
//        Cursor c = ExecuteQuery("select * from " + Tables.Modules);
//        return SelectAllModules(c);
//    }

    //Particular pathway all modules
    public ArrayList<TableModules> GetModules(Pathways.PathwayEnum path, String... studentID) {

        _dbHelper = getReadableDatabase();

        String _query = "select m.*,sp.is_completed from " + Tables.Modules + " m inner join " + Tables.PathwayModules + " pm on substr(m." + TableModules.COLUMN_CODE + ",1,7) = substr(pm." + TablePathwayModules.COLUMN_ID_MODULE + ",1,7) " +
                " left join " + Tables.StudentPathway + " sp on substr(sp." + TableStudentPathway.COLUMN_MODULE + ",1,7)=substr(pm." + TablePathwayModules.COLUMN_ID_MODULE + ",1,7) " +
                "and sp.student_id=" + ((studentID.length >= 1) ? studentID[0] : "sp.student_id") +
                " where pm." + TablePathwayModules.COLUMN_ID_PATHWAY + " IN (0,?) order by " + TableModules.COLUMN_SEMESTER + "," + TableModules.COLUMN_CODE;


        Cursor c = ExecuteQuery(_query, String.valueOf(path.ordinal()));
        return SelectAllModules(c);
    }

    public ArrayList<TableModules> GetModulesByPathway(Pathways.PathwayEnum path) {
        return GetModules(path);
    }


    //custom student pathway
    public List<TableModules> GetModulesForStudent(int ID) {

        List<TableModules> _modules, _modulesCopy;
        //Get pathway from student ID
        int _pathway = GetStudentByWintecId(ID).get_pathway();
        _modules = _modulesCopy = GetModules(Pathways.PathwayEnum.values()[_pathway], String.valueOf(ID));
        List<TablePreRequisites> _prereqs = GetAllPreRequisites(Pathways.PathwayEnum.values()[_pathway]);


        //Each modules for the pathway
        criteria:
        for (int i = 0; i < _modules.size(); i++) {

            boolean _is_enabled = false;
            String _currentModule = _modules.get(i).get_code();

            ArrayList<Boolean> _eachCriteria = new ArrayList<>();
            String _prerequisite = null;

            //pathway based pre-requisites
            for (TablePreRequisites prereq : _prereqs) {

                if (_currentModule.equals(prereq.get_code())) {

                    _prerequisite = prereq.get_prereqcode();

                    //find if prerequisite module has been completed or not
                    for (TableModules _eachModule : _modules) {

                        if (_prerequisite.equals(_eachModule.get_code())) {
                            boolean is_completed = _eachModule.get_is_completed();
                            if (!prereq.is_is_combo()) {
                                _modules.get(i).set_is_enabled(is_completed);
                                break criteria;
                            } else
                                _eachCriteria.add(is_completed);
                        }

                    }
                }
            }

            _modules.get(i).set_is_enabled(Arrays.asList(_eachCriteria).contains(true) || _prerequisite == null);

        }


        return _modules;
    }


    private ContentValues ModuleContentValues(TableModules module) {
        ContentValues _values = new ContentValues();

        if (module.get_name() != null)
            _values.put(TableModules.COLUMN_NAME, module.get_name());
        if (module.get_code() != null)
            _values.put(TableModules.COLUMN_CODE, module.get_code());
        if (module.get_credits() != 0)
            _values.put(TableModules.COLUMN_CREDITS, module.get_credits());
        if (module.get_details() != null)
            _values.put(TableModules.COLUMN_DETAILS, module.get_details());
        if (module.get_sem() != 0)
            _values.put(TableModules.COLUMN_SEMESTER, module.get_sem());
        if (module.get_level() != 0)
            _values.put(TableModules.COLUMN_LEVEL, module.get_level());
        if (module.get_url() != null)
            _values.put(TableModules.COLUMN_URL, module.get_url());
        if (module.get_is_completed())
            _values.put(TableModules.COLUMN_URL, module.get_is_completed());

        return _values;

    }

    public boolean InsertModule(TableModules module) {
        //if id exists then update
        long _row = 0;

        try {
            _dbHelper = getWritableDatabase();
            _row = _dbHelper.insert(Tables.Modules.toString(), null, ModuleContentValues(module));
        } catch (SQLException ex) {
            Log.d(TAG, "InsertModule: " + ex.toString());
        }
        return _row > 0;
    }

    //By ID
    private boolean UpdateModuleByID(TableModules module) {
        int count = 0;
        try {
            _dbHelper = getWritableDatabase();
            count = _dbHelper.update(Tables.Modules.toString(), ModuleContentValues(module), TableModules.COLUMN_ID + " where ?", new String[]{String.valueOf(module.get_id())});
        } catch (SQLException ex) {
            //log
        }
        return count > 0;
    }

    public boolean DeleteModuleByCode(String code) {
        try {
            _dbHelper = getWritableDatabase();
            _dbHelper.delete(Tables.Modules.toString(), TableModules.COLUMN_ID + " where " + TableModules.COLUMN_CODE + "= ?", new String[]{code});
        } catch (SQLException ex) {
            //log
        }
        return true;
    }


    //Students
    //add where


    public List<?> GetAllStudents() {
        _dbHelper = getReadableDatabase();
        Cursor c = ExecuteQuery("select * from " + Tables.Students);
        return SelectStudents(c);
    }

    public TableStudents GetStudentByWintecId(int id) {
        _dbHelper = getReadableDatabase();
        Cursor c = ExecuteQuery("select * from " + Tables.Students + " where " + TableStudents.COLUMN_ID_WINTEC + "=?", String.valueOf(id));
        return SelectStudents(c).get(0);
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
                    int _pathway = c.getInt(c.getColumnIndex(TableStudents.COLUMN_PATHWAY));
                    _returnList.add(new TableStudents(_id, _wintec_id, _name, _degree, _photo, _pathway));

                }
                c.moveToNext();
            }
        }
        c.close();
        return _returnList;
    }


    //prerequisites


    public List<TablePreRequisites> GetAllPreRequisites(Pathways.PathwayEnum path) {

        Cursor _prereqCursor = ExecuteQuery("select * from " + Tables.PreRequisites + " where " + TablePreRequisites.COLUMN_STREAM
                + "=?", String.valueOf(path.ordinal()));

        List<TablePreRequisites> _prereqs = new ArrayList<TablePreRequisites>();
        if (_prereqCursor.moveToFirst()) {

            do {

                String _moduleCode = _prereqCursor.getString(_prereqCursor.getColumnIndex(TablePreRequisites.COLUMN_CODE));
                String _prereqCode = _prereqCursor.getString(_prereqCursor.getColumnIndex(TablePreRequisites.COLUMN_PREREQ));
                int _prereqCombination = _prereqCursor.getInt(_prereqCursor.getColumnIndex(TablePreRequisites.COLUMN_COMBINATION));

                _prereqs.add(new TablePreRequisites(_moduleCode, _prereqCode, _prereqCombination == 1));

            }
            while (_prereqCursor.moveToNext());

        }
        _prereqCursor.close();


        return _prereqs;
    }


    //StudentPathway Save

    public void InsertPathway() {
        _dbHelper = this.getWritableDatabase();

        for (TableModules _each : Profile.modules) {

            ContentValues _values = new ContentValues();

            int _studentID = Profile.studentid;
            _values.put(TableStudentPathway.COLUMN_STUDENT_ID, _studentID);
            _values.put(TableStudentPathway.COLUMN_MODULE, _each.get_code());
            _values.put(TableStudentPathway.COLUMN_IS_COMPLETED, _each.get_is_completed());


            long id = _dbHelper.insertWithOnConflict(Tables.StudentPathway.toString(), null, _values, SQLiteDatabase.CONFLICT_IGNORE);

            if (id == -1) {
                _dbHelper.update(Tables.StudentPathway.toString(), _values,
                        TableStudentPathway.COLUMN_STUDENT_ID + "=? and " + TableStudentPathway.COLUMN_MODULE + "=?",
                        new String[]{String.valueOf(_studentID), _each.get_code()});

            }


        }

    }

}
