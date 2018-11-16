package nz.park.kenneth.wintecdm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;


@SuppressLint("ValidFragment")
public class MyPathwayFragment extends Fragment {

    ViewGroup viewContainer;


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
        String msg = "";

        if (_dbhelper.InsertPathway())
            msg = "Saving Success";
        else
            msg = "Saving Failed";


        // because the background of toast message was white, chagne the color
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toast.show();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewContainer = container;

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

        expandEachGroup(semesterList.size());


        pathwayList.setOnScrollListener(lv_onScrollListener);


        return view;
    }

    private AbsListView.OnScrollListener lv_onScrollListener = new AbsListView.OnScrollListener() {
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                listAdapter.setScroll(false);
                listAdapter.notifyDataSetChanged();
            } else {
                listAdapter.setScroll(true);
            }
        }
    };

    public void expandEachGroup(int count) {
        int i = 0;
        while (i < count) {
            pathwayList.expandGroup(i);
            i++;
        }

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
