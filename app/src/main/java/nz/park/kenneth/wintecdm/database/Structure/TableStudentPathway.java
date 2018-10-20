package nz.park.kenneth.wintecdm.database.Structure;

public class TableStudentPathway {

    //Edit later

    public static final String COLUMN_ID = "id";
//    public static final String COLUMN_ID_STUDENT = "wintec_id";
//    public static final String COLUMN_ID_PATHWAY = "idPathway";
//    public static final String COLUMN_COMPLETED = "completed";

    public TableStudentPathway(){}

    private int _student_id;
    private int _module_id;
    private boolean _is_completed;

    public TableStudentPathway(int studentid, int moduleid, boolean iscompleted){
        this._student_id=studentid;
        this._module_id=moduleid;
        this._is_completed=iscompleted;
    }

    public int get_student_id() {
        return _student_id;
    }

    public void set_student_id(int _student_id) {
        this._student_id = _student_id;
    }

    public int get_module_id() {
        return _module_id;
    }

    public void set_module_id(int _module_id) {
        this._module_id = _module_id;
    }

    public boolean is_is_completed() {
        return _is_completed;
    }

    public void set_is_completed(boolean _is_completed) {
        this._is_completed = _is_completed;
    }




}
