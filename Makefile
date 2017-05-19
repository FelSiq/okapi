CC = javac
RUNPROG = java
DEFPATH = ./src
EXECNAME = MainInterface
PLOTPATH = $(DEFPATH)/plots
GENLPATH = $(DEFPATH)/general
EXTRPATH = $(DEFPATH)/extras

all: plots extras general
	$(CC) $(DEFPATH)/*.java
plots:
	$(CC) $(PLOTPATH)/*.java
extras:
	$(CC) $(EXTRPATH)/*.java
general:
	$(CC) $(GENLPATH)/*.java
run:
	$(RUNPROG) -cp $(DEFPATH) $(EXECNAME)