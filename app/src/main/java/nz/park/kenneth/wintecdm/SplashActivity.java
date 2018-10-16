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

                // Compulsory Modules
                String queryModuleCOMP501​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Information Technology Operations', 'COMP501​', 15, '', 'To enable students to apply fundamental IT technical support concepts and practice, and configure and administer systems and applications to meet organisational requirements.', 1, 5, 'https://www.wintec.ac.nz/modules/COMP501')";
                String queryModuleCOMP502​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Fundamentals of Programming and Problem Solving', 'COMP502​', 15, '', 'To enable students to apply the principles of software development to create simple working applications and use problem-solving and decision-making techniques to provide innovative and timely Information Technology outcomes.', 1, 5, 'https://www.wintec.ac.nz/modules/COMP502')";
                String queryModuleINFO501​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Professional Practice', 'INFO501​', 15, '', 'To enable students to apply professional, legal, and ethical principles and practices in a socially responsible manner as an emerging IT professional, and apply communication, personal and interpersonal skills to enhance effectiveness in an IT role.', 1, 5, 'https://www.wintec.ac.nz/modules/INFO501')";
                String queryModuleINFO502​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Business Systems Analysis and Design', 'INFO502​', 15, '', 'The student will be able to apply the fundamentals of information systems concepts and practice to support and enhance organisational processes and systems; and apply the fundamentals of interaction design concepts and practice to enhance interface design.', 1, 5, 'https://www.wintec.ac.nz/modules/INFO502')";
                String queryModuleCOMP503​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Introduction to Networks', 'COMP503​', 15, '', 'To enable students to apply a broad operational knowledge of networking, and associated services and technologies to meet typical organisational requirements.', 2, 5, 'https://www.wintec.ac.nz/modules/COMP503')";
                String queryModuleCOMP504​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Operating Systems and Systems Support', 'COMP504​', 15, '', 'To enable students to select, install, and configure IT hardware and systems software and use graphical (GUI) and command line interfaces (CLI) to meet organisational requirements.', 2, 5, 'https://www.wintec.ac.nz/modules/COMP504')";
                String queryModuleINFO503​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Database Principles', 'INFO503​', 15, '', 'To enable students to apply a broad operational knowledge of database administration to meet typical organisational data storage and retrieval requirements, and apply conceptual knowledge of cloud services and virtualisation.', 2, 5, 'https://www.wintec.ac.nz/modules/INFO503')";
                String queryModuleINFO504​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Technical Support', 'INFO504​', 15, '', 'To enable students to demonstrate an operational knowledge and understanding of IT service management, identify common issues related to IT security, and troubleshoot and resolve a range of common system problems.', 2, 5, 'https://www.wintec.ac.nz/modules/INFO504')";
                String queryModuleCOMP601​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Object Oriented Programming', 'COMP601​', 15, 'INFO502​​', 'To enable students to gain the skills to develop software applications using Object Oriented Programming techniques. Students will enrich their programming and problem solving skills by applying classes, objects, constructors, methods and properties, inheritance and polymorphism to develop programming applications.', 3, 6, 'https://www.wintec.ac.nz/modules/COMP601')";
                String queryModuleINFO601​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Database Modelling and SQL', 'INFO601​', 15, 'INFO503​', 'To enable students to apply an indepth understanding of database modelling, database design and SQL concepts.', 3, 6, 'https://www.wintec.ac.nz/modules/INFO601')";
                String queryModuleMATH601​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Mathematics for Information Technology', 'MATH601​', 15, '', 'To enable students to gain mathematical skills and acquire in-depth understanding of mathematics as applied to Information Technology.', 3, 6, 'https://www.wintec.ac.nz/modules/MATH601')";
                String queryModuleCOMP602​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Web Development', 'COMP602​', 15, 'INFO502​​|COMP502​', 'To enable students to gain the in depth knowledge and skills required to be able to write programs in web programming languages that solve various web programming tasks.', 3, 6, 'https://www.wintec.ac.nz/modules/COMP602')";
                String queryModuleINFO602INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Business, Interpersonal Communications and Technical Writing', 'INFO602', 15, '', 'To enable students to develop an understanding of the principles and processes involved in effective interpersonal communication and technical writing used in managing client relationships.', 4, 6, 'https://www.wintec.ac.nz/modules/INFO602')";
                String queryModuleCOMP603INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "The Web Environment', 'COMP603', 15, 'COMP602', 'To enable students to examine the environment that software developers work in with respect to web applications, and to develop the knowledge and skills required to work with the servers, protocols and automation tools they can expect to encounter in this area.', 4, 6, 'https://www.wintec.ac.nz/modules/COMP603')";
                String queryModuleBIZM701​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Business Essentials for IT Professionals', 'BIZM701​', 15, 'INFO602​', 'To enable students to develop an understanding of the common principles of business practice whilst focussing on the theoretical and practical concepts of accounting, marketing and management in order to understand the business context within which Information Technology solutions are developed.', 5, 7,  'https://www.wintec.ac.nz/modules/BIZM701')";
                String queryModuleINFO701​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Project Management', 'INFO701​', 15, 'INFO502​​', 'To enable students to understand and apply the theory of project management with particular emphasis on Information Technology projects.', 5, 7,  'https://www.wintec.ac.nz/modules/INFO701')";
                wintecDPM_DB.execSQL(queryModuleCOMP501​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP502​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO501​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO502​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP503​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP504​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO503​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO504​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP601​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO601​INSERT);
                wintecDPM_DB.execSQL(queryModuleMATH601​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP602​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO602INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP603INSERT);
                wintecDPM_DB.execSQL(queryModuleBIZM701​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO701​INSERT);

                // Network Engineering
                String queryModuleINFO603​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Systems Administration', 'INFO603​', 15, '', 'To enable students to gain the skills and knowledge required to effectively plan, install and administer a Microsoft Windows Server.', 4, 6, 'https://www.wintec.ac.nz/modules/INFO603')";
                String queryModuleCOMP604​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Routing and Switching Essentials', 'COMP604​', 15, 'INFO503​', 'To enable students to configure, troubleshoot and understand the operation of Routers, Routing Protocols, Switches and VLANs in a networking environment, and complete the Cisco Certified Network Associate 2 (CCNA2) Curriculum.', 4, 6, 'https://www.wintec.ac.nz/modules/COMP604')";
                String queryModuleCOMP701​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Advanced Networking', 'COMP701​', 15, 'INFO603​', 'To enable students to investigate and configure advanced system administration tools, advanced network virtualisation and wireless networking technologies. Students will also research emerging networking technologies.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP701')";
                String queryModuleCOMP702​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Scaling Networks', 'COMP702​', 15, 'COMP604​', 'To enable students to gain an understanding of the components and the operation of advanced switched and routed networks, and to complete the Cisco Certified Network Associate 3 (CCNA3) curriculum.', 5, 7, 'https://www.wintec.ac.nz/modules/COMP702')";
                String queryModuleCOMP704INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Network Security', 'COMP704 ​', 15, 'COMP604​', 'To enable students to understand and configure the components, and operation of Virtual Private Networks, firewalls and network security, which will enable them to complete the Cisco Certified Network Associate Security curriculum.', 5, 7, 'https://www.wintec.ac.nz/modules/COMP704')";
                String queryModuleCOMP705​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Connecting Networks', 'COMP705​', 15, 'COMP702​', 'To enable students to gain an understanding of the components and operation of Wide Area Networks (WANs), network security, network management, and to complete the Cisco Certified Network Associate 4 (CCNA4) curriculum.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP705')";
                String queryModuleINFO702​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Cyber Security', 'INFO702​', 15, 'COMP504​|COMP601​​', 'To enable students to investigate computer system attacks and vulnerabilities in relation to operating systems (OS), applications, networking and websites, and investigate the security techniques for protecting a computer system from such attacks.', 6, 7, 'https://www.wintec.ac.nz/modules/INFO702')";
                String queryModuleCOMP703INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Network Engineering Project', 'COMP703', 15, 'INFO603|INFO701|COMP702', 'This module will enable students to develop a networking solution from a set of requirements documents. This module is the Network Engineering Capstone Project', 6, 7, 'https://www.wintec.ac.nz/modules/COMP703')";
                wintecDPM_DB.execSQL(queryModuleINFO603​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP604​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP701​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP702​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP704INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP705​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO702​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP703INSERT);

                // Software Engineering
                String queryModuleCOMP605​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Data Structures and Algorithms​​', 'COMP605​', 15, 'COMP601 and MATH601', 'To enable students to apply programming and analytical skills to implement and analyze common data structures for computer programs. Students will cover a wide range of computer programming algorithms.', 4, 6, 'https://www.wintec.ac.nz/modules/COMP605')";
                String queryModuleMATH602​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Mathematics for Programming', 'MATH602​', 15, 'MATH601​', 'To enable students to obtain the mathematical skills to facilitate an in-depth understanding of advanced programming techniques. Students will be able to solve problems in recurrence functions, asymptotic functions, algorithmic puzzles, combinatorics and graph theory and advanced topics in probability and statistics.', 4, 6, 'https://www.wintec.ac.nz/modules/MATH602')";
                String queryModuleINFO703​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Big Data and Analytics', 'INFO703​', 15, 'COMP605​ and MATH602​', 'To enable students to gain the practical knowledge and skills required to store, manage and analyse large amounts of data, using appropriate algorithms.', 5, 7, 'https://www.wintec.ac.nz/modules/INFO703')";
                String queryModuleCOMP707​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Principles of Software Testing', 'COMP707​', 15, 'COMP605​', 'Students will gain comprehensive knowledge of software testing methodologies and software testing tools used in industry and apply fundamental aspects of software testing incorporating system requirements, quality assurance, testing processes, automation, testing types and testing levels. This forms the third part of the Software Engineering Capstone Project.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP707')";
                String queryModuleCOMP706​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Game Development', 'COMP706​', 15, 'COMP601​,COMP605​ and MATH602​', 'To enable students to understand supporting theories and principles of game design and apply these to the art and science of game design, development and programming.', 5, 7, 'https://www.wintec.ac.nz/modules/COMP706')";
                String queryModuleCOMP709​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Mobile Applications Development', 'COMP709​', 15, 'COMP601​,COMP605​ and MATH602​', 'To enable students to design, develop and implement mobile applications on a given platform.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP709')";
                String queryModuleINFO704​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Data-Warehousing and Business Intelligence', 'INFO704​', 15, '​', 'To enable students to examine the main components of data warehousing and apply it to business intelligence applications, enabling them to provide solutions which incorporate extracting data from different sources, storing data in a data warehouse and developing applications for business decision-making.', 6, 7, 'https://www.wintec.ac.nz/modules/INFO704')";
                String queryModuleCOMP708INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Software Engineering Project', 'COMP708', 15, 'COMP605, MATH602', 'Students will gain advanced software development skills. They will be able to provide an in depth analysis of prototyping, performance, correctness, software reusability, scalability, quality and maintenance and versioning. This module is the Software Engineering capstone project.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP708')";
                wintecDPM_DB.execSQL(queryModuleCOMP605​INSERT);
                wintecDPM_DB.execSQL(queryModuleMATH602​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO703​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP707​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP706​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP709​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO704​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP708INSERT);

                // Database Architecture
                String queryModuleCOMP606​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Web Programming', 'COMP606​', 15, 'COMP602​', 'To enable students to gain the in depth knowledge and skills required to be able to write programs in web programming languages that solve various web programming tasks.', 4, 6, 'https://www.wintec.ac.nz/modules/COMP606')";
                String queryModuleINFO604​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Database Systems', 'INFO604​', 15, 'INFO601|INFO503​', 'To enable students to configure, troubleshoot and understand the operation of Routers, Routing Protocols, Switches and VLANs in a networking environment, and complete the Cisco Certified Network Associate 2 (CCNA2) Curriculum.', 4, 6, 'https://www.wintec.ac.nz/modules/INFO604')";
                String queryModuleINFO706​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Database Front-End Applications', 'INFO706​', 15, 'INFO601​|INFO604​', 'To enable students to understand and apply various front end applications and their interfaces with supporting databases.', 5, 7, 'https://www.wintec.ac.nz/modules/INFO706')";
                String queryModuleINFO707​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Cloud Server Databases', 'INFO707​', 15, 'INFO601​|INFO604​', 'To enable students to gain an in-depth knowledge of cloud server database concepts, fundamentals and essentials. They will apply practical skills to install, setup, configure, manage and maintain a cloud server database and deploy cloud database services to support database applications.', 5, 7, 'https://www.wintec.ac.nz/modules/INFO707')";
                /*//String queryModuleCOMP709​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Mobile Applications Development', 'COMP709​', 15, 'COMP601​|MATH601', 'To enable students to design, develop and implement mobile applications on a given platform.',6, 7, 'https://www.wintec.ac.nz/modules/COMP709')";
                //String queryModuleINFO704​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Data-Warehousing and Business Intelligence', 'INFO704​', 15, '​INFO601', 'To enable students to examine the main components of data warehousing and apply it to business intelligence applications, enabling them to provide solutions which incorporate extracting data from different sources, storing data in a data warehouse and developing applications for business decision-making.', 6, 7, 'https://www.wintec.ac.nz/modules/INFO704')";*/
                String queryModuleCOMP710INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Web Application Development', 'COMP710', 15, 'COMP601|COMP605|MATH602', 'To enable students to apply practical knowledge of Model View Controller (MVC) frameworks to plan, design and implement web applications. The core focus will be on architecture design and implementation of a web application that will meet a set of functional requirements and user interface requirements, and address business models.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP710')";
                String queryModuleINFO705INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Database Architecture Project', 'INFO705', 15, 'INFO601|INFO604', 'To enable the student to further develop their knowledge of Database Architecture by analysing, designing and implementing a database solution. Alternative and new approaches to database systems will be explored and evaluated by using practical-based solutions. This module is the Database Architecture Capstone Project.', 6, 7, 'https://www.wintec.ac.nz/modules/INFO705')";
                wintecDPM_DB.execSQL(queryModuleCOMP606​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO604​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO706​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO707​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP710INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO705INSERT);
                
                // Multimedia and Web Development
                /*//String queryModuleCOMP606​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Web Programming', 'COMP606​', 15, 'COMP602​', 'To enable students to gain the in depth knowledge and skills required to be able to write programs in web programming languages that solve various web programming tasks.', 4, 6, 'https://www.wintec.ac.nz/modules/COMP606')";*/
                String queryModuleCOMP607​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Visual Effects and Animation', 'COMP607​', 15, 'COMP602​', 'To enable students to develop the knowledge and skills required to design and develop effective graphics and animation, and to apply various visual effects for static graphics as well as create 3D models and produce 2D and 3D animation.', 4, 6, 'https://www.wintec.ac.nz/modules/COMP607')";
                String queryModuleINFO708​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Data Visualisation', 'INFO708​', 15, 'COMP606​|COMP607​', 'To enable students to study and apply visual techniques that transform data into a format efficient for human perception, cognition, and communication, thus allowing for extraction of meaningful information and insight. Students will investigate data visualisation techniques, human visual systems and cognitive perception, and design, construct, and evaluate data visualisations.', 5, 7, 'https://www.wintec.ac.nz/modules/INFO708')";
                String queryModuleCOMP706_INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Games Development', 'COMP706', 15, 'COMP601|COMP605|MATH602', 'To enable students to understand supporting theories and principles of game design and apply these to the art and science of game design, development and programming.', 5, 7, 'https://www.wintec.ac.nz/modules/COMP706')";
                String queryModuleINFO702_INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Cyber Security', 'INFO702 ​', 15, 'COMP504​|COMP601​', 'To enable students to investigate computer system attacks and vulnerabilities in relation to operating systems (OS), applications, networking and websites, and investigate the security techniques for protecting a computer system from such attacks.', 6, 7, 'https://www.wintec.ac.nz/modules/INFO702')";
                /*//String queryModuleCOMP709​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Mobile Applications Development', 'COMP709​', 15, 'COMP601|MATH601|COMP605|MATH605', 'To enable students to design, develop and implement mobile applications on a given platform.', 6, 7, 'https://www.wintec.ac.nz/modules/COMP709')";*/
                String queryModuleINFO709​INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Human Computer Interaction​', 'INFO709​', 15, '​', 'To enable students to understand the supporting theories and principles of user interface design with respect to human computer interaction. They will investigate applications in human computer interaction and apply usability best practices and processes.', 6, 7, 'https://www.wintec.ac.nz/modules/INFO709')";
                String queryModuleCOMP711INSERT = "INSERT INTO modules (" + TableModules.COLUMN_NAME + ", " + TableModules.COLUMN_CODE + ", " + TableModules.COLUMN_CREDITS + ", " + TableModules.COLUMN_PREREQ + ", " + TableModules.COLUMN_DETAILS + ", " + TableModules.COLUMN_SEMESTER + ", " + TableModules.COLUMN_LEVEL + ", " + TableModules.COLUMN_URL + ") VALUES ('" + "Web Development Project', 'COMP711', 15, 'COMP602|COMP606', 'On completion of this course students will be able to build a complete web application following the entire web development process from end to end, using contemporary software development architecture and frameworks. Students will be capable of operating within applicable professional standards and practice, both independently and collaboratively as part of a team', 6, 7, 'https://www.wintec.ac.nz/modules/COMP711')";
                wintecDPM_DB.execSQL(queryModuleCOMP607​INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO708​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP706_INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO702_INSERT);
                wintecDPM_DB.execSQL(queryModuleINFO709​INSERT);
                wintecDPM_DB.execSQL(queryModuleCOMP711INSERT);

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
