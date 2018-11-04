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

    }
}
