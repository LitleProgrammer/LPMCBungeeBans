package de.littleprogrammer.lpmcbans;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;

public class JedisTerminal extends JedisPubSub {

    private String name;

    public JedisTerminal(String name) {
        this.name = name;
    }
    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals("ban")){
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                player.sendMessage(message);
            }
        }
        //System.out.printf("name: %s method: %s channel: %s message: %s\n", name, "onMessage", channel, message);
    }


}
