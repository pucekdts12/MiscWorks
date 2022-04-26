package galaga;

import javafx.scene.paint.Paint;



public class Player extends Enemy{
  //protected double w,h,x,y;
  //protected Paint color=Paint.valueOf("00FF00");
  Player(){    
    this(-100,-100);
  }
  Player(double X,double Y){
    super(X,Y);
    w=40;h=80;
    color=Paint.valueOf("00FF00");
  }
  public void run(){
    try{
      while(running && !exit){
        update(0,0); //only draw
        this.sleep(10);
      }

    }catch( InterruptedException e ){}
  }
  /*public void draw(){
    synchronized(game.map){
      game.map.save();
      // custom draw here
      game.map.restore();
    }
  }*/
  public void shoot(){
    //System.out.println("SHOOT");
    Bullet n = Bullet.spawn(x+(w/2),y+2);
    if( n!=null ) n.setGame(game);
  }
  public void moveLeft(){
    clear();
    x-=10;
    x=Math.max(x,0); // pilnowanie krawedzi
  }
  public void moveRight(){
    clear();
    x+=10;
    x=Math.min(x,game.W);
  }

}