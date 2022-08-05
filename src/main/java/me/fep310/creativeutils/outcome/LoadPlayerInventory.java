package me.fep310.creativeutils.outcome;

import me.fep310.creativeutils.CreativeUtils;
import me.fep310.creativeutils.datafile.InventoriesDataFile;
import me.fep310.peticovapi.outcome.CommandOutcome;
import me.fep310.peticovapi.outcome.OutcomeUtils;
import me.fep310.peticovapi.outcome.PlayerOutcome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class LoadPlayerInventory implements CommandOutcome, PlayerOutcome {

    private final boolean isPublic;
    private String inventoryName;

    public LoadPlayerInventory(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public LoadPlayerInventory(boolean isPublic, String inventoryName) {
        this.isPublic = isPublic;
        this.inventoryName = inventoryName;
    }

    @Override
    public void onOutcome(CommandSender sender, @Nullable String[] args) {

        String loadName = args[isPublic ? 2 : 1].toLowerCase(Locale.ROOT);

        InventoriesDataFile invDataFile = CreativeUtils.getInstance().getInventoriesDataFile();

        Map<Integer, ItemStack> loadedInventory = invDataFile.loadInventory(loadName, (Player) sender, isPublic);

        if (loadedInventory == null) {
            OutcomeUtils.fail(sender, "The inventory \""+loadName+"\" couldn't be loaded.");
            return;
        }

        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        for (Map.Entry<Integer, ItemStack> entry : loadedInventory.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }

        OutcomeUtils.success(sender, "The inventory \""+loadName+"\" was successfully loaded.");
    }

    @Override
    public void onOutcome(Player player) {

        InventoriesDataFile invDataFile = CreativeUtils.getInstance().getInventoriesDataFile();

        Map<Integer, ItemStack> loadedInventory = invDataFile.loadInventory(inventoryName, player, isPublic);

        if (loadedInventory == null) {
            OutcomeUtils.fail(player, "The inventory \""+inventoryName+"\" couldn't be loaded.");
            return;
        }

        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        for (Map.Entry<Integer, ItemStack> entry : loadedInventory.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }

        OutcomeUtils.success(player, "The inventory \""+inventoryName+"\" was successfully loaded.");
    }
}
