package galaga;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Platform;

public class Main extends Application{
  public void start(Stage stage) throws Exception{
    FXMLLoader fxml =  new FXMLLoader(getClass().getResource("/res/galaga.fxml"));
    Parent root = fxml.load();
    Scene scene = new Scene(root);
    stage.setTitle("Galaga");
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(e->{Main.onExit();});
  }
  public static void main(String[] args){
    launch(args);
  }
  public static void onExit(){
    Enemy.running=false;
    Game.running=false;
    Platform.exit();
    System.exit(0);
  }
}