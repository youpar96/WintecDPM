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
public class AddNewStudentFragment extends Fragment {

    DBHelper _dbhelper;

    ViewGroup viewContainer;

    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_GALLERY = 102;
    public static final String FILE_PREFERENCES = "FilePreferences";
    public static final String FILE_PREFERENCES_ID_STUDENT_KEY = "IdStudent";
    private ImageButton btnCameraAddNewStudent, btnGalleryAddNewStudent, btnGoPathwayAddNewStudent, btnGoBackStudentList;
    private CircleImageView ivPhotoAddNewStudent;
    private EditText etIdWintecAddNewStudent, etNameAddNewStudent, etDegreeAddNewStudent, etEmailStudentAddNewStudent;
    private Spinner spinnerPathwayAddNewStudent;
    private Button btnStudentSaveAddNewStudent;

    // ivPhotoAddNewStudent
    // btnGalleryAddNewStudent
    // btnCameraAddNewStudent
    // etIdWintecAddNewStudent
    // etNameAddNewStudent
    // etDegreeAddNewStudent
    // spinnerPathwayAddNewStudent
    // etEmailStudentAddNewStudent
    // btnStudentSaveAddNewStudent
    // btnGoPathwayAddNewStudent
    // btnGoBackStudentList

    private static int pathwayPosition = 0;
    private static Bitmap imagem;

    public AddNewStudentFragment() {

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

        final View view = inflater.inflate(R.layout.fragment_add_new_student, container, false);

        /*btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        ivPhotoProfile = view.findViewById(R.id.ivPhotoProfile);
        etIdWintec = view.findViewById(R.id.etIdWintec);
        etName = view.findViewById(R.id.etName);
        etDegree = view.findViewById(R.id.etDegree);
        etEmailStudentProfile = view.findViewById(R.id.etEmailStudentProfile);
        btnSave = view.findViewById(R.id.btnStudentProfileSave);
        spinnerPathway = view.findViewById(R.id.spinnerPathway);*/

        btnCameraAddNewStudent = view.findViewById(R.id.btnCameraAddNewStudent);
        btnGalleryAddNewStudent = view.findViewById(R.id.btnGalleryAddNewStudent);
        ivPhotoAddNewStudent = view.findViewById(R.id.ivPhotoAddNewStudent);
        etIdWintecAddNewStudent = view.findViewById(R.id.etIdWintecAddNewStudent);
        etNameAddNewStudent = view.findViewById(R.id.etNameAddNewStudent);
        etDegreeAddNewStudent = view.findViewById(R.id.etDegreeAddNewStudent);
        etEmailStudentAddNewStudent = view.findViewById(R.id.etEmailStudentAddNewStudent);
        spinnerPathwayAddNewStudent = view.findViewById(R.id.spinnerPathwayAddNewStudent);
        btnStudentSaveAddNewStudent = view.findViewById(R.id.btnStudentSaveAddNewStudent);
        btnGoPathwayAddNewStudent = view.findViewById(R.id.btnGoPathwayAddNewStudent);
        btnGoBackStudentList = view.findViewById(R.id.btnGoBackStudentList);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("Add new student");

        if (imagem != null)
            ivPhotoAddNewStudent.setImageBitmap(imagem);
        

        btnCameraAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }

            }
        });

        btnGalleryAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }
            }
        });

        btnStudentSaveAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validatePropertiesToSave(
                        etIdWintecAddNewStudent.getText().toString(),
                        etNameAddNewStudent.getText().toString(),
                        etDegreeAddNewStudent.getText().toString(),
                        etEmailStudentAddNewStudent.getText().toString())) {

                    int idWintec = Integer.parseInt(etIdWintecAddNewStudent.getText().toString());
                    String name = etNameAddNewStudent.getText().toString();
                    String degree = etDegreeAddNewStudent.getText().toString();
                    String email = etEmailStudentAddNewStudent.getText().toString();
                    Bitmap photo = ((BitmapDrawable) ivPhotoAddNewStudent.getDrawable()).getBitmap();

                    if (idWintecStudentExists(idWintec)) {
                        showToastMessage("Id Wintec already registered");
                    } else if (emailStudentExists(email)) {
                        showToastMessage("Email already registered");
                    } else {
                        saveNewStudent(idWintec, name, degree, photo, email);
                    }
                }
            }
        });

        btnGoBackStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectToStudentList();
            }
        });


        btnGoPathwayAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Profile.studentid = Integer.valueOf(etIdWintecAddNewStudent.getText().toString());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MyPathwayFragment())
                        .addToBackStack(this.getClass().getName())
                        .commit();


            }
        });


        //String[] pathwaysArr = Arrays.toString(Pathways.PathwayEnum.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, Arrays.copyOfRange(Profile.pathways, 1, Profile.pathways.length));

        spinnerPathwayAddNewStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPathwayAddNewStudent.setSelection(position);
                pathwayPosition = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aAdapt.setDropDownViewResource(R.layout.spinner_item);
        spinnerPathwayAddNewStudent.setAdapter(aAdapt);
        aAdapt.notifyDataSetChanged();


        //change method to student modules later
        //Profile.modules = _dbhelper.GetModulesForStudent(Profile.studentid);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


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
                    ivPhotoAddNewStudent.setImageBitmap(imagem);
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
            etIdWintecAddNewStudent.setBackground(shape);
        }

        if (name.matches("")) {
            isValid = false;
            etNameAddNewStudent.setBackground(shape);
        }

        if (degree.matches("")) {
            isValid = false;
            etDegreeAddNewStudent.setBackground(shape);
        }

        if (!email.matches("")) {
            if (!isEmailValid(email)) {
                isValid = false;
                etEmailStudentAddNewStudent.setBackground(shape);
            }
        }

        return isValid;
    }

    private void resetEditTextsConfiguration() {
        etIdWintecAddNewStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etIdWintecAddNewStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etNameAddNewStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etNameAddNewStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etDegreeAddNewStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etDegreeAddNewStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etEmailStudentAddNewStudent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etEmailStudentAddNewStudent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));
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


    private TableStudents createNewStudent(int idWintec, String name, String degree, Bitmap photo, String email) {

        TableStudents student = new TableStudents();

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

    private void saveNewStudent(int idWintec, String name, String degree, Bitmap photo, String email) {

        TableStudents _studentObj = createNewStudent(idWintec, name, degree, photo, email);

        boolean studentInserted = _dbhelper.InsertStudentProfile(_studentObj);

        if (studentInserted) {

            this.showToastMessage("Student registered!");


            //this.redirectToStudentList();
        }
    }

    private void showToastMessage(String message) {

        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toast.show();
    }

    private void redirectToStudentList() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentListFragment()).commit();
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private boolean emailStudentExists(String email) {
        TableStudents student = _dbhelper.GetStudentByEmail(email);

        if (student != null
                && student.get_id() > 0) return true;

        return false;
    }

    private boolean idWintecStudentExists(int idWintec) {
        TableStudents student = _dbhelper.GetStudentByWintecId(idWintec);

        if (student != null
                && student.get_id() > 0) return true;

        return false;
    }
}
