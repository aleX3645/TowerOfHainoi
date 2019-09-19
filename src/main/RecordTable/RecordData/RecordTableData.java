package main.RecordTable.RecordData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.MainGame.Time.Time;
import main.RecordTable.RecordData.Data;

import java.io.*;
import java.util.ArrayList;

/**
 * Класс таблицы рекордов
 * */
public class RecordTableData {

    public RecordTableData(int difficulty){
        this.difficulty = difficulty;
        try(BufferedReader reader = new BufferedReader(new FileReader("recordTable" + this.difficulty + ".txt"))){

            String temp;
            while((temp = reader.readLine())!=null){
                String[] stringPlayer = temp.split("-");

                Data cell = new Data(stringPlayer[0],Integer.parseInt(stringPlayer[1]),new Time(stringPlayer[2]));
                table.add(cell);
            }
        }catch (FileNotFoundException fileNotFoundException){ }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    int difficulty;
    private ObservableList<Data> table = FXCollections.observableArrayList();

    /**
     * Добавляет новую ячейку
     * */
    public void addNewRecord(Data newCell){
        if(table.size() == 0){
            table.add(newCell);
            Save();

            return;
        }
        for(int i = 0; i < table.size(); i++){
            if(newCell.getMoves() < table.get(i).getMoves()){
                table.add(i,newCell);
                Save();

                return;

            }
            if(newCell.getMoves() == table.get(i).getMoves() &&
                    compare(newCell.getTime(),table.get(i).getTime())){
                table.add(i, newCell);
                Save();

                return;
            }
        }
        table.add(newCell);
        Save();
    }


    /**
     * Сохраняет таблицу рекордов
     * */
    private void Save(){
        try(FileWriter writer = new FileWriter("recordTable" + this.difficulty + ".txt",false)){
            writer.write(toString());
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    /**
     * Сбрасывает таблицу рекордов
     * */
    public void Reset(){
        table = FXCollections.observableArrayList();
        Save();
    }

    /**
     * Сравнивает 2 времени, возвращает true, если первое больше второго
     * */
    private boolean compare(Time time1, Time time2){
        if(time1.getMinutes()<time2.getMinutes() || time1.getMinutes()==time2.getMinutes() && time1.getSeconds() < time2.getSeconds()
                || time1.getMinutes() == time2.getMinutes() && time1.getSeconds() == time2.getSeconds() && time1.getMseconds()<time2.getMseconds()){
            return  true;
        }else{
            return false;
        }
    }

    public ObservableList<Data> getList(){
        return table;
    }

    /**
     * Приводит таблицу к строке
     * */
    @Override
    public String toString(){
        String stringTable = "";

        if(table.size() == 0){
            return stringTable;
        }

        stringTable += table.get(0).getName() + "-";
        stringTable += table.get(0).getMoves() + "-";
        stringTable += table.get(0).getTime().toString();
        for(int i = 1; i < table.size(); i++){
            stringTable += "\n";
            stringTable += table.get(i).getName() + "-";
            stringTable += table.get(i).getMoves() + "-";
            stringTable += table.get(i).getTime().toString();
        }

        return stringTable;
    }


}



