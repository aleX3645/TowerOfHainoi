package main.Difficulty;


import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import main.BuildPane.GamePane;
import main.MainGame.ControllerMain;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import main.Menu.MenuController;

/**
 * Контроллер для определения сложности.
 * */
public class DifficultyScene {

    @FXML
    private RadioButton easyBtn, midBtn, hardBtn, insaneBtn;
    @FXML
    private Button playButton, backButton;

    Stage primaryStage;
    GamePane gamePane;
    /**
     * Инициализирует класс и добавляет события кнопок.
     * */
    public void Init(Stage primaryStage, GamePane gamePane){
        addButtonEvents();
        this.primaryStage = primaryStage;
        this.gamePane = gamePane;
    }

    Background background= new Background(new BackgroundFill(Color.web("00c482"), CornerRadii.EMPTY, Insets.EMPTY));
    /**
     * Сложность игры, где целое число означет количество колец
     * */
    private int difficulty = 3;
    /**
     * Добавляет события кнопок.
     * */
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

            if(gamePane.returnGame().getTime().getMinutes() != 0 ||
                    gamePane.returnGame().getTime().getSeconds() != 0 ||
                    gamePane.returnGame().getTime().getMseconds() != 0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Внимание");
                alert.setHeaderText("Вы уверены, что хотите начать новую игру, прогресс прошлой игры будет утеренен");
                ButtonType yes = new ButtonType("Начать");
                ButtonType no = new ButtonType("Отмена");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(yes,no);
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get() == no){return;}
            }

            ControllerMain controller = new ControllerMain(primaryStage);
            controller.Init(difficulty);

        });

        backButton.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Menu/Menu.fxml"));

            Parent root = new Group();
            try {
                root = loader.load();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            MenuController menuController = loader.getController();
            menuController.setPrimaryStage(primaryStage);
            menuController.setGamePain(gamePane);

            gamePane.clearPain();
            gamePane.setRoot(root);
        });

    }
}
