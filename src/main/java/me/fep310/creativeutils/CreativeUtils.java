package me.fep310.creativeutils;

import me.fep310.creativeutils.command.InventoryCmdBlueprint;
import me.fep310.creativeutils.datafile.InventoriesDataFile;
import me.fep310.peticovapi.util.Setup;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreativeUtils extends JavaPlugin {

    private static CreativeUtils instance;
    public static CreativeUtils getInstance() { return instance; }

    private InventoriesDataFile inventoriesDataFile;
    public InventoriesDataFile getInventoriesDataFile() { return inventoriesDataFile; }

    // TODO: /inventory undo ->
    //      Every time a player loads an inventory, the previous inventory should be saved so that they can get it back
    //      if the the load was an accident.

    @Override
    public void onEnable() {

        instance = this;

        inventoriesDataFile = new InventoriesDataFile(this, "inventories.yml");

        Setup.aCommand(new InventoryCmdBlueprint("inventory"), this, true);
    }

    @Override
    public void onDisable() {
    }
}
