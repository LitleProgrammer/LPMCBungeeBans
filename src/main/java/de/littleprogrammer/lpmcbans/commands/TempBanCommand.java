package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import de.littleprogrammer.lpmcbans.TimeStampCalculator;
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
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TempBanCommand extends Command implements TabExecutor {

    private CustomePlayer customePlayer;
    private UUIDConverter uuidConverter;
    private TimeStampCalculator timeStampCalculator;
    private Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    private Timestamp banTill = Timestamp.valueOf(LocalDateTime.now());

    public TempBanCommand() {
        super("tempBan");
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
            for (String player : customePlayer.getPlayersInDB()){
                playerName = uuidConverter.NAME(player);
                completions.add(playerName);
            }
        }

        return completions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        timeStampCalculator = new TimeStampCalculator();

        Calendar cal = Calendar.getInstance();

        ProxiedPlayer player = (ProxiedPlayer) sender;
        try {
            customePlayer = new CustomePlayer(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        uuidConverter = new UUIDConverter();

        if (customePlayer.getRank().equalsIgnoreCase("ADMIN")){
            if (args.length == 2){
                if (customePlayer.getPlayersInDB().contains(uuidConverter.UUID(args[0]).toString())){
                    UUID targetUUID = UUID.fromString(uuidConverter.UUID(args[0]));

                    try {
                        customePlayer = new CustomePlayer(targetUUID);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    if (customePlayer.getBan() == 0){
                        try {
                            //Banning the player
                            customePlayer.setBan((byte) 1);
                            //Calculating the time till the player is banned and setting it
                            cal.setTimeInMillis(now.getTime());
                            cal.add(Calendar.HOUR, Integer.parseInt(args[1]));
                            banTill = new Timestamp(cal.getTime().getTime());
                            customePlayer.setBanTimestamp(banTill);

                            //Blaming the player in chat and disconnecting him
                            ProxyServer.getInstance().broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + args[0] + ChatColor.RESET + ChatColor.GREEN + " wurde gebannt" + ChatColor.RED + " L " + ChatColor.GREEN + "in den Chat!");
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                            if (target != null && target.isConnected()){
                                target.disconnect(ChatColor.RED + "Du bist gebannt!\n" + ChatColor.WHITE + "Um einen Entbannungsantrag zu stellen gehe auf:\n" + "kp.lpmc.me");
                            }
                            //Sending done message
                            player.sendMessage(ChatColor.GREEN + "✔  " + ChatColor.WHITE + "Der Spieler " + ChatColor.GOLD + args[0] + ChatColor.WHITE + " wurde gebannt");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        player.sendMessage(ChatColor.RED + "Der Spieler ist bereits gebannt!");
                    }
                }else {
                    player.sendMessage(ChatColor.RED + "Du musst einen Spieler angeben");
                }
            }else {
                player.sendMessage(ChatColor.RED + "Benutzung: /temBan <player> <time (in hours)>");
            }
        }else {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung dafür!");
        }
    }
}
