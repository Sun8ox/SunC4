package mc.sun8ox.c4;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class EventHandlers implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void playerLeave(org.bukkit.event.player.PlayerQuitEvent e) {
        Player p = e.getPlayer();

        ArrayList<C4> toRemove = new ArrayList<>();

        for(C4 c4 : Main.c4s) {
            if(Objects.equals(c4.ownerName, p.getName())){
                c4.block.setType(Material.AIR);

                toRemove.add(c4);
            }
        }

        Main.c4s.removeAll(toRemove);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void blockBreak(BlockBreakEvent e) {

        Block brokenBlock = e.getBlock();


        if(brokenBlock.getType() == Material.PLAYER_HEAD){
            C4 c4toRemove = null;

            for (C4 c4 : Main.c4s) {
                if(!c4.isExploding){
                    if(brokenBlock.getX() == c4.block.getX() && brokenBlock.getY() == c4.block.getY() && brokenBlock.getZ() == c4.block.getZ()){
                        e.setCancelled(true);
                        brokenBlock.setType(Material.AIR);

                        e.getPlayer().sendMessage(Main.prefix + "You have successfully defused a C4!");

                        c4toRemove = c4;

                    }
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(Main.prefix + "You cannot defuse a C4 that is exploding!");
                }
            }

            Main.c4s.remove(c4toRemove);
        }


    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerClick(PlayerInteractEvent e) {

        if(e.getAction().isRightClick()){
            ItemStack is = e.getItem();
            Player p = e.getPlayer();

            if(is == null) return;

            if(is.lore() != null){

                if(Objects.requireNonNull(is.lore()).contains(Main.C4Lore)){


                    if(e.getClickedBlock() != null){

                        // Main.plugin.getLogger().info(e.getAction().name());

                        Block b = e.getClickedBlock().getRelative(e.getBlockFace());

                        if(b.getType() == Material.AIR){
                            if(p.isSneaking()){
                                C4 c4 = new C4();
                                c4.block = Objects.requireNonNull(b);
                                c4.ownerName = p.getName();
                                c4.isExploding = false;

                                p.getWorld().playSound(c4.block.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 100, 1 );

                                Main.c4s.add(c4);

                                p.sendMessage(Main.prefix + "You have successfully placed a C4!");
                            } else {
                                e.setCancelled(true);
                                p.sendMessage(Main.prefix + "You must be sneaking to place a C4!");
                            }
                        } else {
                            p.sendMessage(Main.prefix + "You cannot place a C4 there!");
                        }
                    } else {
                        p.sendMessage(Main.prefix + "You cannot place a C4 there!");
                    }

                }

                if(Objects.requireNonNull(is.lore()).contains(Main.DetonatorLore)){
                    boolean bom = false;

                    ArrayList<C4> toRemove = new ArrayList<>();

                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_HIT, 100, 1 );

                    int delay = 0;

                    for(C4 c4 : Main.c4s) {
                        if(Objects.equals(c4.ownerName, p.getName())) {
                            c4.isExploding = true;

                            if (c4.block.getType() == Material.PLAYER_WALL_HEAD || c4.block.getType() == Material.PLAYER_HEAD) {
                                Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        c4.block.setType(Material.AIR);
                                        c4.block.getLocation().createExplosion(Main.config.getInt("c4-power"));
                                    }
                                }, delay);

                                if(!bom){
                                    bom = true;
                                }
                            }

                            toRemove.add(c4);

                            delay += 5;
                        }
                    }

                    Main.c4s.removeAll(toRemove);

                    if(bom){
                        p.sendMessage(Main.prefix + "You have successfully detonated your C4s!");
                    } else {
                        p.sendMessage(Main.prefix + "You do not have any C4s to detonate!");
                    }

                    e.setCancelled(true);
                }

                if(Objects.requireNonNull(is.lore()).contains(Main.CoreLore)){
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(Main.prefix + "You cannot place C4 core!");
                }


            }
        }

    }

}
