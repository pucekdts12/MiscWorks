package mandelbrot;
import mandelbrot.Complex;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class Mandelbrot extends Application{

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxml =  new FXMLLoader(getClass().getResource("/res/mandelbrot.fxml"));
    //Parent root = FXMLLoader.load(getClass().getResource("/res/mandelbrot.fxml"));
    Parent root = fxml.load();
    //MandelbrotController ctr = fxml.getController();
    //ctr._stage=stage;
    Scene scene = new Scene(root);
    //stage.widthProperty().bind(scene.widthProperty());
    //stage.heightProperty().bind(scene.heightProperty());
    //stage.sizeToScene();
    stage.setTitle("Mandelbrot");
    stage.setScene(scene);
    stage.show();
  }
  
  public static void main(String[] args){
    launch(args);
  }
  
  
}