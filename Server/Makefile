# Variáveis
GRADLE := ./gradlew
JAR_FILE := build/libs/$(shell basename `pwd`)-0.0.1-SNAPSHOT.jar
RUN_PORT := 8080

# Tarefas
.PHONY: clean build run test package

# Limpar o projeto
clean:
	@echo "🧹 Limpando o projeto..."
	$(GRADLE) clean

# Compilar o projeto
build:
	@echo "🔧 Compilando o projeto..."
	$(GRADLE) build

# Executar os testes
test:
	@echo "🧪 Executando os testes..."
	$(GRADLE) test

# Empacotar o projeto (gera o JAR)
package:
	@echo "📦 Empacotando o projeto..."
	$(GRADLE) bootJar

# Executar a aplicação diretamente
run:
	@echo "🚀 Iniciando a aplicação na porta $(RUN_PORT)..."
	$(GRADLE) bootRun

# Executar o JAR
run-jar: package
	@echo "🚀 Iniciando a aplicação a partir do JAR na porta $(RUN_PORT)..."
	java -jar $(JAR_FILE)

# Tarefa padrão: limpar, compilar, testar e empacotar
all: clean build test package
	@echo "✅ Tudo pronto!"
