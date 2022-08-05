package me.fep310.creativeutils.gui;

import me.fep310.peticovapi.inventorygui.BackgroundFillType;
import me.fep310.peticovapi.inventorygui.InventoryGUI;
import me.fep310.peticovapi.util.PeticovUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class CustomGUITest extends InventoryGUI {

    public CustomGUITest() {
        super(InventoryType.CHEST, "Custom GUI Test");

        setBackground(
                PeticovUtil.itemStack(Material.WHITE_STAINED_GLASS_PANE, 1, ""),
                BackgroundFillType.FULL);
    }
}
