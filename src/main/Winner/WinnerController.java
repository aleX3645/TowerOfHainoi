package main.Winner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainGame.Time.Time;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;
import main.RecordTable.RecordTableController;

import java.io.IOException;

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
    /**Ссылка на контроллер таблицы ркордов*/
    RecordTableController recordController;

    /**Количество ходов*/
    int moves = 0;
    Data data = new Data("0",0,new Time());
    /**Затраченное время*/
    Time time;
    /**
     * инициализирует контроллер по сложности, количеству шагов и времени
     * */
    public void Init(int difficulty, int moves, Time time, RecordTableController recordController){

        this.moves = moves;
        this.time = time;
        this.recordController = recordController;
        data = new Data(nameTextField.getText(),moves,time);

        System.out.println(difficulty);

        recordTableData = new RecordTableData(difficulty);

        timeLabel.setText(time.toString());
        moveLabel.setText(Integer.toString(moves));
        winnerPlace.setText("Вы заняли " + recordTableData.getPlace(data) + "-е место");
        System.out.println(nameTextField.getText());
        saveButton.setOnAction(event -> {

            recordTableData.addNewRecord(data);
            recordController.Refresh();

            nameTextField.setText("");
            closeStage();
        });

        toMenuButton.setOnAction(event -> {
            Parent root = null;

            try {
                root = FXMLLoader.load(getClass().getResource("/main/Menu/Menu.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = new Stage();
            stage.setTitle("Хайнойские башни");
            stage.setScene(new Scene(root, 310, 310));
            stage.setMinWidth(300);
            stage.setMinHeight(300);
            stage.show();

            closeStage();
        });
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
