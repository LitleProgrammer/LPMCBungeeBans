package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.BadWords;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BadWordsCommand extends Command implements TabExecutor {

    private BadWords badWords;
    public BadWordsCommand() {
        super("BadWords");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1){
            completions.add("add");
            completions.add("remove");
        }

        return completions;
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        try {
            badWords = new BadWords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        switch (args[0]){
            case "add":

                try {
                    badWords.addBadWord(args[1]);
                    player.sendMessage(ChatColor.GREEN + "✔ " + ChatColor.WHITE + "Das Wort " + ChatColor.GOLD + args[1] + ChatColor.WHITE + " wurde in der Liste mit BadWords hinzugefügt");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case "remove":

                try {
                    badWords.removeBadWord(args[1]);
                    player.sendMessage(ChatColor.GREEN + "✔ " + ChatColor.WHITE + "Das Wort " + ChatColor.GOLD + args[1] + ChatColor.WHITE + " wurde aus der Liste mit BadWords entfernt");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
