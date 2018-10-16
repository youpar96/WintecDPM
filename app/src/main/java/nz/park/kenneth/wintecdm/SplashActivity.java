package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.TableClients;
import nz.park.kenneth.wintecdm.database.TableModules;
import nz.park.kenneth.wintecdm.database.TablePathwayModule;
import nz.park.kenneth.wintecdm.database.TablePathways;
import nz.park.kenneth.wintecdm.database.TableStudentPathway;
import nz.park.kenneth.wintecdm.database.TableStudents;

public class SplashActivity extends AppCompatActivity {

    private static final String NAME_DATABASE = "wintec_dpm.db";
    private static int TIME_OUT = 3000;
    DBHelper _database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SQLiteDatabase wintecDPM_DB = openOrCreateDatabase(NAME_DATABASE, MODE_PRIVATE, null);

        this.createDatabase(wintecDPM_DB);
        this.populateDatabase(wintecDPM_DB);

        // To delete database
        //getApplicationContext().deleteDatabase(NAME_DATABASE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToMainActivity();
            }
        }, TIME_OUT);
    }

    public void createDatabase(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            this.createModules(wintecDPM_DB);

            this.createPathways(wintecDPM_DB);

            this.createClients(wintecDPM_DB);

            this.createStudents(wintecDPM_DB);

            this.createPathwayModule(wintecDPM_DB);

            this.createStudentPathway(wintecDPM_DB);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void createModules(SQLiteDatabase wintecDPM_DB)
    {
        String queryModules =
                "CREATE TABLE IF NOT EXISTS " + TableModules.TABLE_NAME +
                        "(" + TableModules.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TableModules.COLUMN_NAME + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CODE + " VARCHAR NOT NULL, " +
                        TableModules.COLUMN_CREDITS + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_PREREQ + " VARCHAR, " +
                        TableModules.COLUMN_DETAILS + " TEXT, " +
                        TableModules.COLUMN_SEMESTER + " INTEGER NOT NULL," +
                        TableModules.COLUMN_LEVEL + " INTEGER NOT NULL, " +
                        TableModules.COLUMN_URL + " VARCHAR)";

        wintecDPM_DB.execSQL(queryModules);
    }

    private void createPathways(SQLiteDatabase wintecDPM_DB)
    {
        String queryPathways =
                "CREATE TABLE IF NOT EXISTS " + TablePathways.TABLE_NAME +
                        "(" + TablePathways.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TablePathways.COLUMN_NAME + " VARCHAR NOT NULL)";

        wintecDPM_DB.execSQL(queryPathways);
    }

    private void createClients(SQLiteDatabase wintecDPM_DB)
    {
        String queryClients =
                "CREATE TABLE IF NOT EXISTS " + TableClients.TABLE_NAME +
                        "(" + TableClients.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TableClients.COLUMN_NAME + " VARCHAR NOT NULL)";

        wintecDPM_DB.execSQL(queryClients);
    }

    private void createStudents(SQLiteDatabase wintecDPM_DB)
    {
        String queryStudents =
                "CREATE TABLE IF NOT EXISTS " + TableStudents.TABLE_NAME +
                        "(" + TableStudents.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TableStudents.COLUMN_ID_WINTEC + " INTEGER NOT NULL, " +
                        TableStudents.COLUMN_NAME + " VARCHAR NOT NULL," +
                        TableStudents.COLUMN_DEGREE + " VARCHAR, " +
                        TableStudents.COLUMN_PHOTO + " BLOB)";

        wintecDPM_DB.execSQL(queryStudents);
    }

    private void createPathwayModule(SQLiteDatabase wintecDPM_DB)
    {
        String queryPathwayModule =
                "CREATE TABLE IF NOT EXISTS " + TablePathwayModule.TABLE_NAME +
                        "(" + TablePathwayModule.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TablePathwayModule.COLUMN_ID_PATHWAY + " INTEGER NOT NULL, " +
                        TablePathwayModule.COLUMN_ID_MODULE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + TablePathwayModule.COLUMN_ID_PATHWAY + ") REFERENCES " + TablePathways.TABLE_NAME + "(" + TablePathways.COLUMN_ID + "), " +
                        "FOREIGN KEY(" + TablePathwayModule.COLUMN_ID_MODULE + ") REFERENCES " + TableModules.TABLE_NAME + "(" + TableModules.COLUMN_ID + "))";

        wintecDPM_DB.execSQL(queryPathwayModule);
    }

    private void createStudentPathway(SQLiteDatabase wintecDPM_DB)
    {
        String queryStudentPathway =
                "CREATE TABLE IF NOT EXISTS " + TableStudentPathway.TABLE_NAME +
                        "(" + TableStudentPathway.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                        TableStudentPathway.COLUMN_ID_STUDENT + " INTEGER NOT NULL, " +
                        TableStudentPathway.COLUMN_ID_PATHWAY + " INTEGER NOT NULL, " +
                        TableStudentPathway.COLUMN_COMPLETED + " BOOLEAN, " +
                        "FOREIGN KEY(" + TableStudentPathway.COLUMN_ID_STUDENT + ") REFERENCES " + TableStudents.TABLE_NAME + "(" + TableStudents.COLUMN_ID + "), " +
                        "FOREIGN KEY(" + TableStudentPathway.COLUMN_ID_PATHWAY + ") REFERENCES " + TablePathways.TABLE_NAME + "(" + TablePathways.COLUMN_ID + "))";

        wintecDPM_DB.execSQL(queryStudentPathway);
    }

    public void populateDatabase(SQLiteDatabase wintecDPM_DB)
    {
        try {

            this.populateModules(wintecDPM_DB);
            this.populatePathways(wintecDPM_DB);
            this.populateClients(wintecDPM_DB);
            this.populateStudents(wintecDPM_DB);
            this.populatePathwayModule(wintecDPM_DB);
            this.populateStudentPathway(wintecDPM_DB);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populateModules(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            String count = "SELECT count(*) FROM " + TableModules.TABLE_NAME;
            Cursor dCursor = wintecDPM_DB.rawQuery(count, null);
            dCursor.moveToFirst();

            int iCount = dCursor.getInt(0);

            if (iCount == 0) {

                String queryModuleCOMP501INSERT = "INSERT INTO " + TableModules.TABLE_NAME +
                        " (" +
                        TableModules.COLUMN_NAME + ", " +
                        TableModules.COLUMN_CODE + ", " +
                        TableModules.COLUMN_CREDITS + ", " +
                        TableModules.COLUMN_PREREQ + ", " +
                        TableModules.COLUMN_DETAILS + ", " +
                        TableModules.COLUMN_SEMESTER + ", " +
                        TableModules.COLUMN_LEVEL + ", " +
                        TableModules.COLUMN_URL + ")" +
                        " VALUES (" +
                        "'Information Technology Operations', " +
                        "'COMP501', " +
                        "15, " +
                        "'', " +
                        "'To enable students to apply fundamental IT technical support concepts and practice, and configure and administer systems and applications to meet organisational requirements.', " +
                        "1, " +
                        "5, " +
                        "'https://www.wintec.ac.nz/modules/COMP501')";
                wintecDPM_DB.execSQL(queryModuleCOMP501INSERT);

                String queryModuleCOMP502INSERT = "INSERT INTO modules " +
                        " (" +
                        TableModules.COLUMN_NAME + ", " +
                        TableModules.COLUMN_CODE + ", " +
                        TableModules.COLUMN_CREDITS + ", " +
                        TableModules.COLUMN_PREREQ + ", " +
                        TableModules.COLUMN_DETAILS + ", " +
                        TableModules.COLUMN_SEMESTER + ", " +
                        TableModules.COLUMN_LEVEL + ", " +
                        TableModules.COLUMN_URL + ")" +
                        " VALUES (" +
                        "'Fundamentals of Programming and Problem Solving', " +
                        "'COMP502', " +
                        "15, " +
                        "'', " +
                        "'To enable students to apply the principles of software development to create simple working applications and use problem-solving and decision-making techniques to provide innovative and timely Information Technology outcomes.', " +
                        "1, " +
                        "5, " +
                        "'https://www.wintec.ac.nz/modules/COMP502')";
                wintecDPM_DB.execSQL(queryModuleCOMP502INSERT);

                String queryModuleINFO501INSERT = "INSERT INTO modules " +
                        " (" +
                        TableModules.COLUMN_NAME + ", " +
                        TableModules.COLUMN_CODE + ", " +
                        TableModules.COLUMN_CREDITS + ", " +
                        TableModules.COLUMN_PREREQ + ", " +
                        TableModules.COLUMN_DETAILS + ", " +
                        TableModules.COLUMN_SEMESTER + ", " +
                        TableModules.COLUMN_LEVEL + ", " +
                        TableModules.COLUMN_URL + ")" +
                        " VALUES (" +
                        "'Professional Practice', " +
                        "'INFO501', " +
                        "15, " +
                        "'', " +
                        "'To enable students to apply professional, legal, and ethical principles and practices in a socially responsible manner as an emerging IT professional, and apply communication, personal and interpersonal skills to enhance effectiveness in an IT role.', " +
                        "1, " +
                        "5, " +
                        "'https://www.wintec.ac.nz/modules/INFO501')";
                wintecDPM_DB.execSQL(queryModuleINFO501INSERT);

                String queryModuleINFO502INSERT = "INSERT INTO modules " +
                        " (" +
                        TableModules.COLUMN_NAME + ", " +
                        TableModules.COLUMN_CODE + ", " +
                        TableModules.COLUMN_CREDITS + ", " +
                        TableModules.COLUMN_PREREQ + ", " +
                        TableModules.COLUMN_DETAILS + ", " +
                        TableModules.COLUMN_SEMESTER + ", " +
                        TableModules.COLUMN_LEVEL + ", " +
                        TableModules.COLUMN_URL + ")" +
                        " VALUES (" +
                        "'Business Systems Analysis and Design', " +
                        "'INFO502', " +
                        "15, " +
                        "'', " +
                        "'The student will be able to apply the fundamentals of information systems concepts and practice to support and enhance organisational processes and systems; and apply the fundamentals of interaction design concepts and practice to enhance interface design.', " +
                        "1, " +
                        "5, " +
                        "'https://www.wintec.ac.nz/modules/INFO502')";
                wintecDPM_DB.execSQL(queryModuleINFO502INSERT);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populatePathways(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            String count = "SELECT count(*) FROM " + TablePathways.TABLE_NAME;
            Cursor dCursor = wintecDPM_DB.rawQuery(count, null);
            dCursor.moveToFirst();

            int iCount = dCursor.getInt(0);

            if (iCount == 0) {

                String queryPathwayNetworkEngineerINSERT = "INSERT INTO " + TablePathways.TABLE_NAME + " (" + TablePathways.COLUMN_NAME + ") VALUES ('Network Engineer')";
                wintecDPM_DB.execSQL(queryPathwayNetworkEngineerINSERT);

                String queryPathwaySoftwareEngineerINSERT = "INSERT INTO " + TablePathways.TABLE_NAME + " (" + TablePathways.COLUMN_NAME + ") VALUES ('Software Engineer')";
                wintecDPM_DB.execSQL(queryPathwaySoftwareEngineerINSERT);

                String queryPathwayDatabaseArchitectureINSERT = "INSERT INTO " + TablePathways.TABLE_NAME + " (" + TablePathways.COLUMN_NAME + ") VALUES ('Database Architecture')";
                wintecDPM_DB.execSQL(queryPathwayDatabaseArchitectureINSERT);

                String queryPathwayMultimediaAndWebDevelopmentINSERT = "INSERT INTO " + TablePathways.TABLE_NAME + " (" + TablePathways.COLUMN_NAME + ") VALUES ('Multimedia and Web Development')";
                wintecDPM_DB.execSQL(queryPathwayMultimediaAndWebDevelopmentINSERT);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populateClients(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            String count = "SELECT count(*) FROM " + TableClients.TABLE_NAME;
            Cursor dCursor = wintecDPM_DB.rawQuery(count, null);
            dCursor.moveToFirst();

            int iCount = dCursor.getInt(0);

            if (iCount == 0) {

                String queryClientBlaineINSERT = "INSERT INTO " + TableClients.TABLE_NAME + " (" + TableClients.COLUMN_NAME + ") VALUES ('Blaine')";
                wintecDPM_DB.execSQL(queryClientBlaineINSERT);

                String queryClientDileepINSERT = "INSERT INTO " + TableClients.TABLE_NAME + " (" + TableClients.COLUMN_NAME + ") VALUES ('Dileep')";
                wintecDPM_DB.execSQL(queryClientDileepINSERT);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populateStudents(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            String count = "SELECT count(*) FROM " + TableStudents.TABLE_NAME;
            Cursor dCursor = wintecDPM_DB.rawQuery(count, null);
            dCursor.moveToFirst();

            int iCount = dCursor.getInt(0);

            if (iCount == 0) {

                String queryModuleCOMP501INSERT = "INSERT INTO " + TableStudents.TABLE_NAME +
                        " (" +
                        TableStudents.COLUMN_ID_WINTEC + ", " +
                        TableStudents.COLUMN_NAME + ", " +
                        TableStudents.COLUMN_DEGREE + ", " +
                        TableStudents.COLUMN_PHOTO + ")" +
                        " VALUES (" +
                        "17458543, " +
                        "'Diego Pupato', " +
                        "null," +
                        "null)";
                wintecDPM_DB.execSQL(queryModuleCOMP501INSERT);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populatePathwayModule(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            String count = "SELECT count(*) FROM " + TablePathwayModule.TABLE_NAME;
            Cursor dCursor = wintecDPM_DB.rawQuery(count, null);
            dCursor.moveToFirst();

            int iCount = dCursor.getInt(0);

            if (iCount == 0) {

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populateStudentPathway(SQLiteDatabase wintecDPM_DB)
    {
        try
        {
            String count = "SELECT count(*) FROM " + TableStudentPathway.TABLE_NAME;
            Cursor dCursor = wintecDPM_DB.rawQuery(count, null);
            dCursor.moveToFirst();

            int iCount = dCursor.getInt(0);

            if (iCount == 0) {

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void moveToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
