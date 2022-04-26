/*#ifndef _LIST_H_
#define _LIST_H_*/

#include<string.h>
#include<stdlib.h>


#ifndef LIST_NAME
#define LIST_NAME(X) X
#endif

#ifndef LISTPOS_NAME
#define LISTPOS_NAME(X) X
#endif

#ifndef LIST_TYPE
#define LIST_TYPE char*
#endif



typedef struct{
  char* key;
  LIST_TYPE val;
}LISTPOS_NAME(ListPos);

typedef struct{
  LISTPOS_NAME(ListPos) * data;
  int size;
}LIST_NAME(List);

LIST_NAME(List) * LIST_NAME(List_Create)(){
  LIST_NAME(List) * t=malloc(sizeof(LIST_NAME(List)));
  t->data=NULL;
  t->size=0;
  if( t==NULL ) printf("[ERROR] Cannot allocate memory for List");
  return t;
}

int LIST_NAME(List_KeyExists)(LIST_NAME(List) * list,char * key){
  int i=0;
  for( i=0;i<list->size;i++ ){
    if( strcmp(list->data[i].key,key)==0 ) return i;
  }
  return -1;
}

int LIST_NAME(List_Resize)(LIST_NAME(List) * list,int size){
  if( list->data==NULL ){
    list->data=malloc(sizeof(LISTPOS_NAME(ListPos))*size);
    if( list->data==NULL ){
      printf("[ERROR] Cannot resize List[1]\n");
      return 0;
    }
    list->size=size;
    return 1;
  }else if(size==0){
    free(list->data);
    list->data=NULL;
    list->size=size;
  }else{
    LISTPOS_NAME(ListPos) * new_data = realloc(list->data,sizeof(LISTPOS_NAME(ListPos))*size);
    if( new_data==NULL ){
      printf("[ERROR] Cannot resize List[2]\n");
      return 0;
    }
    list->data=new_data;
    list->size=size;
  }
  return 1;
}

int LIST_NAME(List_Set)(LIST_NAME(List) * list,char * key,LIST_TYPE val){
  int ind=LIST_NAME(List_KeyExists)(list,key);
  if( ind==-1 ){
    if( LIST_NAME(List_Resize)(list,list->size+1) ){
      list->data[list->size-1]=(LISTPOS_NAME(ListPos)){key,val};
      return 1;
    }
  }else{
    list->data[ind]=(LISTPOS_NAME(ListPos)){key,val};
    return 1;
  }
  return 0;
}

LIST_TYPE LIST_NAME(List_Get)(LIST_NAME(List) * list,char * key){
  int ind=LIST_NAME(List_KeyExists)(list,key);
  if( ind==-1 ) return NULL;
  return list->data[ind].val;
}

int LIST_NAME(List_Remove)(LIST_NAME(List) * self,char * key){
  int ind=LIST_NAME(List_KeyExists)(self,key);
  if( ind==-1 ) return 1;
  self->data[ind]=self->data[ self->size-1 ];
  return LIST_NAME(List_Resize)(self,self->size-1);
}

#undef LIST_NAME
#undef LISTPOS_NAME
#undef LIST_TYPE

/*#endif*/