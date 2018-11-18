package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class moduleEditOnClickListener implements View.OnClickListener {

    String moduleCode;
    Context context;

    public moduleEditOnClickListener(String moduleCode, Context context) {
        this.moduleCode = moduleCode;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) context;

        Bundle bundle = new Bundle();
        bundle.putBoolean("IsUpdate", true);
        bundle.putString("moduleCode", moduleCode);

        Fragment myFragment = new InputModuleFragment();
        myFragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack("SelectPathwayFragment").commit(); //

    }

}




