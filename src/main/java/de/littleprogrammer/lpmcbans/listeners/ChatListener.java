package de.littleprogrammer.lpmcbans.listeners;

import de.littleprogrammer.lpmcbans.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ChatListener implements Listener {

    private BadWords badWords;
    private CustomePlayer customePlayer;
    private TimeStampCalculator timeStampCalculator;
    private HashMap<Integer, String> muted = new HashMap<>();

    @EventHandler
    public void onChat(ChatEvent event) throws SQLException {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        customePlayer = new CustomePlayer(player.getUniqueId());
        badWords = new BadWords();
        timeStampCalculator = new TimeStampCalculator();

        String message = event.getMessage();


        //No swearword detection in these Servers
        if (player.getServer().getInfo().getName().contains("Building-")){
            return;
        }

        if (player.getServer().getInfo().getName().contains("Vocab-")){
            return;
        }


        //Checks words in message with Database, and if player don't have ADMIN rank
        for (String word : badWords.getBadWords()){
            for (String messagePart : message.trim().split("\\s+")){
                if (!customePlayer.getRank().equalsIgnoreCase("ADMIN")) {
                    if (word.equalsIgnoreCase(messagePart)) {
                        event.setCancelled(true);
                    }
                }
            }
        }


        //Checks if the player can be unmutet
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (customePlayer.getMute() == 1){
            if (timeStampCalculator.calculateDifference(now.toString(), customePlayer.getMuteTimestamp().toString(), "hours") >= 1){
                event.setCancelled(false);
                customePlayer.setMute((byte) 0);
                customePlayer.setMuteTimestamp(now);
                player.sendMessage(ChatColor.RED + "Du wurdest entmutet bitte verhalte dich in zukunft besser im Chat!");
            }
            if (customePlayer.getRank().equalsIgnoreCase("ADMIN")) {
                customePlayer.setMute((byte) 0);
                player.sendMessage(ChatColor.RED + "Du wurdest von dem Web Inerface gemuted, nun wurdest du entmuted weil du ein Admin bist.\nVerhalte dich bitte besser im Chat das nächste mal!");
            }
        }


        //Checks if the event is cancelled
        if (event.isCancelled()){
            //Checks if the player doesn't have the rank ADMIN
            if (!customePlayer.getRank().equalsIgnoreCase("ADMIN")){
                //Send message to player
                player.sendMessage(ChatColor.RED + "Du bist nun für 1 Stunde gemuted, weil du ein verbotenes Wort benutzt hast!");
                //Mutes player in Database
                customePlayer.setMute((byte) 1);
                //Sets Timestamp in Database
                customePlayer.setMuteTimestamp(now);
                //Irrelevant
                muted.put(60, player.getDisplayName());
            }


            //Checks if the player does have the rank ADMIN
            if (customePlayer.getRank().equalsIgnoreCase("ADMIN")){
                //Sends message to ADMIN player to warn him
                player.sendMessage(ChatColor.RED + "Bitte achte auf deine Wortwahl.");
                player.sendMessage(ChatColor.RED + "Du bist ein Admin deshalb wirst du nicht gemuted");
                customePlayer.setMute((byte) 0);
            }
        }
    }
}
