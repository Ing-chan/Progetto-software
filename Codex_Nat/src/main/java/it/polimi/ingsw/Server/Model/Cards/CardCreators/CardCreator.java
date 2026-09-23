package it.polimi.ingsw.Server.Model.Cards.CardCreators;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Server.Model.Cards.PlayableCard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CardCreator implements Runnable {
    private ArrayList<? extends PlayableCard> cards;
    final private String filename;

    public CardCreator(String filename) {
        this.filename = filename;
    }

    public ArrayList<PlayableCard> getCards() {
        return (ArrayList<PlayableCard>) cards;
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

                if(filename.endsWith("GoldCardMain.json")) {
                    cards = objectMapper.readValue(inputStream, new TypeReference<ArrayList<GoldCardCreator>>() {});
                }
                if(filename.endsWith("ResourceCardMain.json")) {
                    cards = objectMapper.readValue(inputStream, new TypeReference<ArrayList<ResourceCardCreator>>() {});
                }
                if(filename.endsWith("FirstCardMain.json")) {
                    cards =objectMapper.readValue(inputStream, new TypeReference<ArrayList<FirstCardCreator>>() {});
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFile() {
        return filename;
    }
}
