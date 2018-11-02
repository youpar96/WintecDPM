package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;

public class DetailPathway extends AppCompatActivity {

    DBHelper _dbhelper;
    // View objects
    ExpandableListView pathwayList;
    TextView tvDetailPathway;

    // Variables
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList, programmeList;
    HashMap<String, List<String>> programmeMap;

//    private static final int YEAR_COUNT = 3;
//    private static final int SEMESTER_COUNT = 6;

//    private String[][] programmeNames = {
//            {"IT Operations", "Fundamentals of Programming and Problem Solving", "Professional Practice", "Business Systems Analysis & Design"},
//            {"Introduction to Networks (Cisco 1)", "Operating Systems & Systems Support", "Database Principles", "Technical Support"},
//            {"Object Oriented Programming", "Data-modelling and SQL", "Mathematics for IT", "Web Development"},
//            {"Business, Interpersonal Communications & Technical Writing", "The Web Environment", "Data Structures and Algorithms", "Mathmatics for programming"},
//            {"Project Management", "Business Essentials for IT Professionals", "Big Data and Analytics", "Games Development"},
//            {"Data-Warehousing and Business Intelligence", "Principles of Software Testing", "Mobile Apps Development", "Software Engineering Project"}
//    };
//
//    private String[][] programmeCodes = {
//            {"COMP501", "COMP502", "INFO501", "INFO502"},
//            {"COMP503", "COMP504", "INFO503", "INFO504"},
//            {"COMP601", "INFO601", "MATH601", "COMP602"},
//            {"INFO602", "COMP603", "COMP605", "MATT602"},
//            {"INFO701", "BIZM701", "INFO703", "COMP706"},
//            {"INFO704", "COMP707", "COMP709", "COMP708"}
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pathway);

        _dbhelper = new DBHelper(getApplicationContext(), null);

        Intent i = getIntent();

        Pathways.PathwayEnum _path = null;


        tvDetailPathway = findViewById(R.id.tvDetailPathway);

        String pathWay = i.getStringExtra("pathWay");
        String title = "Pathway > ";

        switch (pathWay) {
            case "S":
                title += "Software Engineering";
                _path = Pathways.PathwayEnum.Software;
                break;
            case "D":
                title += "Database Architecture";
                _path = Pathways.PathwayEnum.Db;
                break;
            case "N":
                title += "Networking";
                _path = Pathways.PathwayEnum.Networking;
                break;
            case "W":
                title += "Multi Media Web Development";
                _path = Pathways.PathwayEnum.Web;
                break;
            default:
                break;
        }

        ArrayList<TableModules> _values = _dbhelper.GetModulesByPathway(_path);


        tvDetailPathway.setText(title);

        // to prepare lists to display on expandable list view
        init(_values);

        pathwayList = findViewById(R.id.pathwayList);
        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(this, semesterList, programmeMap);

        pathwayList.setAdapter(listAdapter);
    }

    public void init(ArrayList<TableModules> modules) {

        semesterList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<String>>();
        programmeList = new ArrayList<String>();

        String semDisplay = null;


        for (int i = 0; i < modules.size(); i++) {

            TableModules currentItem = modules.get(i);
            TableModules nextItem = (i + 1 < modules.size()) ? modules.get(i + 1) : null;


            semDisplay = "Semester" + String.valueOf(currentItem.get_sem());
            programmeList.add(currentItem.get_code() + " | " + currentItem.get_name());

            if (nextItem == null || currentItem.get_sem() != nextItem.get_sem()) {
                semesterList.add(semDisplay);
                programmeMap.put(semDisplay, programmeList);
                programmeList = new ArrayList<String>();
            }


        }


        // set semesters
//        for (int i = 0; i < SEMESTER_COUNT; i++) {
//            semesterList.add("Semester" + (i + 1));
//        }

        // set programmes
//        for (int i = 0; i < SEMESTER_COUNT; i++) {
//            programmeList = new ArrayList<String>();
//
//            for (int j = 0; j < programmeNames[i].length; j++) {
//                programmeList.add(programmeCodes[i][j] + " | " + programmeNames[i][j]);
//            }
//
//            programmeMap.put(semesterList.get(i), programmeList);
//        }
    }
}
