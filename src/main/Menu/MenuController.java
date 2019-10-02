package main.Menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;

import main.BuildPane.GamePane;
import main.Difficulty.DifficultyScene;
import main.MainGame.ControllerMain;
import main.MainGame.Main.Game;
import main.RecordTable.RecordTableController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Контроллер для меню
 * */
public class MenuController {

    @FXML
    private Button newButton;

    /**
     * Событие нажатия на продолжить, десериализует класс game и отправляет в контроллер
     * */
    @FXML
    public void onClickContinue(){

        gamePane.clearPain();

        ControllerMain controller = new ControllerMain(primaryStage,gamePane);
        controller.Init(gamePane.returnGame().getDifficulty());
    }

    /**
     * Событие нажатия на таблицу рекордов.
     * */
    @FXML
    public void onClickTable() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/RecordTable/RecordTable.fxml"));
        Parent root = loader.load();

        gamePane.clearPain();
        gamePane.setRoot(root);

        RecordTableController controller = loader.getController();
        controller.Init(3,primaryStage,gamePane);
    }

    /**
     * Событие нажатия на выход.
     * */
    @FXML
    public void onClickExit(){System.exit(0);}

    /**
     * Событие нажатия на "новая игра".
     * */
    @FXML
    public void onClickNew() throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Difficulty/difficultyScene.fxml"));
        Parent root = loader.load();

        gamePane.clearPain();
        gamePane.setRoot(root);

        DifficultyScene controller = loader.getController();
        controller.Init(primaryStage,gamePane);
    }

    private GamePane gamePane = new GamePane();
    public void setGamePain(GamePane gamePain) {
        this.gamePane = gamePain;
    }

    private Stage primaryStage = new Stage();
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
}
