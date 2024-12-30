import logicircuit.LCInputPin;

public interface IOComponent {
    public void setInput(boolean[] inputs);

    public boolean getOutput();

    public void draw();

    public boolean allowPin(LCInputPin pin);

    public void setUsedPin(LCInputPin pin);

    public String getName();
}
