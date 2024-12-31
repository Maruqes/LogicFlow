import java.util.ArrayList;
import java.util.HashMap;

public class Language extends ProcessCommands {
    private static HashMap<String, HandleTokensInterface> commands = new HashMap<String, HandleTokensInterface>();

    public Language(MainCircuit circuit) {
        super(circuit, commands);
        HandleTokensInterface randfunc = (tokens) -> test(tokens);
        commands.put("mds", randfunc);
    }

    // todo some programming language stuff like loops int at minimum
    public String test(ArrayList<String> tokens) {
        String err = "";
        System.out.println("Test");
        return err;
    }

}
