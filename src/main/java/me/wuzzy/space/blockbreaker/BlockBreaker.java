package me.wuzzy.space.blockbreaker;
import me.wuzzy.space.blockbreaker.Commands.GiveCommand;
import me.wuzzy.space.blockbreaker.Commands.TestCommand;
import me.wuzzy.space.blockbreaker.SQL.SQL;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BlockBreaker extends JavaPlugin {

    private HashMap<Block, BlockBreakerObject> hashMap= new HashMap<Block, BlockBreakerObject>();
    public SQL sql = new SQL(this);

    @Override
    public void onEnable() {
        sql.initializeDatabase();
        sql.createIfDoesntExist();
        sql.loadFromDataBase();


        new Events(this);
        new GiveCommand(this);
        new TestCommand(this);
    }

    @Override
    public void onDisable() {
        sql.closeConnection();
    }

    public void addToHashMap(Block block, BlockBreakerObject obj){
        hashMap.put(block,obj);
    }
    public void removeFromHashMap(Block block){
        hashMap.remove(block);
    }
    public boolean existsInHashMap(Block block){
        return hashMap.containsKey(block);
    }

    //TODO make the level thing change colors based on level for extra nice
    public ItemStack getBlockBreakerItem(int level) {
        ItemStack blockBreakerItem = new ItemStack(Material.DISPENSER);
        ItemMeta itemMeta =blockBreakerItem.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET+"Block Breaker"+ChatColor.GREEN+" <"+level+">");

        List<String> loreList = new ArrayList<String>();
        loreList.add(ChatColor.translateAlternateColorCodes('&',"&7Level: &a&l"+level));
        loreList.add("");
        loreList.add(ChatColor.GRAY+"Breaks blocks in front of it");
        itemMeta.setLore(loreList);

        NamespacedKey key = new NamespacedKey(this, "level");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);

        blockBreakerItem.setItemMeta(itemMeta);
        return blockBreakerItem;

    }





}
