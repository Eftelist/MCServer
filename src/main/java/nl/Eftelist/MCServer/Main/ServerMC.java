package nl.Eftelist.MCServer.Main;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.ServerLoginHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import nl.Eftelist.MCServer.Server.Entity.EntityManager;
import nl.Eftelist.MCServer.Server.Protocol.Custom.ClientPlayerSpawnPacket;
import nl.Eftelist.MCServer.Server.Protocol.ProtocolListener;
import nl.Eftelist.MCServer.Server.World.Levels.LevelManager;

import java.net.Proxy;

public class ServerMC {

    // Set variables
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 25565;
    private static final Proxy PROXY = Proxy.NO_PROXY;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;
    private static final boolean VERIFY_USERS = false;
    private static Server server;
    private static LevelManager levelManager;

    // Create server
    public ServerMC(){
        server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory(PROXY));
        server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, AUTH_PROXY);
        server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, VERIFY_USERS);
        createPing();
        createHandlers();
        setFlags();
        createManagers();
        createListenersAndBind();
        createCustomPackets();
    }

    private void createCustomPackets() {
        PacketProtocol sessionProto = server.createPacketProtocol();
        sessionProto.registerOutgoing(900, ClientPlayerSpawnPacket.class);
        System.out.println("lel");
    }

    private void createManagers() {
        levelManager = new LevelManager();
    }

    private void createListenersAndBind() {
        server.addListener(new ProtocolListener());
        server.bind();
    }

    private void setFlags() {
        server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 100);
    }

    private void createHandlers() {
        server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
            @Override
            public void loggedIn(Session session) {
                session.send(new ServerJoinGamePacket(EntityManager.getNextId(), false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 10, WorldType.FLAT, false));
            }
        });
    }

    private void createPing(){
        server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
            @Override
            public ServerStatusInfo buildInfo(Session session) {
                return new ServerStatusInfo(new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION), new PlayerInfo(20, 0, new GameProfile[0]), new TextMessage("A MinecartJ server!"), null);
            }
        });
    }

    public static Server getServer() {
        return server;
    }

    public static LevelManager getLevelManager() {
        return levelManager;
    }
}
