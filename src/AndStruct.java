import logicircuit.LCComponent;

public class AndStruct implements ComponentInterface {
    private boolean[] inputs;
    private int setX;
    private int setY;
    private String legend;
    private String name;

    public AndStruct(String nome){
        name = nome;
        setX = 0;
        setY = 0;
        legend = "";
    }

    public AndStruct(String nome, int x, int y){
        name = nome;
        setX = x;
        setY = y;
        legend = "";
    }

    public AndStruct(String nome, int x, int y, String legenda){
        name = nome;
        setX = x;
        setY = y;
        legend = legenda;
    }



    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("AND gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("AND gate inputs are not set");
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
        Main.drawPannel.drawComponent(LCComponent.AND, setX, setY, legend);
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
