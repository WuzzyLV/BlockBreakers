package me.wuzzy.space.blockbreaker;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static org.bukkit.Bukkit.getServer;

public class Events implements Listener {
    BlockBreaker instance;

    public Events(BlockBreaker inst){
        instance=inst;
        getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        ItemStack placedItem = event.getItemInHand();
        NamespacedKey key = new NamespacedKey(instance, "level");
        PersistentDataContainer pdc= placedItem.getItemMeta().getPersistentDataContainer();

        if (pdc.has(key, PersistentDataType.INTEGER)){
            int level = pdc.get(key, PersistentDataType.INTEGER);
            event.getPlayer().sendMessage("You placed a lvl "+level+" blockBreaker");

            Block block =event.getBlock();


            instance.addToHashMap(block, new BlockBreakerObject(block, level, instance));
            instance.sql.addLocation(block,level);

            BlockState blockState=block.getState();
            TileState tileState = (TileState) blockState;
            PersistentDataContainer blockPDC = tileState.getPersistentDataContainer();

            blockPDC.set(key, PersistentDataType.INTEGER,level);

            NamespacedKey keyOwner = new NamespacedKey(instance, "owner");
            String userUUID= event.getPlayer().getUniqueId().toString();

            blockPDC.set(keyOwner, PersistentDataType.STRING,userUUID);
            tileState.update();
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        Block block =event.getBlock();
        if (instance.existsInHashMap(block)){
            instance.removeFromHashMap(block);
        }
    }
}
