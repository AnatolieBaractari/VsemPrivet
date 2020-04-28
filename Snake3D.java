package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {
    private Scene scene;
    public enum Direction {
        UP,DOWN,RIGHT,LEFT
    }
    private Direction direction=Direction.RIGHT;
    public static final int BLOCK_SIZE=30;
    public static final int APP_W=53*BLOCK_SIZE;
    public static final int APP_H=40*BLOCK_SIZE;



    private Timeline timeline=new Timeline();
    private Box food=new Box(30,30,30);
    private Random r=new Random();
    private Box[] snake=new Box[1001];
    private Box floor=new Box(1600,1,1300);
    private Camera camera1=new PerspectiveCamera();

    private int intscore=0;
    private int frames=0;
    private Label score =new Label();
    private int bodyIndex=0;
    private int gotoX;
    private int gotoZ;
    private int thisX;
    private int thisZ;

    private Scene createContent(){
        Group root =new Group();

        root.setTranslateZ(-200);
        scene=new Scene(root,800,600,true);
        scene.setCamera(camera1);
        camera1.setTranslateY(camera1.getTranslateY()-700);
        camera1.setTranslateZ(camera1.getTranslateZ()-200);
        camera1.setRotationAxis(Rotate.X_AXIS);
        camera1.setRotate(-30);

        floor.setTranslateY(16);
        floor.setTranslateX(800);
        floor.setTranslateZ(600);

        food.setTranslateX((int)(Math.random()*(APP_W-BLOCK_SIZE))/BLOCK_SIZE*BLOCK_SIZE);
        food.setTranslateZ((int)(Math.random()*(APP_H-BLOCK_SIZE))/BLOCK_SIZE*BLOCK_SIZE);
        PhongMaterial material=new PhongMaterial(Color.LIGHTCYAN);
        food.setMaterial(material);

        PhongMaterial mat=new PhongMaterial();
        mat.setDiffuseColor(Color.RED);
        for(int i=1;i<1001;i++){
            snake[i]=new Box(30,30,30);
            if(i%2==0) {
                snake[i].setMaterial(mat);
            }
            else{
                snake[i].setMaterial(new PhongMaterial(Color.SEAGREEN));
            }
            snake[i].setTranslateX(3000);
            snake[i].setTranslateZ(-i*20);
        }
        snake[0]=new Box(30,30,30);
        snake[0].setMaterial(mat);
        snake[0].setTranslateX(20);
        snake[0].setTranslateY(0);


        KeyFrame frame = new KeyFrame(Duration.seconds(0.1),e->{
            frames+=1;
            if (frames==360){
                frames=0;
            }

            gotoX=(int)(snake[0].getTranslateX());
            gotoZ=(int)(snake[0].getTranslateZ());
            switch (direction){
                case UP:
                    snake[0].setTranslateZ(snake[0].getTranslateZ()-30);
                    break;

                case DOWN:
                    snake[0].setTranslateZ(snake[0].getTranslateZ()+30);
                    break;

                case LEFT:
                    snake[0].setTranslateX(snake[0].getTranslateX()-30);
                    break;

                case RIGHT:
                    snake[0].setTranslateX(snake[0].getTranslateX()+30);
                    break;
            }
            for(int i=1;i<=bodyIndex;i++){
                thisX= (int) snake[i].getTranslateX();
                thisZ= (int) snake[i].getTranslateZ();
                snake[i].setTranslateX(gotoX);
                snake[i].setTranslateZ(gotoZ);
                gotoX=thisX;
                gotoZ=thisZ;
            }
            if(snake[0].getBoundsInParent().intersects(food.getBoundsInParent())){
                food.setTranslateX((int)(Math.random()*(APP_W-BLOCK_SIZE))/BLOCK_SIZE*BLOCK_SIZE);
                food.setTranslateZ((int)(Math.random()*(APP_H-BLOCK_SIZE))/BLOCK_SIZE*BLOCK_SIZE);
                if(bodyIndex<1000) {
                    intscore++;
                    score.setText(intscore + "");
                    bodyIndex++;
                }
            }

            for(int i=1;i<=bodyIndex;i++){
                if(snake[0].getTranslateX()==snake[i].getTranslateX()
                        && snake[0].getTranslateZ()==snake[i].getTranslateZ()){
                    restartGame();
                }
            }


            if(snake[0].getTranslateX()<0 && direction==Direction.LEFT){
                snake[0].setTranslateX(APP_W);
            }
            if (snake[0].getTranslateX()>APP_W && direction==Direction.RIGHT){
                snake[0].setTranslateX(0);
            }

            if(snake[0].getTranslateZ()<0 && direction==Direction.UP){
                snake[0].setTranslateZ(APP_H);
            }
            if (snake[0].getTranslateZ()>APP_H && direction==Direction.DOWN){
                snake[0].setTranslateZ(0);
            }


        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Animation.INDEFINITE);

        for(int i=0;i<1001;i++){
            root.getChildren().add(snake[i]);
        }
        root.getChildren().addAll(food,floor);

        scene.setOnKeyPressed(e->{
            switch (e.getCode()) {
                case S:
                case DOWN:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case W:
                case UP:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
                case A:
                case LEFT:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case D:
                case RIGHT:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;

                case Q:
                    int max = bodyIndex + 10;
                    for (int i = bodyIndex; i <= (max); i++) {
                        intscore++;
                        score.setText(intscore * 69 + "");
                        bodyIndex++;
                        if(bodyIndex%2==0) {
                            snake[bodyIndex].setMaterial(new PhongMaterial(Color.RED));
                        }
                        else{
                            snake[bodyIndex].setMaterial(new PhongMaterial(Color.SEAGREEN));
                        }
                    }
                    break;
                case R:
                    if(bodyIndex>10){
                        int min = bodyIndex - 10;
                        for (int i = bodyIndex + 1; i >=(min); i--) {
                            intscore--;
                            score.setText(intscore * 69 + "");
                            snake[bodyIndex].setMaterial(new PhongMaterial(Color.TRANSPARENT));
                            bodyIndex--;
                            snake[bodyIndex].setTranslateY(-1000);
                        }
                    }

                    break;

            }
        });
        return scene;
    }


    private void restartGame(){
        stopGame();
        startGame();
    }

    private void stopGame(){
        timeline.stop();
        for(int i=1;i<=bodyIndex;i++){
            snake[i].setTranslateY(-10000);
            snake[i].setTranslateZ(-2000);
            intscore=0;
        }
    }

    private void startGame(){
        bodyIndex=0;
        snake[0].setTranslateY(0);
        snake[0].setTranslateX(20);
        direction= Direction.RIGHT;
        timeline.play();
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(createContent());
        stage.show();
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

}