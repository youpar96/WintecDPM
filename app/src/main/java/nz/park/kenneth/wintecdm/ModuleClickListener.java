package nz.park.kenneth.wintecdm;

import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ModuleClickListener implements View.OnLongClickListener {
    View convertView;
    ImageView ivEdit, ivDelete;

    public ModuleClickListener(View convertView) {
        this.convertView = convertView;
        ivDelete = convertView.findViewById(R.id.deleteModule);

    }

    @Override
    public boolean onLongClick(View v) {

        EditText et = convertView.findViewById(R.id.lblListItem);
        et.setBackgroundColor(Color.LTGRAY);
        et.setCursorVisible(true);
        et.setFocusable(true);

        et.setInputType(InputType.TYPE_CLASS_TEXT);

        ivDelete.setImageResource(R.drawable.ic_delete_black_24dp);

        return true;
    }


};