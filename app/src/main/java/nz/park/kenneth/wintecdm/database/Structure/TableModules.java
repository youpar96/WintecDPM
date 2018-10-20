package nz.park.kenneth.wintecdm.database.Structure;

import nz.park.kenneth.wintecdm.database.FieldOrder;

public class TableModules {



    public static final String COLUMN_ID = "id";
    @FieldOrder(order = 1)
    public static final String COLUMN_NAME = "name";
    @FieldOrder(order = 2)
    public static final String COLUMN_CODE = "code";
    @FieldOrder(order = 3)
    public static final String COLUMN_CREDITS = "credits";
    @FieldOrder(order = 4)
    public static final String COLUMN_PREREQ = "prereq";
    @FieldOrder(order = 5)
    public static final String COLUMN_DETAILS = "details";
    @FieldOrder(order = 6)
    public static final String COLUMN_SEMESTER = "semester";
    @FieldOrder(order = 7)
    public static final String COLUMN_LEVEL = "level";
    @FieldOrder(order = 8)
    public static final String COLUMN_URL = "url";



    private int _id;
    private String _name;
    private String _code;
    private int _credits;
    private String _prereq;
    private String _details;
    private int _sem;
    private int _level;
    private String _url;

    public TableModules(){

    }

    public TableModules(int id,String name,String code,int credits,String prereq,String details,int semester,int level,String url){
        this._id=id;
        this._name=name;
        this._code=code;
        this._credits=credits;
        this._prereq=prereq;
        this._sem=semester;
        this._level=level;
        this._url=url;

    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_code() {
        return _code;
    }

    public void set_code(String _code) {
        this._code = _code;
    }

    public int get_credits() {
        return _credits;
    }

    public void set_credits(int _credits) {
        this._credits = _credits;
    }

    public String get_prereq() {
        return _prereq;
    }

    public void set_prereq(String _prereq) {
        this._prereq = _prereq;
    }

    public String get_details() {
        return _details;
    }

    public void set_details(String _details) {
        this._details = _details;
    }

    public int get_sem() {
        return _sem;
    }

    public void set_sem(int _sem) {
        this._sem = _sem;
    }

    public int get_level() {
        return _level;
    }

    public void set_level(int _level) {
        this._level = _level;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }




}
