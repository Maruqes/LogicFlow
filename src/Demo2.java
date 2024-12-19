import java.util.*;

import circuito2.Circuito2;
import circuito2.Componente2;
import circuito2.Conexao2;

import logicircuit.*;
import static logicircuit.LCComponent.*;
import static logicircuit.LCInputPin.*;

public class Demo2 {
    // Valid commands
    private static final Set<String> validCmds = new HashSet<>(Arrays.asList("ADD", "WIRE", "TURN", "EXIT"));
    // Circuito
    private Circuito2 circuito;

    /**
     * Método main
     */
    public static void main(String[] args) {
        new Demo2();
    }

    /**
     * Construtor da classe Demo2 com instruções de inicialização
     */
    private Demo2() {
        Demo2.ProcessCommand processCommand = new Demo2.ProcessCommand();
        LCDFrameCmd frame = new LCDFrameCmd(processCommand,"Demo de Logic Circuit", 900, 700);
        circuito = new Circuito2(frame.drawPanel());
    }

    /**
     * Classe que implementa a Interface LCDFrameCmd.CmdProcessor
     *  que processa os commandos e deve devolver uma mensagem de erro se necessário
     */
    class ProcessCommand implements CmdProcessor {
        @Override
        public String process(String text) {
            // Introduzido para facilitar a introdução de vários comandos
            String[] cmds = text.split(";");
            for(String cmd: cmds) {
                String msgErro = processCommand(cmd);
                if (msgErro != null) return msgErro;
            }
            return null;
        }
    }

    /**
     * Processa a string com o comando
     * @param text Comando
     * @return Mensagem de erro ou null
     */
    private String processCommand(String text) {
        String errorMsg = null;
        // Split string based on one or more spaces
        String[] cmdTokens = text.trim().split("\\s+");


        if (validCmds.contains(cmdTokens[0].toUpperCase())) {
            switch (cmdTokens[0].toUpperCase()) {
                case "ADD":
                    errorMsg = processAdd(cmdTokens);
                    break;
                case "WIRE":
                    errorMsg = processWire(cmdTokens);
                    break;
                case "TURN":
                    errorMsg = processTurn(cmdTokens);
                    break;
            }
        } else {
            errorMsg = "Comando inválido. Comandos válidos: " + validCmds.toString();
        }
        return errorMsg;
    }

    // NOTAs:
    // Nos exemplos a seguir é utilizada uma estratégia baseada em arrays de strings
    //  para identificar os elementos do comando
    // Uma possível melhoria seria a utilização do método String.matches()
    //  e das classes java.util.regex.Pattern e java.util.regex.Matcher
    //
    // Codigo exemplo sem nenhum tipo de validacao de dados e incompleto
    // Exemplo sem recurso a classes com herança

    /**
     * Processar commando ADD
     *   com formato ADD <id>:<tipo_porta>@<coord_X>,<coord_Y> [<legenda>]
     * @param tokens Tokens do comando resultantes de separação por espaço
     * @return Mensagem de erro ou null
     */
    private String processAdd(String[] tokens) {
        String msgErro = null;
        LCComponent component = null;

        String[] comp = tokens[1].split("[:@,]");

        switch (comp[1].toUpperCase()) {
            case "AND": component = AND; break;
            case "OR": component = OR; break;
            case "LED": component = LED; break;
            case "SWITCH": component = SWITCH; break;
        }
        if (component != null) {
            String legend = null;
            if ((tokens.length > 2) && !tokens[2].isEmpty())
                legend = String.join(" ",Arrays.copyOfRange(tokens, 2, tokens.length));
            circuito.addComponente(new Componente2(comp[0], component,
                    Integer.parseInt(comp[2]), Integer.parseInt(comp[3]), false, legend, 0));
            circuito.draw();
        }
        else msgErro = "Componente inválido";
        return msgErro;
    }

    /**
     * Processar commando WIRE
     *   com formato WIRE <id_elemento1>  <id_elemento2> <input_pin_elemento2>
     * @param tokens Tokens do comando resultantes de separação por espaço
     * @return Mensagem de erro ou null
     */
    private String processWire(String[] tokens) {
        LCInputPin pin = null;
        switch (tokens[3].toUpperCase()) {
            case "PIN_A": pin = PIN_A; break;
            case "PIN_B": pin = PIN_B; break;
            case "PIN_C": pin = PIN_C; break;
        }
        Componente2 in = circuito.getComponente(tokens[1]);
        Componente2 out = circuito.getComponente(tokens[2]);
        if (in!=null && out!=null && pin!=null) {
            circuito.addConexao(new Conexao2(in, out, pin, false));
            circuito.draw();
        }
        else return "Erro em Wire";
        return null;
    }

    /**
     * Processar commando TURN
     *   com formato TURN <state> <id_elemento_entrada>
     * @param tokens Tokens do comando resultantes de separação por espaço
     * @return Mensagem de erro ou null
     */
    private String processTurn(String[] tokens) {
        boolean state = tokens[1].equalsIgnoreCase("ON");
        Componente2 comp = circuito.getComponente(tokens[2]);
        if (comp!=null) {
            comp.setState(state);
            circuito.draw();
        }
        else return "Erro em TURN";
        return null;
    }
}
