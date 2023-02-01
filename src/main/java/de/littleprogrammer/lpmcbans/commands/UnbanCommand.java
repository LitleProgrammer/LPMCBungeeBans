package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import de.littleprogrammer.lpmcbans.UUIDConverter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnbanCommand extends Command implements TabExecutor {

    private CustomePlayer customePlayer;
    private UUIDConverter uuidConverter;

    public UnbanCommand() {
        super("Unban");
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

        String playerName;

        if (args.length == 1){
            for (String player : customePlayer.getPlayersBanned()){
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
                    if (customePlayer.getBan() == 1){
                        try {
                            customePlayer.setBan((byte) 0);
                            player.sendMessage(ChatColor.GREEN + "✔  " + ChatColor.WHITE + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.WHITE + " wurde entbannt");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        player.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht gebannt!");
                    }
                }else {
                    player.sendMessage(ChatColor.RED + "Der Spieler war nie zuvor auf diesem Server!");
                }
            }else {
                player.sendMessage(ChatColor.RED + "Benutzung: /ban <player>");
            }
        }else {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung dafür!");
        }
    }
}

