package nl.Eftelist.MCServer.Server.Vectors;

import nl.Eftelist.MCServer.Server.Objects.Location;

public class Vector3 {

    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Location toLocation() {
        return new Location(x,y,z,0.0f,0.0f);
    }
}
