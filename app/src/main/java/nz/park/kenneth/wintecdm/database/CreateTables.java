package nz.park.kenneth.wintecdm.database;

import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

import nz.park.kenneth.wintecdm.database.Structure.TableClients;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathwayModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathways;
import nz.park.kenneth.wintecdm.database.Structure.TablePreRequisites;
import nz.park.kenneth.wintecdm.database.Structure.TableStudentPathway;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;

public class CreateTables implements Closeable {

    SQLiteDatabase _db;

    public CreateTables(Closeable db) {
        this._db = (SQLiteDatabase) db;


        createModules();
        createPathways();
        createStudents();
        createPathwayModule();
        createPreRequisites();
        createStudentPathway();
    }

    private void createStudents() {
        String queryStudents =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.Students +
                        " (" + TableStudents.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " " + TableStudents.COLUMN_ID_WINTEC + " INTEGER NOT NULL," +
                        " " + TableStudents.COLUMN_NAME + " VARCHAR NOT NULL," +
                        " " + TableStudents.COLUMN_DEGREE + " VARCHAR NOT NULL," +
                        " " + TableStudents.COLUMN_PHOTO + " BLOB," +
                        " " + TableStudents.COLUMN_PATHWAY + " INTEGER NOT NULL," +
                        " " + TableStudents.COLUMN_EMAIL + " VARCHAR," +
                        "UNIQUE(" + TableStudents.COLUMN_ID_WINTEC + ") ON CONFLICT IGNORE)";

        _db.execSQL(queryStudents);
    }

    private void createModules() {
        String queryModules =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.Modules.toString() +
                        "(" + TableModules.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TableModules.COLUMN_NAME + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CODE + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CREDITS + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_DETAILS + " TEXT, " +
                        TableModules.COLUMN_SEMESTER + " INTEGER NOT NULL," +
                        TableModules.COLUMN_LEVEL + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_URL + " VARCHAR," +
                        "UNIQUE(" + TableModules.COLUMN_CODE + ") ON CONFLICT REPLACE ) ";

        _db.execSQL(queryModules);
    }

    private void createPathways() {
        String queryPathways =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.Pathways.toString() +
                        "(" + TablePathways.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        TablePathways.COLUMN_NAME + " VARCHAR NOT NULL," +
                        "UNIQUE(" + TablePathways.COLUMN_NAME + ") ON CONFLICT REPLACE)";

        _db.execSQL(queryPathways);
    }

    private void createPreRequisites() {
        String queryClients =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.PreRequisites +
                        "(" + TablePreRequisites.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TablePreRequisites.COLUMN_STREAM + " INT NOT NULL, " +
                        TablePreRequisites.COLUMN_CODE + " VARCHAR NOT NULL, " +
                        TablePreRequisites.COLUMN_PREREQ + " VARCHAR, " +
                        TablePreRequisites.COLUMN_COMBINATION + " INT DEFAULT 0, " +
                        "FOREIGN KEY(" + TablePreRequisites.COLUMN_CODE + ") REFERENCES " + DBHelper.Tables.Modules + "(" + TableModules.COLUMN_CODE + "), " +
                        "FOREIGN KEY(" + TablePreRequisites.COLUMN_STREAM + ") REFERENCES " + DBHelper.Tables.Pathways + "(" + TablePathways.COLUMN_ID + "), " +
                        "UNIQUE(" + TablePreRequisites.COLUMN_STREAM + ","
                        + TablePreRequisites.COLUMN_CODE + ","
                        + TablePreRequisites.COLUMN_PREREQ + ","
                        + TablePreRequisites.COLUMN_COMBINATION
                        + ") ON CONFLICT IGNORE)";


        _db.execSQL(queryClients);
    }

    private void createPathwayModule() {
        String queryPathwayModule =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.PathwayModules.toString() +
                        "(" + TablePathwayModules.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TablePathwayModules.COLUMN_ID_PATHWAY + " INTEGER NOT NULL, " +
                        TablePathwayModules.COLUMN_ID_MODULE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + TablePathwayModules.COLUMN_ID_PATHWAY + ") REFERENCES " + DBHelper.Tables.Pathways + "(" + TablePathways.COLUMN_ID + "), " +
                        "FOREIGN KEY(" + TablePathwayModules.COLUMN_ID_MODULE + ") REFERENCES " + DBHelper.Tables.Modules + "(" + TableModules.COLUMN_ID + ")" +
                        "UNIQUE(" + TablePathwayModules.COLUMN_ID_PATHWAY + "," +
                        TablePathwayModules.COLUMN_ID_MODULE + ") ON CONFLICT REPLACE)";

        _db.execSQL(queryPathwayModule);
    }

    private void createStudentPathway() {
        String queryStudentPathway =
                "CREATE TABLE IF NOT EXISTS " + DBHelper.Tables.StudentPathway.toString() +
                        "(" + TableStudentPathway.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TableStudentPathway.COLUMN_STUDENT_ID + " INT NOT NULL, " +
                        TableStudentPathway.COLUMN_MODULE + " VARCHAR, " +
                        //TableStudentPathway.COLUMN_IS_ENABLED + " INT DEFAULT 0, " +
                        TableStudentPathway.COLUMN_IS_COMPLETED + " INT DEFAULT 0," +
                        "UNIQUE(" + TableStudentPathway.COLUMN_STUDENT_ID + "," + TableStudentPathway.COLUMN_MODULE
                        + ") ON CONFLICT REPLACE)";

        _db.execSQL(queryStudentPathway);
    }


    @Override
    public void close() throws IOException {
        _db.close();
    }
}
