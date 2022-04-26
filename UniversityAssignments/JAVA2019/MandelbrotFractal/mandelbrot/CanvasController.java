package mandelbrot;

import javafx.scene.layout.GridPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.effect.BlendMode;


public class CanvasController{
  public VBox root;
  public Canvas mainCanvas;
  private GraphicsContext g;
  private double iSX;
  private double iSY;
  public void initialize(){
    System.out.println("Loaded");
    g=mainCanvas.getGraphicsContext2D();
    g.setStroke(Paint.valueOf("#F00"));
    mainCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e)->{
      clear();
      iSX=e.getX();
      iSY=e.getY();
      g.beginPath();
      g.moveTo(iSX,iSY);
    });
    mainCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent e)->{
      
    });
    mainCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,(MouseEvent e)->{
      g.rect(iSX,iSY,e.getX()-iSX,e.getY()-iSY);
      g.closePath();
      g.stroke();
      g.fill();
    });
  }
  public void hello(){

  };
  public void clear(){
    g.clearRect(0,0,mainCanvas.getWidth(),mainCanvas.getHeight());
  };
  public void draw(){

  };
  public void rectangle(){
    g.save();
    //g.setGlobalBlendMode(BlendMode.SCREEN);
    g.setGlobalAlpha(0.5);
    g.rect(40,40,100,100);
    g.setStroke(Paint.valueOf("#000"));
    g.setFill(Paint.valueOf("#00F"));
    g.stroke();
    g.fill();


    g.restore();
  };
}