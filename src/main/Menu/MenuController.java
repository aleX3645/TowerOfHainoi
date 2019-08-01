package main.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Difficulty.DifficultyScene;
import main.MainGame.ControllerMain;
import main.MainGame.Game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MenuController {

    @FXML
    private Button newButton;

    @FXML
    public void onClickContinue(){

        Game game;
        try{
            FileInputStream fis = new FileInputStream("save.ser");
            ObjectInputStream oin = new ObjectInputStream(fis);
            game = (Game) oin.readObject();
            game.buildCylinders();
        }catch(Exception ex){
            //here with exception need message and interrupt
            ex.printStackTrace();
            game = new Game(3);
        }


        ControllerMain controller = new ControllerMain(game);
        controller.Init(0);
        closeStage();
    }

    @FXML
    public void onClickTable() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/RecordTable/RecordTable.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        closeStage();
    }

    @FXML
    public void onClickExit(){System.exit(0);}

    @FXML
    public void onClickNew(ActionEvent event) throws Exception{

        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Difficulty/difficultyScene.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 600, 400));

        DifficultyScene controller = loader.getController();
        controller.Init();
        stage.show();

        closeStage();

    }


    private void closeStage()
    {
        Stage stage = (Stage) newButton.getScene().getWindow();
        stage.close();
    }

}
