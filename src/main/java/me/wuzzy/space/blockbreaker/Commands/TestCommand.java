package me.wuzzy.space.blockbreaker.Commands;

import me.wuzzy.space.blockbreaker.BlockBreaker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor {
    BlockBreaker instance;
    public TestCommand(BlockBreaker inst){
        instance=inst;
        instance.getCommand("bbtest").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        instance.sql.printDatabase();

        if (args.length==0){
            return true;
        }
        if (args[0].equalsIgnoreCase("drop")){
            instance.getLogger().info(ChatColor.GREEN+"Dropped Table");
            instance.sql.sendQuery("TRUNCATE TABLE BlockBreakers;");
        }

        return true;
    }
}
