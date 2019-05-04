package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button newButton;

    private void closeStage()
    {
        newButton.setText("dddd");
        Stage stage = (Stage) newButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickNew(ActionEvent event) throws Exception{

        closeStage();

        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        stage.setScene(new Scene(root));
        stage.show();

    }

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

}
