package nz.park.kenneth.wintecdm;

import java.util.ArrayList;
import java.util.List;

import nz.park.kenneth.wintecdm.database.Structure.TableModules;

public class Profile {

    public static boolean isAdmin=false;
    public static int studentid=1000; //Test
    public static List<TableModules> modules;

    static {
        modules = new ArrayList<>();

    }


    public static final String PACKAGE = "nz.park.kenneth.wintecdm.database.";  //common package path
    public static final String STRUCTURE = "Structure.Table";

}
