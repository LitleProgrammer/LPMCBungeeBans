package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.CustomePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class ListCommand extends Command {

    private CustomePlayer customePlayer;

    public ListCommand() {
        super("listenC");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        try {
            customePlayer = new CustomePlayer(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < customePlayer.getPlayersInDB().size(); i++) {
            player.sendMessage(customePlayer.getPlayersInDB().get(i));
        }
    }
}
