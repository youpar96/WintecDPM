package nz.park.kenneth.wintecdm;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class CheckListener implements SwitchCompat.OnCheckedChangeListener {

    View _view;
    int _checkFlag = 0;

    public CheckListener(View view) {
        this._view = view;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



        ViewGroup _group = (ViewGroup) _view;
        View _currentView;
        //if (isChecked) {
        for (int i = 0; i < _group.getChildCount(); i++) {
            _currentView = _group.getChildAt(i);

            if (_currentView instanceof SwitchCompat) {
                SwitchCompat _switch = (SwitchCompat) _currentView;

                boolean _isChecked = _switch.isChecked();


            }

        }

        //  }

    }


}
