package main.RecordTable;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;

public class RecordTableController {

    @FXML
    TableView recordTable;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn movesColumn;
    @FXML
    TableColumn timeColumn;

    RecordTableData recordTableData = new RecordTableData();

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        movesColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("moves"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("time"));

        recordTable.setItems(recordTableData.getList());
    }

}

