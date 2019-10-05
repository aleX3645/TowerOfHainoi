package main.StartDirecrory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import main.BuildPane.GamePane;
import main.Menu.MenuController;

/**
 * Класс старта программы
 * */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Menu/Menu.fxml"));

        Parent root = new Group();
        try {
            root = loader.load();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        GamePane gamePane = new GamePane(root);

        MenuController menuController = loader.getController();
        menuController.setGamePain(gamePane);
        menuController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(gamePane.returnPane(), 310, 310);
        scene.getStylesheets().add("/Resources/StyleClass.css");

        primaryStage.setTitle("Хайнойские башни");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
