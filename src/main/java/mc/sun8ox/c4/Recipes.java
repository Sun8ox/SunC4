package mc.sun8ox.c4;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;

public class Recipes {


    public static Recipe coreRecipe() {

        ItemStack core = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta coreMeta = (SkullMeta) core.getItemMeta();
        coreMeta.setPlayerProfile(Main.coreProfile);
        coreMeta.displayName(Main.CoreName);
        coreMeta.lore(List.of(Main.CoreLore));

        core.setItemMeta(coreMeta);

        ShapedRecipe coreRecipe = new ShapedRecipe(Objects.requireNonNull(NamespacedKey.fromString("core", Main.plugin)), core);

        coreRecipe.shape("TIT", "IRI", "TIT");
        coreRecipe.setIngredient('I', Material.GUNPOWDER);
        coreRecipe.setIngredient('T', Material.TNT);
        coreRecipe.setIngredient('R', Material.REDSTONE_BLOCK);

        return coreRecipe;
    }

    public static Recipe c4Recipe() {

        // Dependency
        ItemStack core = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta coreMeta = (SkullMeta) core.getItemMeta();
        coreMeta.setPlayerProfile(Main.coreProfile);
        coreMeta.displayName(Main.CoreName);
        coreMeta.lore(List.of(Main.CoreLore));
        core.setItemMeta(coreMeta);

        // Result
        ItemStack c4 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta c4Meta = (SkullMeta) c4.getItemMeta();
        c4Meta.displayName(Main.C4Name);
        c4Meta.setPlayerProfile(Main.c4Profile);
        c4Meta.setUnbreakable(true);
        c4.setItemMeta(c4Meta);
        c4.lore(List.of(Main.C4Lore));

        ShapedRecipe c4Recipe = new ShapedRecipe(Objects.requireNonNull(NamespacedKey.fromString("c4", Main.plugin)), c4);

        c4Recipe.shape("TRT", "ECE", "EEE");
        c4Recipe.setIngredient('T', Material.REDSTONE_TORCH);
        c4Recipe.setIngredient('R', Material.GUNPOWDER);
        c4Recipe.setIngredient('E', Material.TNT);
        c4Recipe.setIngredient('C', core);


        return c4Recipe;
    }

    public static Recipe detonatorRecipe() {

        // Result
        ItemStack detonator = new ItemStack(Material.TRIPWIRE_HOOK);
        detonator.lore(List.of(Main.DetonatorLore));
        detonator.setAmount(1);
        ItemMeta detonatorMeta = detonator.getItemMeta();
        detonatorMeta.displayName(Main.DetonatorName);
        detonator.setItemMeta(detonatorMeta);

        ShapedRecipe detonatorRecipe = new ShapedRecipe(Objects.requireNonNull(NamespacedKey.fromString("detonator", Main.plugin)), detonator);

        detonatorRecipe.shape("*I*", "BCL", "*R*");
        detonatorRecipe.setIngredient('I', Material.IRON_INGOT);
        detonatorRecipe.setIngredient('B', Material.STONE_BUTTON);
        detonatorRecipe.setIngredient('L', Material.LEVER);
        detonatorRecipe.setIngredient('C', Material.GLOWSTONE);
        detonatorRecipe.setIngredient('R', Material.REDSTONE);

        return detonatorRecipe;
    }


}
