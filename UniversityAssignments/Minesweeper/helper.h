#ifndef _DTS_H_
#define _DTS_H_
#define SDL_MAIN_HANDLED
#include<SDL2/SDL.h>
#include<GL/gl.h>

extern void DTS_drawRectA(SDL_Renderer*,int,int,int,int,int,int,int,int);
extern void DTS_drawRect(SDL_Renderer*,int,int,int,int,int,int,int);
extern void DTS_drawDigit(SDL_Renderer*,int,int,int,int,int,int,int,int);
extern void DTS_drawLine(SDL_Renderer*,int,int,int,int,double,int,int,int);




#endif /* _DTS_H_ */