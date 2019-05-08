package sample;

import javafx.scene.Group;
import javafx.scene.shape.Cylinder;


import java.util.ArrayList;
import java.util.Stack;

public class Game {

    private int difficulty;
    private final int maxSize = 200;
    private Stack<Integer>[] field = new Stack[3];
    private Stack<Cylinder>[] pField = new Stack[3];

    public Game(int difficulty){

        this.difficulty = difficulty;
        Stack<Integer> temp = new Stack<>();
        Stack<Cylinder> tempPointer = new Stack<>();
        for(int i = difficulty; i > 0; i--){
            temp.push(i);
            //tempPointer.push(new Cylinder())
        }
        field[0] = temp;


    }

    final int size = 40;
    public Group returnGameField(){
        ArrayList<ArrayList<Cylinder>> stractField = new ArrayList<>();

        for(int i = 0; i< 3; ++i){
            ArrayList<Cylinder> temp = new ArrayList<>();

            Cylinder bottom = new Cylinder();
            bottom.setRadius(maxSize);
            bottom.setHeight(24);
            bottom.translateXProperty().set(700);
            bottom.translateYProperty().set(500+(difficulty+1)*size/2);
            temp.add(bottom);

            Cylinder top = new Cylinder();
            top.setRadius(50);
            top.setHeight((difficulty+1)*size);
            top.translateXProperty().set(700);
            top.translateYProperty().set(500+((difficulty+1)*size/2)-(difficulty+1)*size/2-12);
            temp.add(top);

            stractField.add(temp);
        }

        stractField.get(1).get(0).translateXProperty().set(250);
        stractField.get(1).get(1).translateXProperty().set(250);

        stractField.get(2).get(0).translateXProperty().set(1150);
        stractField.get(2).get(1).translateXProperty().set(1150);

        return getGroup(stractField);
    }

   // public Stack<Cylinder>[] returnTechnicalGameField(){
   //     return field;
  //  }

    private Group getGroup(ArrayList<ArrayList<Cylinder>> field){
        Group group = new Group();
        for (int i = 0; i< 3; ++i){
            for(int j = 0; j<2;++j){
                group.getChildren().add(field.get(i).get(j));
            }
        }
        return group;
    }

    public int Move(int from, int to){
        if(field[from].size() == 0){
            return -1;
        }
        if(field[to].size() == 0 || field[to].peek() > field[from].peek()){
            int temp = field[from].pop();
            field[to].push(temp);

            if(field[2].size() == difficulty){
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
