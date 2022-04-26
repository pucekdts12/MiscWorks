package dtdraw;
import javafx.scene.canvas.GraphicsContext; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.io.IOException; 
 
public class Rectangle extends Shape{ 
  public Point s,e;
  public Rectangle( Point S,Point E ){
    s=S;e=E;
  }
  public Rectangle(){
    s=new Point(0,0);
    e=new Point(0,0);
  }
  public void draw(GraphicsContext ctx){
    ctx.beginPath();
    //ctx.moveTo(s.x,s.y);
    ctx.rect( s.x,s.y,e.x-s.x,e.y-s.y );
    
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
    return new Rectangle();
  }
  

} 
