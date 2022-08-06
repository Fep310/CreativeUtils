package me.fep310.creativeutils.datafile;

import me.fep310.peticovapi.datafile.PluginDataFile;
import me.fep310.peticovapi.util.PeticovUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.*;

public class InventoriesDataFile extends PluginDataFile {

    public InventoriesDataFile(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    @Override
    protected void setupDefaults() {

        configFile.addDefault("public", null);
        configFile.addDefault("private", null);

        super.setupDefaults();
    }

    public void saveInventory(String name, Player player, boolean isPublic) {

        Inventory inventory = player.getInventory();
        Map<Integer, ItemStack> savedInventory = new HashMap<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack currentItem = inventory.getItem(i);
            if (currentItem == null)
                continue;
            if (currentItem.getType() == Material.AIR)
                continue;
            savedInventory.put(i, currentItem);
        }

        String pathPrefix = isPublic ?
                "public." + name +"." :
                "private." + player.getUniqueId() +"."+ name +".";

        configFile.set(pathPrefix+"amount", savedInventory.size());

        for (Map.Entry<Integer, ItemStack> entry : savedInventory.entrySet()) {
            configFile.set(pathPrefix+"slots."+entry.getKey(), entry.getValue());
        }

        save();
    }

    public @Nullable Map<Integer, ItemStack> loadInventory(String name, Player player, boolean isPublic) {

        reload();

        String path = isPublic ?
                "public." + name +"." :
                "private." + player.getUniqueId() +"."+ name +".";

        int stacksAmount = configFile.getInt(path+"amount");

        if (stacksAmount == 0)
            return null;

        Map<Integer, ItemStack> stacksMap = new HashMap<>();
        ConfigurationSection slotsSection = configFile.getConfigurationSection(path+"slots");

        if (slotsSection == null)
            return null;

        Set<String> stacksKeys = slotsSection.getKeys(true);

        for (String key : stacksKeys) {
            Integer intKey = PeticovUtil.stringToInteger(key);
            if (intKey == null)
                continue;
            stacksMap.put(intKey, configFile.getItemStack(path+"slots."+key));
        }

        return stacksMap;
    }

    public List<String> getPublicInventoryNames() {

        String publicPath = "public.";

        ConfigurationSection inventoriesSection = configFile.getConfigurationSection(publicPath);

        if (inventoriesSection == null)
            return List.of();

        return inventoriesSection.getKeys(false).stream().toList();
    }

    public List<String> getPrivateInventoryNames(Player player) {

        if (player == null)
            return List.of();

        String path = "private."+player.getUniqueId()+".";

        ConfigurationSection inventoriesSection = configFile.getConfigurationSection(path);

        if (inventoriesSection == null)
            return List.of();

        return inventoriesSection.getKeys(false).stream().toList();
    }
}
