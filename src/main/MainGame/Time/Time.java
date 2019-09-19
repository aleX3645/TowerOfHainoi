package main.MainGame.Time;

import java.io.Serializable;

/**
 * Класс секундомера
 * */
public class Time implements Serializable {

    public Time() {}
    public Time(String time) {

        String[] temp = time.split(":");
        minutes = Integer.parseInt(temp[0]);
        System.out.println(temp[1]);
        temp = temp[1].split("\\.");
        seconds = Integer.parseInt(temp[0]);
        mseconds = Integer.parseInt(temp[1]);

    }


    int minutes = 0;
    int seconds = 0;
    int mseconds = 0;
    /**
     * Добавляет переданное время к общему затраченному времени
     * */
    public void addTime(int time){
        mseconds+=time;
        if(mseconds >= 100){
            mseconds=0;
            seconds+=1;

        }
        if(seconds >= 60){
            seconds = 0;
            minutes+=1;
        }
    }

    /**
     * Возвращает количество затраченных минут
     * */
    public int getMinutes(){
        return minutes;
    }

    /**
     * Возвращает количество затраченных секунд
     * */
    public int getSeconds(){
        return seconds;
    }

    /**
     * Возвращает количество затраченных милисекунд
     * */
    public int getMseconds(){ return mseconds; }

    /**
     * Возвращает текстовое представление затраченного времени
     * */
    @Override
    public String toString(){
        String result = "";

        if(minutes< 10){
            result += 0;
            result += minutes;
            result += ":";
        }else{
            result += minutes;
            result += ":";
        }

        if(seconds< 10){
            result += 0;
            result += seconds;
            result += ".";
        }else{
            result += seconds;
            result += ".";
        }

        if(mseconds< 10){
            result += 0;
            result += mseconds;
        }else{

            result += mseconds;

        }

        return result;
    }
}
