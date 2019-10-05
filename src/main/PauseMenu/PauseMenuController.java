package main.PauseMenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.BuildPane.GamePane;
import main.MainGame.ControllerMain;
import main.Menu.MenuController;

public class PauseMenuController {

    private GamePane gamePane;
    private Stage primaryStage;
    private ControllerMain controllerMain;


    public void Init(GamePane gamePane, Stage primaryStage, ControllerMain controllerMain){
        this.gamePane = gamePane;
        this.primaryStage = primaryStage;
        this.controllerMain = controllerMain;
    }
    @FXML
    public void onClickContinue(){
        gamePane.clearPain();
        controllerMain.ContinueGame();
    }

    @FXML
    public void onClickToMenu(){

        controllerMain.Save();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Menu/Menu.fxml"));

        Parent root = new Group();
        try {
            root = loader.load();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        GamePane newGamePane = new GamePane();

        newGamePane.setRoot(root);

        Scene scene = new Scene(newGamePane.returnPane());
        scene.getStylesheets().add("/Resources/StyleClass.css");
        primaryStage.setScene(scene);


        MenuController menuController = loader.getController();
        menuController.setPrimaryStage(primaryStage);
        menuController.setGamePain(newGamePane);
    }

}
