#ifndef _MINES_H_
#define _MINES_H_

#include<stdio.h>
#include<stdlib.h>
#include "style.h"

typedef struct{
  int revealed;
  char type;
  int checked;
} Field;

typedef struct{
  Field* data;
  int W;
  int H;
  int size;
  int mines;
  int revealed;
}Fields;

Fields* Fields_Create(int W,int H){
  Fields* self=malloc(sizeof(Fields));
  self->size=0;
  if( self==NULL ){
    printf("[Error] Cannot allocate memory for Fields[1]\n");
    return NULL;
  }  
  self->data=malloc( sizeof(Field)*W*H );
  if( self->data==NULL ){
    printf("[Error] Cannot allocate memory for Fields[2]\n");
    free(self);
    return NULL;
  }
  int i=0;
  for( i=0;i<W*H;i++ ){
    self->data[i]=(Field){0,'0',0};
  }
  self->W=W;
  self->H=H;
  self->size=W*H;
  self->mines=0;
  self->revealed=0;
  return self;
}
void Fields_Delete(Fields * self){
  if( self==NULL ) return;
  if( self->data!=NULL )
    free(self->data);
  self->data=NULL;
  free(self);
  self=NULL;
}
Field Fields_Get(Fields* self, int x, int y){
  return self->data[y*self->W+x];
}

void Fields_Set(Fields* self,int x,int y,Field val){
  self->data[y*self->W+x]=val;
}

int Fields_isIn(Fields* self,int x,int y){
  return ( x>-1 && x<self->W && y>-1 && y<self->H );
}

int Fields_isMine(Fields* map,int x,int y){
  if( !Fields_isIn(map,x,y) ) return 0;
  return Fields_Get(map,x,y).type=='X';
}




void Field_Reveal(Fields* map,int x,int y,int zerosOnly){
  if( !Fields_isIn(map,x,y) ) return;
  Field tmp=Fields_Get(map,x,y);
  if( tmp.revealed || tmp.type=='X' )
    return;
  
  if( zerosOnly && tmp.type!='0' ) return;
  
  tmp.revealed=1;
  Fields_Set(map,x,y,tmp);
  map->revealed++;
  
  zerosOnly=0;
  
  if( tmp.type!='0' )
    zerosOnly=1;
  
  Field_Reveal(map,x+1,y,zerosOnly);
  Field_Reveal(map,x-1,y,zerosOnly);
  Field_Reveal(map,x,y-1,zerosOnly);
  Field_Reveal(map,x,y+1,zerosOnly);
  
  
  
  if( !zerosOnly ){
    Field_Reveal(map,x+1,y+1,zerosOnly);
    Field_Reveal(map,x+1,y-1,zerosOnly);
    Field_Reveal(map,x-1,y+1,zerosOnly);
    Field_Reveal(map,x-1,y-1,zerosOnly);
  }
  
  
}

int Mines_Check(Fields* map, int x,int y){
  int howMany=0;
  howMany+=Fields_isMine(map,x-1,y-1);
  howMany+=Fields_isMine(map,x,y-1);
  howMany+=Fields_isMine(map,x+1,y-1);
  howMany+=Fields_isMine(map,x-1,y);
  howMany+=Fields_isMine(map,x+1,y);
  howMany+=Fields_isMine(map,x-1,y+1);
  howMany+=Fields_isMine(map,x,y+1);
  howMany+=Fields_isMine(map,x+1,y+1);
  return howMany;
}

int Mines_Generate(Fields* map,int mines){
  if( mines>=map->size ){
    printf("[ERROR]Couldn't generate map[1]\n");
    return -1;
  }
  int tmp=0;
  int tries=1;
  map->mines=mines;
  while( mines>0 ){
    tmp=rand()%map->size;
    if( map->data[tmp].type!='X' ){
      map->data[tmp]=(Field){0,'X',0};
      mines--;
    }else{
      tries++;
      if( tries>100 ){
        printf("[ERROR]Couldn't generate map[2]\n");
        return -1;
      }
    }
  }
  int i,j;
  for( i=0;i<map->W;i++ ){
    for( j=0;j<map->H;j++ ){
      if( Fields_isMine(map,i,j) ) continue;
      Fields_Set( map,i,j,(Field){0,'0'+Mines_Check(map,i,j),0} );
    }
  }
  
  return 1;
}




Style * Field_CreateDefaultStyle(){
  Style * style=Style_Create();
  Style_Set(style,"width","10%");
  Style_Set(style,"height","10%");
  Style_SetCalced(style,"border",1);
  Style_Set(style,"color","120,120,120");
  return style;
}



#endif