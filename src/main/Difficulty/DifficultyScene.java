package main.Difficulty;


import main.MainGame.ControllerMain;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

/**
 * Контроллер для определения сложности.
 * */
public class DifficultyScene {

    @FXML
    private RadioButton easyBtn, midBtn, hardBtn, insaneBtn;
    @FXML
    private Button playButton, backButton;

    /**
     * Инициализирует класс и добавляет события кнопок.
     * */
    public void Init(){
        addButtonEvents();
    }

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
            stage.setScene(new Scene(Objects.requireNonNull(root), 310, 310));
            stage.setMinWidth(300);
            stage.setMinHeight(300);
            stage.show();

            closeStage();
        });

    }

    /**
     * Закрывает сцену.
     * */
    private void closeStage()
    {
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.close();
    }
}
