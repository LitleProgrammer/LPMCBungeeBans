package de.littleprogrammer.lpmcbans;

import de.littleprogrammer.lpmcbans.commands.BanCommand;
import de.littleprogrammer.lpmcbans.commands.KickCommand;
import de.littleprogrammer.lpmcbans.commands.MuteCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.JedisPubSub;

import java.io.Console;
import java.util.UUID;

public class JedisTerminal extends JedisPubSub {

    private String name;
    private BanCommand banCommand;
    private KickCommand kickCommand;
    private MuteCommand muteCommand;

    public JedisTerminal(String name) {
        this.name = name;
    }
    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals("ban")){
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                banCommand = new BanCommand();
                banCommand.Ban(UUID.fromString(message));
            }
        }
        if (channel.equals("kick")){
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                player.sendMessage("kick-" + message);
                kickCommand = new KickCommand();
                kickCommand.Kick(UUID.fromString(message));

            }
        }
        if (channel.equals("mute")){
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                muteCommand = new MuteCommand();
                muteCommand.Mute(UUID.fromString(message)); //this.UUIDConverter = null
            }
        }
    }


}
