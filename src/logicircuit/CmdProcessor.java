package logicircuit;
/**
 * Interface to be implemented by any class whose instances are intended to process a command in LCDFrameCmd
 * The class must define a method called process with a string as argument
 * @author pmc
 * @version 1.0
 */
public interface CmdProcessor {
    /**
     * Process the command
     *
     * @param text Command
     * @return Error message (or null)
     */
    String process(String text);
}
