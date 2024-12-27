# Variáveis
SRC_DIR = src
OUT_DIR = out
CLASS_NAME = Main
PACKAGE_DIR = $(SRC_DIR)/logicircuit
JAVA_FILES = $(SRC_DIR)/$(CLASS_NAME).java $(PACKAGE_DIR)/*.java

# Objetivos
.PHONY: all clean run

# Compilar todos os ficheiros Java necessários
all: $(OUT_DIR)/$(CLASS_NAME).class

$(OUT_DIR)/$(CLASS_NAME).class: $(JAVA_FILES)
	mkdir -p $(OUT_DIR)
	javac -d $(OUT_DIR) -cp $(SRC_DIR) $(JAVA_FILES)

# Executar o programa
run: all
	java -cp $(OUT_DIR) $(CLASS_NAME)

# Limpar ficheiros compilados
clean:
	rm -rf $(OUT_DIR)


make full: clean all run