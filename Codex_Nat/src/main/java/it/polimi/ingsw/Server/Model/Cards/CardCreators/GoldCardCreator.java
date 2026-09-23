package it.polimi.ingsw.Server.Model.Cards.CardCreators;

import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Enums.CardColour;
import it.polimi.ingsw.Server.Model.Enums.GoldCardObjective;
import it.polimi.ingsw.Server.Model.Enums.Resource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class GoldCardCreator extends PlayableCard {

    @JsonCreator
    public GoldCardCreator(@JsonProperty("TypeCard")String id,@JsonProperty("CardID") int cardID,@JsonProperty("Color") CardColour colour,@JsonProperty("Points") int points,@JsonProperty("CardRule") GoldCardObjective objective,@JsonProperty("resource1") Resource r1,@JsonProperty("resource2") Resource r2, @JsonProperty("resource3") Resource r3, @JsonProperty("resource4")  Resource r4, @JsonProperty("ResourceCost") ArrayList<Resource> resourceNeeded) {
        super(id, cardID, colour, points, objective, r1, r2, r3, r4, resourceNeeded);
    }
}
