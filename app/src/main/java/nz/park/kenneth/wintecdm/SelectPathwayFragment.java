package nz.park.kenneth.wintecdm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectPathwayFragment extends Fragment implements View.OnClickListener {

    CardView cvSoftware, cvDatabase, cvNetworking, cvWebDevelopment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_pathway, container, false);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("Select Pathway");

        cvSoftware = view.findViewById(R.id.cvSoftware);
        cvDatabase = view.findViewById(R.id.cvDatabase);
        cvNetworking = view.findViewById(R.id.cvNetworking);
        cvWebDevelopment = view.findViewById(R.id.cvWebDevelopment);

        cvSoftware.setOnClickListener(this);
        cvDatabase.setOnClickListener(this);
        cvNetworking.setOnClickListener(this);
        cvWebDevelopment.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        String pathWay = null;

        switch (view.getId()) {
            case R.id.cvSoftware:
                pathWay = "S";
                break;
            case R.id.cvDatabase:
                pathWay = "D";
                break;
            case R.id.cvNetworking:
                pathWay = "N";
                break;
            case R.id.cvWebDevelopment:
                pathWay = "W";
                break;
            default:
                break;
        }

        ((NavigationMainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailPathwayFragment(pathWay)).commit();
    }
}
