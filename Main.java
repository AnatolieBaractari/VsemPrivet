package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {
    private Scene scene;
    private Group group;
    private Camera camera1=new PerspectiveCamera();
    private PhongMaterial material=new PhongMaterial();
    private AmbientLight light=new AmbientLight();
    private Sphere [] fireballs;
    private int fireballCounter=0;
    private Box [] boxes;
    private KeyFrame frame;
    private Timeline t;
    private Label label=new Label();
    private int frameCounter=0;

    private Scene createContent() {
        group=new Group();
        group.getChildren().addAll(createFireballs());
        group.getChildren().addAll(createBasis());
        group.getChildren().addAll(createMap());
        group.getChildren().add(createLabel());
        scene=new Scene(group,1000,800,true);

        camera1.setRotationAxis(Rotate.Y_AXIS);
        camera1.setTranslateZ(camera1.getTranslateZ()+100);
        scene.setCamera(camera1);

        frame=new KeyFrame(Duration.seconds(0.016),event->{
            frameCounter++;
            checkFire();
            camera1.setTranslateZ(camera1.getTranslateZ()+45);
            if(frameCounter<400){
                label.setTranslateZ(label.getTranslateZ()+45);
            }

            scene.setOnKeyPressed(e->{
                switch (e.getCode()){
                    case UP:
                        camera1.setTranslateZ(camera1.getTranslateZ()+150);
                        break;
                    case DOWN:
                        camera1.setTranslateZ(camera1.getTranslateZ()-50);
                        break;
                    case RIGHT:
                        if(camera1.getTranslateX()<500)
                        camera1.setTranslateX(camera1.getTranslateX()+50);
                        break;
                    case LEFT:
                        if(camera1.getTranslateX()>-300)
                        camera1.setTranslateX(camera1.getTranslateX()-50);
                        break;
                    case A:
                        camera1.setRotate(camera1.getRotate()-1);
                        break;
                    case D:
                        camera1.setRotate(camera1.getRotate()+1);
                        break;
                }

            });

            scene.setOnMouseClicked(e->{

                TranslateTransition t=new TranslateTransition(Duration.seconds(0.5),fireballs[fireballCounter]);
                t.setCycleCount(1);
                t.setFromX(camera1.getTranslateX()+450);
                t.setFromZ(camera1.getTranslateZ());
                t.setFromY(camera1.getTranslateY()+450);
                t.setToX(e.getX());
                t.setToZ(e.getZ());
                t.setToY(e.getY());

                FadeTransition f=new FadeTransition(Duration.seconds(0.5),fireballs[fireballCounter]);
                f.setFromValue(1);
                f.setToValue(0);
                f.setCycleCount(1);

                t.play();
                f.play();

                if(fireballCounter>=9){
                    fireballCounter=0;
                }
                else {
                    fireballCounter++;
                }
            });
        });

        t=new Timeline();
        t.setCycleCount(Animation.INDEFINITE);
        t.getKeyFrames().add(frame);

        return scene;
    }

    private Label createLabel() {
        label=new Label();
        label.setText("Click to shoot\nPress UP to accelerate\nPress DOWN to slow down\nPress LEFT to go left\nPress RIGHT to go right\nPress A to rotate to left\nPress D to rotate to right\n");
        label.setFont(Font.font(50));
        label.setTextFill(Color.BLUE);
        label.setTranslateX(300);
        label.setTranslateY(0);
        label.setTranslateZ(2500);
        return label;
    }

    private void checkFire() {
        for(int i=0;i<10;i++){
            for(int j=0;j<200;j++){
                if(fireballs[i].getBoundsInParent().intersects(boxes[j].getBoundsInParent())){
                    boxes[j].setMaterial(new PhongMaterial(Color.TRANSPARENT));
                }
            }
        }
    }


    private Sphere[] createFireballs() {
        PhongMaterial material=new PhongMaterial();
        material.setDiffuseMap(new Image("fireball.png"));
        fireballs=new Sphere[10];
        for(int i=0;i<10;i++){
            fireballs[i]=new Sphere(80);
            fireballs[i].setMaterial(material);
        }
        return fireballs;
    }

    private Box[] createBasis() {
        PhongMaterial material1=new PhongMaterial(Color.LIGHTCORAL);
        material1.setSelfIlluminationMap(new Image("illumination1.png"));
        Box [] uno =new Box[2];
        uno[0]=new Box(4000,10,700000);
        uno[0].setTranslateY(500);
        uno[0].setTranslateX(460);
        uno[1]=new Box(4000,10,700000);
        uno[1].setTranslateY(-100);
        uno[1].setTranslateX(460);
        for (int i=0;i<2;i++){
            uno[i].setMaterial(material1);
        }
       return uno;
    }

    private Box[] createMap() {
        material.setDiffuseColor(Color.LIGHTBLUE);
        Random r1=new Random();
        boxes=new Box[1000];
        double sum1=20000,sum2=20000;
        for(int i=0;i<1000;i++){
            int red=r1.nextInt(5)+1;
            double r= r1.nextInt(2)+1;
            if(i%2==0){
                boxes[i]=new Box(r*120,r*150,r*600);
                boxes[i].setTranslateZ(sum1);
                boxes[i].setTranslateX(0);
                boxes[i].setTranslateY(300);
                sum1+=(r*2000)+200;
            }
            else if(i%2==1){
                boxes[i]=new Box(r*120,r*150,r*600);
                boxes[i].setTranslateZ(sum2);
                boxes[i].setTranslateX(1200);
                boxes[i].setTranslateY(300);
                sum2+=(r*2000)+200;
            }
            if(red==1){
                boxes[i].setMaterial(new PhongMaterial(Color.RED));
            }
            else
                boxes[i].setMaterial(material);

        }
      return boxes;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(createContent());
        primaryStage.show();
        t.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
