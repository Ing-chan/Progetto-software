package it.polimi.ingsw.Server.Model.Cards.CardCreators;

import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Enums.CardColour;
import it.polimi.ingsw.Server.Model.Enums.Resource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceCardCreator extends PlayableCard {

    @JsonCreator
    public ResourceCardCreator(@JsonProperty("TypeCard")String id,@JsonProperty("CardID") int cardID, @JsonProperty("Color") CardColour colour,@JsonProperty("Points") int points,@JsonProperty("resource1") Resource r1,@JsonProperty("resource2") Resource r2, @JsonProperty("resource3")Resource r3,@JsonProperty("resource4") Resource r4) {
        super(id, cardID, colour, points, r1, r2, r3, r4);
    }
}
