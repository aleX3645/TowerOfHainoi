package main.MainGame.Time;

import java.io.Serializable;

public class Time implements Serializable {

    public Time() {

    }

    public Time(String time) {

        String[] temp = time.split(":");
        minutes = Integer.parseInt(temp[0]);

        temp = temp[1].split("\\.");
        seconds = Integer.parseInt(temp[0]);
        mseconds = Integer.parseInt(temp[1]);

    }

    int minutes = 0;
    int seconds = 0;
    int mseconds = 0;

    public void addTime(int time){
        mseconds+=time;
        if(mseconds >= 1000){
            mseconds=0;
            seconds+=1;
        }
        if(seconds >= 60){
            seconds = 0;
            minutes+=1;
        }
    }


    public int getMinutes(){
        return minutes;
    }

    public int getSeconds(){
        return seconds;
    }

    public int getMseconds(){
        return mseconds;
    }

    @Override
    public String toString(){
        return minutes + ":" + seconds + "." + mseconds;
    }
}
