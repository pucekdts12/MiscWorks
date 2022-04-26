package mandelbrot;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class CanvasFX extends Application{

  @Override
  public void start(Stage stage) throws Exception {
    // Contents
    System.out.println(getClass().getResource("/res/layout.fxml"));
    Parent root = FXMLLoader.load(getClass().getResource("/res/canvasfx.fxml"));
    //Scene scene = new Scene(root,400,400);
    Scene scene = new Scene(root);
    stage.setTitle("CanvasFX");
    stage.setScene(scene);
    stage.show();
  }
  
  public static void main(String[] args){
    launch(args);
  }
  
}