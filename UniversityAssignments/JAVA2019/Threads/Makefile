JAVAPATH:=f:/_res_/JAVA/java_13/jdk-13+33
JAVAFX:=f:/_res_/JAVA/java_13/javafx-sdk-13/lib
WITH_JAVAFX:=--module-path $(JAVAFX) --add-modules javafx.controls,javafx.fxml
EXTRA_LIBS:=
EXTRA_LIBS:=
MODULE?=
MODULE_ARGS?=


#SRC:=Main.class Controller.class
#SRC:=$(addprefix galaga/,$(SRC))
SRC:=$(wildcard galaga/*.java)
SRC:=$(subst .java,.class,$(SRC))
RES:=galaga.fxml galaga.css
RES:=$(addprefix res/,$(RES))


DTS_SRC:=$(wildcard dts/*.java)
DTS_SRC:=$(subst .java,.class,$(DTS_SRC))

.PHONY:all run

all:Game.jar
	


Game.jar:$(SRC) $(RES) $(DTS_SRC)
	$(JAVAPATH)/bin/jar -cvfe $@ galaga.Main $^

%.class:%.java
	$(JAVAPATH)/bin/javac $(EXTRA_LIBS) $(WITH_JAVAFX) $^


run:
	$(JAVAPATH)/bin/java $(WITH_JAVAFX) $(EXTRA_LIBS) -jar Game.jar


clean:
	rm ./arkanoid/*.class