package main.MainGame.Main;

import javafx.scene.Group;
import javafx.scene.input.PickResult;
import javafx.scene.shape.Cylinder;
import main.MainGame.ControllerMain;
import main.MainGame.Time.Time;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Класс игры. В конструктор передается общее количество колец и по ним генерируется поле игры.
 * */
public class Game implements Serializable{

    /**Сложность игры*/
    private double difficulty;
    /**Хранит номер шеста с которого идет перенос, tempStackNumber = -1, когда никакой шест не выбран.*/
    private transient int tempStackNumber = -1;

    /**Содержит целочислинные переменные, где чем больше номер тем больше размер кольца(дублирует стек с кольцами, но вместо радиуса используется целые числа),
     * необходимо для сравнения колец*/
    private ArrayList<Integer>[] field = new ArrayList[3];
    /**Содержит стеки с кольцами, где номер стека соответствует левому, правому, и центральному шесту в такой последовательности в массиве*/
    private transient Stack<Cylinder>[] pField = new Stack[3];
    /**Содержит шесты*/
    private transient ArrayList<ArrayList<Cylinder>> stractField = new ArrayList<>();


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

        buildCylinders();
    }

    /**Содержит контроллер игры*/
    private transient ControllerMain controllerMain = new ControllerMain();
    /**
     * Передает ссылку на контроллер игры.
     * */
    public void setControllerMain(ControllerMain controllerMain){
        this.controllerMain = controllerMain;
    }

    /**
     * Метод вызывается при нажатии на игровые фигуры.
     * Определяется с какого и на какой шпиль переносятся кольца.
     * */
    private void clickHandler(Cylinder cylinder){
        if(tempStackNumber == -1){
            System.out.println("here -1");
            if(pField[0].contains(cylinder)){
                tempStackNumber = 0;
            }else{
                if(pField[1].contains(cylinder)){
                    tempStackNumber = 1;
                }else{
                    if(pField[2].contains(cylinder)){
                        tempStackNumber = 2;
                    }else{
                        tempStackNumber = returnStructNumber(cylinder);
                    }
                }
            }
        }else{
            System.out.println(tempStackNumber);
            if(pField[0].contains(cylinder)){
                Move(tempStackNumber,0);
                tempStackNumber = -1;
            }else{
                if(pField[1].contains(cylinder)){
                    Move(tempStackNumber,1);
                    tempStackNumber = -1;
                }else{
                    if(pField[2].contains(cylinder)){
                        Move(tempStackNumber,2);
                        tempStackNumber = -1;
                    }else{
                        Move(tempStackNumber,returnStructNumber(cylinder));
                        tempStackNumber = -1;
                    }
                }
            }
        }
    }

    /**
     * Вспомогательный метод для clickHandler.
     * Возвращает номер шеста на который нажимает пользователь
     * */
    private int returnStructNumber(Cylinder cylinder){
        if(stractField.get(0).contains(cylinder)){
            return 1;
        }else{
            if(stractField.get(1).contains(cylinder)){
                return 0;
            }else{
                return 2;
            }
        }
    }



    private final double maxSize = 200;
    private final double blockSize = 40;
    private final double xLeft = 250;
    private final double xRight = 1150;
    private final double xCenter = 700;
    private final double topR = 30;
    /**
     * Возвращает группу со всеми элементами для добавления на сцену
     * */
    public Group returnGameField(){

        MaterialsGenerator materialsGenerator= new MaterialsGenerator();
        stractField = new ArrayList<>();

        for(int i = 0; i< 3; ++i){

            ArrayList<Cylinder> temp = new ArrayList<>();

            Cylinder bottom = new Cylinder();
            bottom.setRadius(maxSize);
            bottom.setHeight(24.0);
            bottom.translateXProperty().set(xCenter);
            bottom.translateYProperty().set(500.0+(difficulty+1.5)*blockSize/2+12);
            bottom.setMaterial(materialsGenerator.getFieldTexture());

            bottom.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler((Cylinder)pr.getIntersectedNode());
            });

            temp.add(bottom);

            Cylinder top = new Cylinder();
            top.setRadius(topR);
            top.setHeight((difficulty+1.5)*blockSize);
            top.translateXProperty().set(xCenter);
            top.translateYProperty().set(500.0);
            top.setMaterial(materialsGenerator.getFieldTexture());

            top.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler((Cylinder)pr.getIntersectedNode());
            });

            temp.add(top);

            stractField.add(temp);
        }

        stractField.get(1).get(0).translateXProperty().set(xLeft);
        stractField.get(1).get(1).translateXProperty().set(xLeft);

        stractField.get(2).get(0).translateXProperty().set(xRight);
        stractField.get(2).get(1).translateXProperty().set(xRight);

        return getGroup(stractField);
    }

    /**
     * Составляет группу на основе листа элементов
     * */
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

    /**
     * проверяет можно ли передвинуть кольцо с одного шеста на другой,
     * если можно передвигает и передает в главный контроллер результат передвижения.
     * 1 - Передвинуть можно.
     * -1 - Передвинуть нельзя.
     * 0 - Победа пользователя.
     * */
    public void Move(int from, int to){

        if(field[from].size() == 0){
            return;
        }

        if(field[to].size() == 0 || field[to].get(field[to].size()-1) > field[from].get(field[from].size()-1)){
            field[to].add(field[from].get(field[from].size()-1));
            field[from].remove(field[from].size()-1);
            pMove(from, to);

            if(field[2].size() == difficulty){
                System.out.println("win");
                controllerMain.MoveHandler(0);
            }
            else{
                System.out.println("can");
                controllerMain.MoveHandler(1);
            }
        }
        else{
            System.out.println("cannot");
            controllerMain.MoveHandler(-1);
        }
    }

    /**
     * Передвигает кольцо с одного шеста на другой
     * */
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

    /**Затраченное время (для сохранения)*/
    private Time time = new Time();
    /**Количество ходов (для сохранения)*/
    private int moves = 0;
    /**
     * Сохраняет время и количество шагов для дальнейшей сериализации.
     * */
    public void save(Time time, int moves){
        this.time = time;
        this.moves = moves;

    }

    /**
     * Возвращает сохраненное время
     * */
    public Time getTime() {
        return time;
    }

    /**
     * Возвращает сохраненное количество шагов
     * */
    public int getMoves() {
        return moves;
    }

    /**
     * Создает кольца
     * */
    public void buildCylinders(){
        tempStackNumber = -1;

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

            block.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler((Cylinder)pr.getIntersectedNode());
            });
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

            block.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler((Cylinder)pr.getIntersectedNode());
            });
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

            block.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler((Cylinder)pr.getIntersectedNode());
            });
            temp2.push(block);
        }
        tempField[2] = temp2;

        pField = tempField;
    }
}
