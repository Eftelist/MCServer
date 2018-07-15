package nl.Eftelist.MCServer.Server.Protocol.Custom;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientPlayerPositionAndLook2 extends MinecraftPacket {

    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;

    public ClientPlayerPositionAndLook2(double x, double y, double z, float yaw, float pitch, boolean onGround){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void read(NetInput netInput) {

    }

    @Override
    public void write(NetOutput netOutput) {
        try {
            netOutput.writeDouble(x);
            netOutput.writeDouble(y - 1.62);
            netOutput.writeDouble(z);
            netOutput.writeFloat(yaw);
            netOutput.writeFloat(pitch);
            netOutput.writeBoolean(onGround);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
