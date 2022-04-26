package dtdraw;
import javafx.scene.canvas.GraphicsContext; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.io.IOException; 
import java.util.ArrayList;
 
public class Polygon extends Shape{ 
  public ArrayList<Point> points;

  transient static Polygon cur;

  public Polygon(){
    if( cur==null || !shift){
      cur=this;
      points=new ArrayList<>();
    }
  }

  public void draw(GraphicsContext ctx){
    if( points.size()<1 ) return;

    Point s = points.get(0);
    ctx.beginPath();
    ctx.moveTo(s.x,s.y);
    for( Point p : points ){
      ctx.lineTo(p.x,p.y);
    }
    ctx.closePath();
    applyStyle(ctx);
    ctx.stroke();
    ctx.fill();
  } 


  public void handleStart(Point S){
    if( cur.points.size()==0 ){
      cur.points.add(S);
      cur.points.add(S);
    }else{
      cur.points.add(S);
    }
  }

  public Shape handleUpdate(Point L){
    cur.points.set(cur.points.size()-1,L);
    return cur;
  }

  public Shape handleEnd(Point E){
    return cur;
  }

  public Shape undo(){
    if( points.size()<3 ){
      cur=null;
      return this;
    }
    points.remove( points.size()-1 );
    return null;
  }

  
  

} 
