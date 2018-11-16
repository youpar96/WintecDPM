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

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;

@SuppressLint("ValidFragment")
public class DetailPathwayFragment extends Fragment {

    // View objects
    ExpandableListView pathwayList;

    // Variables
    nz.park.kenneth.wintecdm.ExpandableListAdapter listAdapter;
    List<String> yearList, semesterList, programmeList;
    HashMap<String, List<String>> programmeMap;

    private String pathWay;

    public void setPathWay(String pathWay){
        this.pathWay = pathWay;
    }

    // create constructor to get the pathWay value instead of getting from intent object
    public DetailPathwayFragment(){
        super();
    }

    public DetailPathwayFragment(String pathWay) {
        super();
        this.pathWay = pathWay;
    }

    private static final int YEAR_COUNT = 3;
    private static final int SEMESTER_COUNT = 6;
    private DBHelper _dbhelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            String pathWay = savedInstanceState.getString("pathWay");

            setPathWay(pathWay);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("pathWay", pathWay);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pathway, container, false);
        _dbhelper = new DBHelper(getContext(), null);

        String title = "Pathway > ";
        Pathways.PathwayEnum _path = null;


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

        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(title);


        // to prepare lists to display on expandable list view
        init(_values);

        pathwayList = view.findViewById(R.id.pathwayList);
        listAdapter = new nz.park.kenneth.wintecdm.ExpandableListAdapter(getContext(), semesterList, programmeMap);

        pathwayList.setAdapter(listAdapter);

        return view;
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
            programmeList.add(currentItem.get_code() + " | " + currentItem.get_name() +" | "+ currentItem.get_url());

            if (nextItem == null || currentItem.get_sem() != nextItem.get_sem()) {
                semesterList.add(semDisplay);
                programmeMap.put(semDisplay, programmeList);
                programmeList = new ArrayList<String>();
            }


        }

    }
}
