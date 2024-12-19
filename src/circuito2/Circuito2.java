package circuito2;

import logicircuit.LCDPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Circuito2 {
    private Map<String, Componente2> comps;
    private List<Conexao2> linhas;
    // Campo para armazenar o Panel onde serão represntados os objetos
    private LCDPanel drawPanel;

    public Circuito2(LCDPanel drawPanel) {
        comps = new HashMap<>();
        linhas = new ArrayList<>();
        this.drawPanel = drawPanel;
    }

    public boolean addComponente(Componente2 component) {
        if (comps.containsKey(component.getId()))
            return false;
        comps.put(component.getId(), component);
        return true;
    }

    public Componente2 getComponente(String id) {
        return comps.get(id);
    }

    public void addConexao(Conexao2 conexao) {
        linhas.add(conexao);
    }

    public void draw() {
        drawPanel.clear();;
        for(Componente2 cp: comps.values()) {
            cp.draw(drawPanel);
        }
        for(Conexao2 c: linhas) {
            c.draw(drawPanel);
        }
    }
}
