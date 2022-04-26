JAVAPATH:=f:/_res_/JAVA/java_13/jdk-13+33
JAVAFX:=f:/_res_/JAVA/java_13/javafx-sdk-13/lib
WITH_JAVAFX:=--module-path $(JAVAFX) --add-modules javafx.controls,javafx.fxml
EXTRA_LIBS:=
EXTRA_LIBS:=
MODULE?=mandelbrot.CanvasFX
MODULE_ARGS?=


#SRC:=Mandelbrot.class MandelbrotController.class Complex.class MandelFractal.class MyCanvas.class
#SRC:=Mandelbrot.class MandelbrotController.class Complex.class MandelFractal.class ResizableCanvas.class MyPane.class
#SRC:=$(addprefix mandelbrot/,$(SRC))
SRC:=$(wildcard mandelbrot/*.java)
SRC:=$(subst .java,.class,$(SRC))

RES:=mandelbrot.fxml mandelbrot.css
RES:=$(addprefix res/,$(RES))


.PHONY:all run

all:Mandelbrot.jar

%.class:%.java
	$(JAVAPATH)/bin/javac $(EXTRA_LIBS) $(WITH_JAVAFX) $^

#CanvasFX.jar:mandelbrot/CanvasFX.class res/canvasfx.fxml mandelbrot/CanvasController.class res/canvasfx.css
#	$(JAVAPATH)/bin/jar -cvfe $@ mandelbrot.CanvasFX $^

Mandelbrot.jar:$(SRC) $(RES)
	$(JAVAPATH)/bin/jar -cvfe $@ mandelbrot.Mandelbrot $^

run:
	$(JAVAPATH)/bin/java $(WITH_JAVAFX) $(EXTRA_LIBS) -jar Mandelbrot.jar


clean:
	rm ./mandelbrot/*.class