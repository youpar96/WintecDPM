package nz.park.kenneth.wintecdm.database.Structure;

import nz.park.kenneth.wintecdm.database.FieldOrder;

public class TableStudents {

    public static final String COLUMN_ID = "id";
    @FieldOrder(order = 1)
    public static final String COLUMN_ID_WINTEC = "wintec_id";
    @FieldOrder(order = 2)
    public static final String COLUMN_NAME = "name";
    @FieldOrder(order = 3)
    public static final String COLUMN_DEGREE = "degree";
    @FieldOrder(order = 4)
    public static final String COLUMN_PHOTO = "photo";


    private int _id;

    private int _wintec_id;
    private String _name;
    private String _degree;
    private String _photo;

    public TableStudents() {
    }

    public TableStudents(int id, int wintec_id, String name, String degree, String photo) {
        this._id = id;
        this._wintec_id = wintec_id;
        this._name = name;
        this._degree = degree;
        this._photo = photo;

    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_wintec_id() {
        return _wintec_id;
    }

    public void set_wintec_id(int _wintec_id) {
        this._wintec_id = _wintec_id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_degree() {
        return _degree;
    }

    public void set_degree(String _degree) {
        this._degree = _degree;
    }

    public String get_photo() {
        return _photo;
    }

    public void set_photo(String _photo) {
        this._photo = _photo;
    }


}


