package com.brxnxx.fakeplayers.commands;

import com.brxnxx.fakeplayers.factory.FakePlayerGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnPlayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        /**
         * TODO: add TAB/VISIBILITY options
         */

        Player executor = (Player) commandSender;
        if (!executor.isOp()) return false;

        if (args.length == 1) {
            executor.sendMessage("1");
            if (args[0] != null) {
                int amount = 0;

                try {
                    amount = Integer.parseInt(args[0]);
                } catch (NumberFormatException nfe) {
                    executor.sendMessage("§cPlease use numbers...");
                    return false;
                }

                executor.sendMessage("" + amount);

                FakePlayerGenerator.generatePlayer(executor, amount);
                executor.sendMessage("§aSuccess! Spawned " + amount + " §afake players!");
            }
        }

        return false;
    }

}
