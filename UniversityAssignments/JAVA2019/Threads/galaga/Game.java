package galaga;

import dts.MyPane;

import java.util.Vector;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.canvas.*;
import java.util.Stack;

public class Game extends Thread{
  public GraphicsContext map;
  public Enemy[][] spatial;
  public double W,H;
  static int COLS=4,ROWS=8;
  public static Random random=new Random();
  public UpdateScore score;
  public Player pl;
  public boolean isPaused=false;
  public Stack<Integer> pendingScore=new Stack<Integer>();
  public static boolean running=true;
  //int score=0;
  Game(MyPane p,double w,double h,GraphicsContext m){
    spatial = new Enemy[COLS][ROWS];
    map=m;
    W=w;H=h;
    pl=new Player();
    pl.moveTo((W-pl.w)/2,H-pl.h);
    pl.setGame(this);
    pl.start();
    for( int y=0;y<3;y++ ){
      for( int x=0;x<COLS;x++ ){
        addEnemy(x,y);
      }
    }
  }
  public void addEnemy(int x,int y){
    if( spatial[x][y]!=null ) return;
    //Enemy e=new Enemy(x*(W/COLS),y*(H/ROWS)+(H/ROWS-Enemy.h)/2);
    Enemy e=new Enemy();
    e.moveTo(x*(W/COLS),y*(H/ROWS)+(H/ROWS-e.h)/2);
    spatial[x][y]=e;
    e.setGame(this);
    e.start();
  }

  public Enemy getSpatial(int x,int y){
    if( x<0 || x>COLS || y<0 || y>COLS) return null;
    return spatial[x][y];
  }
  public Enemy getSpatial(double X,double Y){
    int x=(int)(X/(W/COLS));
    int y=(int)(Y/(H/ROWS));
    return getSpatial(x,y);
  }
  public boolean removeOnSpatial(int x,int y){
    if( x<0 || x>COLS || y<0 || y>COLS) return false;
    spatial[x][y]=null; 
    return true;
  }
  public boolean removeOnSpatial(double X,double Y){
    return removeOnSpatial((int)(X/(W/COLS)),(int)(Y/(H/ROWS)));
  }
  public void pause(){
    isPaused=true;
  }
  public void unpause(){
    isPaused=false;
  }
  
  public void run(){
    try{
      synchronized(pendingScore){
        while(running){
          if( pendingScore.empty() ){
            pendingScore.wait();
          }
          score.updateScore(pendingScore.pop());
        }
      }
    }catch( InterruptedException e ){}
  }

}