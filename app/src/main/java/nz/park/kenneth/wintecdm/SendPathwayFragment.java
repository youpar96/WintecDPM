package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import nz.park.kenneth.wintecdm.database.DBHelper;
import nz.park.kenneth.wintecdm.database.Data.Pathways;
import nz.park.kenneth.wintecdm.database.Structure.TableModules;
import nz.park.kenneth.wintecdm.database.Structure.TableStudents;

public class SendPathwayFragment extends Fragment {

    private EditText etStudentID;
    private Button btnSend;
    private final String TEMPLATE = "email.html";
    private DBHelper _dbhelper;

    public SendPathwayFragment() {

    }

    public static SendPathwayFragment newInstance(String param1, String param2) {
        SendPathwayFragment fragment = new SendPathwayFragment();
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_send_pathway, container, false);
        _dbhelper = new DBHelper(getContext(), null);

        // change the title on toolbar
        Toolbar toolbar = (Toolbar) ((NavigationMainActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("Email Pathway");

        etStudentID = view.findViewById(R.id.etStudentID);
        btnSend = (Button) view.findViewById(R.id.btnEmail);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _msg = "";
                //Validation

                String studentID = etStudentID.getText().toString();
                if (studentID == null && studentID.isEmpty())
                    _msg = "Not a valid student ID";
                else if (!isInt(studentID))
                    _msg = "Please enter number only";
                else {

                    TableStudents studentDetails = _dbhelper.GetStudentByWintecId(Integer.parseInt(studentID));
                    List<TableModules> studentmodules = _dbhelper.GetModulesForStudent(Integer.parseInt(studentID));

                    if (studentDetails.get_id() == 0)
                        _msg = "Cant find Student ID!";
                    else if (studentmodules.size() == 0)
                        _msg = "Couldnt access modules";

                    else {

                        String Recepient = studentDetails.get_email();
                        String body = LoadData(); //getResources().getString(R.string.email_template);
                        String path = Pathways.PathwayEnum.values()[studentDetails.get_pathway()].toString();


                        body = body.replace("[NAME]", studentDetails.get_name());
                        body = body.replace("[PATH]", path);

                        int i = 1;
                        for (TableModules modules : studentmodules) {
                            body = body.replace(String.format("[CODE%d]", i), modules.get_code());
                            body = body.replace(String.format("[MODULE%d]", i), modules.get_name());
                            body = body.replace(String.format("[LEVEL%d]", i), modules.get_name());

                            i++;
                        }


                        if (SendMail(Recepient, body))
                            etStudentID.setText("");
                        
                    }


                }

                if (!_msg.isEmpty()) {
                    Toast toast = Toast.makeText(getContext(), _msg, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    TextView text = (TextView) view.findViewById(android.R.id.message);
                    text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    toast.show();
                }
            }
        });

        return view;
    }

    private boolean isInt(String value) {
        Boolean _return = false;
        try {
            Integer.parseInt(value);
            _return = true;
        } catch (Exception nfe) {

        }
        return _return;

    }

    private String LoadData() {
        String tContents = "";

        try {
            InputStream stream = getContext().getAssets().open(TEMPLATE);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

    private boolean SendMail(String recepient, String body) {

        String subject = "Degree Plan";
        String emailAddress = String.format("mailto:%s", recepient);

        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setType("text/html");
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body);
        mailIntent.setData(Uri.parse(emailAddress));
        mailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mailIntent);

        return true;
    }

}
