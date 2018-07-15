package nl.Eftelist.MCServer.Server.Protocol.Custom;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ClientPlayerPositionAndLook extends MinecraftPacket {

    private double x,y,z;
    private float yaw,pitch;
    private int bitfield;
    private int id;

    public ClientPlayerPositionAndLook(double x, double y, double z, float yaw, float pitch, int bitfield, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.bitfield = bitfield;
        this.id = id;
    }


    @Override
    public void read(NetInput netInput) throws IOException {

    }

    @Override
    public void write(NetOutput netOutput) throws IOException {
        netOutput.writeDouble(x);
        netOutput.writeDouble(y);
        netOutput.writeDouble(z);
        netOutput.writeFloat(yaw);
        netOutput.writeFloat(pitch);
        netOutput.writeByte(bitfield);
        netOutput.writeVarInt(id);
    }
}
