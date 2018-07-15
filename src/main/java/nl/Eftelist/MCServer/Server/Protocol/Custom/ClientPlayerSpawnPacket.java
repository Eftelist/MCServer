package nl.Eftelist.MCServer.Server.Protocol.Custom;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import io.netty.util.NetUtil;

import java.io.IOException;

public class ClientPlayerSpawnPacket extends MinecraftPacket {

    protected int x;
    protected int y;
    protected int z;

    public ClientPlayerSpawnPacket(boolean b, double x, double y, double z, float yaw, float pitch) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    protected ClientPlayerSpawnPacket() {
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public void write(NetOutput netOutput) {
        try {
            com.github.steveice10.mc.protocol.util.NetUtil.writePosition(netOutput,new Position(x,y,z));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void read(NetInput netInput) {
        long pos = 0;
        try {
            pos = netInput.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
