package galaga;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.*;

public class Enemy extends Thread{
  //protected static double w=40,h=40;
  protected double w,h,x,y;
  public static boolean running=true;
  protected boolean exit=false;
  protected boolean visible=false;
  protected Game game;
  protected Paint color;
  Enemy(){
    this(0,0);
  }
  Enemy(double X,double Y){
    w=40;h=40;
    y=Y;x=X;
    color=Paint.valueOf("#FF0000");
  }

  public void setGame(Game g){game=g;}

  public void run(){
    try{
      this.sleep(Game.random.nextInt(15)*200);
      visible=true;
      //synchronized( game.pause ){
        while(running && !exit){
          for( int i=0;i<10;i++ ){
            update(5,0);
            this.sleep(200);
          }
          for( int i=0;i<10;i++ ){
            update(-5,0);
            this.sleep(200);
          }
          /*if( game.isPaused ){
            game.pause.wait();
          }*/

        }
      //}
    }catch( InterruptedException e ){}
  }

  public void exit(){
    exit=true;
  }
  public void update(int X,int Y){
    clear();
    x+=X;
    y+=Y;
    draw();
  }
  public void draw(){
    synchronized(game.map){
      game.map.save();
      game.map.setFill(color);
      game.map.fillRect(x,y,w,h);
      game.map.restore();
    }
  }

  /*public void draw(){
    synchronized(game.map){
      game.map.fillRect(x,y,w,h);
    }
  }*/
  public void clear(){
    synchronized(game.map){
      game.map.clearRect(x,y,w,h);
    }
  }

  public void moveTo(double X,double Y){
    x=X;y=Y;
  }

  public void onHit(){
    exit=true;
    interrupt();
    clear();
    //game.updateScore(10);
    //game.score.updateScore(10);
    synchronized(game.pendingScore){ // przejscie z callbakow na tresc zadania
      game.pendingScore.push(10);
      game.pendingScore.notifyAll();
    }
    game.removeOnSpatial(x,y);
  }
}