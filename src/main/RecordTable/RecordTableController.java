package main.RecordTable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Difficulty.DifficultyScene;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;

import java.io.IOException;

public class RecordTableController {

    @FXML
    TableView recordTable;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn movesColumn;
    @FXML
    TableColumn timeColumn;
    @FXML
    Button toMenuButton;

    RecordTableData recordTableData = new RecordTableData();

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        movesColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("moves"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("time"));

        recordTable.setItems(recordTableData.getList());
    }

    @FXML
    public void onClickToMenu(ActionEvent event) throws Exception{

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

    }

    private void closeStage()
    {
        Stage stage = (Stage) toMenuButton.getScene().getWindow();
        stage.close();
    }

}

