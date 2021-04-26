
JC=javac -d bin

BIN=./bin
MAIN=Driver

JAVA_FILES=Driver.java DrawPanel.java

run: $(BIN)/Driver.class
	java -cp $(BIN) $(MAIN) $(FILE)

$(BIN)/Driver.class: $(JAVA_FILES)
	$(JC) Driver.java DrawPanel.java -d $(BIN)

clean:
	rm -f bin/*.class
