#ifndef _HANDLEARGS_H_
#define _HANDLEARGS_H_

typedef struct{
  int w;
  int h;
  int m;
  int help_run;
  int seed;
}ParsedParams;





ParsedParams handleArgs(int n,char** args){
  int i=1;
  ParsedParams tmp;
  tmp.w=10;
  tmp.h=10;
  tmp.m=10;
  tmp.help_run=0;
  tmp.seed=0;
  for( i=1;i<n;i++ ){
    if( strcmp(args[i],"-h")==0 ){
      printf("%s",
      "Minesweeper using SDL2 library\n"
      "Field color meaning:\n"
      "  grey - unrevealed\n"
      "  blue - marked\n"
      "  black - revealed empty\n"
      "  red - revealed mine\n"
      "Program arguments:\n"
      "  -h: this help screen\n"
      "  -c: show controls\n"
      "  -m=<AMOUNT>: sets amount of mines to <AMOUNT> [default: 10]\n"
      "  -w=<SIZE>: sets board width to <SIZE> [default: 10]\n"
      "  -h=<SIZE>: sets board height to <SIZE> [default: 10]\n"
      );
      tmp.help_run=1;
    }else if( strcmp(args[i],"-c")==0 ){
      printf(
      "Controls:\n"
      "  Left Mouse Button: Reveal field\n"
      "  Right Mouse Button: Mark field\n"
      "  F2 Key: Restarts game with current parameters\n"
      "  F3 Key: Restarts game with new parameters\n"
      "  R Key: Restarts game with default parameters\n"
      );
      tmp.help_run=1;
    }else if( sscanf(args[i],"-w=%d",&tmp.w)==1 ){
      /*printf("W:%d\n",tmp.w);*/
    }else if( sscanf(args[i],"-h=%d",&tmp.h)==1 ){
      /*printf("H:%d\n",tmp.h);*/
    }else if( sscanf(args[i],"-m=%d",&tmp.m)==1 ){
      /*printf("M:%d\n",tmp.m);*/
    }else if( sscanf(args[i],"-s=%d",&tmp.seed)==1 ){
    }else{
      printf("[WARN] Unknown parametr [%d]",i);
    }
  }
  return tmp;
}



#endif