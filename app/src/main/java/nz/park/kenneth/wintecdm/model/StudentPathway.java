package nz.park.kenneth.wintecdm.model;

import java.io.Serializable;

public class StudentPathway implements Serializable {

    private int Id;

    private int IdStudent;

    private int IdPathway;

    private Boolean Completed;

    public StudentPathway(int idStudent, int idPathway, Boolean completed) {
        IdStudent = idStudent;
        IdPathway = idPathway;
        Completed = completed;
    }

    public int getId() {
        return Id;
    }

    /*public void setId(int id) {
        Id = id;
    }*/

    public int getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(int idStudent) {
        IdStudent = idStudent;
    }

    public int getIdPathway() {
        return IdPathway;
    }

    public void setIdPathway(int idPathway) {
        IdPathway = idPathway;
    }

    public Boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(Boolean completed) {
        Completed = completed;
    }
}
