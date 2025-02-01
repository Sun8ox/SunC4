package mc.sun8ox.c4;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class Main extends JavaPlugin {

    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&cC4&8] &e");


    public static FileConfiguration config;

    public static Component C4Name;
    public static Component C4Lore;

    public static Component DetonatorName;
    public static Component DetonatorLore;

    public static Component CoreName;
    public static Component CoreLore;

    public static Plugin plugin;


    public static ArrayList<C4> c4s;

    public static PlayerProfile c4Profile;
    public static PlayerProfile coreProfile;

    public static boolean craftingEnabled = false;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        config = getConfig();
        plugin = this;

        getServer().getPluginManager().registerEvents(new EventHandlers(), this);

        c4s = new ArrayList<C4>();

        craftingEnabled = getConfig().getBoolean("c4-crafting-enabled");

        C4Name = text()
                .content("")
                .append(text("[").color(DARK_GRAY))
                .append(text("C4").color(RED))
                .append(text("]").color(DARK_GRAY))
                .build();

        DetonatorName = text()
                .content("")
                .append(text("[").color(DARK_GRAY))
                .append(text("Detonator").color(RED))
                .append(text("]").color(DARK_GRAY))
                .build();

        CoreName = text()
                .content("")
                .append(text("[").color(DARK_GRAY))
                .append(text("Core").color(AQUA))
                .append(text("]").color(DARK_GRAY))
                .build();

        C4Lore = text("Remotely controlled C4").color(DARK_GRAY);
        DetonatorLore = text("Remote control for C4s").color(DARK_GRAY);
        CoreLore = text("C4 Core").color(DARK_GRAY);

        try {
            c4Profile = Bukkit.createProfile(UUID.randomUUID());
            PlayerTextures textures = c4Profile.getTextures();
            textures.setSkin(new URL(Objects.requireNonNull(config.getString("c4-skin"))));
            c4Profile.setTextures(textures);

            coreProfile = Bukkit.createProfile(UUID.randomUUID());
            PlayerTextures coreTextures = coreProfile.getTextures();
            coreTextures.setSkin(new URL(Objects.requireNonNull(config.getString("core-skin"))));
            coreProfile.setTextures(coreTextures);

            // Bukkit.getScheduler().runTaskAsynchronously(this, () -> c4Profile.complete());
        } catch (MalformedURLException e) {
            getLogger().info(Main.prefix + "Incorrect C4 skin url!");
        }

        if(craftingEnabled){
            getServer().addRecipe(Recipes.coreRecipe());
            getServer().addRecipe(Recipes.c4Recipe());
            getServer().addRecipe(Recipes.detonatorRecipe());

            getLogger().info("C4 crafting enabled!");
        }


        getLogger().info("Plugin loaded!");
        getLogger().info("Welcome :)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (C4 c4 : c4s) {
            c4.block.setType(Material.AIR);
        }

        if(craftingEnabled){
            getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("core", Main.plugin)));
            getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("c4", Main.plugin)));
            getServer().removeRecipe(Objects.requireNonNull(NamespacedKey.fromString("detonator", Main.plugin)));
        }

        getLogger().info("Plugin unloaded!");
        getLogger().info("Goodbye :(");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(cmd.getName().equalsIgnoreCase("givec4")){
            if(sender instanceof Player p){
                if(p.hasPermission("sun8ox.c4.give")){
                    
                    ItemStack c4 = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta c4Meta = (SkullMeta) c4.getItemMeta();
                    c4Meta.displayName(C4Name);
                    c4Meta.setPlayerProfile(c4Profile);
                    c4Meta.setUnbreakable(true);
                    c4.setItemMeta(c4Meta);

                    int amount = 1;
                    if(args.length > 0){
                        try {
                            amount = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(prefix + "Invalid amount!");
                        }
                    }

                    c4.setAmount(amount);
                    c4.lore(List.of(C4Lore));


                    p.getWorld().dropItem(p.getLocation(), c4);

                    p.sendMessage(prefix + "Enjoy ur new C4 :)");


                } else {
                    sender.sendMessage(prefix + "You do not have permission to use this command!");
                }
            } else {
                sender.sendMessage(prefix + "You must be a player to use this command!");
            }
        }

        else if(cmd.getName().equalsIgnoreCase("givedetonator")) {
            if (sender instanceof Player p) {
                if (p.hasPermission("sun8ox.c4.give")) {

                    ItemStack detonator = new ItemStack(Material.TRIPWIRE_HOOK);
                    detonator.lore(List.of(Main.DetonatorLore));
                    detonator.setAmount(1);
                    ItemMeta detonatorMeta = detonator.getItemMeta();
                    detonatorMeta.displayName(Main.DetonatorName);
                    detonator.setItemMeta(detonatorMeta);

                    p.getWorld().dropItem(p.getLocation(), detonator);

                } else {
                    sender.sendMessage(prefix + "You do not have permission to use this command!");
                }
            } else {
                sender.sendMessage(prefix + "You must be a player to use this command!");
            }
        }

        else if(cmd.getName().equalsIgnoreCase("givecore")) {
            if (sender instanceof Player p) {
                if (p.hasPermission("sun8ox.c4.give")) {

                    ItemStack core = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta coreMeta = (SkullMeta) core.getItemMeta();
                    coreMeta.setPlayerProfile(Main.coreProfile);
                    coreMeta.displayName(Main.CoreName);
                    coreMeta.lore(List.of(Main.CoreLore));
                    core.setAmount(1);
                    core.setItemMeta(coreMeta);

                    p.getWorld().dropItem(p.getLocation(), core);

                } else {
                    sender.sendMessage(prefix + "You do not have permission to use this command!");
                }
            } else {
                sender.sendMessage(prefix + "You must be a player to use this command!");
            }
        }

        return super.onCommand(sender, cmd, label, args);

    }
}
