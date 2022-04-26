#include<stdio.h>
#define SDL_MAIN_HANDLED
#include<SDL2/SDL.h>
#include<GL/gl.h>

#include "helper.h"

extern void DTS_drawRectA(SDL_Renderer * render,int X,int Y,int W,int H,int R,int G,int B,int A){
  SDL_Rect rect;
  rect.w=W;
  rect.h=H;
  rect.x=X;
  rect.y=Y;
  SDL_SetRenderDrawColor(render,R,G,B,A);
  SDL_RenderFillRect(render,&rect);
}

extern void DTS_drawRect(SDL_Renderer * render,int X,int Y,int W,int H,int R,int G,int B){
  DTS_drawRectA(render,X,Y,W,H,R,G,B,255);
}

extern void DTS_drawLine(SDL_Renderer * render,int X1,int Y1,int X2,int Y2,double scale,int R,int G,int B){
  SDL_SetRenderDrawColor(render,R,G,B,255);
  SDL_RenderDrawLine(render,X1,X2,Y1,Y2);/* cos nie halo z kolorem */
}


extern void DTS_drawDigit(SDL_Renderer * renderer,int digit,int X , int Y, int W,int H,int R,int G,int B){
  if( digit<0 || digit >9 ){
    printf("[WARN]DTS_drawDigit incorrect digit[1]\n");
    return;
  }
  int digits[10][7]={ {1,1,1,1,1,1,0},{0,1,1,0,0,0,0},{1,1,0,1,1,0,1},{1,1,1,1,0,0,1},{0,1,1,0,0,1,1},{1,0,1,1,0,1,1},{1,0,1,1,1,1,1},{1,1,1,0,0,0,0},{1,1,1,1,1,1,1},{1,1,1,1,0,1,1} };
  
  int S=H*0.4;
  int stroke=H*0.1;
  /*int stroke=2;*/
  X+=(W-S)/2;
  Y+=(H-S*2)/2;
  if( digits[digit][0] )
    DTS_drawRect(renderer,0+X,0+Y,S,stroke,R,G,B);
  if( digits[digit][1] )
    DTS_drawRect(renderer,X+S-stroke,Y, stroke/*w*/, S/*h*/, R,G,B);
  if( digits[digit][2] )
    DTS_drawRect(renderer,X+S-stroke,Y+S, stroke/*w*/, S/*h*/, R,G,B);
  if( digits[digit][3] )
    DTS_drawRect(renderer,X,Y+S*2-stroke, S/*w*/, stroke/*h*/, R,G,B);
  if( digits[digit][4] )
    DTS_drawRect(renderer,X,Y+S, stroke/*w*/, S/*h*/, R,G,B);
  if( digits[digit][5] )
    DTS_drawRect(renderer,X,Y, stroke/*w*/, S/*h*/, R,G,B);
  if( digits[digit][6] )
    DTS_drawRect(renderer,X,Y+S-stroke/2, S/*w*/, stroke/*h*/, R,G,B);
  
}