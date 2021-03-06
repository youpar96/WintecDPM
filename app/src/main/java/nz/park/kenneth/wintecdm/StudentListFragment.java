package nz.park.kenneth.wintecdm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.adapter.StudentsAdapter;
import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;
import nz.park.kenneth.wintecdm.helper.RecyclerItemClickListener;
import nz.park.kenneth.wintecdm.model.Student;


public class StudentListFragment extends Fragment {

    DBHelper _dbhelper;
    ViewGroup viewContainer;

    public static final String EDIT_FILE_PREFERENCES = "EditStudentFilePreferences";
    public static final String EDIT_FILE_PREFERENCES_ID_STUDENT_KEY = "IdStudent_Edit";

    private RecyclerView rvStudentList;
    private StudentsAdapter StudentsAdapter;
    private ArrayList<TableStudents> listStudents = new ArrayList<>();

    public StudentListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewContainer = container;
        _dbhelper = new DBHelper(getContext(), null);

        final View view = inflater.inflate(R.layout.fragment_student_list, container, false);

        rvStudentList = view.findViewById(R.id.rvStudentList);

        // configure adapter
        StudentsAdapter = new StudentsAdapter(listStudents, getActivity());


        // configure recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStudentList.setLayoutManager(layoutManager);
        rvStudentList.setHasFixedSize(true);

        //DividerItemDecoration
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.custom_divider));
        rvStudentList.addItemDecoration(itemDecorator);

        rvStudentList.setAdapter(StudentsAdapter);

        //rvStudentList.addOnItemTouchListener
        rvStudentList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        rvStudentList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                TableStudents student = listStudents.get(position);
                                boolean addNewStudent = student.get_wintec_id() <= 0;

                                if (addNewStudent) {
                                    // fragment to add new student
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddNewStudentFragment()).commit();
                                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                    drawer.closeDrawer(GravityCompat.START);
                                } else {
                                    // Save student id in SharedPreferences
                                    setIdStudentSharedPreferences(student.get_id());

                                    // fragment to edit/delete student
                                    /*Toast toast = Toast.makeText(getContext(), student.get_name(), Toast.LENGTH_LONG);
                                    view = toast.getView();
                                    TextView text = (TextView) view.findViewById(android.R.id.message);
                                    text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                    toast.show();*/

                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditDeleteStudentFragment()).commit();
                                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        this.CreateAddNewStudent();

        return view;
    }

    private void CreateAddNewStudent() {
        TableStudents addNewStudent = new TableStudents();
        addNewStudent.set_name("Add new student");

        Bitmap iconAddNewStudent = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.add_new_student);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        iconAddNewStudent.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] photoBlob = stream.toByteArray();

        addNewStudent.set_photo(photoBlob);

        listStudents.add(addNewStudent);
    }

    @Override
    public void onStart() {
        super.onStart();

        setIdStudentSharedPreferences(0);

        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("Students");

        getAllStudents();
    }

    @Override
    public void onStop() {
        super.onStop();

        listStudents = new ArrayList<>();
    }

    public void getAllStudents() {
        List<TableStudents> studentList = _dbhelper.GetAllStudents();

        for (TableStudents student : studentList) {
            listStudents.add(student);
        }
    }

    private void setIdStudentSharedPreferences(int idStudent) {
        SharedPreferences preferences = getContext().getSharedPreferences(EDIT_FILE_PREFERENCES, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(EDIT_FILE_PREFERENCES_ID_STUDENT_KEY, idStudent);
        editor.commit();
    }
}