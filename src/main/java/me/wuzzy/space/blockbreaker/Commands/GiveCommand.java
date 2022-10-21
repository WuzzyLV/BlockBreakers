package me.wuzzy.space.blockbreaker.Commands;

import me.wuzzy.space.blockbreaker.BlockBreaker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {

    BlockBreaker instance;

    public GiveCommand(BlockBreaker inst){
        instance=inst;
        instance.getCommand("bbgive").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player=(Player) sender;

            ItemStack itemStack=instance.getBlockBreakerItem(Integer.parseInt(args[0]));

            player.getInventory().addItem(itemStack);

            player.sendMessage(itemStack.getItemMeta().getDisplayName()+ ChatColor.RESET +" added to your inventory u a wanka");
        }else {
            sender.sendMessage("Only players can use this command");
        }


        return true;
    }
}
