import java.io.IOException;
import java.util.Arrays;

import javax.swing.Timer;

import logicircuit.LCComponent;
import logicircuit.LCDFrameCmd;
import logicircuit.LCDPanel;

public class ProgCircuito {
    public static LCDPanel drawPannel;

    public static int SCREEN_WIDTH = 900;
    public static int SCREEN_HEIGHT = 700;

    public static void DRAW_ALL_STUFF(MainCircuit circuit) {
        Timer timer = new Timer(10, e -> {
            drawPannel.clear();
            circuit.drawCircuit();
            ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    public static void restartProgram(String... newArgs) throws IOException {
        String javaBin = System.getProperty("java.home") + "/bin/java";
        String classPath = System.getProperty("java.class.path");
        String className = ProgCircuito.class.getName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classPath, className);

        builder.command().addAll(Arrays.asList(newArgs));

        builder.start();

        System.exit(0);
    }

    public static void main(String[] args) {
        System.out.println("You are running the normal version of LogicFlow");

        MainCircuit circuit = new MainCircuit();

        if (args.length > 0) {
            SCREEN_WIDTH = Integer.parseInt(args[0]);
            SCREEN_HEIGHT = Integer.parseInt(args[1]);
        }

        ProcessCommands parser = new ProcessCommands(circuit);
        LCDFrameCmd frame = new LCDFrameCmd(parser, "LogicFlow", SCREEN_WIDTH, 700);
        drawPannel = frame.drawPanel();
        drawPannel.clear();
    }
}
