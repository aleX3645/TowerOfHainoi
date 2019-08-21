package main.Difficulty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import main.MainGame.ControllerMain;

import java.io.IOException;


public class DifficultyScene {

    @FXML
    private RadioButton easyBtn;

    @FXML
    private RadioButton midBtn;

    @FXML
    private RadioButton hardBtn;

    @FXML
    private RadioButton insaneBtn;

    @FXML
    private Button playButton;

    @FXML
    private Button backButton;


    public void Init(){
        addButtonEvents();
    }


    int difficulty = 3;
    private void addButtonEvents(){

        easyBtn.setOnAction(event -> {
            midBtn.setSelected(false);
            hardBtn.setSelected(false);
            insaneBtn.setSelected(false);
            difficulty = 3;
        });

        midBtn.setOnAction(event -> {
            easyBtn.setSelected(false);
            hardBtn.setSelected(false);
            insaneBtn.setSelected(false);
            difficulty = 5;
            System.out.println("ddd");
        });

        hardBtn.setOnAction(event -> {
            midBtn.setSelected(false);
            easyBtn.setSelected(false);
            insaneBtn.setSelected(false);
            difficulty = 8;
        });

        insaneBtn.setOnAction(event -> {
            midBtn.setSelected(false);
            hardBtn.setSelected(false);
            easyBtn.setSelected(false);
            difficulty = 15;
        });

        playButton.setOnAction(event -> {
            ControllerMain controller = new ControllerMain();
            controller.Init(difficulty);
            closeStage();
        });

        backButton.setOnAction(event -> {
            Parent root = null;

            try {
                root = FXMLLoader.load(getClass().getResource("/main/Menu/Menu.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = new Stage();
            stage.setTitle("Хайнойские башни");
            stage.setScene(new Scene(root, 310, 310));
            stage.setMinWidth(300);
            stage.setMinHeight(300);
            stage.show();
            closeStage();
        });

    }


    private void closeStage()
    {
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.close();
    }
}
