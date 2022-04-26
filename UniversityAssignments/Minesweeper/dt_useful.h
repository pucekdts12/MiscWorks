#ifndef _DTUSEFUL_H_
#define _DTUSEFUL_H_

#include<stdio.h>
#include<stdarg.h>


int dt_round(double var){
  int rounded=var;
  if( var-rounded>0.48 ) rounded++;
  return rounded;
}

char * dt_sprintf(char* format,...){
  va_list argList;
  va_start(argList, format);
  char* ret;
  /*size_t needed_size=vsnprintf(NULL,0,format,argList);
  char * ret = malloc(needed_size*sizeof(char));
  vsprintf(ret,format,argList);*/
  vasprintf(&ret,format,argList);
  
  va_end(argList);
  return ret;
}


#endif