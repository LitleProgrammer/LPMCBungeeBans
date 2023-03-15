package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import de.littleprogrammer.lpmcbans.Main;
import de.littleprogrammer.lpmcbans.TimeStampCalculator;
import de.littleprogrammer.lpmcbans.UUIDConverter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class BanCommand extends Command implements TabExecutor {

    private CustomePlayer customePlayer;
    private UUIDConverter uuidConverter;
    private TimeStampCalculator timeStampCalculator;
    private Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    private Timestamp banTill = Timestamp.valueOf(LocalDateTime.now());
    public BanCommand() {
        super("Ban");
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

        /*if (args.length == 1){
            for (String player : customePlayer.getPlayersInDB()){
                playerName = uuidConverter.NAME(player);
                completions.add(playerName);
            }
        }*/

        if (args.length == 1) {
            for (String playerNameTab : customePlayer.getPlayerNamesInDB()) {
                completions.add(playerNameTab);
            }
        }

        return completions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        timeStampCalculator = new TimeStampCalculator();

        Calendar cal = Calendar.getInstance();

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            try {
                customePlayer = new CustomePlayer(player.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            uuidConverter = new UUIDConverter();

            if (customePlayer.getRank().equalsIgnoreCase("ADMIN")) {
                if (args.length == 1) {
                    if (customePlayer.getPlayersInDB().contains(uuidConverter.UUID(args[0]).toString())) {
                        UUID targetUUID = UUID.fromString(uuidConverter.UUID(args[0]));

                        try {
                            customePlayer = new CustomePlayer(targetUUID);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (customePlayer.getBan() == 0) {
                            try {
                                customePlayer.setBan((byte) 1);
                                //Calculating the time till the player is banned and setting it
                                cal.setTimeInMillis(now.getTime());
                                cal.add(Calendar.YEAR, 99);
                                banTill = new Timestamp(cal.getTime().getTime());
                                customePlayer.setBanTimestamp(banTill);
                                ProxyServer.getInstance().broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + args[0] + ChatColor.RESET + ChatColor.GREEN + " wurde gebannt" + ChatColor.RED + " L " + ChatColor.GREEN + "in den Chat!");
                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                                if (target != null && target.isConnected()) {
                                    target.disconnect(ChatColor.RED + "Du bist gebannt!\n" + ChatColor.WHITE + "Um einen Entbannungsantrag zu stellen gehe auf:\n" + "kp.lpmc.me");
                                }
                                player.sendMessage(ChatColor.GREEN + "✔  " + ChatColor.WHITE + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.WHITE + " wurde gebannt");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Der Spieler ist bereits gebannt!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Du musst einen Spieler angeben");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Benutzung: /ban <player>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung dafür!");
            }
        }
        if (!(sender instanceof ProxiedPlayer)){
            uuidConverter = new UUIDConverter();
                if (args.length == 1) {

                        UUID targetUUID = UUID.fromString(uuidConverter.UUID(args[0]));
                        try {
                            customePlayer = new CustomePlayer(targetUUID);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (customePlayer.getBan() == 0) {
                            try {
                                customePlayer.setBan((byte) 1);
                                //Calculating the time till the player is banned and setting it
                                cal.setTimeInMillis(now.getTime());
                                cal.add(Calendar.YEAR, 99);
                                banTill = new Timestamp(cal.getTime().getTime());
                                customePlayer.setBanTimestamp(banTill);
                                ProxyServer.getInstance().broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + args[0] + ChatColor.RESET + ChatColor.GREEN + " wurde gebannt" + ChatColor.RED + " L " + ChatColor.GREEN + "in den Chat!");
                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                                if (target != null && target.isConnected()) {
                                    target.disconnect(ChatColor.RED + "Du bist gebannt!\n" + ChatColor.WHITE + "Um einen Entbannungsantrag zu stellen gehe auf:\n" + "kp.lpmc.me");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
        }
    }

    public void Ban(UUID playerUUID) throws SQLException {

        uuidConverter = new UUIDConverter();

        UUID targetUUID = playerUUID;
        String targetName = uuidConverter.NAME(String.valueOf(targetUUID));
        Calendar cal = Calendar.getInstance();

        try {
            customePlayer = new CustomePlayer(targetUUID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);

        if (customePlayer.getBan() == 0) {
            if (target == null) {
                PreparedStatement statement = Main.getInstance().getDatabase().getConnection().prepareStatement("UPDATE players SET BAN=1 WHERE UUID='" + playerUUID + "';");
                statement.executeUpdate();

            } else if (target.isConnected()) {
                try {
                    customePlayer.setBan((byte) 1);
                    //Calculating the time till the player is banned and setting it
                    cal.setTimeInMillis(now.getTime());
                    cal.add(Calendar.YEAR, 99);
                    banTill = new Timestamp(cal.getTime().getTime());
                    customePlayer.setBanTimestamp(banTill);
                    ProxyServer.getInstance().broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + targetName + ChatColor.RESET + ChatColor.GREEN + " wurde gebannt" + ChatColor.RED + " L " + ChatColor.GREEN + "in den Chat!");
                    target.disconnect(ChatColor.RED + "Du bist gebannt!\n" + ChatColor.RED + "Um einen Entbannungsantrag zu stellen gehe auf:\n " + ChatColor.WHITE + "https://lpmc.me/appeal");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

