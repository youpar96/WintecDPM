package nz.park.kenneth.wintecdm.model;

import java.io.Serializable;

public class Client implements Serializable {

    private int Id;

    private String Name;

    public Client(String name) {
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
