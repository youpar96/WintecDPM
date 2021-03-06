package nz.park.kenneth.wintecdm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Data.PreRequisites;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePathwayModules;
import nz.park.kenneth.wintecdm.database.Structure.TablePreRequisites;
import nz.park.kenneth.wintecdm.model.Pathway;

public class InputModuleFragment extends Fragment {

    Spinner spinnerPathway, spinnerSemester;
    EditText moduleCode, moduleName, moduleCredits;
    MultiAutoCompleteTextView modulePreqCode;
    RadioGroup radiolevel;
    Button btnModuleSave, btnModuleDelete;
    CheckBox chkPrereq, chkIsCombination;
    TextView tvPrereqCode;

    private DBHelper dbHelper;
    private int pathwayPosition;

    private boolean isUpdate = false;

    public InputModuleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_input_module, container, false);
        dbHelper = new DBHelper(getContext(), null);

        initialize(view);
        populateSpinners();
        return view;
    }

    @Override
    public void onStart() {
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        String title = "Create New Module";
        super.onStart();
        Bundle args = getArguments();

        if (args != null) {
            isUpdate = args.getBoolean("IsUpdate");

            if (isUpdate) {

                title = "Update Module";
                String moduleCode = args.getString("moduleCode");

                TableModules module = dbHelper.SelectModuleDetails(moduleCode);
                LoadContent(module);

            }
        }
        toolbar.setTitle(title);
        btnModuleDelete.setVisibility(isUpdate ? View.VISIBLE : View.GONE);
    }


    private void initialize(View view) {
        spinnerPathway = (Spinner) view.findViewById(R.id.spinnerPathway);
        spinnerSemester = (Spinner) view.findViewById(R.id.spinnerSemester);
        moduleCode = (EditText) view.findViewById(R.id.moduleCode);
        moduleName = (EditText) view.findViewById(R.id.moduleName);
        moduleCredits = (EditText) view.findViewById(R.id.moduleCredits);
        modulePreqCode = (MultiAutoCompleteTextView) view.findViewById(R.id.modulePreqCode);
        btnModuleSave = (Button) view.findViewById(R.id.btnModuleSave);
        btnModuleDelete = (Button) view.findViewById(R.id.btnModuleDelete);
        tvPrereqCode = view.findViewById(R.id.tvPrereqCode);
        radiolevel = view.findViewById(R.id.radioLevel);


        chkPrereq = (CheckBox) view.findViewById(R.id.chkPrereq);
        chkIsCombination = (CheckBox) view.findViewById(R.id.chkIsCombination);

        btnModuleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
        btnModuleDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("CONFIRMATION")
                        .setMessage(Html.fromHtml("<font color='#000'>Delete Module?</font>"))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Delete();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });

        chkPrereq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                modulePreqCode.setEnabled(isChecked);
                chkIsCombination.setEnabled(isChecked);

                Float _alpha = isChecked ? 1f : 0.5f;

                tvPrereqCode.setAlpha(_alpha);
                modulePreqCode.setAlpha(_alpha);
                chkIsCombination.setAlpha(_alpha);


                LoadPreReqAutoComplete();

            }
        });

    }

    private void LoadContent(TableModules module) {

        int pathway = Profile.selectedPath.ordinal(); //dbHelper.GetModulePathway(module.get_code());

        spinnerPathway.setSelection(pathway);

        String _moduleCode = module.get_code();
        moduleCode.setText(_moduleCode);
        moduleName.setText(module.get_name());
        moduleCredits.setText(String.valueOf(module.get_credits()));

        //another call

        List<TablePreRequisites> _preReqs = dbHelper.GetPreRequisites(pathway,_moduleCode);
        String _preReqText = "";
        List<Boolean> _isCombination = new ArrayList<>();
        if (_preReqs.size() > 0) {

            chkPrereq.setChecked(true);
            for (TablePreRequisites eachprereq : _preReqs) {
                _preReqText += eachprereq.get_prereqcode() + ",";
                _isCombination.add(eachprereq.is_is_combo());

            }

            modulePreqCode.setText(_preReqText);
            chkIsCombination.setChecked(!_isCombination.contains(false));
        }


        spinnerSemester.setSelection(module.get_sem() - 1);
        ((RadioButton) radiolevel.getChildAt(module.get_level() - 5)).setChecked(true);

    }


    private void populateSpinners() {

        String[] sems = new String[]{"1", "2", "3", "4", "5", "6"};


        //pathways
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, Profile.pathways);
        spinnerPathway.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPathway.setSelection(position);
                pathwayPosition = position;
                LoadPreReqAutoComplete();
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

        LoadPreReqAutoComplete();

    }

    private void LoadPreReqAutoComplete() {

        if (modulePreqCode.isEnabled()) {

            final List<String> moduleCodes = new ArrayList<>();
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    List<TableModules> modules = dbHelper.GetModulesByPathway(Pathways.PathwayEnum.values()[pathwayPosition]);
                    for (TableModules eachModule : modules) {
                        moduleCodes.add(eachModule.get_code());
                    }
                }
            });
            //Pre-req modules

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getActivity(), R.layout.spinner_item, moduleCodes);

            modulePreqCode.setThreshold(1);
            modulePreqCode.setAdapter(adapter);
            modulePreqCode.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        }
    }


    private boolean Save() {
        String _pathway = spinnerPathway.getSelectedItem().toString();
        String _moduleCode = moduleCode.getText().toString();
        String _moduleName = moduleName.getText().toString();
        String _moduleCredits = moduleCredits.getText().toString();
        String _modulePreReqs = modulePreqCode.getText().toString();
        String _semester = spinnerSemester.getSelectedItem().toString();

        RadioButton _radioButton = (RadioButton) getView().findViewById(radiolevel.getCheckedRadioButtonId());
        String _level = _radioButton.getText().toString();


        String _msg = "";

        if (_moduleCode.isEmpty())
            _msg = "Please enter module code";
        else if (_moduleName.isEmpty())
            _msg = "Please enter module name";
        else if (_moduleCredits.isEmpty())
            _msg = "Please enter module credits";
        else if (!isInt(_moduleCredits))
            _msg = "Please enter numeric value for credits";

        else {

            //Modules
            TableModules _moduleObj = new TableModules();
            _moduleObj.set_code(_moduleCode);
            _moduleObj.set_name(_moduleName);
            _moduleObj.set_credits(Integer.valueOf(_moduleCredits));
            _moduleObj.set_sem(Integer.valueOf(_semester));
            _moduleObj.set_level(Integer.valueOf(_level));

            dbHelper.InsertModule(_moduleObj);
            //Pathway Module
            dbHelper.InsertPathwayModules(new TablePathwayModules(pathwayPosition, _moduleCode));


            //Prerequisites
            if (!_modulePreReqs.isEmpty()) {
                String[] _prereqs = _modulePreReqs.split(",");
                boolean _isCombination = chkIsCombination.isChecked();

                dbHelper.InsertPrereq(Pathways.PathwayEnum.values()[pathwayPosition],
                        _moduleCode, Arrays.copyOf(_prereqs, _prereqs.length - 1), _isCombination);
            }

            _msg = "Saved Successfully!";
            ClearValues();

        }

        if (!_msg.isEmpty()) {
            Toast toast = Toast.makeText(getContext(), _msg, Toast.LENGTH_LONG);
            View view = toast.getView();
            TextView text = (TextView) view.findViewById(android.R.id.message);
            text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            toast.show();
        }

        return true;
    }

    private Boolean Delete() {
        boolean _return = false;
        String _msg = null;
        String _moduleCode = moduleCode.getText().toString();

        if (!_moduleCode.isEmpty()) {
            _return = dbHelper.DeleteModuleByCode(_moduleCode);
            ClearValues();
        }

        return _return;
    }


    private void ClearValues() {
        spinnerPathway.setSelection(0);
        moduleCode.setText("");
        moduleName.setText("");
        moduleCredits.setText("");
        modulePreqCode.setText("");
        spinnerSemester.setSelection(0);


    }

    private boolean isInt(String d) {
        boolean _return = false;
        try {
            Integer.parseInt(d);
            _return = true;
        } catch (Exception ex) {

        }
        return _return;
    }


}
