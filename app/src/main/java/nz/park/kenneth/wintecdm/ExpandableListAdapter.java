package nz.park.kenneth.wintecdm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private Map<String, List<Structure>> _mylistDataChild;
    private boolean _ismyPathway = false;

    private CheckListener _listener;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;

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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Object _child = getChild(groupPosition, childPosition);
        final String childText = _ismyPathway ? ((Structure) _child).getSubject() : (String) _child;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(_ismyPathway ? R.layout.my_list_item : R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(_ismyPathway ? R.id.mylblListItem : R.id.lblListItem);
        txtListChild.setText(childText);

        if (_ismyPathway) {

            CheckBox chbox = convertView.findViewById(R.id.chkCompleted);
            Structure _val = (Structure) _child;
            chbox.setChecked(_val.getCompleted());

            _listener= new CheckListener(convertView.getRootView());
            chbox.setOnCheckedChangeListener(_listener);


            boolean _enabled = _val.getEnabled();
            chbox.setEnabled(_enabled);
            txtListChild.setAlpha(_enabled ? 1f : 0.5f);
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
        if(isExpanded){
            lblListHeaderImg.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        }else{
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
