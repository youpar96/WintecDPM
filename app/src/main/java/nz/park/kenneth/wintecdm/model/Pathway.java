package nz.park.kenneth.wintecdm.model;

import java.io.Serializable;

public class Pathway implements Serializable {

    private int Id;

    private String Name;

    public Pathway(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    /*public void setId(int id) {
        Id = id;
    }*/

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
