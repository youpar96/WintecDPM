package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPathway extends AppCompatActivity {

    // View objects
    ExpandableListView pathwayList;
    TextView tvDetailPathway;

    // Variables
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, programmeList;
    HashMap<String, List<String>> programmeMap;

    private static final int YEAR_COUNT = 3;

    private String[][] softwareProgrammeNames = {
            {"IT Operations", "Fundamentals of Programming and Problem Solving", "Professional Practice", "Business Systems Analysis & Design"},
            {"Introduction to Networks (Cisco 1)", "Operating Systems & Systems Support", "Database Principles", "Technical Support"},
            {"Object Oriented Programming", "Data-modelling and SQL", "Mathematics for IT", "Web Development"},
            {"Business, Interpersonal Communications & Technical Writing", "The Web Environment", "Data Structures and Algorithms", "Mathmatics for programming"},
            {"Project Management", "Business Essentials for IT Professionals", "Big Data and Analytics", "Games Development"},
            {"Data-Warehousing and Business Intelligence", "Principles of Software Testing", "Mobile Apps Development", "Software Engineering Project"}
    };

    private String[][] softwareProgrammeCodes = {
            {"COMP501", "COMP502", "INFO501", "INFO502"},
            {"COMP503", "COMP504", "INFO503", "INFO504"},
            {"COMP601", "INFO601", "MATH601", "COMP602"},
            {"INFO602", "COMP603", "COMP605", "MATT602"},
            {"INFO701", "BIZM701", "INFO703", "COMP706"},
            {"INFO704", "COMP707", "COMP709", "COMP708"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pathway);

        Intent i = getIntent();

        tvDetailPathway = findViewById(R.id.tvDetailPathway);

        String pathWay = i.getStringExtra("pathWay");
        String title = "";

        switch(pathWay){
            case "S":
                title = "Software Engineering";
                break;
            case "D":
                title = "Database Architecture";
                break;
            case "N":
                title = "Networking";
                break;
            case "W":
                title = "Multi Media Web Development";
                break;
            default:
                break;
        }

        tvDetailPathway.setText(title);

        // to prepare lists to display on expandable list view
        init();

        pathwayList = findViewById(R.id.pathwayList);
        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(this, yearList, programmeMap);

        pathwayList.setAdapter(listAdapter);
    }

    public void init(){
        yearList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<String>>();

        // set years
        yearList.add("Year1");
        yearList.add("Year2");
        yearList.add("Year3");

        // set programmes
        for(int i=0; i<softwareProgrammeNames.length; i++){
            programmeList = new ArrayList<String>();

            for(int j=0; j<softwareProgrammeNames[i].length; j++){
                programmeList.add(softwareProgrammeNames[i][j]);
            }

            programmeMap.put(yearList.get(i), programmeList);
        }
    }
}
