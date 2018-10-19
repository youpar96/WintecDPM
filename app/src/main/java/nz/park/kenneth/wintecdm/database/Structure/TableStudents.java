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
}


