package nz.park.kenneth.wintecdm.database.Structure;

import nz.park.kenneth.wintecdm.database.FieldOrder;

public class TablePreRequisites {

    public static final String COLUMN_ID="id";
    private String _code;
    private String _prereq;
    private boolean _is_combo;


    public TablePreRequisites(){

    }


    public TablePreRequisites(String code,boolean is_combo){

        this._code=code;
        this._is_combo=is_combo;

    }

    public String get_prereqcode() {
        return _prereq;
    }

    public void set_prereqcode(String _code) {
        this._prereq = _code;
    }

    public String get_code() {
        return _code;
    }

    public void set_code(String _code) {
        this._code = _code;
    }

    public boolean is_is_combo() {
        return _is_combo;
    }

    public void set_is_combo(boolean _is_combo) {
        this._is_combo = _is_combo;
    }

    @FieldOrder(order = 1)
    public static final String COLUMN_STREAM="stream";
    @FieldOrder(order = 2)
    public static final String COLUMN_CODE = "code";
    @FieldOrder(order = 3)
    public static final String COLUMN_PREREQ = "prereq";
    @FieldOrder(order = 4)
    public static final String COLUMN_COMBINATION = "is_combination";


}
