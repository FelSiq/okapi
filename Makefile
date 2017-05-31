CC = javac
RUNPROG = java
DEFPATH = ./src
EXECNAME = MainInterface
DEFTPACK = OkapiPack
PLOTPATH = $(DEFPATH)/plots$(DEFTPACK)
GENLPATH = $(DEFPATH)/general$(DEFTPACK)
EXTRPATH = $(DEFPATH)/extras$(DEFTPACK)

all: plots extras general
	$(CC) -cp $(DEFPATH)/*.java
plots:
	$(CC) $(PLOTPATH)/*.java
extras:
	$(CC) $(EXTRPATH)/*.java
general:
	$(CC) $(GENLPATH)/*.java
run:
	$(RUNPROG) -cp $(DEFPATH) $(EXECNAME)