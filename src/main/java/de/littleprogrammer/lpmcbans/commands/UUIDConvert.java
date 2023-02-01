package de.littleprogrammer.lpmcbans.commands;

import de.littleprogrammer.lpmcbans.UUIDConverter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


public class UUIDConvert extends Command {

    private UUIDConverter uuidConverter;

    public UUIDConvert() {
        super("convert");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        uuidConverter = new UUIDConverter();

        sender.sendMessage(uuidConverter.UUID(args[0]));

    }
}
