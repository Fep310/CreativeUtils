package me.fep310.creativeutils.outcome;

import me.fep310.peticovapi.outcome.PlayerClickInventoryOutcome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ClickOnInventory implements PlayerClickInventoryOutcome {

    private final boolean isPublic;
    private final String name;

    public ClickOnInventory(boolean isPublic, String name) {
        this.isPublic = isPublic;
        this.name = name;
    }

    @Override
    public void onOutcome(Player player, boolean b, ClickType clickType) {

    }
}
