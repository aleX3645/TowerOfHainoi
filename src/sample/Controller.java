package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button newButton;

    @FXML
    public void onClickContinue(){
        System.out.println("Continue");
    }

    @FXML
    public void onClickTable(){
        System.out.println("Record Table");
    }

    @FXML
    public void onClickExit(){
        System.exit(0);
    }

    @FXML
    public void onClickNew(ActionEvent event) throws Exception{

        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("difficultyScene.fxml"));
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
