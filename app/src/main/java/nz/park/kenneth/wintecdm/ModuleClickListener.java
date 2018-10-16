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
//        ivEdit = convertView.findViewById(R.id.editModule);
        ivDelete = convertView.findViewById(R.id.deleteModule);
        ivDelete.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onLongClick(View v) {

        EditText et = convertView.findViewById(R.id.lblListItem);
        et.setBackgroundColor(Color.LTGRAY);
        et.setCursorVisible(true);
        et.setInputType(InputType.TYPE_CLASS_TEXT);

//        ivEdit.setVisibility(View.VISIBLE);
//        ivEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });


        ivDelete.setVisibility(View.VISIBLE);

        return true;
    }


};