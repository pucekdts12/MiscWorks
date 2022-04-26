package galaga;
import javafx.scene.paint.Paint;

public class Bullet extends Player{
  //protected Paint color=Paint.valueOf("00FF00");
  private static int amount=0;
  private static int LIMIT=1;
  //protected double w=5,h=5;
  private Bullet(double x,double y){
    super(x,y);
    color=Paint.valueOf("#00AAFF");
    w=5;h=5;
    amount++;
  }
  public static Bullet spawn(double x,double y){
    if( amount>=LIMIT )  return null;

    Bullet b=new Bullet(x,y);
    b.start();
    return b;
  }

  public void run(){
    try{
      //this.sleep( 1000 );
      while(running && !exit){
        update(0,-10);
        checkHit();
        this.sleep(20);
      }

    }catch( InterruptedException e ){}
  }

  public void checkHit(){
    Enemy e=game.getSpatial(x,y);
    if( e!=null ){
      if( !e.visible ) return;
      if( x>e.x+e.w || e.x>x+w ) return;
      if( y>e.y+e.h || e.y>y+h ) return;
      e.onHit();
      destroy(0);
    }else{
      if( x<0 || x+w>game.W ){destroy(-1);return;}
      if( y<0 || y+h>game.H ){destroy(-1);return;}
    }

  }
  private void destroy(int s){
    --amount;
    exit=false;
    interrupt();
    game.score.updateScore(s);
    clear();
  }


}