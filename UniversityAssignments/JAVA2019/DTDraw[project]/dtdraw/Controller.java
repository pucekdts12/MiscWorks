package dtdraw;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import javafx.scene.layout.*;
import javafx.scene.canvas.*;

import javafx.stage.Stage;

import javafx.scene.paint.Paint;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import javafx.application.Platform;

import javafx.scene.input.*;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.ColorPicker;

import javafx.scene.control.Alert;

public class Controller{
  public static String VERSION="0.8";
  public Layer mainLayer;
  public StackPane layers;
  public ToggleGroup tools;
  public ColorPicker strokeColor,fillColor;
  public Slider strokeWidth;
  public Label strokeWidthText;

  Point S,L;
  Shape current;
  public void initialize(){

    InitMouseCallbacks();
    fillColor.setValue(Color.valueOf("#00000000"));
    //strokeWidthText.setText("1");
    strokeWidthText.setText( (int)strokeWidth.getValue()+"" );
    strokeWidth.valueProperty().addListener( (o,oldval,newval)->{
      strokeWidth.setValue(newval.intValue());
      strokeWidthText.setText(newval.intValue()+"");
    } );


  }

  private void InitMouseCallbacks(){
    layers.addEventHandler(MouseEvent.MOUSE_PRESSED,(MouseEvent e)->{
      try{
        Class s = Class.forName( "dtdraw."+((ToolButton)tools.getSelectedToggle()).getTool() );
        Shape.shift=e.isShiftDown();
        current = (Shape)s.getConstructor().newInstance();
        //current.updateStyle( strokeColor.getValue().toString(),fillColor.getValue().toString(),1.0 );
        current.updateStyle( strokeColor.getValue().toString(),fillColor.getValue().toString(),new Integer(strokeWidthText.getText()) );
      }catch(Exception exc){
        System.out.println("Cannot create selected shape!");
      }

      Shape.shift=e.isShiftDown();
      S=new Point(e.getX(),e.getY());
      current.handleStart(S);
      
    });

    layers.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent e)->{
      Shape.shift=e.isShiftDown();

      L=new Point(e.getX(),e.getY());
      
      mainLayer.update();
      Shape tmp = current.handleUpdate(L);
      try{

        mainLayer.draw( tmp );

      }catch(Exception ex){}
    });

    layers.addEventHandler(MouseEvent.MOUSE_RELEASED,(e)->{
      Shape.shift=e.isShiftDown();

      L=new Point(e.getX(),e.getY());
      Shape toSave=current.handleEnd(L);
      if( toSave!=null ){
        mainLayer.addShape( toSave );
      }
    });
  }
  private File showFileDialog( boolean save ){
    var dialog = new FileChooser();
    dialog.setTitle( (save?"Select save file location":"Select file to open") );
    dialog.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("DTDraw Files","*.dtdraw") );
    dialog.setInitialDirectory(new File("."));
    File file = (save ? dialog.showSaveDialog(null) : dialog.showOpenDialog(null));
    return file;
  }


  public void save(){
    try{
      var out=new ObjectOutputStream(new FileOutputStream( showFileDialog(true) ));
      out.writeObject(VERSION);
      mainLayer.writeObject(out);
      out.close();
    }catch(Exception e){}
  }

  public void load(){
    try{
      var in=new ObjectInputStream(new FileInputStream( showFileDialog(false) ));
      String version = (String)in.readObject();
      if( !version.equals(VERSION) ) throw new Exception("");
      mainLayer.readObject(in);
      in.close();
    }catch(Exception e){
      Alert a = new Alert(Alert.AlertType.ERROR);
      a.setTitle("Can't load file!");
      a.setContentText(e.getMessage());
      a.showAndWait();
    }
  }
  public void change(){
    Polygon.cur=null;
  }
  public void undo(){
    mainLayer.undo();
  }
  
  



}