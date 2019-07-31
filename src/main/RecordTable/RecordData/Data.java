package main.RecordTable.RecordData;

import main.MainGame.Time.Time;

public class Data {
    public Data(String name, int moves, Time time){

        this.name = name;
        this.moves = moves;
        this.time = time;
    }

    private String name;
    private Time time;
    private int moves;

    public String getName() {
        return name;
    }

    public int getMoves(){
        return moves;
    }

    public Time getTime(){
        return time;
    }
}
