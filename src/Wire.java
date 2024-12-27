public class Wire {
    private boolean state;
    private BasicComponentInterface component1;
    private BasicComponentInterface component2;

    public Wire(BasicComponentInterface component1, BasicComponentInterface component2) {
        this.component1 = component1;
        this.component2 = component2;
        this.state = false;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public void draw() {

        if (component2 instanceof IOComponent) {
            IOComponent ioComponent = (IOComponent) component2;
            Main.drawPannel.drawWire(component1.getType(), component1.getXY()[0], component1.getXY()[1],
                    component2.getType(), component2.getXY()[0], component2.getXY()[1], ioComponent.getNextPin(),
                    state);
        }

    }
}
