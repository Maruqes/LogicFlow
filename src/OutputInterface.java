import logicircuit.LCInputPin;

public interface OutputInterface {
    public void setValue(int state);

    public int getValue();

    public void draw();

    public boolean allowPin(LCInputPin pin);

    public void setNotUsedPin(LCInputPin pin);

    public void setUsedPin(LCInputPin pin);

    public String getName();

    public String Strigonize();

    public void setPosition(int x, int y);

    public void PrintAllInfo();
}
