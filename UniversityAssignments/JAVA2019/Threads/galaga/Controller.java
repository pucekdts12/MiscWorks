package galaga;

import dts.*;

import javafx.scene.layout.*;
import javafx.scene.canvas.*;


import javafx.scene.paint.Paint;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Label;

//import javafx.scene.input.MouseEvent;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.KeyCode;
import javafx.scene.input.*;

public class Controller{
  public VBox root;
  public MyPane game;
  public Canvas enemies;
  private GraphicsContext gc;
  private Stage parent;
  public Label score;
  int iScore=0;
  public void initialize(){
    gc=enemies.getGraphicsContext2D();
    Game main=new Game(game,game.getWidth(),game.getHeight(),gc);
    main.score=(int x)->{
      iScore+=x;
      Platform.runLater(()->{score.setText(""+iScore);});
    };
    main.start();
    /*enemies.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e)->{
      main.checkHit(e.getX(),e.getY());
    });*/

    Platform.runLater(()->{
      parent = (Stage)root.getScene().getWindow();
      parent.addEventHandler(KeyEvent.KEY_PRESSED,(KeyEvent e)->{
        KeyCode key=e.getCode();
        if( key==key.SPACE ){
          main.pl.shoot();
        }else if( key==key.LEFT ){
          main.pl.moveLeft();
        }else if( key==key.RIGHT ){
          main.pl.moveRight();
        }else{
          System.out.println("[KEY]::"+e.getCode());
        }
        
      });
    });
  }

}