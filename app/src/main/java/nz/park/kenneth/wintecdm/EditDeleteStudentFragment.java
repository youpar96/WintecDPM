package nz.park.kenneth.wintecdm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;
import nz.park.kenneth.wintecdm.model.Student;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class EditDeleteStudentFragment extends Fragment {

    DBHelper _dbhelper;

    ViewGroup viewContainer;

    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_GALLERY = 102;
    public static final String EDIT_FILE_PREFERENCES = "EditStudentFilePreferences";
    public static final String EDIT_FILE_PREFERENCES_ID_STUDENT_KEY = "IdStudent_Edit";

    private ImageButton btnCameraEditStudent, btnGalleryEditStudent, btnGoPathwayEditStudent, btnGoBackStudentList_Edit;
    private CircleImageView ivPhotoEditStudent;
    private EditText etIdWintecEditStudent, etNameEditStudent, etDegreeEditStudent, etEmailStudentEditStudent;
    private Spinner spinnerPathwayEditStudent;
    private Button btnStudentSaveEditStudent, btnDeleteStudent;

    private static int pathwayPosition = 0;

    public EditDeleteStudentFragment() {

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

        viewContainer = container;

        _dbhelper = new DBHelper(getContext(), null);

        final View view = inflater.inflate(R.layout.fragment_edit_delete_student, container, false);

        btnCameraEditStudent = view.findViewById(R.id.btnCameraEditStudent);
        btnGalleryEditStudent = view.findViewById(R.id.btnGalleryEditStudent);
        ivPhotoEditStudent = view.findViewById(R.id.ivPhotoEditStudent);
        etIdWintecEditStudent = view.findViewById(R.id.etIdWintecEditStudent);
        etNameEditStudent = view.findViewById(R.id.etNameEditStudent);
        etDegreeEditStudent = view.findViewById(R.id.etDegreeEditStudent);
        etEmailStudentEditStudent = view.findViewById(R.id.etEmailStudentEditStudent);
        spinnerPathwayEditStudent = view.findViewById(R.id.spinnerPathwayEditStudent);
        btnStudentSaveEditStudent = view.findViewById(R.id.btnStudentSaveEditStudent);
        btnDeleteStudent = view.findViewById(R.id.btnDeleteStudent);
        btnGoPathwayEditStudent = view.findViewById(R.id.btnGoPathwayEditStudent);
        btnGoBackStudentList_Edit = view.findViewById(R.id.btnGoBackStudentList_Edit);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("Edit student");

        this.LoadStudentDetails();

        btnCameraEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }

            }
        });

        btnGalleryEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }
            }
        });

        btnStudentSaveEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validatePropertiesToSave(
                        etIdWintecEditStudent.getText().toString(),
                        etNameEditStudent.getText().toString(),
                        etDegreeEditStudent.getText().toString(),
                        etEmailStudentEditStudent.getText().toString()))
                {

                    int idWintec = Integer.parseInt(etIdWintecEditStudent.getText().toString());
                    String name = etNameEditStudent.getText().toString();
                    String degree = etDegreeEditStudent.getText().toString();
                    String email = etEmailStudentEditStudent.getText().toString();
                    Bitmap photo = ((BitmapDrawable) ivPhotoEditStudent.getDrawable()).getBitmap();

                    if(idWintecStudentExists(idWintec))
                    {
                        showToastMessage("Id Wintec already registered");
                    }
                    else if(emailStudentExists(email))
                    {
                        showToastMessage("Email already registered");
                    }
                    else
                    {
                        updateStudent(idWintec, name, degree, photo, email);
                    }
                }
            }
        });

        btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteStudent();
            }
        });

        btnGoBackStudentList_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectToStudentList();
            }
        });


        //String[] pathwaysArr = Arrays.toString(Pathways.PathwayEnum.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, Arrays.copyOfRange(Profile.pathways, 1, Profile.pathways.length));

        spinnerPathwayEditStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPathwayEditStudent.setSelection(position);
                pathwayPosition = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aAdapt.setDropDownViewResource(R.layout.spinner_item);
        spinnerPathwayEditStudent.setAdapter(aAdapt);
        aAdapt.notifyDataSetChanged();


        //change method to student modules later
        //Profile.modules = _dbhelper.GetModulesForStudent(Profile.studentid);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case REQUEST_CODE_CAMERA:

                        imagem = (Bitmap) data.getExtras().get("data");
                        break;

                    case REQUEST_CODE_GALLERY:

                        Uri locationImage = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), locationImage);
                        break;
                }

                if (imagem != null) {
                    ivPhotoEditStudent.setImageBitmap(imagem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validatePropertiesToSave(String idWintec, String name, String degree, String email) {
        boolean isValid = true;

        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.RED);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(6);

        this.resetEditTextsConfiguration();

        if (idWintec.matches("")) {
            isValid = false;
            etIdWintecEditStudent.setBackground(shape);
        }

        if (name.matches("")) {
            isValid = false;
            etNameEditStudent.setBackground(shape);
        }

        if (degree.matches("")) {
            isValid = false;
            etDegreeEditStudent.setBackground(shape);
        }

        if (!email.matches("")) {
            if (!isEmailValid(email)) {
                isValid = false;
                etEmailStudentEditStudent.setBackground(shape);
            }
        }

        return isValid;
    }

    private void resetEditTextsConfiguration() {
        etIdWintecEditStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etIdWintecEditStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etNameEditStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etNameEditStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etDegreeEditStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etDegreeEditStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etEmailStudentEditStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etEmailStudentEditStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    private void updateStudent(int idWintec, String name, String degree, Bitmap photo, String email) {

        int idStudent = this.getIdStudentSharedPreferences();
        TableStudents student = _dbhelper.GetStudentById(idStudent);

        if(student != null
                && student.get_id() > 0)
        {
            TableStudents studentToUpdate = updatePropertiesStudent(student, idWintec, name, degree, photo, email);

            boolean hadSuccess = _dbhelper.UpdateStudentByID(studentToUpdate);

            if(hadSuccess)
            {
                this.showToastMessage("Student updated!");

                redirectToStudentList();
            }
        }
    }

    private TableStudents updatePropertiesStudent(TableStudents student, int idWintec, String name, String degree, Bitmap photo, String email) {
        student.set_wintec_id(idWintec);
        student.set_name(name.toUpperCase());
        student.set_degree(degree);
        student.set_pathway(pathwayPosition);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] photoBlob = byteArrayOutputStream.toByteArray();

        student.set_photo(photoBlob);

        if (!email.matches("")) {
            student.set_email(email.toLowerCase());
        }

        return student;
    }

    private void showToastMessage(String message) {

        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toast.show();
    }

    private void redirectToStudentList()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentListFragment()).commit();
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private boolean emailStudentExists(String email)
    {
        int idStudent = getIdStudentSharedPreferences();
        TableStudents student = _dbhelper.GetStudentByEmail(email);

        if(student != null
            && student.get_id() > 0
            && student.get_id() != idStudent) return true;

        return false;
    }

    private boolean idWintecStudentExists(int idWintec)
    {
        int idStudent = getIdStudentSharedPreferences();
        TableStudents student = _dbhelper.GetStudentByWintecId(idWintec);

        if(student != null
            && student.get_id() > 0
            && student.get_id() != idStudent) return true;

        return false;
    }

    private int getIdStudentSharedPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences(EDIT_FILE_PREFERENCES, getContext().MODE_PRIVATE);
        int idStudent = preferences.getInt(EDIT_FILE_PREFERENCES_ID_STUDENT_KEY, 0);

        return idStudent;
    }

    private void LoadStudentDetails() {

        if (this.hasSharedPreferencesFile()) {

            int idStudent = getIdStudentSharedPreferences();
            TableStudents student = _dbhelper.GetStudentById(idStudent);

            if (student != null
                    && student.get_id() > 0) {
                etIdWintecEditStudent.setText(String.valueOf(student.get_wintec_id()));
                etNameEditStudent.setText(student.get_name());
                etDegreeEditStudent.setText(student.get_degree());
                etEmailStudentEditStudent.setText(student.get_email());

                if(student.get_photo() != null) {
                    ivPhotoEditStudent.setImageBitmap(BitmapFactory.decodeByteArray(student.get_photo(), 0, student.get_photo().length));
                }
            }
        }
    }

    private boolean hasSharedPreferencesFile() {
        SharedPreferences preferences = getContext().getSharedPreferences(EDIT_FILE_PREFERENCES, getContext().MODE_PRIVATE);

        return preferences.contains(EDIT_FILE_PREFERENCES_ID_STUDENT_KEY);
    }

    private void deleteStudent()
    {
        if (this.hasSharedPreferencesFile()) {

            int idStudent = getIdStudentSharedPreferences();
            TableStudents student = _dbhelper.GetStudentById(idStudent);

            if (student != null
                    && student.get_id() > 0) {

                boolean hadSuccess = _dbhelper.DeleteStudentById(idStudent);

                if(hadSuccess)
                {
                    this.showToastMessage("Student deleted");

                    redirectToStudentList();
                }
            }
        }
    }
}
