package nl.Eftelist.MCServer.Server.World.Levels.Test;

import nl.Eftelist.MCServer.Server.Vectors.Vector3;
import nl.Eftelist.MCServer.Server.World.Levels.Level;

public class Test implements Level {

    @Override
    public Vector3 getSpawnPoint() {
        return new Vector3(0,82,0);
    }
}
