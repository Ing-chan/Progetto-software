package it.polimi.ingsw.Server.Model.Cards.CardCreators;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ObjectiveCreator implements Runnable {
    private ArrayList<ObjectiveCard> cards;
    final private String filename;

    public ObjectiveCreator(String filename) {
        this.filename = filename;
    }

    public ArrayList<ObjectiveCard> getCards() {
        return cards;
    }

    @Override
    public void run() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            InputStream inputStream = CardCreator.class.getResourceAsStream(filename);

            if (inputStream == null) {
                System.out.println("File not found!");
                return;
            }

            cards = objectMapper.readValue(inputStream, new TypeReference<ArrayList<ObjectiveCard>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
