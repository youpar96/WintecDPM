package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutUs extends Fragment {

    private Button btnBack;

    public AboutUs() {
        // Required empty public constructor
    }


    public static AboutUs newInstance(String param1, String param2) {
        AboutUs fragment = new AboutUs();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_us, container, false);


        btnBack = v.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SelectPathwayFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("About Us");

        return v;
    }


}
