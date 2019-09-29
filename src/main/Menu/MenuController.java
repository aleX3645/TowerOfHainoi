package main.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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

        Game game;
        try{
            FileInputStream fis = new FileInputStream("save.ser");
            ObjectInputStream oin = new ObjectInputStream(fis);
            game = (Game) oin.readObject();
            game.buildTorus();
        }catch(Exception ex){
            ex.printStackTrace();
            game = new Game(3);
        }

        ControllerMain controller = new ControllerMain(game);
        controller.Init(game.getDifficulty());
        closeStage();
    }

    /**
     * Событие нажатия на таблицу рекордов.
     * */
    @FXML
    public void onClickTable() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/RecordTable/RecordTable.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 600, 400));

        RecordTableController controller = loader.getController();
        controller.Init(3);

        stage.setResizable(false);
        stage.show();

        closeStage();
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
    public void onClickNew(ActionEvent event) throws Exception{

        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Difficulty/difficultyScene.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);


        DifficultyScene controller = loader.getController();
        controller.Init();
        stage.show();

        closeStage();

    }

    /**
     * Закрывает окно
     * */
    public void closeStage()
    {
        Stage stage = (Stage) newButton.getScene().getWindow();
        stage.close();
    }

}
