package main.Winner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.BuildPane.GamePane;
import main.MainGame.Main.Game;
import main.MainGame.Time.Time;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;
import main.RecordTable.RecordTableController;

import java.io.IOException;
import java.util.Objects;

/**
 * Контроллер победного окна
 * */
public class WinnerController {

    @FXML
    private Button saveButton;
    @FXML
    private Button toMenuButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label timeLabel;
    @FXML
    private Label moveLabel;
    @FXML
    private Label winnerPlace;


    /**Таблица рекордов*/
    private RecordTableData recordTableData = new RecordTableData(3);

    /**Количество ходов*/
    private int moves;
    private Data data = new Data("0",0,new Time());
    /**Затраченное время*/
    private Time time;

    public WinnerController() {
        moves = 0;
    }

    /**
     * инициализирует контроллер по сложности, количеству шагов и времени
     * */
    public void Init(int moves, Time time,int difficulty, RecordTableController recordController){

        this.moves = moves;
        this.time = time;

        data = new Data(nameTextField.getText(),moves,time);

        recordTableData = new RecordTableData(difficulty);

        timeLabel.setText(time.toString());
        moveLabel.setText(Integer.toString(moves));
        winnerPlace.setText("Вы заняли " + recordTableData.getPlace(data) + "-е место");
        System.out.println(nameTextField.getText());
        saveButton.setOnAction(event -> {

            data = new Data(nameTextField.getText(),moves,time);
            recordTableData.addNewRecord(data);
            recordController.Refresh();

            nameTextField.setText("");
            closeStage();
        });

        toMenuButton.setOnAction(event -> closeStage());
    }

    /**
     * Закрывает окно
     * */
    private void closeStage()
    {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

}
