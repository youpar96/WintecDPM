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
public class ProfileFragment extends Fragment {

    DBHelper _dbhelper;

    ViewGroup viewContainer;

    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_GALLERY = 102;
    public static final String FILE_PREFERENCES = "FilePreferences";
    public static final String FILE_PREFERENCES_ID_STUDENT_KEY = "IdStudent";
    private ImageButton btnCamera, btnGallery;
    private CircleImageView ivPhotoProfile;
    private EditText etIdWintec, etName, etDegree, etEmailStudentProfile;
    private Button btnSave;
    private static boolean studentExists = false;
    private Spinner spinnerPathway;

    private static int pathwayPosition = 0;

    public ProfileFragment() {

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

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        ivPhotoProfile = view.findViewById(R.id.ivPhotoProfile);
        etIdWintec = view.findViewById(R.id.etIdWintec);
        etName = view.findViewById(R.id.etName);
        etDegree = view.findViewById(R.id.etDegree);
        etEmailStudentProfile = view.findViewById(R.id.etEmailStudentProfile);
        btnSave = view.findViewById(R.id.btnStudentProfileSave);
        spinnerPathway = view.findViewById(R.id.spinnerPathway);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("My Profile");

        this.LoadStudentDetails();

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validatePropertiesToSave(etIdWintec.getText().toString(), etName.getText().toString(), etDegree.getText().toString(), etEmailStudentProfile.getText().toString())) {
                    int idWintec = Integer.parseInt(etIdWintec.getText().toString());
                    String name = etName.getText().toString();
                    String degree = etDegree.getText().toString();
                    String email = etEmailStudentProfile.getText().toString();
                    Bitmap photo = ((BitmapDrawable) ivPhotoProfile.getDrawable()).getBitmap();

                    saveProfile(idWintec, name, degree, photo, email);
                }
            }
        });


        //String[] pathwaysArr = Arrays.toString(Pathways.PathwayEnum.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, Arrays.copyOfRange(Profile.pathways, 1, Profile.pathways.length));

        spinnerPathway.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPathway.setSelection(position);
                pathwayPosition = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aAdapt.setDropDownViewResource(R.layout.spinner_item);
        spinnerPathway.setAdapter(aAdapt);
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
                    ivPhotoProfile.setImageBitmap(imagem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfile(int idWintec, String name, String degree, Bitmap photo, String email) {

        if (studentExists) {
            //SharedPreferences preferences = getContext().getSharedPreferences(FILE_PREFERENCES, getContext().MODE_PRIVATE);
            //String idStudent = preferences.getString(FILE_PREFERENCES_ID_STUDENT_KEY, "0");
            int idStudent = this.getIdStudentSharedPreferences();
            TableStudents student = _dbhelper.GetStudentById(idStudent);

            TableStudents studentToUpdate = updatePropertiesStudent(student, idWintec, name, degree, photo, email);
            _dbhelper.UpdateStudentByID(studentToUpdate);

            this.showToastMessage("Profile updated!");
        } else {
            TableStudents _studentObj = updatePropertiesStudent(new TableStudents(), idWintec, name, degree, photo, email);

            boolean studentInserted = _dbhelper.InsertStudentProfile(_studentObj);

            if (studentInserted) {
                TableStudents student = _dbhelper.GetStudentByWintecId(idWintec);

                // set IdWintec saved to get Student Details when load MyProfile screen
                this.setIdStudentSharedPreferences(student.get_wintec_id());

                /*SharedPreferences preferences = getContext().getSharedPreferences(FILE_PREFERENCES, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(FILE_PREFERENCES_ID_STUDENT_KEY, String.valueOf(student.get_id()));
                editor.commit();*/

                this.showToastMessage("Profile created!");
            }
        }

        //Student
        /*TableStudents _studentObj = new TableStudents();

        _studentObj.set_wintec_id(Integer.parseInt(etIdWintec.getText().toString()));
        _studentObj.set_name(etName.getText().toString());
        _studentObj.set_degree(etDegree.getText().toString());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] photoBlob = byteArrayOutputStream.toByteArray();

        _studentObj.set_photo(photoBlob);

        if(!etEmailStudentProfile.getText().toString().matches(""))
        {
            _studentObj.set_email(etEmailStudentProfile.getText().toString());
        }*/
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
            etIdWintec.setBackground(shape);
        }

        if (name.matches("")) {
            isValid = false;
            etName.setBackground(shape);
        }

        if (degree.matches("")) {
            isValid = false;
            etDegree.setBackground(shape);
        }

        if (!email.matches("")) {
            if (!isEmailValid(email)) {
                isValid = false;
                etEmailStudentProfile.setBackground(shape);
            }
        }

        return isValid;
    }

    private void resetEditTextsConfiguration() {
        etIdWintec.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etIdWintec.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etName.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etDegree.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etDegree.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));

        etEmailStudentProfile.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        etEmailStudentProfile.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yellow_back_black_border));
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

    private void LoadStudentDetails() {
        // pega o shared preferences
        //SharedPreferences preferences = getContext().getSharedPreferences(FILE_PREFERENCES, getContext().MODE_PRIVATE);

        if (this.hasSharedPreferencesFile()) {
            //if(preferences.contains(FILE_PREFERENCES_ID_STUDENT_KEY))
            //{
            //String idStudent = preferences.getString(FILE_PREFERENCES_ID_STUDENT_KEY, "0");
            //editor.putString("studentId", String.valueOf(student.get_id()));

            //GetStudentById
            //TableStudents student = _dbhelper.GetStudentById(Integer.parseInt(idStudent));
            //TableStudents student = _dbhelper.GetStudentById(6);

            int idStudent = getIdStudentSharedPreferences();
            TableStudents student = _dbhelper.GetStudentByWintecId(idStudent);

            if (student != null
                    && student.get_id() > 0) {
                etIdWintec.setText(String.valueOf(student.get_wintec_id()));
                etName.setText(student.get_name());
                etDegree.setText(student.get_degree());
                etEmailStudentProfile.setText(student.get_email());

                //ivPhotoProfile.setImageBitmap(imagem);
                ivPhotoProfile.setImageBitmap(BitmapFactory.decodeByteArray(student.get_photo(), 0, student.get_photo().length));

                studentExists = true;
            }
        }
    }

    private TableStudents updatePropertiesStudent(TableStudents student, int idWintec, String name, String degree, Bitmap photo, String email) {
        student.set_wintec_id(idWintec);
        student.set_name(name);
        student.set_degree(degree);
        student.set_pathway(pathwayPosition);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] photoBlob = byteArrayOutputStream.toByteArray();

        student.set_photo(photoBlob);

        if (!email.matches("")) {
            student.set_email(email);
        }

        return student;
    }

    private boolean hasSharedPreferencesFile() {
        SharedPreferences preferences = getContext().getSharedPreferences(FILE_PREFERENCES, getContext().MODE_PRIVATE);

        return preferences.contains(FILE_PREFERENCES_ID_STUDENT_KEY);
    }

    private int getIdStudentSharedPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences(FILE_PREFERENCES, getContext().MODE_PRIVATE);
        int idStudent = preferences.getInt(FILE_PREFERENCES_ID_STUDENT_KEY, 0);

        return idStudent;
    }

    private void setIdStudentSharedPreferences(int idStudent) {
        SharedPreferences preferences = getContext().getSharedPreferences(FILE_PREFERENCES, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(FILE_PREFERENCES_ID_STUDENT_KEY, idStudent);
        editor.commit();
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        //toast.setTex
        View toastView = toast.getView();

        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        toastMessage.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.wintecBasicBackground));
        toast.show();
    }
}
