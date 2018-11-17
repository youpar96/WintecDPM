package nz.park.kenneth.wintecdm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class InputModuleFragment extends Fragment {

    Spinner spinnerPathway;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_module, container, false);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);

        // use indicatior as create or modify module
        String title = "Create New Module";
        String mode = "C";
        if("U".equals(mode)){
            title = "Update Module";
        }
        toolbar.setTitle(title);

        spinnerPathway = view.findViewById(R.id.spinnerPathway);

        // set data into spinner
        addSpinnerData();

        spinnerPathway.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // set the value of the spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void addSpinnerData(){
        // have to get the four pathways - these are dummy
        List<String> pathwayList = new ArrayList<>();

        pathwayList.add("Software Enginerring");
        pathwayList.add("Database Architecture");
        pathwayList.add("Networking");
        pathwayList.add("Web Development");

        ArrayAdapter<String> aAdapt = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, pathwayList);
        aAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPathway.setAdapter(aAdapt);
    }

}
