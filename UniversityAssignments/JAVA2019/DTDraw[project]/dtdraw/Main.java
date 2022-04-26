package dtdraw;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Platform;

public class Main extends Application{
  public void start(Stage stage) throws Exception{
    FXMLLoader fxml =  new FXMLLoader(getClass().getResource("/res/layout.fxml"));
    Parent root = fxml.load();
    Scene scene = new Scene(root);
    stage.setTitle("DTDraw");
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(e->{Main.onExit();});
  }
  public static void main(String[] args){
    launch(args);
  }
  public static void onExit(){
    Platform.exit();
    System.exit(0);
  }
}