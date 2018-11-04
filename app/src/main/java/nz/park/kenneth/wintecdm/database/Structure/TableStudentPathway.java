package nz.park.kenneth.wintecdm.database.Structure;

import nz.park.kenneth.wintecdm.database.FieldOrder;

public class TableStudentPathway {

    public static final String COLUMN_ID = "id";
    @FieldOrder(order = 1)
    public static final String COLUMN_STUDENT_ID = "student_id";
    @FieldOrder(order = 2)
    public static final String COLUMN_MODULE = "module";
    @FieldOrder(order = 3)
    public static final String COLUMN_IS_COMPLETED = "is_completed";
//    @FieldOrder(order = 4)
//    public static final String COLUMN_IS_ENABLED = "is_enabled";


    private int _student_id;
    private String _module_code;
    private int _sem;
    private String _module_name;
    private boolean _is_completed;
    private boolean _is_enabled;

    public TableStudentPathway(String modulecode, boolean iscompleted, boolean isenabled) {
        this._module_code = modulecode;
        this._is_completed = iscompleted;
        this._is_enabled = isenabled;

    }

    public TableStudentPathway(int studentid, int sem, String modulecode, String module, boolean iscompleted, boolean isenabled) {
        this._student_id = studentid;
        this._sem = sem;
        this._module_code = modulecode;
        this._module_name = module;
        this._is_completed = iscompleted;
        this._is_enabled = isenabled;
    }

    public int get_student_id() {
        return _student_id;
    }

    public int get_sem() {
        return _sem;
    }

    public String get_module_code() {
        return _module_code;
    }

    public String get_module() {
        return _module_name;
    }

    public boolean get_is_completed() {
        return _is_completed;
    }

    public boolean get_is_enabled() {
        return _is_enabled;
    }


}
