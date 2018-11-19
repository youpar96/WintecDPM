package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ImportExportFragment extends Fragment {

    EditText etFolderSearch;
    Button btnSearch, btnImport, btnExport;
    TextView tvTitle;

    static boolean IsImport = true;

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


        etFolderSearch = v.findViewById(R.id.etFolderSearch);
        btnSearch = v.findViewById(R.id.btnSearch);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
