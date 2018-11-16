package nz.park.kenneth.wintecdm;

public class Structure {

    private String Subject;
    private Boolean Enabled;
    private Boolean Completed;

    public Structure(String Subject, Boolean Enabled, Boolean Completed) {
        this.Subject = Subject;
        this.Enabled = Enabled;
        this.Completed = Completed;

    }

    public String getSubject() {
        return Subject;
    }

    public Boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(Boolean isCompleted) {
        Completed = isCompleted;
    }


    public Boolean getEnabled() {
        return Enabled;
    }
}
