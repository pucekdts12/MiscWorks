package dtdraw;
import javafx.scene.canvas.GraphicsContext; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.io.IOException; 
 
public class Line extends Shape{ 
  public Point s,e;
  transient static protected Point last;
  public Line( Point S,Point E ){
    s=S;e=E;
  }
  public Line(){
    s=new Point(0,0);
    e=new Point(0,0);
  }
  public void draw(GraphicsContext ctx){
    ctx.beginPath();
    ctx.moveTo(s.x,s.y);
    ctx.lineTo(e.x,e.y);
    ctx.closePath();
    applyStyle(ctx);
    ctx.stroke();
  } 


  public void handleStart(Point S){
    if( !shift ){
      s=S;
    }else{
      s=last;
    }
  }
  public Shape handleUpdate(Point L){
    e=L;
    return this;
  }
  public Shape handleEnd(Point L){
    e=L;
    last=L;
    //return ( s.x==e.x && s.y==e.y?null:this );
    return this;
  }

} 
