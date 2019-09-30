package main.MainGame.Main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.PickResult;
import javafx.scene.shape.Cylinder;
import javafx.util.Duration;
import main.MainGame.ControllerMain;
import main.MainGame.Shapes.Toroid;
import main.MainGame.Time.Time;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Класс игры. В конструктор передается общее количество колец и по ним генерируется поле игры.
 * */
public class Game implements Serializable{

    /**Сложность игры*/
    private float difficulty;
    /**Хранит номер шеста с которого идет перенос, tempStackNumber = -1, когда никакой шест не выбран.*/
    private transient int tempStackNumber = -1;

    /**Содержит целочислинные переменные, где чем больше номер тем больше размер кольца(дублирует стек с кольцами, но вместо радиуса используется целые числа),
     * необходимо для сравнения колец*/
    private ArrayList<Integer>[] field = new ArrayList[3];
    /**Содержит стеки с кольцами, где номер стека соответствует левому, правому, и центральному шесту в такой последовательности в массиве*/
    private transient volatile Stack<Toroid>[] pField = new Stack[3];
    /**Содержит шесты*/
    private transient ArrayList<ArrayList<Cylinder>> stractField = new ArrayList<>();


    public Game(int difficulty){

        this.difficulty = difficulty;

        ArrayList<Integer> temp = new ArrayList<>();

        for(int i = difficulty; i > 0; i--){
            temp.add(i);
        }

        field[0] = temp;
        field[1] = new ArrayList<>();
        field[2] = new ArrayList<>();

        buildTorus();
    }

    /**Содержит контроллер игры*/
    private transient ControllerMain controllerMain = new ControllerMain();
    /**
     * Передает ссылку на контроллер игры.
     * */
    public void setControllerMain(ControllerMain controllerMain){
        this.controllerMain = controllerMain;
    }
    /**Определяет первый раз или не первый заходит в метод clickHandler*/
    private boolean first = true;
    /**
     * Метод вызывается при нажатии на игровые фигуры.
     * Определяется с какого и на какой шпиль переносятся кольца.
     * */
    private void clickHandler(Node node){

        if(!auto && first){

            first = false;

            controllerMain.StartTimer();
        }

        if(tempStackNumber == -1){

            Toroid toroid;
            if(node instanceof Cylinder){
                tempStackNumber = returnStructNumber((Cylinder)node);
                return;
            }else{ toroid = (Toroid) node; }

            if(pField[0].contains(toroid)){
                tempStackNumber = 0;
            }else{
                if(pField[1].contains(toroid)){
                    tempStackNumber = 1;
                }else{ tempStackNumber = 2; }
            }
        }else{
            Toroid toroid;

            if(node instanceof Cylinder){
                move(tempStackNumber,returnStructNumber((Cylinder)node));
                tempStackNumber = -1;
                return;
            }else{ toroid = (Toroid) node; }

            if(pField[0].contains(toroid)){
                move(tempStackNumber,0);
                tempStackNumber = -1;
            }else{
                if(pField[1].contains(toroid)){
                    move(tempStackNumber,1);
                    tempStackNumber = -1;
                }else{
                    move(tempStackNumber,2);
                    tempStackNumber = -1;
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


    /**Максимальный размер в ширину(размер низа шеста)*/
    private final float maxSize = 200;
    /**Высота кольца(диаметр)*/
    private final float blockSize = 40;
    /**Координата x левого шеста*/
    private final float xLeft = 250;
    /**Координата x правого шеста*/
    private final float xRight = 1150;
    /**Координата x центра*/
    private final float xCenter = 700;
    /**Радиус шеста*/
    private final float topR = 30;
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
                clickHandler(pr.getIntersectedNode());
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
                clickHandler(pr.getIntersectedNode());
            });
            temp.add(top);

            stractField.add(temp);
        }

        stractField.get(1).get(0).translateXProperty().set(xLeft);
        stractField.get(1).get(1).translateXProperty().set(xLeft);

        stractField.get(2).get(0).translateXProperty().set(xRight);
        stractField.get(2).get(1).translateXProperty().set(xRight);

        return getGroup();
    }

    /**
     * Возвращает группу построенную на основе всех элементов
     * */
    private Group getGroup(){

        Group group = new Group();
        for (int i = 0; i< 3; ++i){
            for(int j = 0; j<2;++j){
                group.getChildren().add(stractField.get(i).get(j));
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
     * */
    private void move(int from, int to){

        if(field[from].size() == 0){
            return;
        }

        boolean win = false;
        if(field[to].size() == 0 || field[to].get(field[to].size()-1) > field[from].get(field[from].size()-1)){
            field[to].add(field[from].get(field[from].size()-1));
            field[from].remove(field[from].size()-1);

            if(field[2].size() == difficulty){
                System.out.println("win");
                if(auto){
                    Platform.runLater(() -> controllerMain.MoveHandler(1));
                }
                win = true;
            }
            else{
                System.out.println("can");
                if(auto){
                    Platform.runLater(() -> controllerMain.MoveHandler(1));
                }else{
                    controllerMain.MoveHandler(1);
                }
            }

            pMove(from, to, win);
        }
        else{
            System.out.println("cannot");
            if(auto){
                Platform.runLater(() -> controllerMain.MoveHandler(-1));
            }else{
                controllerMain.MoveHandler(-1);
            }
        }
    }

    /**
     * Передвигает кольцо с одного шеста на другой
     * */
    private void pMove(int from, int to, boolean win){

        double yStart = 500+(difficulty+1.5)*blockSize/2+20;

        Toroid temp = pField[from].pop();
        pField[to].push(temp);
        double x = xLeft;
        switch (to) {
            case 0:
                x = xLeft;
                break;
            case 1:
                x = xCenter;
                break;
            case 2:
                x = xRight;
                break;
        }
        animate(temp,x,yStart-pField[to].size()*blockSize, win);
        //temp.translateYProperty().set(yStart-pField[to].size()*blockSize);

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
     * Возвращает сохраненное время, необходимо для передачи в winnerController
     * */
    public Time getTime() {
        return time;
    }

    /**
     * Возвращает сохраненное количество шагов, необходимо для передачи в winnerController
     * */
    public int getMoves() {
        return moves;
    }

    /**
     * Возвращает сохраненную сложность, необходимо для передачи в winnerController, если объект сериализовался
     * */
    public int getDifficulty() {
        return  (int)difficulty;
    }

    /**
     * Создает кольца
     * */
    public void buildTorus(){
        tempStackNumber = -1;

        MaterialsGenerator materialsGenerator= new MaterialsGenerator();
        double yStart = 500+(difficulty+1.5)*blockSize/2+20;

        Stack<Toroid>[] tempField = new Stack[3];

        Stack<Toroid> temp = new Stack<>();
        for (int i = 0; i< field[0].size(); i++){
            Toroid block = new Toroid((maxSize-(difficulty-field[0].get(i)+1)*(maxSize-topR)/(difficulty+1)-topR)/2+topR,
                    (maxSize-(difficulty-field[0].get(i)+1)*(maxSize-topR)/(difficulty+1)-topR)/2,
                    blockSize/2,
                    45);

            block.translateXProperty().set(xLeft);
            block.translateYProperty().set(yStart-(i+1)*blockSize);
            block.setMaterial(materialsGenerator.GetMAterialById((int)difficulty-field[0].get(i)));
            block.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler(pr.getIntersectedNode());
            });
            temp.push(block);
        }
        tempField[0] = temp;

        Stack<Toroid> temp1 = new Stack<>();
        for (int i = 0; i< field[1].size(); i++){
            Toroid block = new Toroid((maxSize-(difficulty-field[1].get(i)+1)*(maxSize-topR)/(difficulty+1)-topR)/2+topR,
                    (maxSize-(difficulty-field[1].get(i)+1)*(maxSize-topR)/(difficulty+1)-topR)/2,
                    blockSize/2,
                    45);

            block.translateXProperty().set(xCenter);
            block.translateYProperty().set(yStart-(i+1)*blockSize);
            block.setMaterial(materialsGenerator.GetMAterialById((int)difficulty-field[1].get(i)));
            block.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler(pr.getIntersectedNode());
            });
            temp1.push(block);
        }
        tempField[1] = temp1;

        Stack<Toroid> temp2 = new Stack<>();
        for (int i = 0; i< field[2].size(); i++){
            Toroid block = new Toroid((maxSize-(difficulty-field[2].get(i)+1)*(maxSize-topR)/(difficulty+1)-topR)/2+topR,
                    (maxSize-(difficulty-field[2].get(i)+1)*(maxSize-topR)/(difficulty+1)-topR)/2,
                    blockSize/2,
                    45);

            block.translateXProperty().set(xRight);
            block.translateYProperty().set(yStart-(i+1)*blockSize);
            block.setMaterial(materialsGenerator.GetMAterialById((int)difficulty-field[2].get(i)));
            block.setOnMouseClicked(e->{
                PickResult pr = e.getPickResult();
                clickHandler(pr.getIntersectedNode());
            });
            temp2.push(block);
        }
        tempField[2] = temp2;

        pField = tempField;
    }

    /**Определяет идет ли анимация*/
    private volatile Boolean animationInProcess = false;
    /**
     * Анимирует переход колец
     * */
    private void animate(Toroid toroid, double toX, double toY, boolean win){
        double y = stractField.get(0).get(1).getTranslateY()-stractField.get(0).get(1).getHeight()/2-blockSize/2;
        Thread animation = new Thread(()->{
            Timeline goingUp = new Timeline();
            Timeline goingDown = new Timeline();
            Timeline goingToX = new Timeline();

            goingUp.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(toroid.translateYProperty(), toroid.translateYProperty().getValue())),

                    new KeyFrame(new Duration(400),
                            new KeyValue(toroid.translateYProperty(), y)));

            goingToX.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(toroid.translateXProperty(), toroid.getTranslateX())),

                    new KeyFrame(new Duration(500),
                            new KeyValue(toroid.translateXProperty(), toX)));

            goingDown.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(toroid.translateYProperty(), y)),

                    new KeyFrame(new Duration(400),
                            new KeyValue(toroid.translateYProperty(), toY)));

            goingUp.play();
            while(goingUp.getStatus() == Animation.Status.RUNNING){
                try{
                    Thread.sleep(50);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            goingToX.play();
            while(goingToX.getStatus() == Animation.Status.RUNNING){
                try{
                    Thread.sleep(50);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            goingDown.play();
            while(goingDown.getStatus() == Animation.Status.RUNNING){
                try{
                    Thread.sleep(50);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            if(win && !auto){
                Platform.runLater(() -> controllerMain.MoveHandler(0));
            }
            if(win && auto){
                Platform.runLater(() -> controllerMain.MoveHandler(2));
            }
            animationInProcess = false;
        });
        animation.start();
    }

    /**Определяет включен ли автоматический сбор*/
    private Boolean auto = false;
    /**
     * Начинает автоматический сбор
     * */
    public void auto(){
        auto = true;
        Thread thread = new Thread(() -> autoSolve(0,1,2,(int)difficulty));
        thread.start();
    }

    /**
     * Автоматический сбор
     * */
    private void autoSolve(int a, int b, int c, int n){

            if(n>0) {
                 autoSolve(a,c,b,n-1);
                 move(a,c);

                 animationInProcess = true;
                 while(animationInProcess){
                     try{
                         Thread.sleep(50);
                     }catch (Exception ex){
                         ex.printStackTrace();
                     }
                 }
                 autoSolve(b,a,c,n-1);
            }

    }
}
