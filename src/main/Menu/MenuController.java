package main.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Difficulty.DifficultyScene;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button newButton;

    @FXML
    public void onClickContinue(){System.out.println("continue");}

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
