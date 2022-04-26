#define _GNU_SOURCE 1
#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<math.h>

#define SDL_MAIN_HANDLED
#include<SDL2/SDL.h>
#include<GL/gl.h>

#include "helper.h"
#include "dt_useful.h"
#include "map.h"
#include "handleArgs.h"

void draw_Fields(Fields*);
void gameEnd(int);
int RestartGame();

SDL_Window * window=NULL;
SDL_Renderer * window_renderer=NULL;
Style * window_style=NULL;
int Width=600;
int Height=600;

Style * mine_style=NULL;
Style * field_style=NULL;
Style * flag_style=NULL;

Style * map_style=NULL;
Fields* main_map=NULL;

ParsedParams MainParams;
int _Seed_=0;

int _DC[9][3]={{255,255,255},{0,100,255},{0,190,0},{200,0,0},{200,0,200},{128,0,0},{64,224,208},{0,240,0},{150,150,150}};
int _GameEnded=0;

void render(){
  SDL_RenderClear(window_renderer);
  DTS_drawRect(window_renderer,0,0 ,Width,Height,54,54,54);
  draw_Fields(main_map);
  /*DTS_drawLine(window_renderer,0,0,Width,Height,1,255,0,0);*/ /* test linii */
  SDL_RenderPresent(window_renderer);
}

void draw_Fields(Fields* map){
  int i,j;
  int r=255,g=0,b=0;
  
  int x=0;
  int y=0;
  
  int fW=dt_round( *Style_GetCalced(field_style,"width") );
  int fH=dt_round( *Style_GetCalced(field_style,"height") );
  int fB=dt_round( *Style_GetCalced(field_style,"border") );
  
  char * mine_color=Style_Get(mine_style,"color");
  char * field_color=Style_Get(field_style,"color");
  char * checked_color=Style_Get(flag_style,"color");
  
  Field field;
  int digit=0;
  for( i=0;i<map->W;i++ ){
    y+=fB;
    x=0;
    for( j=0;j<map->H;j++ ){
      x+=fB;
      field=Fields_Get(map,i,j);
      if( field.revealed ){
        r=0;
        g=0;
        b=0;
        DTS_drawRect(window_renderer, j*fW+fB,i*fH+fB, fW-fB,fH-fB ,r,g,b);
        digit=field.type-'0';
        if( digit!=0 )
          DTS_drawDigit(window_renderer,digit ,j*fW+fB,i*fH+fB, fW-fB,fH-fB,_DC[digit][0],_DC[digit][1],_DC[digit][2]);
        continue;
      }else if( field.checked==1 ){
        sscanf(checked_color,"%d,%d,%d",&r,&g,&b );
      }else if( field.type=='X' ){
        if( _GameEnded )
          sscanf(mine_color,"%d,%d,%d",&r,&g,&b );
        else
          sscanf(field_color,"%d,%d,%d",&r,&g,&b );
      }else{
        sscanf(field_color,"%d,%d,%d",&r,&g,&b );
      }
      DTS_drawRect(window_renderer, j*fW+fB,i*fH+fB, fW-fB,fH-fB ,r,g,b);
    }
    y+=fW;
  }
}


void click_handle( Fields* map,char b,int mX , int mY ){
  if( _GameEnded ) return;
  int fW=dt_round( *Style_GetCalced(field_style,"width") );
  int fH=dt_round( *Style_GetCalced(field_style,"height") );
  int fB=dt_round( *Style_GetCalced(field_style,"border") );
  int i,j;
  Field field;
  for( i=0;i<map->W;i++ ){
    for( j=0;j<map->H;j++ ){
      if( mX>j*fW-fB && mX<j*fW+fW && mY>i*fH && mY<i*fH+fH){
        field=Fields_Get(map,i,j);
        if( b=='P' ){
          field.checked=!field.checked;
          Fields_Set(map,i,j,field);
        }else if( b=='L' ){
          if( !field.checked && field.type=='X' ){
            gameEnd(0);
          }else{
            Field_Reveal(map,i,j,0);
            if( (map->size-map->revealed)==map->mines ){
              gameEnd(1);
            }
          }
        }
      }
    }
  }
}

void gameEnd(int victory){
  if(!victory)
    printf("Game Over\n");
  else
    printf("Victory\n");
  printf("Press F2 to start new game\n");
  _GameEnded=1;
}

void StyleInit(){
  window_style=Style_Create();
  Style_SetCalced(window_style,"width",Width);
  Style_SetCalced(window_style,"height",Height);
  
  field_style=Field_CreateDefaultStyle();
  Style_Set(field_style,"height",dt_sprintf("%d%%",100/MainParams.h));
  Style_Set(field_style,"width",dt_sprintf("%d%%",100/MainParams.w));
  field_style->parent=window_style;
  
  
  flag_style=Style_Create();
  Style_Set(flag_style,"color","0,120,255");
  
  mine_style=Style_Create();
  Style_Set(mine_style,"color","180,0,0");
}
void StyleUpdate(int changedDim){
  Style_SetCalced(window_style,"width",Width);
  Style_SetCalced(window_style,"height",Height);
  Style_MarkRecalc(mine_style);
  Style_MarkRecalc(field_style);
  if( changedDim ){
    Style_Set(field_style,"height",dt_sprintf("%d%%",100/MainParams.h));
    Style_Set(field_style,"width",dt_sprintf("%d%%",100/MainParams.w));
  }
}

int RestartGame(){
  if( MainParams.seed==0 ){
    _Seed_=time(NULL);
    srand(_Seed_);
  }else{
    srand(MainParams.seed);
    _Seed_=MainParams.seed;
  }
  printf("Seed is %d\n",_Seed_);
  
  Fields_Delete(main_map);
  main_map=Fields_Create(MainParams.h,MainParams.w);
  if( Mines_Generate(main_map,MainParams.m)==-1 ){
    return 1;
  }
  printf("Mines on the field: %d\n",MainParams.m);
  _GameEnded=0;
  return 0;
}

int main(int argv,char**args){
  
  int quit=0;
  MainParams=handleArgs(argv,args);
  
  if( MainParams.help_run ) return 0;
  
  if( SDL_Init(SDL_INIT_EVERYTHING)!=0 ){
    printf("[ERROR]Couldn't initlialize SDL: %s\n",SDL_GetError());
    return -1;
  }
  window = SDL_CreateWindow("Saper",SDL_WINDOWPOS_CENTERED,SDL_WINDOWPOS_CENTERED,Width,Height,SDL_WINDOW_RESIZABLE);
  if( window==NULL ){
    printf("[ERROR]Can't create SDL window: %s\n",SDL_GetError());
    SDL_Quit();
    return 1;
  }
  window_renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
  
  if ( RestartGame() ){
    printf("[Error] Number of mines can't be greater than number of fields.\n");
    quit=1;
  }
  
  
  StyleInit();
  
  
  
  SDL_Event e;
  
  int getInput=0;
  while(!quit){
    while(SDL_PollEvent(&e)!=0){
      switch(e.type){
        case SDL_QUIT:
          quit=1;
          break;
        case SDL_WINDOWEVENT:
          if(e.window.event==SDL_WINDOWEVENT_RESIZED) {
            SDL_GetWindowSize(window,&Width,&Height);
            StyleUpdate(0);
            render();
          }
          break;
        case SDL_MOUSEBUTTONUP:
          if( e.button.button==SDL_BUTTON_LEFT ){
            click_handle(main_map,'L',e.button.x,e.button.y);
          }else if( e.button.button==SDL_BUTTON_RIGHT ){
            click_handle(main_map,'P',e.button.x,e.button.y);
          }
          break;
        case SDL_KEYUP:
          switch( e.key.keysym.sym ){
            case SDLK_F2:
              printf("Restarting...\n");
              RestartGame();
            break;
            case SDLK_F3:
              getInput=1;
            break;
            case SDLK_r:
              MainParams.w=10;
              MainParams.h=10;
              MainParams.m=10;
              StyleUpdate(1);
              RestartGame();
            break;
          }
      }
    }
    render();
    if( getInput ){
      printf("Type new game parameters[WxH:M]\n");
      /*scanf("%dx%d:%d",&MainParams.w,&MainParams.h,&MainParams.m);*/
      if (scanf("%dx%d:%d",&MainParams.w,&MainParams.h,&MainParams.m) != 3) {
        printf("[WARN] Unknown input format, resetting to defaults.\n");
        fflush(stdin);
        MainParams.w=10;
        MainParams.h=10;
        MainParams.m=10;
      }
      StyleUpdate(1);
      RestartGame();
      getInput=0;
    }
    
  }
  
  
  SDL_DestroyWindow(window);
  SDL_Quit();
  
  
  return 0;
}

