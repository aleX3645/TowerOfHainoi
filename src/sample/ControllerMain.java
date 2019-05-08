package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Cylinder;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class ControllerMain{

    @FXML
    private Label timeLabel;

    @FXML
    private AnchorPane myAnc;

    private Game game;
    private Stack<Cylinder>[] gameField;

    public void Init(int difficulty){
        game = new Game(difficulty);
        //this.gameField = gameField;

        myAnc.getChildren().add(game.returnGameField());
    }





       // Game game = new Game(8);
       // Scene scene = new Scene(game.returnGameField(), 1400, 2000);
       // Stage stage = (Stage) timeLabel.getScene().getWindow();
       // stage.setScene(scene);

}
