package nz.park.kenneth.wintecdm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPathway extends AppCompatActivity {

    // View objects
    ExpandableListView pathwayList;

    // Variables
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, programmeList;
    HashMap<String, List<String>> programmeMap;

    private static final int YEAR_COUNT = 3;

    private String[][] programmes = {
            {"Comp501", "Comp502", "Info501", "Info502"},
            {"Comp601", "Comp602", "Info601", "Info602"},
            {"Comp701", "Comp702", "Info701", "Info702"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pathway);

        init();

        pathwayList = findViewById(R.id.pathwayList);
        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(this, yearList, programmeMap);

        pathwayList.setAdapter(listAdapter);
    }

    public void init(){
        yearList = new ArrayList<String>();
        programmeList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<String>>();

        // set years
        yearList.add("Year1");
        yearList.add("Year2");
        yearList.add("Year3");

        // set programmes
        for(int i=0; i<programmes.length; i++){
            for(int j=0; j<programmes[i].length; j++){
                programmeList.add(programmes[i][j]);
            }

            programmeMap.put(yearList.get(i), programmeList);
        }
    }
}
