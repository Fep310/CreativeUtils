package me.fep310.creativeutils.outcome;

import me.fep310.creativeutils.CreativeUtils;
import me.fep310.peticovapi.outcome.CommandOutcome;
import me.fep310.peticovapi.outcome.OutcomeUtils;
import me.fep310.peticovapi.util.PeticovUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SavePlayerInventory implements CommandOutcome {

    private final boolean isPublic;

    public SavePlayerInventory(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public void onOutcome(CommandSender sender, @Nullable String[] args) {

        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();

        if (inventory.isEmpty()) {
            OutcomeUtils.fail(sender, "Your inventory is empty. There is nothing to save.");
            return;
        }

        if (args == null || args[isPublic ? 2 : 1] == null)
            return;

        String saveName = args[isPublic ? 2 : 1].toLowerCase(Locale.ROOT);

        CreativeUtils.getInstance().getInventoriesDataFile().saveInventory(saveName, player, isPublic);

        OutcomeUtils.success(sender, "You successfully saved your inventory.");
        PeticovUtil.sendMessage(sender,
                isPublic ?
                        "&eTo access it again, use /inventory load public "+saveName+"." :
                        "&eTo access it again, use /inventory load "+saveName+".");
    }
}
