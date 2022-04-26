package dtdraw;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.io.Serializable;

public class Layer extends Canvas implements Serializable{
  public ArrayList<Shape> objects;

  transient GraphicsContext c;
  //protected Canvas c;

  public Layer(){
    objects=new ArrayList<Shape>();
    c=getGraphicsContext2D();
  }

  public Layer(ArrayList<Shape> A){
    objects=A;
    c=getGraphicsContext2D();
  }



  public void update(){
    c.clearRect( 0,0,getWidth(),getHeight() );
    for( Shape s : objects ){
      draw(s);
    }
  }
  public void draw(Shape s){
    //System.out.println("c is "+c);
    s.draw(c);
  }

  public void writeObject(ObjectOutputStream out) throws IOException{
    out.writeObject( objects );
  }

  public void readObject(ObjectInputStream in)throws IOException,ClassNotFoundException{
    clearObjects();
    objects=(ArrayList<Shape>)in.readObject();
    update();
  }

  public void load(ObjectInputStream in) throws IOException,ClassNotFoundException{
    objects=(ArrayList<Shape>)in.readObject();
  }

  public void addShape(Shape s){
    if( !objects.contains(s) ){
      objects.add(s);
    }
    
    update();
  }

  public void clearObjects(){
    c.clearRect( 0,0,getWidth(),getHeight() );
    objects.clear();
  }
  public void undo(){
    if( objects.isEmpty() ) return;
    Shape toDelete=objects.get(objects.size()-1).undo();
    if( toDelete!=null ){
      objects.remove(toDelete);
    }
    update();
  }


}