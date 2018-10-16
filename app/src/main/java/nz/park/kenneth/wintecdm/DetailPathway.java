package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
    ImageView delete;

    // Variables
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList, programmeList;
    HashMap<String, List<String>> programmeMap;

    private static final int YEAR_COUNT = 3;
    private static final int SEMESTER_COUNT = 6;

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
        String title = "Pathway > ";

        switch (pathWay) {
            case "S":
                title += "Software Engineering";
                break;
            case "D":
                title += "Database Architecture";
                break;
            case "N":
                title += "Networking";
                break;
            case "W":
                title += "Multi Media Web Development";
                break;
            default:
                break;
        }

        tvDetailPathway.setText(title);

        // to prepare lists to display on expandable list view
        init();

        pathwayList = findViewById(R.id.pathwayList);
        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(this, semesterList, programmeMap);
        pathwayList.setAdapter(listAdapter);


    }

    public void init() {
        yearList = new ArrayList<String>();
        semesterList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<String>>();

        // set years
        for (int i = 0; i < YEAR_COUNT; i++) {
            yearList.add("Year" + (i + 1));
        }

        // set semesters
        for (int i = 0; i < SEMESTER_COUNT; i++) {
            semesterList.add("Semester" + (i + 1));
        }

        // set programmes
        for (int i = 0; i < SEMESTER_COUNT; i++) {
            programmeList = new ArrayList<String>();

            for (int j = 0; j < softwareProgrammeNames[i].length; j++) {
                programmeList.add(softwareProgrammeCodes[i][j] + " | " + softwareProgrammeNames[i][j]);
            }

            programmeMap.put(semesterList.get(i), programmeList);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                EditText et = ((EditText) v);
                et.setBackgroundColor(Color.WHITE);
                et.setCursorVisible(false);
                et.setInputType(InputType.TYPE_NULL);

//                ImageView edit = (ImageView) findViewById(R.id.editModule);
//                edit.setVisibility(View.INVISIBLE);
                delete = findViewById(R.id.deleteModule);
                delete.setVisibility(View.INVISIBLE);


                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }


        }
        return super.dispatchTouchEvent(event);
    }
}
