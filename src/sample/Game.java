package sample;

import javafx.scene.Group;
import javafx.scene.shape.Cylinder;


import java.util.ArrayList;
import java.util.Stack;

public class Game {

    private int difficulty;
    private final int maxSize = 200;
    final int blockSize = 40;
    int xLeft = 250;
    int xRight = 1150;
    int xCenter = 700;
    int topR = 30;
    private Stack<Integer>[] field = new Stack[3];
    private Stack<Cylinder>[] pField = new Stack[3];

    public Game(int difficulty){
        double yStart = 500+(difficulty+1.5)*blockSize/2+20;

        this.difficulty = difficulty;
        Stack<Integer> temp = new Stack<>();
        for(int i = difficulty; i > 0; i--){
            temp.push(i);
            //tempPointer.push(new Cylinder())
        }
        field[0] = temp;

        Stack<Cylinder> tempPointer = new Stack<>();
        for(int j=1;j<= difficulty; ++j){
            Cylinder block = new Cylinder();
            block.setHeight(blockSize);
            block.setRadius(maxSize-j*(maxSize-topR)/(difficulty+1));
            block.translateXProperty().set(xLeft);
            block.translateYProperty().set(yStart-j*blockSize);
            tempPointer.push(block);
        }
        pField[0] = tempPointer;


    }


    public Group returnGameField(){
        ArrayList<ArrayList<Cylinder>> stractField = new ArrayList<>();

        for(int i = 0; i< 3; ++i){
            ArrayList<Cylinder> temp = new ArrayList<>();

            Cylinder bottom = new Cylinder();
            bottom.setRadius(maxSize);
            bottom.setHeight(24);
            bottom.translateXProperty().set(xCenter);
            bottom.translateYProperty().set(500+(difficulty+1.5)*blockSize/2+12);
            temp.add(bottom);

            Cylinder top = new Cylinder();
            top.setRadius(topR);
            top.setHeight((difficulty+1.5)*blockSize);
            top.translateXProperty().set(xCenter);
            top.translateYProperty().set(500);
            temp.add(top);

            stractField.add(temp);
        }

        stractField.get(1).get(0).translateXProperty().set(xLeft);
        stractField.get(1).get(1).translateXProperty().set(xLeft);

        stractField.get(2).get(0).translateXProperty().set(xRight);
        stractField.get(2).get(1).translateXProperty().set(xRight);

        return getGroup(stractField);
    }

    private Group getGroup(ArrayList<ArrayList<Cylinder>> field){
        Group group = new Group();
        for (int i = 0; i< 3; ++i){
            for(int j = 0; j<2;++j){
                group.getChildren().add(field.get(i).get(j));
            }
        }

        for(int i = 0; i< 3; i++){
            Stack<Cylinder> temp = pField[i];
            if(temp == null){
                continue;
            }
            while( temp.size() != 0){
                group.getChildren().add(temp.pop());
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
            pMove(from, to);

            if(field[2].size() == difficulty){
                System.out.println("win");
                return 0;
            }
            else{
                System.out.println("can");
                return 1;
            }

        }
        else{
            System.out.println("cannot");
            return -1;
        }


    }

    private void pMove(int from, int to){
        Cylinder temp = pField[from].pop();
        pField[to].push(temp);

        if(to == 0){
            temp.translateXProperty().set(xLeft);
        }else{
            if(to == 1){
                temp.translateXProperty().set(xCenter);
            }else{
                temp.translateXProperty().set(xLeft);
            }
        }

        temp.translateYProperty().set(maxSize-pField[to].size()*(maxSize-topR)/( pField[to].size()+1));
    }

}
