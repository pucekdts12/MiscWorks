RES:=f:\kompilacja\_res

DEPS:=SDL2

LIBS:=$(addprefix -l,$(DEPS)) -lm

LIB_PATH:=$(addprefix -L$(RES)\,$(DEPS))
LIB_PATH:=$(addsuffix \bin,$(LIB_PATH))

INCLUDES:=$(addprefix -I$(RES)\,$(DEPS))
INCLUDES:=$(addsuffix \include,$(INCLUDES))


LIB_EXT:=.dll
LIB_NAME:=helper$(LIB_EXT)


LIB_SRC:=helper.c
LIB_HEADERS:=helper.h

EXE_NAME:=game
EXE_EXT:=.exe
EXE_DEPS:=map.h style.h list.h handleArgs.h dt_useful.h


EXE_SRC:=game.c


MYCC_FLAGS:= -Wall -Wl,-rpath,. -ansi -m32 $(INCLUDES) $(LIB_PATH) -L. $(LIBS)



CC:=gcc

all:$(LIB_NAME) $(EXE_NAME)
	
	
clean:
	rm $(LIB_NAME)$(LIB_EXT)
	rm $(EXE_NAME)$(EXE_EXT)
	
test: CC:=@echo $(CC)
test: all	
	

	
$(EXE_NAME): $(EXE_SRC) $(LIB_HEADERS) $(EXE_DEPS)
	$(CC) $(MYCC_FLAGS) -lhelper $(EXE_SRC) -o $@$(EXE_EXT)
	
$(LIB_NAME): $(LIB_SRC) 
	$(CC) $(MYCC_FLAGS) -shared $(LIB_SRC) -o $@
	
	
	
	