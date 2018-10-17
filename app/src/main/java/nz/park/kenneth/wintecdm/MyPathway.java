package nz.park.kenneth.wintecdm;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPathway extends AppCompatActivity {

    ExpandableListView pathwayList;
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList;

    Map<String,List<Structure>> programmeMap;

    public MyPathway(){
        semesterList = new ArrayList<String>();
        programmeMap = new HashMap<String,List<Structure>>();

    }

    //Test data
    private List<Structure> LoadSubjects(){

        List<Structure> subjects=subjects=new ArrayList<Structure>();

        subjects.add(new Structure("COMP501 | IT Operations",true,true));
        subjects.add(new Structure("COMP502 | Fundamentals",true,true));
        subjects.add(new Structure("INFO501 | Professional Practice",true,false));
        return subjects;
    }

    private List<Structure> LoadSubjects1(){

        List<Structure> subjects=subjects=new ArrayList<Structure>();

        subjects.add(new Structure("COMP600 | IT Essentials",false,false));
        subjects.add(new Structure("COMP601 | Fundamentals",false,false));
        subjects.add(new Structure("COMP602 | Object Oriented Programming",false,false));
        return subjects;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pathway);


        //Test data
        semesterList.addAll(Arrays.asList("Semester1","Semester2"));
        programmeMap.put("Semester1",LoadSubjects());
        programmeMap.put("Semester2",LoadSubjects1());


        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(this, semesterList, programmeMap);

        pathwayList = findViewById(R.id.mypathwayList);
        pathwayList.setAdapter(listAdapter);
    }

}


