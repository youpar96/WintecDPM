package nz.park.kenneth.wintecdm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pathway, container, false);
        _dbhelper = new DBHelper(getContext(), null);
        ArrayList<TableModules> _values = _dbhelper.GetModulesByPathway(Pathways.PathwayEnum.Software);
        initialize(_values);

        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(getContext(), semesterList, programmeMap);


        pathwayList = view.findViewById(R.id.mypathwayList);
        pathwayList.setAdapter(listAdapter);


        return view;
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
