package sample;

import java.util.ArrayList;
import java.util.Stack;

public class Game {

    private int difficulty;
    private ArrayList<Stack<Integer>> field = new ArrayList<>();

    public Game(int difficulty){

        this.difficulty = difficulty;
        Stack<Integer> temp = new Stack<>();
        for(int i = difficulty; i > 0; i--){
            temp.push(i);
        }
        field.add(temp);

        for(int i = 0; i< 2; i++){
            field.add(new Stack<Integer>());
        }
    }

    public int Move(int from, int to){
        if(field.get(from).peek() == null){
            return -1;
        }
        if(field.get(to).peek() == null || field.get(to).peek() > field.get(from).peek()){
            int temp = field.get(from).pop();
            field.get(to).push(temp);

            if(field.get(field.size()-1).size() == difficulty){
                return 0;
            }
            else{
                return 1;
            }

        }
        else{
            return -1;
        }


    }
}
