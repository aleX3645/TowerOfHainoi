package main.MainGame.Main;

import javafx.scene.Group;
import javafx.scene.shape.Cylinder;
import main.MainGame.Time.Time;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class Game implements Serializable{

    private double difficulty;
    private final double maxSize = 200;
    private final double blockSize = 40;
    private final double xLeft = 250;
    private final double xRight = 1150;
    private final double xCenter = 700;
    private final double topR = 30;


    private ArrayList<Integer>[] field = new ArrayList[3];
    private transient Stack<Cylinder>[] pField = new Stack[3];

    public Game(int difficulty){

        MaterialsGenerator materialsGenerator= new MaterialsGenerator();
        double yStart = 500+(difficulty+1.5)*blockSize/2+20;
        this.difficulty = difficulty;
        ArrayList<Integer> temp = new ArrayList<>();

        for(int i = difficulty; i > 0; i--){
            temp.add(i);
        }

        field[0] = temp;
        field[1] = new ArrayList<>();
        field[2] = new ArrayList<>();

        Stack<Cylinder> tempPointer = new Stack<>();
        for(int j=1;j<= difficulty; ++j){
            Cylinder block = new Cylinder();
            block.setHeight(blockSize);
            block.setRadius(maxSize-j*(maxSize-topR)/(difficulty+1));
            block.translateXProperty().set(xLeft);
            block.translateYProperty().set(yStart-j*blockSize);
            block.setMaterial(materialsGenerator.GetNextMaterial());
            tempPointer.push(block);
        }

        pField[0] = tempPointer;
        pField[1] = new Stack<>();
        pField[2] = new Stack<>();
    }


    public Group returnGameField(){

        MaterialsGenerator materialsGenerator= new MaterialsGenerator();
        ArrayList<ArrayList<Cylinder>> stractField = new ArrayList<>();

        for(int i = 0; i< 3; ++i){

            ArrayList<Cylinder> temp = new ArrayList<>();

            Cylinder bottom = new Cylinder();
            bottom.setRadius(maxSize);
            bottom.setHeight(24.0);
            bottom.translateXProperty().set(xCenter);
            bottom.translateYProperty().set(500.0+(difficulty+1.5)*blockSize/2+12);
            bottom.setMaterial(materialsGenerator.getFieldTexture());
            temp.add(bottom);

            Cylinder top = new Cylinder();
            top.setRadius(topR);
            top.setHeight((difficulty+1.5)*blockSize);
            top.translateXProperty().set(xCenter);
            top.translateYProperty().set(500.0);
            top.setMaterial(materialsGenerator.getFieldTexture());
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

            Stack<Cylinder> temp = (Stack<Cylinder>) pField[i].clone();
            if(temp.size() == 0){
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
        //hmm
        if(field[to].size() == 0 || field[to].get(field[to].size()-1) > field[from].get(field[from].size()-1)){
            field[to].add(field[from].get(field[from].size()-1));
            field[from].remove(field[from].size()-1);
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

        double yStart = 500+(difficulty+1.5)*blockSize/2+20;

        Cylinder temp = pField[from].pop();
        pField[to].push(temp);

        switch (to) {
            case 0:
                temp.translateXProperty().set(xLeft);
                break;
            case 1:
                temp.translateXProperty().set(xCenter);
                break;
            case 2:
                temp.translateXProperty().set(xRight);
                break;
            }

        temp.translateYProperty().set(yStart-pField[to].size()*blockSize);
    }

    private Time time;
    private int moves;
    public void save(Time time, int moves){
        this.time = time;
        this.moves = moves;

    }

    public Time getTime() {
        return time;
    }

    public int getMoves() {
        return moves;
    }

    public void buildCylinders(){

        MaterialsGenerator materialsGenerator= new MaterialsGenerator();
        double yStart = 500+(difficulty+1.5)*blockSize/2+20;

        Stack<Cylinder>[] tempField = new Stack[3];
        Stack<Cylinder> temp = new Stack<>();
        for (int i = 0; i< field[0].size(); i++){
            Cylinder block = new Cylinder();
            block.setHeight(blockSize);
            block.setRadius(maxSize-(difficulty-field[0].get(i)+1)*(maxSize-topR)/(difficulty+1));
            block.translateXProperty().set(xLeft);
            block.translateYProperty().set(yStart-(i+1)*blockSize);
            block.setMaterial(materialsGenerator.GetMAterialById((int)difficulty-field[0].get(i)));
            temp.push(block);
        }

        tempField[0] = temp;


        Stack<Cylinder> temp1 = new Stack<>();
        for (int i = 0; i< field[1].size(); i++){
            Cylinder block = new Cylinder();
            block.setHeight(blockSize);
            block.setRadius(maxSize-(difficulty-field[1].get(i)+1)*(maxSize-topR)/(difficulty+1));
            block.translateXProperty().set(xCenter);
            block.translateYProperty().set(yStart-(i+1)*blockSize);
            block.setMaterial(materialsGenerator.GetMAterialById((int)difficulty-field[1].get(i)));
            temp1.push(block);
        }
        tempField[1] = temp1;

        Stack<Cylinder> temp2 = new Stack<>();
        for (int i = 0; i< field[2].size(); i++){
            Cylinder block = new Cylinder();
            block.setHeight(blockSize);
            block.setRadius(maxSize-(difficulty-field[2].get(i)+1)*(maxSize-topR)/(difficulty+1));
            block.translateXProperty().set(xRight);
            block.translateYProperty().set(yStart-(i+1)*blockSize);
            block.setMaterial(materialsGenerator.GetMAterialById((int)difficulty-field[2].get(i)));
            temp2.push(block);
        }
        tempField[2] = temp2;

        pField = tempField;
    }
}
