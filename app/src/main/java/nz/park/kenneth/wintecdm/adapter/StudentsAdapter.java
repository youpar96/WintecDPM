package nz.park.kenneth.wintecdm.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nz.park.kenneth.wintecdm.R;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;
import nz.park.kenneth.wintecdm.model.Student;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder> {

    private List<TableStudents> studentsList;
    private Context _context;

    public StudentsAdapter(List<TableStudents> studentsList, Context context) {
        this.studentsList = studentsList;
        this._context = context;
    }

    @NonNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemList = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_list_students, viewGroup, false);

        StudentsViewHolder studentsViewHolder = new StudentsViewHolder(itemList);

        return studentsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolder studentsViewHolder, int position) {

        TableStudents student = studentsList.get(position);

        if(student.get_wintec_id() <= 0)
        {
            studentsViewHolder.tvStudentListIdWintec.setVisibility(View.GONE);
            studentsViewHolder.tvStudentListDegree.setVisibility(View.GONE);

            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;*/

            studentsViewHolder.tvStudentListName.setText(student.get_name());
            //studentsViewHolder.tvStudentListName.setLayoutParams(layoutParams);
            studentsViewHolder.tvStudentListName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        }
        else {
            studentsViewHolder.tvStudentListIdWintec.setText(String.valueOf(student.get_wintec_id()));
            studentsViewHolder.tvStudentListName.setText(student.get_name());
            studentsViewHolder.tvStudentListDegree.setText(student.get_degree());
        }

        if(student.get_photo() == null)
        {
            //int idPhotoAvatar = _context.getResources().getIdentifier(R.drawable.avatar,)
            studentsViewHolder.ivStudentListPhoto.setImageDrawable(_context.getResources().getDrawable(R.drawable.avatar));
        }
        else
        {
            studentsViewHolder.ivStudentListPhoto.setImageBitmap(BitmapFactory.decodeByteArray(student.get_photo(), 0, student.get_photo().length));
        }

    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class StudentsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivStudentListPhoto;
        TextView tvStudentListName;
        TextView tvStudentListDegree;
        TextView tvStudentListIdWintec;

        public StudentsViewHolder(@NonNull View itemView) {
            super(itemView);

            ivStudentListPhoto = itemView.findViewById(R.id.ivStudentListPhoto);
            tvStudentListName = itemView.findViewById(R.id.tvStudentListName);
            tvStudentListDegree = itemView.findViewById(R.id.tvStudentListDegree);
            tvStudentListIdWintec = itemView.findViewById(R.id.tvStudentListIdWintec);
        }
    }
}