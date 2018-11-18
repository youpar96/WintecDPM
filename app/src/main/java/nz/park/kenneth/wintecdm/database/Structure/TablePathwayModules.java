package nz.park.kenneth.wintecdm.database.Structure;

import nz.park.kenneth.wintecdm.database.FieldOrder;

public class TablePathwayModules {

    public static final String COLUMN_ID = "id";
    @FieldOrder(order = 1)
    public static final String COLUMN_ID_PATHWAY = "pathway"; //code for both
    @FieldOrder(order = 2)
    public static final String COLUMN_ID_MODULE = "module";


    private int pathway;
    private String module;

    public TablePathwayModules(){

    }

    public TablePathwayModules(int pathway, String module) {

        this.pathway = pathway;
        this.module = module;
    }

    public int getPathway() {
        return this.pathway;
    }

    public String getModule() {
        return this.module;
    }


}
