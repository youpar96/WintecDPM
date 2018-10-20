package nz.park.kenneth.wintecdm.database;

import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

import nz.park.kenneth.wintecdm.database.Structure.TableClients;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathwayModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathways;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;

public class CreateTables implements Closeable{

    SQLiteDatabase _db;

    public CreateTables(Closeable db){
        this._db = (SQLiteDatabase) db;


        createModules();
        createPathways();
        createStudents();
        createPathwayModule();
    }

    private void createStudents() {
        String queryStudents =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.Students +
                        " (" + TableStudents.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        " " + TableStudents.COLUMN_ID_WINTEC + " INTEGER NOT NULL," +
                        " " + TableStudents.COLUMN_NAME + " VARCHAR NOT NULL," +
                        " " + TableStudents.COLUMN_DEGREE + " VARCHAR NOT NULL," +
                        " " + TableStudents.COLUMN_PHOTO + " BLOB)";

        _db.execSQL(queryStudents);
    }

    private void createModules() {
        String queryModules =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.Modules.toString() +
                        "(" + TableModules.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        TableModules.COLUMN_NAME + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CODE + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CREDITS + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_PREREQ + " VARCHAR  NULL, " +
                        TableModules.COLUMN_DETAILS + " TEXT, " +
                        TableModules.COLUMN_SEMESTER + " INTEGER NOT NULL," +
                        TableModules.COLUMN_LEVEL + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_URL + " VARCHAR)";

        _db.execSQL(queryModules);
    }

    private void createPathways() {


        String queryPathways =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.Pathways.toString() +
                        "(" + TablePathways.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        TablePathways.COLUMN_NAME + " VARCHAR NOT NULL)";

        _db.execSQL(queryPathways);
    }

    private void createClients() {
//        String queryClients =
//                "CREATE TABLE IF NOT EXISTS " + TableClients.TABLE_NAME +
//                        "(" + TableClients.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
//                        TableClients.COLUMN_NAME + " VARCHAR NOT NULL)";
//
//        _db.execSQL(queryClients);
    }

    private void createPathwayModule() {
        String queryPathwayModule =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.PathwayModules.toString() +
                        "(" + TablePathwayModules.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TablePathwayModules.COLUMN_ID_PATHWAY + " INTEGER NOT NULL, " +
                        TablePathwayModules.COLUMN_ID_MODULE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + TablePathwayModules.COLUMN_ID_PATHWAY + ") REFERENCES " + DBHelper.Tables.Pathways + "(" + TablePathways.COLUMN_ID + "), " +
                        "FOREIGN KEY(" + TablePathwayModules.COLUMN_ID_MODULE + ") REFERENCES " + DBHelper.Tables.Modules + "(" + TableModules.COLUMN_ID + "))";

        _db.execSQL(queryPathwayModule);
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


    @Override
    public void close() throws IOException {
        _db.close();
    }
}
