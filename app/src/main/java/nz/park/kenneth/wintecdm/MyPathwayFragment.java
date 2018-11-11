package nz.park.kenneth.wintecdm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;


@SuppressLint("ValidFragment")
public class MyPathwayFragment extends Fragment {


    ExpandableListView pathwayList;
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList;

    DBHelper _dbhelper;
    Map<String, List<Structure>> programmeMap;
    Button btnPathwaySave;

    public MyPathwayFragment() {
        semesterList = new ArrayList<String>();
        programmeMap = new HashMap<String, List<Structure>>();

    }

    public static MyPathwayFragment newInstance(String param1, String param2) {
        MyPathwayFragment fragment = new MyPathwayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }


    private void SavePathway() {

        _dbhelper.InsertPathway();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //modules.clear();
        View view = inflater.inflate(R.layout.fragment_my_pathway, container, false);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("My Pathway");

        btnPathwaySave = view.findViewById(R.id.btnPathwaySave);
        btnPathwaySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePathway();
            }
        });

        _dbhelper = new DBHelper(getContext(), null);

        //change method to student modules later
        Profile.modules = _dbhelper.GetModulesForStudent(Profile.studentid);
        initialize(Profile.modules);

        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(getContext(), semesterList, programmeMap);


        pathwayList = view.findViewById(R.id.mypathwayList);
        pathwayList.setAdapter(listAdapter);


        return view;
    }

    public void initialize(List<TableModules> modules) {

        String semDisplay = null;
        List<Structure> programmeList = new ArrayList();

        for (int i = 0; i < modules.size(); i++) {

            TableModules currentItem = modules.get(i);
            TableModules nextItem = (i + 1 < modules.size()) ? modules.get(i + 1) : null;


            semDisplay = "Semester" + String.valueOf(currentItem.get_sem());

            //change
            programmeList.add(new Structure(currentItem.get_code() + " | " + currentItem.get_name(), currentItem.get_is_enabled(), currentItem.get_is_completed()));

            if (nextItem == null || currentItem.get_sem() != nextItem.get_sem()) {
                semesterList.add(semDisplay);
                programmeMap.put(semDisplay, programmeList);
                programmeList = new ArrayList<Structure>();
            }


        }

    }
}
