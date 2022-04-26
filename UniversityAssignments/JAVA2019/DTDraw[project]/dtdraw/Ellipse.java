package dtdraw;
import javafx.scene.canvas.GraphicsContext; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.io.IOException; 
 
public class Ellipse extends Shape{ 
  public Point s,e;
  public Ellipse( Point S,Point E ){
    s=S;e=E;
  }
  public Ellipse(){
    s=new Point(0,0);
    e=new Point(0,0);
  }
  public void draw(GraphicsContext ctx){
    ctx.beginPath();
    ctx.arc( (s.x+e.x)/2,(s.y+e.y)/2,Math.abs(e.x-s.x)/2,Math.abs(e.y-s.y)/2,0,360 );
    ctx.closePath();
    applyStyle(ctx);
    ctx.fill();
    ctx.stroke();
  } 


  public void handleStart(Point S){
    s=S;
  }
  public Shape handleUpdate(Point L){
    e=L;
    return this;
  }
  public Shape handleEnd(Point L){
    e=L;
    return ( s.x==e.x && s.y==e.y?null:this );
  }
  public Shape build(){
    return new Ellipse();
  }
  

} 
