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

    private String name;
    private Time time;
    private int moves;

    /**
     * Возвращает имя
     * */
    public String getName() {
        return name;
    }

    /**
     * Возвращает количество шагов
     * */
    public int getMoves(){
        return moves;
    }

    /**
     * Возвращает время затраченное на прохождение
     * */
    public Time getTime(){
        return time;
    }
}
