package nz.park.kenneth.wintecdm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class DetailPathwayFragment extends Fragment {

    // View objects
    ExpandableListView pathwayList;
    TextView tvDetailPathway;

    // Variables
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList, programmeList;
    HashMap<String, List<String>> programmeMap;

    private String pathWay;

    // create constructor to get the pathWay value instead of getting from intent object
    public DetailPathwayFragment(String pathWay){
        this.pathWay = pathWay;
    }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pathway, container, false);

        String title = "Pathway > ";

        switch(pathWay){
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

        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(title);


        // to prepare lists to display on expandable list view
        init();

        pathwayList = view.findViewById(R.id.pathwayList);
        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(getContext(), semesterList, programmeMap);

        pathwayList.setAdapter(listAdapter);

        return view;
    }

    public void init(){
        yearList = new ArrayList<String>();
        semesterList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<String>>();

        // set years
        for(int i=0; i<YEAR_COUNT; i++){
            yearList.add("Year" + (i + 1));
        }

        // set semesters
        for(int i=0; i<SEMESTER_COUNT; i++){
            semesterList.add("Semester" + (i + 1));
        }

        // set programmes
        for(int i=0; i<SEMESTER_COUNT; i++){
            programmeList = new ArrayList<String>();

            for(int j=0; j<softwareProgrammeNames[i].length; j++){
                programmeList.add(softwareProgrammeCodes[i][j] + " | " + softwareProgrammeNames[i][j]);
            }

            programmeMap.put(semesterList.get(i), programmeList);
        }
    }
}
