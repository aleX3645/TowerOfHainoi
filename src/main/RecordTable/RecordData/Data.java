package main.RecordTable.RecordData;

import main.MainGame.Time.Time;

/**
 * Класс ячейки таблицы рекордов
 * */
public class Data {
    public Data(String name, int moves, Time time){

        this.name = name;
        this.moves = moves;
        this.time = time;
    }

    /**Имя поставившего рекорд*/
    private String name;
    /**Затраченное время*/
    private Time time;
    /**Затренные шаги*/
    private int moves;

    /**
     * Возвращает имя
     * */
    String getName() {
        return name;
    }

    /**
     * Возвращает количество шагов
     * */
    int getMoves(){
        return moves;
    }

    /**
     * Возвращает время затраченное на прохождение
     * */
    Time getTime(){
        return time;
    }
}
