import java.util.ArrayList;

import logicircuit.LCComponent;

public class MainCircuit {
    private ArrayList<Switch> switches;
    private ArrayList<IOComponent> components;
    private ArrayList<OutputInterface> outputs; // leds and displays

    public MainCircuit() {
        switches = new ArrayList<Switch>();
        components = new ArrayList<IOComponent>();
        outputs = new ArrayList<OutputInterface>();
    }

   /**  
    Draw a component that has no state
    @param cmp Component type from LCComponent (AND, NAND, NOR, NOT, OR, XOR)
    @param nome name
    @param x coordinate X
    @param y coordinate Y
    @param legenda legend
    */
    public void add(LCComponent cmp, String nome, int x, int y, String legenda) {
        if(cmp == LCComponent.AND){
            And and1 = new And(nome, x, y, legenda);
            components.add(and1);
        }else if(cmp == LCComponent.NAND){
            Nand nadn1 = new Nand(nome, x, y, legenda);
            components.add(nadn1);
        }else if(cmp == LCComponent.NOR){
            Nor nor1 = new Nor(nome, x, y, legenda);
            components.add(nor1);
        }else if(cmp == LCComponent.NOT){
            Not not1 = new Not(nome, x, y, legenda);
            components.add(not1);
        }else if(cmp == LCComponent.OR){
            Or or1 = new Or(nome, x, y, legenda);
            components.add(or1);
        }else if(cmp == LCComponent.XOR){
            Xor xor1 = new Xor(nome, x, y, legenda);
            components.add(xor1);
        }else{
            throw new IllegalArgumentException("Invalid value");
        }
    }

    /**  
    Draw a component that has no state
    @param cmp Component type from LCComponent (SWITCH)
    @param state state
    @param nome name
    @param setX coordinate X
    @param setY coordinate Y
    @param legenda legend
    */
    public void add(LCComponent cmp, boolean state, String nome, int setX, int setY, String legend) {
        if(cmp == LCComponent.SWITCH){
            Switch switch1 = new Switch(state, nome, setX, setY, legend);
            switches.add(switch1);
        }else{
            throw new IllegalArgumentException("Invalid value");
        }
    }

    /**  
    Draw a component that has no state
    @param cmp Component type from LCComponent (DISPLAY, LED)
    @param value value
    @param nome name
    @param SetX coordinate X
    @param SetY coordinate Y
    @param legend legend
    */
    public void add(LCComponent cmp, int value, String nome, int setX, int setY, String legend) {
        if(cmp == LCComponent.BIT3_DISPLAY){
            Display3bit display1 = new Display3bit(value, nome, setX, setY, legend);
            outputs.add(display1);
        }else if(cmp == LCComponent.LED){
            Led led1 = new Led(value, nome, setX, setY, legend);
            outputs.add(led1);
        }else{
            throw new IllegalArgumentException("Invalid value");
        }
    }

    public void wire(LCComponent cmp, int value, String nome, int setX, int setY, String legend) {
        if(cmp == LCComponent.BIT3_DISPLAY){
            Display3bit display1 = new Display3bit(value, nome, setX, setY, legend);
            outputs.add(display1);
        }else if(cmp == LCComponent.LED){
            Led led1 = new Led(value, nome, setX, setY, legend);
            outputs.add(led1);
        }else{
            throw new IllegalArgumentException("Invalid value");
        }
    }

    // add cada um deles uma funcao pra settar valores

    public void drawCircuit() {
        System.out.println(switches.size());
        for (Switch s : switches) {
            s.draw();
        }
        for (IOComponent c : components) {
            c.draw();
        }
        for (OutputInterface o : outputs) {
            o.draw();
        }
    }

}
