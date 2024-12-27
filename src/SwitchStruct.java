import logicircuit.LCComponent;

public class SwitchStruct implements ComponentInterface {
    private boolean[] inputs;
    private int setX;
    private int setY;
    private String legend;
    private String name;

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 1) {
            throw new IllegalArgumentException("SWITCH gate must have 1 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("SWITCH gate inputs are not set");
        }
        return inputs[0] && inputs[1];
    }

    @Override
    public void setPosition(int x, int y){
        setX = x;
        setY = y;
    }

    @Override
    public void setLegend(String legend){
        this.legend = legend;
    }

    @Override
    public void draw(){
        if (inputs == null) {
            throw new IllegalStateException("SWITCH gate inputs are not set");
        }
        Main.drawPannel.drawComponent(LCComponent.SWITCH, setX, setY, inputs[0], legend);
    }

    @Override
    public String getName(){
        return name;
    } 

    @Override
    public int[] getXY(){
        return new int[]{setX, setY};
    }

    @Override
    public String getLegend(){
        return legend;
    }
}

//
