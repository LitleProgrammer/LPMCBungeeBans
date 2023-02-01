package de.littleprogrammer.lpmcbans.listeners;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import de.littleprogrammer.lpmcbans.UUIDConverter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ConnectListeners implements Listener {

    private CustomePlayer customePlayer;
    private UUIDConverter uuidConverter;
    private Timestamp now = Timestamp.valueOf(LocalDateTime.now());


    @EventHandler
    public void onPostLogin(PostLoginEvent event) throws SQLException {

        try {
            customePlayer = new CustomePlayer(event.getPlayer().getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (customePlayer.getBan() == 1){
            if (!customePlayer.getRank().equalsIgnoreCase("ADMIN")) {
                if (customePlayer.getBanTimestamp().after(now)) {
                    event.getPlayer().disconnect(ChatColor.RED + "Du bist bis zum " + ChatColor.WHITE + customePlayer.getBanTimestamp().toString() + " (CET/MEZ)" + ChatColor.RED + " von diesem Server gebannt!\n" + ChatColor.WHITE + "Um einen Entbannungsantrag zu stellen gehe auf:\n" + "///");
                }else {
                    try {
                        customePlayer.setBan((byte) 0);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    event.getPlayer().sendMessage(ChatColor.RED + "Du bist nun entbannt. Bitte verhalte dich in Zukunft besser auf diesem Server!");
                }
            }
        }
        customePlayer.setOnline((byte) 1);
        System.out.println("set online");
        if (customePlayer.getName() == null || customePlayer.getName().equals("noplayernameherenow")) {
            customePlayer.setName(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) throws SQLException {
        try {
            customePlayer = new CustomePlayer(event.getPlayer().getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        customePlayer.setOnline((byte) 0);
        System.out.println("set offline");
    }
}
