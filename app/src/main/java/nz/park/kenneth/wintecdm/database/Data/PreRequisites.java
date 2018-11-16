package nz.park.kenneth.wintecdm.database.Data;

public class PreRequisites {

    //Confirm if specialization needed
    //Specialization, Module, Pre-requisite Module, Is pre-req combination of multiple subjects
    public String[][] content = {

            //General
            {"0", "COMP601​", "INFO502​​", "0"},
            {"0", "INFO601​", "INFO503​", "0"},
            {"0", "COMP602​", "INFO502​​", "0"},
            {"0", "COMP602​", "COMP502​", "0"},
            {"0", "COMP603", "COMP602", "0"},
            {"0", "BIZM701​", "INFO602​", "0"},
            {"0", "INFO701​", "INFO502​​", "0"},

            //Networking
            {"1", "COMP604​", "COMP503​", "0"},
            {"1", "COMP701​", "INFO603​", "0"},
            {"1", "COMP702​", "COMP604​", "0"},
            {"1", "COMP704", "COMP604​", "0"},
            {"1", "COMP705​", "COMP702​", "0"},
            {"1", "INFO702​", "COMP504​", "0"},
            {"1", "INFO702​", "COMP601​​", "0"},
            {"1", "COMP703", "INFO603", "0"},
            {"1", "COMP703", "INFO701", "0"},
            {"1", "COMP703", "COMP702", "0"},

            //Software
            {"2", "COMP605​", "COMP601", "1"},
            {"2", "COMP605​", "MATH601", "1"},
            {"2", "MATH602​", "MATH601​", "0"},
            {"2", "INFO703​", "COMP605​", "1"},
            {"2", "INFO703​", "MATH602​", "1"},
            {"2", "COMP707​", "COMP605​", "0"},
            {"2", "COMP706​", "COMP601​", "1"},
            {"2", "COMP706​", "COMP605​", "1"},
            {"2", "COMP706​", "MATH602​", "1"},
            {"2", "COMP709", "COMP601​", "1"},
            {"2", "COMP709", "COMP605​", "1"},
            {"2", "COMP709", "MATH602​", "1"},
            {"2", "COMP601", "MATH602", "0"},
            {"2", "COMP708", "COMP601", "1"},
            {"2", "COMP708", "MATH602", "1"},


            //Database
            {"3", "COMP606​", "COMP602​", "0"},
            {"3", "INFO604​", "INFO601", "0"},
            {"3", "INFO604​", "INFO503​", "0"},
            {"3", "INFO706​", "INFO601​", "0"},
            {"3", "INFO706​", "INFO604​", "0"},
            {"3", "INFO707​", "INFO601​", "0"},
            {"3", "INFO707​", "INFO604​", "0"},
            {"3", "COMP709​", "COMP601​", "0"},
            {"3", "COMP709​", "MATH601", "0"},
            {"3", "INFO704​", "​INFO601", "0"},
            {"3", "COMP710", "COMP601", "0"},
            {"3", "COMP710", "COMP605", "0"},
            {"3", "COMP710", "MATH602", "0"},
            {"3", "INFO705", "INFO601", "0"},
            {"3", "INFO705", "INFO604", "0"},

            //Web
            {"4", "COMP606​", "COMP602​", "0"},
            {"4", "COMP607​", "COMP602​", "0"},
            {"4", "INFO708​", "COMP606​", "0"},
            {"4", "INFO708​", "COMP607​", "0"},
            {"4", "COMP706", "COMP601", "0"},
            {"4", "COMP706", "COMP605", "0"},
            {"4", "COMP706", "MATH602", "0"},
            {"4", "COMP702", "COMP504​", "0"},
            {"4", "COMP702", "COMP601​", "0"},
            {"4", "COMP709​", "COMP601", "0"},
            {"4", "COMP709​", "MATH601", "0"},
            {"4", "COMP709​", "COMP605", "0"},
            {"4", "COMP709​", "MATH605", "0"},
            {"4", "COMP711", "COMP602", "0"},
            {"4", "COMP711", "COMP606", "0"}


    };


}
