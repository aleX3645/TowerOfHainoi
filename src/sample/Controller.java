package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private Button button;

    @FXML
    public void onClickMethod(){
        button.setText("Thanks!");
    }

}
