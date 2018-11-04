package nz.park.kenneth.wintecdm.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

public class PopulateTables implements Closeable {

    private SQLiteDatabase _dbHelper;
    private static final String PACKAGE = "nz.park.kenneth.wintecdm.database."; //common package path
    private static final String TAG = "Populate data"; //log stuff
    private static final String DATA_ATTRIBUTE = "content"; //Property name in each data file
    private static final String STRUCTURE = "Structure.Table";
    private static final String DATA = "Data.";

    public PopulateTables(Closeable db) {

        _dbHelper = (SQLiteDatabase) db;

        populate(DBHelper.Tables.Modules);
        populate(DBHelper.Tables.Students);
        populate(DBHelper.Tables.Pathways);
        populate(DBHelper.Tables.PathwayModules);
        populate(DBHelper.Tables.PreRequisites);


    }


    private boolean Validate(String table) {

        boolean _return = false;
        Cursor c = _dbHelper.rawQuery("SELECT * FROM " + table + " Limit 2", null);
        c.moveToFirst();
        _return = c.moveToFirst();
        c.close();
        return _return;
    }

    public void populate(DBHelper.Tables table) {

        String _tableName = table.toString();
        try {

            if (!Validate(_tableName)) {
                Class<?> _class = Class.forName(String.format("%s%s%s", PACKAGE, STRUCTURE, _tableName));
                Class _dataClass = Class.forName(String.format("%s%s%s", PACKAGE, DATA, _tableName));
                Field _datafield = _dataClass.getDeclaredField(DATA_ATTRIBUTE);
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
            Log.d(TAG, "populate: exception" + e.getMessage());
        }

    }

    @Override
    public void close() throws IOException {
        _dbHelper.close();
    }


}
