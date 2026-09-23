package it.polimi.ingsw.Server.Model.Cards;

import it.polimi.ingsw.Server.Model.Enums.Resource;

import java.io.Serializable;

public class Corner{

    private Resource resource;
    private Boolean Active; //active means that it's not under another card


    public Corner(Resource res) {
        resource=res;
        Active = true;
    }

    public boolean getCornerState() {
        return Active;
    }

    public boolean isActive() {
        return Active;
    }

    public Resource getResource() {
        return resource;
    }

    public void deactivate() {
        Active = false;
    }

}

