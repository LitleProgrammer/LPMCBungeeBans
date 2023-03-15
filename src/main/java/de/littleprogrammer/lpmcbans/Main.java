package de.littleprogrammer.lpmcbans;

import de.littleprogrammer.lpmcbans.commands.*;
import de.littleprogrammer.lpmcbans.listeners.ChatListener;
import de.littleprogrammer.lpmcbans.listeners.ConnectListeners;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Main extends Plugin {

    private static Main instance;
    private Database database;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        //listeners
        getProxy().getPluginManager();

        //commands
        getProxy().getPluginManager().registerCommand(this, new BanCommand());
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
        getProxy().getPluginManager().registerCommand(this, new KickCommand());
        getProxy().getPluginManager().registerCommand(this, new MuteCommand());
        getProxy().getPluginManager().registerCommand(this, new UnmmuteCommand());
        getProxy().getPluginManager().registerCommand(this, new ListCommand());
        getProxy().getPluginManager().registerCommand(this, new UUIDConvert());
        getProxy().getPluginManager().registerCommand(this, new TempBanCommand());
        getProxy().getPluginManager().registerCommand(this, new BadWordsCommand());

        getProxy().getPluginManager().registerListener(this, new ConnectListeners());
        getProxy().getPluginManager().registerListener(this, new ChatListener());


        //Database & Redis ping
        JedisPooled jedis = new JedisPooled("localhost", 6379);
        getProxy().getScheduler().schedule(this, () -> {
            database = new Database();
            try {
                database.connect();
                if (jedis.get("ping").equalsIgnoreCase("1")){
                    jedis.set("ping", "0");
                }else {
                    jedis.set("ping", "1");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Reconnect Failed (Proxy Bans)");
            }
        }, 59, 57, TimeUnit.SECONDS);//14:44:30


        database = new Database();
        try {
            database.connect();
        }catch (SQLException e){
            System.out.println("Can not connect to the database");
            e.printStackTrace();
        }
        System.out.println("Database is connected = " + database.isConnected());


        //Jedis Connect
        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.execute(() -> jedis.subscribe(new JedisTerminal("onlyOne"), "ban", "kick", "mute", "unban", "unmute"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        database.disconnect();
    }

    public static Main getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }
}
