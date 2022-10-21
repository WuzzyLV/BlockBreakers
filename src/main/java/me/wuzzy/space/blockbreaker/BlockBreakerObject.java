package me.wuzzy.space.blockbreaker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlockBreakerObject {

    Block block;
    BlockData blockData;
    Location location;
    int level;
    int speed;

    public BlockBreakerObject(Block blck, int lvl, BlockBreaker instance){
        block=blck;
        blockData=blck.getBlockData();
        location=blck.getLocation();
        level=lvl;
        speed=6-lvl;
        startTask(instance);
    }
        // TODO Add a block mask for what blocks to break and what blocks to ignore
        // TODO Check if the chunk is loaded
    public void startTask(BlockBreaker instance) {
        new BukkitRunnable(){
            @Override
            public void run() {
                boolean exists = block.getBlockData().equals(blockData);

                if (instance.existsInHashMap(block) && exists) {
                        Container ih = (Container) location.getBlock().getState();
                        Block targetBlock = getBlockInFront(block);

                        if (targetBlock.getType() != Material.AIR) {
                            ih.getInventory().addItem(new ItemStack(targetBlock.getType(), 1));
                            targetBlock.setType(Material.AIR);
                        }

                }else{
                    this.cancel();
                    instance.sql.removeLocation(block);
                }
            }
        }.runTaskTimer(instance,20L*speed,20L*speed);
    }

    public Block getBlockInFront(Block block){
        Vector blockVec =((Directional) block.getBlockData()).getFacing().getDirection();
        int TargetX=block.getX()+blockVec.getBlockX();
        int TargetY=block.getY()+blockVec.getBlockY();
        int TargetZ=block.getZ()+blockVec.getBlockZ();

        return block.getWorld().getBlockAt(TargetX,TargetY,TargetZ);
    }
}
