ifeq ($(OS),Windows_NT)
	JAVAPATH:=f:/_res_/JAVA/java_13/jdk-13+33
	JAVAFX:=f:/_res_/JAVA/java_13/javafx-sdk-13/lib
	LIBEXT:=.dll
	CFLAGS:=-std=c99 -shared
	INCLUDES:=-I$(JAVAPATH)/include -I$(JAVAPATH)/include/win32
	CC_PATH:=f:/Programs/mingw-w64/mingw32/bin
	CC_PATH:=f:/Programs/TDM-GCC-64/bin
	CC:=$(CC_PATH)/gcc
else
	JAVAPATH:=$(HOME)/Programs/java_13/jdk-13+33
	JAVAFX:=$(HOME)/Programs/java_13/javafx-sdk-13/lib
	LIBEXT:=.so
	CFLAGS:=-std=c99 -fPIC -shared
	INCLUDES:=-I$(JAVAPATH)/include -I$(JAVAPATH)/include/linux
endif


WITH_JAVAFX:=--module-path $(JAVAFX) --add-modules javafx.controls,javafx.fxml
EXTRA_LIBS:=$(WITH_JAVAFX)
MODULE?=newelements
ARGS?=interface

SRC:=$(wildcard dtdraw/*.java)
SRC:=$(subst .java,.class,$(SRC))

RES:=res/layout.fxml res/style.css


.PHONY:all run

all:$(SRC) DTDraw.jar

%.class:%.java
	$(JAVAPATH)/bin/javac $(EXTRA_LIBS) $^

DTDraw.jar:$(SRC) $(RES)
	$(JAVAPATH)/bin/jar -cvfe $@ dtdraw.Main $^

run:
	$(JAVAPATH)/bin/java $(WITH_JAVAFX) -jar DTDraw.jar
	


clean:
	