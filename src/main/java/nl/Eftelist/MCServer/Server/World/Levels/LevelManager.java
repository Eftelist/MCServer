package nl.Eftelist.MCServer.Server.World.Levels;

import nl.Eftelist.MCServer.Server.World.Levels.Test.Test;

import java.util.ArrayList;

public class LevelManager {

    private ArrayList<Level> levels = new ArrayList<>();

    public LevelManager(){
        levels.add(new Test());
    }

    public Level getDebugLevel(){
        return levels.get(0);
    }
}
