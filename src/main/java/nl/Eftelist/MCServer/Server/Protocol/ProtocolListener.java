package nl.Eftelist.MCServer.Server.Protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.message.*;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.server.ServerAdapter;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import nl.Eftelist.MCServer.Main.ServerMC;
import nl.Eftelist.MCServer.Server.Objects.Location;
import nl.Eftelist.MCServer.Server.Protocol.Custom.ClientPlayerPositionAndLook;
import nl.Eftelist.MCServer.Server.Protocol.Custom.ClientPlayerPositionAndLook2;
import nl.Eftelist.MCServer.Server.Protocol.Custom.ClientPlayerSpawnPacket;
import nl.Eftelist.MCServer.Server.World.Levels.Level;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProtocolListener extends ServerAdapter {


    @Override
    public void serverClosed(ServerClosedEvent event) {
        System.out.println("Server closed.");
    }

    @Override
    public void sessionAdded(SessionAddedEvent event) {
        event.getSession().addListener(new SessionAdapter() {

            @Override
            public void connected(ConnectedEvent event) {
                Session session = event.getSession();
                System.out.println("testy");
            }

            @Override
            public void packetSending(PacketSendingEvent event) {
                Packet recv = event.getPacket();
                Session session = event.getSession();
                System.out.println("SENDED " +recv.toString());
                if (recv instanceof ServerJoinGamePacket) {
                    // Send Custom PlayerPosition packet
                    PacketProtocol sessionProto = session.getPacketProtocol();

                    // Register protocol
                    sessionProto.registerOutgoing(0x46, ClientPlayerSpawnPacket.class);
                    sessionProto.registerOutgoing(0x2F, ClientPlayerPositionAndLook.class);
                    sessionProto.registerOutgoing(0x2C, ClientPlayerAbilitiesPacket.class);
                    sessionProto.registerOutgoing(0x0E, ClientPlayerPositionRotationPacket.class);

                    Level debug = ServerMC.getLevelManager().getDebugLevel();
                    Location location = debug.getSpawnPoint().toLocation();
                    session.send(new ClientPlayerSpawnPacket(false, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
                    session.send(new ClientPlayerAbilitiesPacket(false, true, true, false, 0, 0));

                    // Send Player Abilities

                    // Recieve Client Settings

                    // Send Player Position & Look
                    sendDelayPacket(session, new ClientPlayerPositionAndLook(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), 0x10, 1), 1, TimeUnit.SECONDS);


                    // Looks like everything is OK now.. i like!
                    // Going to git
                    // Brb.

                }
            }

            public void sendDelayPacket(final Session session, final Packet packet, int delay, TimeUnit unit){
                final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(new Runnable() {
                    @Override
                    public void run() {
                        sendPacketDelay(session,packet);
                    }
                },delay,unit);
            }

            private void sendPacketDelay(Session sesion,Packet packet) {
                sesion.send(packet);
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                Packet recv = event.getPacket();
                Session session = event.getSession();
                System.out.println("RECIEVE "+recv.toString());
                if (event.getPacket() instanceof ClientChatPacket) {
                    ClientChatPacket packet = event.getPacket();
                    GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                    System.out.println(profile.getName() + ": " + packet.getMessage());
                    Message msg = new TextMessage("Hello, ").setStyle(new MessageStyle().setColor(ChatColor.GREEN));
                    Message name = new TextMessage(profile.getName()).setStyle(new MessageStyle().setColor(ChatColor.AQUA).addFormat(ChatFormat.UNDERLINED));
                    Message end = new TextMessage("!");
                    msg.addExtra(name);
                    msg.addExtra(end);
                    event.getSession().send(new ServerChatPacket(msg));
                }
            }
        });
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.GAME) {
            System.out.println("Closing server.");
            event.getServer().close(false);
        }
    }
}
