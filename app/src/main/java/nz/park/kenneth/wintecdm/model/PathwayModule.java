package nz.park.kenneth.wintecdm.model;

import java.io.Serializable;

public class PathwayModule implements Serializable {

    private int Id;

    private int IdPathway;

    private int IdModule;

    public PathwayModule(int idPathway, int idModule) {
        IdPathway = idPathway;
        IdModule = idModule;
    }

    public int getId() {
        return Id;
    }

    /*public void setId(int id) {
        Id = id;
    }*/

    public int getIdPathway() {
        return IdPathway;
    }

    public void setIdPathway(int idPathway) {
        IdPathway = idPathway;
    }

    public int getIdModule() {
        return IdModule;
    }

    public void setIdModule(int idModule) {
        IdModule = idModule;
    }
}
