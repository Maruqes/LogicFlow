# Variáveis
GRADLE := ./gradlew
JAR_FILE := build/libs/$(shell basename `pwd`)-0.0.1-SNAPSHOT.jar
RUN_PORT := 8080

# Tarefas
.PHONY: limpar compilar executar testar empacotar

# Limpar o projeto
limpar:
	@echo "🧹 A limpar o projeto..."
	$(GRADLE) clean

# Compilar o projeto
compilar:
	@echo "🔧 A compilar o projeto..."
	$(GRADLE) build

# Executar os testes
testar:
	@echo "🧪 A executar os testes..."
	$(GRADLE) test

# Empacotar o projeto (gera o JAR)
empacotar:
	@echo "📦 A empacotar o projeto..."
	$(GRADLE) bootJar

# Executar a aplicação diretamente
executar:
	@echo "🚀 A iniciar a aplicação na porta $(RUN_PORT)..."
	$(GRADLE) bootRun

# Executar o JAR
executar-jar: empacotar
	@echo "🚀 A iniciar a aplicação a partir do JAR na porta $(RUN_PORT)..."
	java -jar $(JAR_FILE)

# Tarefa padrão: limpar, compilar, testar e empacotar
tudo: limpar compilar testar empacotar
	@echo "✅ Tudo pronto!"
