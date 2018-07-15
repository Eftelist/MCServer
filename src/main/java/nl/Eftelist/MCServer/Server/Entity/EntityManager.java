package nl.Eftelist.MCServer.Server.Entity;

public class EntityManager {

    private static int id = -1;

    public static int getNextId() {
        id += 1;
        return id;
    }
}
