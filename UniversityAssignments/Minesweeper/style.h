#ifndef _STYLE_H_
#define _STYLE_H_
#include<string.h>
#include<stdio.h>

/*
#define LIST_NAME(X) X
#define LIST_TYPE char*
#define LISTPOS_NAME(X) X
*/
#include "list.h"


#define LIST_NAME(X) D##X
#define LIST_TYPE double*
#define LISTPOS_NAME(X) D##X

#include "list.h"


typedef struct {
  List * props;
  DList * calced;
  void * parent;
} Style;

Style * Style_Create(){
  Style  * self=malloc(sizeof(Style));
  self->props=List_Create();
  self->calced=DList_Create();
  self->parent=NULL;
  return self;
}


void Style_MarkRecalc(Style * self){
  int i=0;
  double * val = malloc(sizeof(double));
  char unit[100];
  for( i=0;i<self->props->size;i++ ){
    sscanf(self->props->data[i].val,"%lf%s",val,unit);
    if( strcmp(unit,"px") ){
      DList_Remove(self->calced,self->props->data[i].key);
    }
  }
}

char * Style_Get(Style * self,char * name){
  return List_Get(self->props,name);
}

int Style_Set(Style * self,char * name,char * val){
  return List_Set(self->props,name,val);
}

int Style_SetCalced(Style * self,char * name,double val){
  double * p_val=malloc(sizeof(double));
  *p_val=val;
  return DList_Set(self->calced,name,p_val);
}

int Style_SetCalcedP(Style * self,char * name,double* val){
  return DList_Set(self->calced,name,val);
}

char* Style_GetProperRelative(char* unit,char * prop){
  if( strcmp(unit,"%")==0 ){
    if( strcmp(prop,"x")==0) return "width";
    if( strcmp(prop,"y")==0) return "height";
  }
  return prop;
}

double* Style_GetCalced(Style * self,char * name){
  double* ret_val = DList_Get(self->calced,name);
  if( ret_val!=NULL ) return ret_val;
  ret_val = malloc(sizeof(double));
  *ret_val=0;
  char * val = Style_Get(self,name);
  if( val==NULL ){
    printf("[WARN] %s is null\n",name);
    return ret_val;
  }
  Style * parent = (Style*)self->parent;
  double * value=malloc(sizeof(double));
  char unit[100];
  sscanf(val,"%lf%s",value,unit);
  if( strcmp(unit,"")==0 ) return ret_val;
  if( strcmp(unit,"px")==0 ){
    DList_Set(self->calced,name,value);
    return value;
  }else if ( strcmp(unit,"%")==0 ){
    if( parent==NULL ) return 0;
    double * context_style = Style_GetCalced(parent,Style_GetProperRelative(unit,name));
    *ret_val=((*value)/100)*(*context_style);
    DList_Set(self->calced,name,ret_val);
    return ret_val;
  }
  return ret_val;
}


#endif