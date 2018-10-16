package nz.park.kenneth.wintecdm.model;

import java.io.Serializable;
import java.sql.Blob;

public class Student implements Serializable {

    private int Id;

    private int IdWintec;

    private String Name;

    private String Degree;

    private Blob Photo;

    public Student(int idWintec, String name) {
        IdWintec = idWintec;
        Name = name;
    }

    public int getId() {
        return Id;
    }

    /*public void setId(int id) {
        Id = id;
    }*/

    public int getIdWintec() {
        return IdWintec;
    }

    public void setIdWintec(int idWintec) {
        IdWintec = idWintec;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public Blob getPhoto() {
        return Photo;
    }

    public void setPhoto(Blob photo) {
        Photo = photo;
    }
}
