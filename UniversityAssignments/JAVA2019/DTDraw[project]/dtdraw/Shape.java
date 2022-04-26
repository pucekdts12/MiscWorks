package dtdraw;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;






public abstract class Shape implements Serializable{
  public int zindex;
  public String fill="#000000FF";
  //public Color fill;
  public String stroke="#000000FF";
  //public Color stroke;
  public double strokeWidth=1.0;
  public static boolean shift=false;
  public abstract void draw(GraphicsContext ctx);
  public abstract void handleStart(Point S);
  public abstract Shape handleUpdate(Point L);
  
  public abstract Shape handleEnd(Point L);


  
  public void updateStyle( String s,String f,double w ){
    strokeWidth=w;stroke=s;fill=f;
  }

  public void applyStyle(GraphicsContext ctx){
    ctx.setStroke( Color.valueOf(stroke) );
    ctx.setFill( Color.valueOf(fill) );
    ctx.setLineWidth(strokeWidth);
  }
  public Shape undo(){
    return this;
  }


}