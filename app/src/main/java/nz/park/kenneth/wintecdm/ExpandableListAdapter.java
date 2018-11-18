package nz.park.kenneth.wintecdm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private Map<String, List<Structure>> _mylistDataChild;
    private boolean _ismyPathway = false;

    private CheckListener _listener;
    private Boolean _isScroll = false;


    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;

    }


    public void setScroll(Boolean status) {
        _isScroll = status;
    }

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 Map<String, List<Structure>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._mylistDataChild = listChildData;
        _ismyPathway = true;

    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        Object child;
        if (_ismyPathway)
            child = this._mylistDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        else
            child = this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);

        return child;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Object _child = getChild(groupPosition, childPosition);
        String childText = _ismyPathway ? ((Structure) _child).getSubject() : (String) _child;
        final String[] childTextArr = childText.split("\\|");

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(_ismyPathway ? R.layout.my_list_item : R.layout.list_item, null);
        }

        TextView txtCode = (TextView) convertView.findViewById(_ismyPathway ? R.id.txtMyPathwayCode : R.id.txtPathwayCode);
        TextView txtName = (TextView) convertView.findViewById(_ismyPathway ? R.id.txtMyPathwayName : R.id.txtPathwayName);

        if (!_ismyPathway) {

            ImageView ivEdit = (ImageView) convertView.findViewById(R.id.ivEditModule);
            ivEdit.setVisibility(Profile.isAdmin ? View.VISIBLE : View.INVISIBLE);
            if (Profile.isAdmin) {
                //on click to update
//                Fragment fragment = new InputModuleFragment();
//                FragmentManager fm = (Activity) _context.getSupportFragmentManager();
//
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();


            }
        }

        //module link to website
        if (!_ismyPathway) {

            txtCode.setClickable(true);
            txtCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse(childTextArr[2].toString().trim()));
                    _context.startActivity(intent);

                }
            });

        }

        txtCode.setText(childTextArr[0]);
        txtName.setText(childTextArr[1]);

        if (_ismyPathway) {


            SwitchCompat swtchbox = convertView.findViewById(R.id.switchCompleted);
            Structure _val = (Structure) _child;
            swtchbox.setChecked(_val.getCompleted());

            // _listener= new CheckListener(convertView.getRootView());

            //swtchbox.setOnCheckedChangeListener(null);
            swtchbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    //SwitchCompat checkbox = (SwitchCompat) buttonView;
                    if (!_isScroll) {
                        int index = (4 * groupPosition) + childPosition;

                        Profile.modules.get(index).set_is_completed(isChecked);

                        Structure _child = (Structure) getChild(groupPosition, childPosition);
                        _child.setCompleted(isChecked);
                    }
                }
            });


            boolean _enabled = _val.getEnabled();


            swtchbox.setEnabled(_enabled);
            swtchbox.setAlpha(_enabled ? 1f : 0.5f);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        if (_ismyPathway)
            count = this._mylistDataChild.get(this._listDataHeader.get(groupPosition)).size();
        else
            count = this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();

        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_group, null);
        }


        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        // change arrow images by expanded status
        ImageView lblListHeaderImg = convertView.findViewById(R.id.lblListHeaderImg);
        if (isExpanded) {
            lblListHeaderImg.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        } else {
            lblListHeaderImg.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }

        if (_ismyPathway) {

        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
