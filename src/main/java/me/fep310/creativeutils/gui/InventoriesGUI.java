package me.fep310.creativeutils.gui;

import me.fep310.creativeutils.CreativeUtils;
import me.fep310.creativeutils.datafile.InventoriesDataFile;
import me.fep310.creativeutils.outcome.LoadPlayerInventory;
import me.fep310.creativeutils.outcome.SavePlayerInventory;
import me.fep310.peticovapi.gui.PageBuilder;
import me.fep310.peticovapi.gui.PagedGUI;
import me.fep310.peticovapi.itemstackbuilder.ItemStackBuilder;
import me.fep310.peticovapi.outcome.OutcomeUtils;
import me.fep310.peticovapi.util.PeticovUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class InventoriesGUI extends PagedGUI {

    public InventoriesGUI() {
        super(3);

        InventoriesDataFile inventoriesDataFile = CreativeUtils.getInstance().getInventoriesDataFile();

        ItemStack backgroundItem = new ItemStackBuilder()
                .setMaterial(Material.WHITE_STAINED_GLASS_PANE)
                .setAmount(1)
                .setName("")
                .get();

        ItemStack saveItem = new ItemStackBuilder()
                .setName("&a&lSave")
                .setMaterial(Material.WRITABLE_BOOK)
                .addLore("&7Click to save your current inventory!")
                .get();

        ItemStack publicListItem = new ItemStackBuilder()
                .setName("&e&lPublic Inventories")
                .setMaterial(Material.CHEST)
                .addLore("&7Click to see all publicly saved inventories!")
                .get();

        ItemStack privateListItem = new ItemStackBuilder()
                .setName("&6&lPrivate Inventories")
                .setMaterial(Material.ENDER_CHEST)
                .addLore("&7Click to see your saved inventories!")
                .get();

        // page 0

        PageBuilder pageBuilder0 = new PageBuilder()
                .setTitle(PeticovUtil.text("Save / Public List / Private List"))
                .setSize(InventoryType.CHEST)
                .setBackground(backgroundItem)

                .setClickableItem(saveItem, 11, (player, leftClick, clickType) ->
                        new SavePlayerInventory(true).onOutcome(player, leftClick, clickType))

                .setClickableItem(publicListItem, 13, (player, leftClick, clickType) ->
                        open(1, player))

                .setClickableItem(privateListItem, 15, (player, leftClick, clickType) ->
                        open(0/*TODO*/, player));

        addPage(0, pageBuilder0.get());

        // page 1

        PageBuilder pageBuilder1 = new PageBuilder()
                .setTitle(PeticovUtil.text("Public List"))
                .setSize(InventoryType.CHEST)
                .setBackground(backgroundItem);

        List<String> publicNames = inventoriesDataFile.getPublicInventoryNames();

        for (int i = 0; i < publicNames.size(); i++) {

            String name = publicNames.get(i);

            Map<Integer, ItemStack> inventoryMap = inventoriesDataFile.loadInventory(name, null, true);
            if (inventoryMap == null || inventoryMap.isEmpty())
                break;

            Material iconMaterial = inventoryMap.values().stream().findFirst().get().getType();
            ItemStack icon = new ItemStackBuilder()
                    .setMaterial(iconMaterial)
                    .setName(name)
                    .get();

            pageBuilder1.setClickableItem(icon, i, (player, leftClick, clickType) -> {
                LoadPlayerInventory.copyToInventory(inventoryMap, player.getInventory());
                OutcomeUtils.success(player, "The inventory \""+name+"\" was successfully loaded.");
            });
        }

        addPage(1, pageBuilder1.get());

        // page 2 (not implemented yet)

        // addPage(2, pageBuilder1.get());
    }
}
