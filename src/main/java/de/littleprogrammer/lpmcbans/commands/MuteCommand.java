package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import de.littleprogrammer.lpmcbans.UUIDConverter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MuteCommand extends Command implements TabExecutor {

    private CustomePlayer customePlayer;
    private UUIDConverter uuidConverter;
    private Timestamp now = Timestamp.valueOf(LocalDateTime.now());

    public MuteCommand(){
        super("Mute");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        uuidConverter = new UUIDConverter();

        ProxiedPlayer playerSender = (ProxiedPlayer) sender;

        try {
            customePlayer = new CustomePlayer(playerSender.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (args.length == 1){
            String playerName;

            for (String player : customePlayer.getPlayersInDB()){
                playerName = uuidConverter.NAME(player);
                completions.add(playerName);
            }
        }

        return completions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        try {
            customePlayer = new CustomePlayer(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        uuidConverter = new UUIDConverter();

        if (customePlayer.getRank().equalsIgnoreCase("ADMIN")){
            if (args.length == 1){
                if (customePlayer.getPlayersInDB().contains(uuidConverter.UUID(args[0]).toString())){
                    UUID targetUUID = UUID.fromString(uuidConverter.UUID(args[0]));

                    try {
                        customePlayer = new CustomePlayer(targetUUID);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (customePlayer.getMute() == 0){
                        try {
                            customePlayer.setMute((byte) 1);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuidConverter.UUID(args[0]));
                            if (target != null && target.isConnected()){
                                target.sendMessage(ChatColor.RED + "Du wurdest gemuted!");
                            }
                            player.sendMessage(ChatColor.GREEN + "✔  " + ChatColor.WHITE + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.WHITE + " wurde gemuted");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        player.sendMessage(ChatColor.RED + "Der Spieler ist bereits gemuted!");
                    }
                }else {
                    player.sendMessage(ChatColor.RED + "Du musst einen Spieler angeben");
                }
            }else {
                player.sendMessage(ChatColor.RED + "/Mute <player>");
            }
        }else {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung dafür!");
        }
    }

    public void Mute(UUID playerUUID) {
            UUID targetUUID = playerUUID;

            try {
                customePlayer = new CustomePlayer(targetUUID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (customePlayer.getMute() == 0){
                try {
                    customePlayer.setMute((byte) 1);
                    customePlayer.setMuteTimestamp(now);
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
                    if (target != null && target.isConnected()){
                        target.sendMessage(ChatColor.RED + "Du wurdest gemuted!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                return;
                //player.sendMessage(ChatColor.RED + "Der Spieler ist bereits gemuted!");
            }
    }

}
