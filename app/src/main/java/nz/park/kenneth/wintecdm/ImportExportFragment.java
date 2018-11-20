package nz.park.kenneth.wintecdm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.FieldOrder;

public class ImportExportFragment extends Fragment {

    EditText etFolderSearch;
    Button btnSubmit, btnImport, btnExport;
    TextView tvTitle;

    private DBHelper dbHelper;

    private static boolean IsImport = true;
    private static final String PACKAGE = "nz.park.kenneth.wintecdm.database.";
    private static final String STRUCTURE = "Structure.Table";
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_import_export, container, false);

        dbHelper = new DBHelper(getContext(), null);
        etFolderSearch = v.findViewById(R.id.etFolderSearch);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        btnImport = v.findViewById(R.id.btnImport);
        btnExport = v.findViewById(R.id.btnExport);
        tvTitle = v.findViewById(R.id.tvTitle);

        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("Import/Export");


        etFolderSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsImport = true;
                SetView();
            }
        });


        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsImport = false;
                SetView();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _directoryPath = etFolderSearch.getText().toString();

                if (!hasPermissions(getContext(), PERMISSIONS))
                    ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, 112);


                if (IsImport)
                    importDB(_directoryPath);
                else
                    exportDB(_directoryPath);
            }
        });


        return v;
    }

    private void SetView() {
        btnImport.setAlpha(IsImport ? 1 : 0.5f);
        btnExport.setAlpha(IsImport ? 0.5f : 1f);
        tvTitle.setText(IsImport ? "Import Data" : "Export Data");

    }

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        try {
            Uri data = result.getData();
            File myFile = new File(data.toString());

            etFolderSearch.setText(myFile.getAbsolutePath());

        } catch (Exception ex) {


        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public void importDB(String storageDirectoryPath) {

        try {

            File directory = Environment.getExternalStorageDirectory();
            File exportFile = new File(directory.getAbsolutePath() + "/WINTEC_DPM");


            File folder = exportFile; // new File(storageDirectoryPath);
            for (File file : folder.listFiles()) {

                String tableName = file.getName();
                tableName = tableName.substring(0,tableName.indexOf('.'));

                InputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                List<String> data = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
                dbHelper.ImportData(DBHelper.Tables.valueOf(tableName), data);
                inputStream.close();

            }


        } catch (Exception ex) {

            Log.e("[Fn] Error in Import: ", ex.toString());
        }
    }


    //check storageDirectoryPath issue later
    public void exportDB(String storageDirectoryPath) {

        String _msg = "";
        try {


            for (DBHelper.Tables table : DBHelper.Tables.values()) {

                // File exportFile = new File(storageDirectoryPath + "/" + table.toString() + ".csv");
//                if (!exportFile.getParentFile().exists())
//                    exportFile.getParentFile().mkdirs();
//

                File directory = Environment.getExternalStorageDirectory();
                File exportFile = new File(directory.getAbsolutePath() + "/WINTEC_DPM/" + table.toString() + ".csv");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                exportFile.createNewFile();

                FileOutputStream fOut = new FileOutputStream(exportFile, false);
                OutputStreamWriter streamWriter = new OutputStreamWriter(fOut);

                List<String> data = dbHelper.ExportData(table);

                for (String each : data) {
                    streamWriter.append(each);
                    streamWriter.append("\n");
                }

                streamWriter.close();
                fOut.close();

            }
            _msg = "Exported successfully!";

        } catch (Exception ex) {
            Log.e("[Fn] Error in Export: ", ex.toString());
            _msg = "Unsuccessful";

        } finally {

            dbHelper.close();
            Toast toast = Toast.makeText(getContext(), _msg, Toast.LENGTH_LONG);
            View view = toast.getView();
            TextView text = (TextView) view.findViewById(android.R.id.message);
            text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            toast.show();
        }


    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
