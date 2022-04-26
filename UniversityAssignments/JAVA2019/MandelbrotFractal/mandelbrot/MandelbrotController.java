package mandelbrot;
import mandelbrot.*;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import java.util.Map;
import java.util.function.Function;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Pos;

public class MandelbrotController{
  public VBox root;
  public HBox menu;
  public Canvas mainCanvas,selectCanvas;
  public TextField flWidth,flHeight,flR,flTL,flBR;
  public MyPane canvasPane;
  private GraphicsContext mgc,sgc;
  private PixelWriter p;
  public Stage _stage;
  private int W,H,iSX,iSY,iLX,iLY;
  private MandelFractal M;
  private ChoiceBox boxFn;
  public void initialize(){//Platform.runLater(()->{});
    initValues();
    flWidth.setText(W+"");
    flHeight.setText(H+"");
    
    InitMouseCallbacks();
    M = new MandelFractal();

    boxFn=new ChoiceBox();
    for( Map.Entry<String,Function<Integer,Color>> x :  M.fnColor.entrySet()){
      boxFn.getItems().add(x.getKey());
    }


    boxFn.getSelectionModel().selectedItemProperty().addListener((e)->{
      M.setColor(((ReadOnlyObjectProperty)(e)).getValue().toString());
      draw();
    });

    boxFn.getSelectionModel().selectLast();
    menu.getChildren().add(boxFn);
    menu.setAlignment(Pos.CENTER_LEFT);
    reset();
    Platform.runLater(()->{ // after initialize
      _stage=(Stage)root.getScene().getWindow();
    });
  }
  public void initValues(){
    mgc=mainCanvas.getGraphicsContext2D();
    sgc=selectCanvas.getGraphicsContext2D();
    sgc.setStroke(Color.WHITE);
    p=mgc.getPixelWriter();

    W=(int)canvasPane.getWidth();
    H=(int)canvasPane.getHeight();
  }

  public void clear(Canvas c){
    c.getGraphicsContext2D().clearRect(0,0,c.getWidth(),c.getHeight());
  }

  public void draw(){
    clear(mainCanvas);
    clear(selectCanvas);
    Complex a=new Complex(flTL.getText());
    Complex b=new Complex(flBR.getText());
    M.draw(p,a,b,W,H);
  }
  public void reset(){
    //clear(mainCanvas);
    //clear(selectCanvas);
    flTL.setText(new Complex(-2,1).toString());
    flBR.setText(new Complex(2,-1).toString());
    //M.draw(p,new Complex(-2,1),new Complex(2,-1),W,H);
    draw();
  }
  public void update(){
    H=Integer.parseInt(flHeight.getText());W=Integer.parseInt(flWidth.getText());
    M.R=Integer.parseInt(flR.getText());
    canvasPane.resize(W,H);
    _stage.sizeToScene();
    reset();
  }

  private void InitMouseCallbacks(){
    selectCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e)->{
      iSX=(int)e.getX();
      iSY=(int)e.getY();      
      clear(selectCanvas);
    });
    selectCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent e)->{
      iLX=(int)e.getX();
      iLY=(int)e.getY();
      clear(selectCanvas);
      sgc.strokeRect(Math.min(iSX,iLX),Math.min(iSY,iLY),Math.abs(iLX-iSX),Math.abs(iLY-iSY));
    });
    selectCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,(e)->{
      Complex a=new Complex();
      Complex b=new Complex();
      M.translate(a,Math.min(iSX,iLX),Math.max(iSY,iLY));
      M.translate(b,Math.max(iSX,iLX),Math.min(iSY,iLY));
      flTL.setText(a.toString());
      flBR.setText(b.toString());
    });
  }
  
}