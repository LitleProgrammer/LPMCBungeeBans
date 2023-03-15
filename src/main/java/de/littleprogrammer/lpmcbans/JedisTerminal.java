package de.littleprogrammer.lpmcbans;

import de.littleprogrammer.lpmcbans.commands.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.JedisPubSub;

import java.io.Console;
import java.sql.SQLException;
import java.util.UUID;

public class JedisTerminal extends JedisPubSub {

    private String name;
    private BanCommand banCommand;
    private KickCommand kickCommand;
    private MuteCommand muteCommand;
    private UnmmuteCommand unmmuteCommand;
    private UnbanCommand unbanCommand;

    public JedisTerminal(String name) {
        this.name = name;
    }
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("m message on channel " + channel + message);
        if (channel.equals("ban")){
                banCommand = new BanCommand();
                try {
                    banCommand.Ban(UUID.fromString(message));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        }
        if (channel.equals("kick")){
                kickCommand = new KickCommand();
                kickCommand.Kick(UUID.fromString(message));
        }
        if (channel.equals("mute")){
                muteCommand = new MuteCommand();
                try {
                    muteCommand.Mute(UUID.fromString(message));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        }
        if (channel.equals("unban")){
            unbanCommand = new UnbanCommand();
            unbanCommand.Unban(UUID.fromString(message));
        }
        if (channel.equals("unmute")){
            unmmuteCommand = new UnmmuteCommand();
            try {
                unmmuteCommand.Unmute(UUID.fromString(message));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
