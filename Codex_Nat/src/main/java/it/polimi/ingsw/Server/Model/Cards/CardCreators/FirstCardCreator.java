package it.polimi.ingsw.Server.Model.Cards.CardCreators;

import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Enums.Resource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class FirstCardCreator extends PlayableCard {

    @JsonCreator
    public FirstCardCreator(@JsonProperty("TypeCard")String id,@JsonProperty("CardID") int cardID, @JsonProperty("resource1")Resource r1, @JsonProperty("resource2")Resource r2,@JsonProperty("resource3") Resource r3,@JsonProperty("resource4") Resource r4,@JsonProperty("ExtraResources") ArrayList<Resource> ExtraResources,@JsonProperty("Backresource1") Resource br1,@JsonProperty("Backresource2") Resource br2,@JsonProperty("Backresource3") Resource br3,@JsonProperty("Backresource4") Resource br4) {
        super(id, cardID, r1, r2, r3, r4, ExtraResources, br1, br2, br3, br4);
    }
}
