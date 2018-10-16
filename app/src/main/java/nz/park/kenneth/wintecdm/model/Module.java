package nz.park.kenneth.wintecdm.model;

import java.io.Serializable;
import java.sql.Blob;

public class Module implements Serializable {

    private int Id;

    private String Name;

    private String Code;

    private int Credits;

    private String Prereq;

    private String Details;

    private int Semester;

    private int Level;

    private String Url;

    public Module(String name, String code, int credits, int semester, int level) {
        Name = name;
        Code = code;
        Credits = credits;
        Semester = semester;
        Level = level;
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

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getCredits() {
        return Credits;
    }

    public void setCredits(int credits) {
        Credits = credits;
    }

    public String getPrereq() {
        return Prereq;
    }

    public void setPrereq(String prereq) {
        Prereq = prereq;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public int getSemester() {
        return Semester;
    }

    public void setSemester(int semester) {
        Semester = semester;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
