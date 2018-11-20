package nz.park.kenneth.wintecdm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
import nz.park.kenneth.wintecdm.database.Structure.TablePathways;
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

        // context.deleteDatabase(DB_NAME); //for test purpose
        this._dbHelper = getInstance();

    }

    private SQLiteDatabase getInstance() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        new CreateTables(db); //create every tables
        new PopulateTables(db); //populate data
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

        List<TableModules> _modules = new ArrayList<>();
        //Get pathway from student ID
        try {
            int _pathway = GetStudentByWintecId(ID).get_pathway();
            _modules = GetModules(Pathways.PathwayEnum.values()[_pathway], String.valueOf(ID));
            List<TablePreRequisites> _prereqs = GetAllPreRequisites(Pathways.PathwayEnum.values()[_pathway]);


            //Each modules for the pathway
            for (int i = 0; i < _modules.size(); i++) {

                boolean _is_enabled = false;
                String _currentModule = _modules.get(i).get_code().trim();

                if (_currentModule.replace('\u00a0', ' ').equals("COMP601​")) {
                    int gh = 0;
                }
                ArrayList<Boolean> _eachCriteria = new ArrayList<>();
                String _prerequisite = null;

                //pathway based pre-requisites
                boolean _hasCombinationOfPrereqs = false;
                criteria:
                for (TablePreRequisites prereq : _prereqs) {

                    if (_currentModule.contains(prereq.get_code())) {
                        _prerequisite = prereq.get_prereqcode().replaceAll("\\P{Print}", "");

                        //find if prerequisite module has been completed or not
                        for (TableModules _eachModule : _modules) {
                            // String test = _eachModule.get_code().replaceAll("\\P{Print}", "");
                            if (_eachModule.get_code().contains(_prerequisite)) {
                                boolean is_completed = _eachModule.get_is_completed();
                                _hasCombinationOfPrereqs = prereq.is_is_combo();
                                _eachCriteria.add(is_completed);


                                if (!_hasCombinationOfPrereqs && is_completed)
                                    break criteria;

                            }
                        }
                    }
                }

                _modules.get(i).set_is_enabled(
                        (_eachCriteria.contains(true) && !_hasCombinationOfPrereqs)
                                || (!_eachCriteria.contains(false) && _hasCombinationOfPrereqs)
                                || _prerequisite == null
                                || _eachCriteria.size() == 0
                );
            }

        } catch (Exception e) {

        }
        return _modules;
    }


    public boolean InsertPathwayModules(TablePathwayModules module) {
        long _row = 0;

        try {
            _dbHelper = getWritableDatabase();

            ContentValues _values = new ContentValues();
            _values.put(TablePathwayModules.COLUMN_ID_PATHWAY, module.getPathway());
            _values.put(TablePathwayModules.COLUMN_ID_MODULE, module.getModule());
            _row = _dbHelper.insertWithOnConflict(Tables.PathwayModules.toString(), null, _values, SQLiteDatabase.CONFLICT_IGNORE);

            if (_row == -1) {
                _dbHelper.update(Tables.PathwayModules.toString(), _values, TablePathwayModules.COLUMN_ID_PATHWAY + "=? and " + TablePathwayModules.COLUMN_ID_MODULE + "=?",
                        new String[]{String.valueOf(module.getPathway()), String.valueOf(module.getModule())}
                );
            }

        } catch (SQLException ex) {
            Log.d(TAG, "Insert PathwayModules: " + ex.toString());
        }
        return _row > 0;
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


    public TableModules SelectModuleDetails(String code) {

        TableModules tableModules = new TableModules();
        try {
            _dbHelper = getReadableDatabase();
            Cursor c = ExecuteQuery("select * from " + Tables.Modules + " where " +
                    TableModules.COLUMN_CODE + "=?", code.trim());

            List<TableModules> _value = SelectAllModules(c);
            tableModules = _value.get(0);
            c.close();
        } catch (Exception ex) {
            Log.d(TAG, "SelectModuleDetails " + ex.toString());
        }
        return tableModules;

    }


    public boolean InsertModule(TableModules module) {
        //if id exists then update
        long _row = 0;

        try {
            _dbHelper = getWritableDatabase();

            ContentValues _values = ModuleContentValues(module);

            _row = _dbHelper.insertWithOnConflict(Tables.Modules.toString(), null, _values, SQLiteDatabase.CONFLICT_IGNORE);

            if (_row == -1) {
                _dbHelper.update(Tables.Modules.toString(), _values, TableModules.COLUMN_CODE + "=?",
                        new String[]{String.valueOf(module.get_code())});

            }

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
            Log.d(TAG, "Fn UpdateModuleByID " + ex.toString());
        }
        return count > 0;
    }


    //prereq
    //pathwaymodules
    //modules
    public boolean DeleteModuleByCode(String code) {

        int _return = 0;
        try {
            _dbHelper = getWritableDatabase();

            _dbHelper.delete(Tables.PreRequisites.toString(), TablePreRequisites.COLUMN_CODE + "=?", new String[]{code});
            _dbHelper.delete(Tables.PathwayModules.toString(), TablePathwayModules.COLUMN_ID_MODULE + "=?", new String[]{code});

            _return = _dbHelper.delete(Tables.Modules.toString(), TableModules.COLUMN_CODE + "= ?", new String[]{code});

        } catch (SQLException ex) {
            Log.d(TAG, "Fn DeleteModuleByCode " + ex.toString());
        }
        return _return > 0;
    }


    //Students
    //add where
    public boolean InsertStudentProfile(TableStudents student) {
        //if id exists then update
        long _row = 0;

        try {
            _dbHelper = getWritableDatabase();

            ContentValues _values = StudentContentValues(student);

            _row = _dbHelper.insertWithOnConflict(Tables.Students.toString(), null, _values, SQLiteDatabase.CONFLICT_IGNORE);

            if (_row == -1) {
                _row = _dbHelper.update(Tables.Students.toString(), _values, TableStudents.COLUMN_ID_WINTEC + "=?",
                        new String[]{String.valueOf(student.get_wintec_id())});

            }
        } catch (SQLException e) {
            Log.d(TAG, "InsertStudent: " + e.toString());
        }

        return _row > 0;
    }

    public boolean UpdateStudentByID(TableStudents student) {
        int count = 0;
        try {
            _dbHelper = getWritableDatabase();

            ContentValues _values = StudentContentValues(student);
            count = _dbHelper.update(Tables.Students.toString(), _values, TableStudents.COLUMN_ID_WINTEC + " = ?", new String[]{String.valueOf(student.get_wintec_id())});

            //count = _dbHelper.update(Tables.Students.toString(), StudentContentValues(student), TableStudents.COLUMN_ID + " where ?", new String[]{String.valueOf(student.get_id())});
        } catch (SQLException ex) {
            Log.d(TAG, "Fn UpdateStudentByID " + ex.toString());
        }
        return count > 0;
    }

    public List<?> GetAllStudents() {
        _dbHelper = getReadableDatabase();
        Cursor c = ExecuteQuery("select * from " + Tables.Students);
        return SelectStudents(c);
    }

    public TableStudents GetStudentById(int id) {

        TableStudents _studentDetail = new TableStudents();

        try {
            _dbHelper = getReadableDatabase();
            Cursor c = ExecuteQuery("select * from " + Tables.Students + " where " + TableStudents.COLUMN_ID + "=?", String.valueOf(id));
            _studentDetail = SelectStudents(c).get(0);
        } catch (Exception ex) {

        }

        return _studentDetail;
    }

    public TableStudents GetStudentByWintecId(int id) {

        TableStudents _studentDetail = new TableStudents();

        try {
            _dbHelper = getReadableDatabase();
            Cursor c = ExecuteQuery("select * from " + Tables.Students + " where " + TableStudents.COLUMN_ID_WINTEC + "=?", String.valueOf(id));
            _studentDetail = SelectStudents(c).get(0);
        } catch (Exception ex) {

        }

        return _studentDetail;
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
                    byte[] _photo = c.getBlob(c.getColumnIndex(TableStudents.COLUMN_PHOTO));
                    int _pathway = c.getInt(c.getColumnIndex(TableStudents.COLUMN_PATHWAY));
                    String _email = c.getString(c.getColumnIndex(TableStudents.COLUMN_EMAIL));

                    _returnList.add(new TableStudents(_id, _wintec_id, _name, _degree, _photo, _pathway, _email));

                }
                c.moveToNext();
            }
        }
        c.close();
        return _returnList;
    }


    private ContentValues StudentContentValues(TableStudents student) {
        ContentValues _values = new ContentValues();

        // id wintec
        if (student.get_wintec_id() != 0)
            _values.put(TableStudents.COLUMN_ID_WINTEC, student.get_wintec_id());

        // name
        if (student.get_name() != null)
            _values.put(TableStudents.COLUMN_NAME, student.get_name());

        //degree
        if (student.get_degree() != null)
            _values.put(TableStudents.COLUMN_DEGREE, student.get_degree());

        // photo
        if (student.get_photo() != null && student.get_photo().length > 0)
            _values.put(TableStudents.COLUMN_PHOTO, student.get_photo());

        // pathway
        if (student.get_pathway() != 0)
            _values.put(TableStudents.COLUMN_PATHWAY, student.get_pathway());

        // email
        if (student.get_email() != null)
            _values.put(TableStudents.COLUMN_EMAIL, student.get_email());

        return _values;

    }

    //prerequisites


    public List<TablePreRequisites> GetAllPreRequisites(Pathways.PathwayEnum path) {

        Cursor _prereqCursor = ExecuteQuery("select * from " + Tables.PreRequisites + " where " + TablePreRequisites.COLUMN_STREAM
                + " IN (0,?)", String.valueOf(path.ordinal()));

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


    public List<TablePreRequisites> GetPreRequisites(int pathway, String moduleCode) {

        Cursor _prereqCursor = ExecuteQuery("select * from " + Tables.PreRequisites
                + " where " + TablePreRequisites.COLUMN_STREAM
                + " IN (0,?) AND "
                + TablePreRequisites.COLUMN_CODE
                + " = ?",String.valueOf(pathway), moduleCode.trim());

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

    public boolean InsertPathway() {
        boolean result = true;

        try {
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
        } catch (Exception e) {
            result = false;
        }

        return result;

    }


    public int GetModulePathway(String module) {

        int pathway = 0;
        try {
            _dbHelper = getReadableDatabase();

            Cursor c = ExecuteQuery("select * from " + Tables.PathwayModules + " where substr(" + TablePathwayModules.COLUMN_ID_MODULE + ",1,7) =?"
                    , module.replace('\u00a0', ' ')

            );
            if (c.moveToFirst())
                pathway = c.getInt(c.getColumnIndex(TablePathwayModules.COLUMN_ID_PATHWAY));

            c.close();
        } catch (Exception ex) {
            Log.d(TAG, "GetModulePathway : " + ex.toString());
        }

        return pathway;
    }


    //Insert PreReq

    public void InsertPrereq(Pathways.PathwayEnum path, String code, String[] prereqs, boolean isCombo) {

        for (String eachPreqReq : prereqs) {

            if (!eachPreqReq.isEmpty()) {
                ContentValues _values = new ContentValues();
                _values.put(TablePreRequisites.COLUMN_STREAM, path.ordinal());
                _values.put(TablePreRequisites.COLUMN_CODE, code);
                _values.put(TablePreRequisites.COLUMN_PREREQ, eachPreqReq);
                _values.put(TablePreRequisites.COLUMN_COMBINATION, isCombo);

                try {
                    _dbHelper = getWritableDatabase();
                    long row = _dbHelper.insertWithOnConflict(Tables.PreRequisites.toString(), null, _values, SQLiteDatabase.CONFLICT_IGNORE);

                    if (row == -1) {


                    }

                } catch (SQLException ex) {
                    Log.d(TAG, "Insert PreReq: " + ex.toString());
                }
            }

        }


    }


    public List<String> ExportData(DBHelper.Tables table) {

        List<String> _values = new ArrayList<>();


        try {
            Class<?> _class = Class.forName(String.format("%s%s%s", Profile.PACKAGE, Profile.STRUCTURE, table));

            int fieldCount = 0;
            String columns = "";
            for (Field f : _class.getDeclaredFields()) {
                if (f.isAnnotationPresent(FieldOrder.class)) {
                    columns += f.get(_class.newInstance()).toString() + ",";
                    fieldCount++;
                }
            }

            columns = columns.substring(0, columns.lastIndexOf(','));

            String query = "select " + columns + " from " + table.toString();
            Cursor c = ExecuteQuery(query);

            if (c.moveToNext()) {

                while (!c.isAfterLast()) {

                    StringBuilder line = new StringBuilder();
                    int index = 0;

                    while (index <= fieldCount) {
                        //int index = f.getAnnotation(FieldOrder.class).order();
                        line.append(c.getString(index) + ",");
                        index++;
                    }
                    _values.add(line.toString());
                    c.moveToNext();
                }


            }

            c.close();
        } catch (Exception ex) {
            Log.d(TAG, "[Fn] ExportData" + ex.toString());

        } finally {


        }
        return _values;

    }


    public void ImportData(DBHelper.Tables table, List<String> data) {


        _dbHelper = this.getWritableDatabase();
        _dbHelper.delete(table.toString(), null, null); //truncate all data

        //Insert content
        _dbHelper.beginTransaction();
        try {

            Class<?> _class = Class.forName(String.format("%s%s%s", Profile.PACKAGE, Profile.STRUCTURE, table));
            for (String eachLine : data) {

                String[] record = eachLine.split(",");

                ContentValues cvalues = new ContentValues();

                int i = 0;
                for (Field f : _class.getDeclaredFields()) {

                    if (f.isAnnotationPresent(FieldOrder.class)) {
                        int index = f.getAnnotation(FieldOrder.class).order();

                        String field = f.getName();
                        cvalues.put(field, record[index]);
                        i++;
                    }

                }
                _dbHelper.insert(table.toString(), null, cvalues);

            }

            _dbHelper.setTransactionSuccessful();

        } catch (Exception ex) {
            Log.d(TAG, "[Fn] ImportData" + ex.toString());
        } finally {
            _dbHelper.endTransaction();
        }

    }

}
