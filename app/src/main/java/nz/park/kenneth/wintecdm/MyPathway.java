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

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;

public class MyPathway extends AppCompatActivity {

    ExpandableListView pathwayList;
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList;

    DBHelper _dbhelper;
    Map<String, List<Structure>> programmeMap;

    public MyPathway() {
        semesterList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<Structure>>();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pathway);

        _dbhelper = new DBHelper(getApplicationContext(), null);
        ArrayList<TableModules> _values = _dbhelper.GetModulesByPathway(Pathways.PathwayEnum.Software);
        initialize(_values);

        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(this, semesterList, programmeMap);


        pathwayList = findViewById(R.id.mypathwayList);
        pathwayList.setAdapter(listAdapter);
    }


    public void initialize(ArrayList<TableModules> modules) {

        String semDisplay = null;
        List<Structure> programmeList = new ArrayList();

        for (int i = 0; i < modules.size(); i++) {

            TableModules currentItem = modules.get(i);
            TableModules nextItem = (i + 1 < modules.size()) ? modules.get(i + 1) : null;


            semDisplay = "Semester" + String.valueOf(currentItem.get_sem());

            //change
            programmeList.add(new Structure(currentItem.get_code() + " | " + currentItem.get_name(), true, false));

            if (nextItem == null || currentItem.get_sem() != nextItem.get_sem()) {
                semesterList.add(semDisplay);
                programmeMap.put(semDisplay, programmeList);
                programmeList = new ArrayList<Structure>();
            }


        }

    }


}


