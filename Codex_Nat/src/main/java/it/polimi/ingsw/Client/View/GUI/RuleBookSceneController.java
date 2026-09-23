package it.polimi.ingsw.Client.View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class RuleBookSceneController extends Gui implements Initializable {
    @FXML
    public ImageView rulePage;
    @FXML
    public Button continueButton;
    @FXML
    public Button backButton;

    String path;
    int i;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        i = 1;
        path = createRulebookPath(i);
        loadImage(rulePage, path);
    }

    public String createRulebookPath(int i){
        if(i<10){
            path = "img/rulebook/0"+i+".png";
        } else {
            path = "img/rulebook/"+i+".png";
        }
        return path;
    }

    private static void loadImage(ImageView imageView, String imagePath) {
        try {
            URL imageUrl = GameBoardController.class.getResource(imagePath);
            if (imageUrl == null) {
                throw new RuntimeException("Image not found: " + imagePath);
            }
            Image image = new Image(imageUrl.toString());
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load image: " + imagePath + ". Error: " + e.getMessage());
        }
    }

    public void onContinueButton() {
        if(i==12){
            return;
        } else if(i < 12){
            i++;
        }
        path = createRulebookPath(i);
        loadImage(rulePage, path);
    }

    public void onBackButton() {
        if(i == 1){
            return;
        } else if(i>1){
            i--;
        }
        path = createRulebookPath(i);
        loadImage(rulePage, path);
    }
}
