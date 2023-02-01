package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import de.littleprogrammer.lpmcbans.UUIDConverter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.Console;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KickCommand extends Command implements TabExecutor {

    private CustomePlayer customePlayer;
    private UUIDConverter uuidConverter;

    private List<String> arguments = new ArrayList<>();

    private StringBuilder sb = new StringBuilder();

    public KickCommand(){
        super("Kick");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1){

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){

                completions.add(player.getDisplayName());
            }
        }

        return completions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            try {
                customePlayer = new CustomePlayer(player.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            uuidConverter = new UUIDConverter();

            if (customePlayer.getRank().equalsIgnoreCase("ADMIN")) {
                if (args.length >= 2) {
                    if (ProxyServer.getInstance().getPlayer(args[0]).isConnected()) {
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

                        arguments = Arrays.asList(args.clone());
                        sb.setLength(0);

                        for (int i = 1; i < arguments.size(); i++) {
                            sb.append(arguments.get(i) + " ");
                        }

                        target.disconnect(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    } else {
                        player.sendMessage(ChatColor.RED + "Du musst einen Spieler angeben und der Spieler muss Online sein!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Benutzung: /kick <player> <reason>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung dafür!");
            }
        }
        if (!(sender instanceof ProxiedPlayer)){
            if (args.length >= 2) {
                if (ProxyServer.getInstance().getPlayer(args[0]).isConnected()) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

                    arguments = Arrays.asList(args.clone());

                    for (int i = 1; i < arguments.size(); i++) {
                        sb.append(arguments.get(i));
                    }

                    target.disconnect(sb.toString());
                }
            }
        }

    }
}
