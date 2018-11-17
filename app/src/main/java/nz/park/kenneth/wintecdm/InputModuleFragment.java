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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.database.DBHelper;

public class InputModuleFragment extends Fragment {

    Spinner spinnerPathway, spinnerSemester;
    EditText moduleCode, moduleName, moduleCredits, modulePreqCode;
    RadioGroup radiolevel;
    Button btnPathwaySave;

    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_module, container, false);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);

        // use indicatior as create or modify module
        String title = "Create New Module";
        String mode = "C";
        if ("U".equals(mode)) {
            title = "Update Module";
        }
        toolbar.setTitle(title);

        initialize(view);
        populateSpinners();


        return view;
    }

    private void initialize(View view) {
        spinnerPathway = (Spinner) view.findViewById(R.id.spinnerPathway);
        spinnerSemester = (Spinner) view.findViewById(R.id.spinnerSemester);
        moduleCode = (EditText) view.findViewById(R.id.moduleCode);
        moduleName = (EditText) view.findViewById(R.id.moduleName);
        moduleCredits = (EditText) view.findViewById(R.id.moduleCredits);
        modulePreqCode = (EditText) view.findViewById(R.id.modulePreqCode);
        btnPathwaySave = (Button) view.findViewById(R.id.btnPathwaySave);

        btnPathwaySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }


    private void populateSpinners() {

        spinnerPathway.setPrompt("- Select -");

        //Dummy values
        String[] pathways = new String[]{"Software", "Database", "Networking", "Web"};
        String[] sems = new String[]{"1", "2", "3", "4", "5", "6"};

        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, pathways);
        spinnerPathway.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPathway.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aAdapt.setDropDownViewResource(R.layout.spinner_item);
        spinnerPathway.setAdapter(aAdapt);
        aAdapt.notifyDataSetChanged();

        //Semesters

        ArrayAdapter<String> aAdaptSems = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, sems);
        aAdaptSems.setDropDownViewResource(R.layout.spinner_item);
        spinnerSemester.setAdapter(aAdaptSems);
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSemester.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private boolean Save() {
        String _pathway = spinnerPathway.getSelectedItem().toString();
        String _moduleCode = moduleCode.getText().toString();
        String _moduleName = moduleName.getText().toString();
        String _modulePreReqs = modulePreqCode.getText().toString();
        String _semester = spinnerSemester.getSelectedItem().toString();

        RadioButton rb = (RadioButton) getView().findViewById(radiolevel.getCheckedRadioButtonId());
        String _level = rb.getText().toString();


        return true;
    }


    private boolean Validate(String value) {
        return true;
    }


}
