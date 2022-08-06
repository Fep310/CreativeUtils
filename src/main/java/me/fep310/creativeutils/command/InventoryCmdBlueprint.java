package me.fep310.creativeutils.command;

import me.fep310.creativeutils.CreativeUtils;
import me.fep310.creativeutils.datafile.InventoriesDataFile;
import me.fep310.creativeutils.outcome.LoadPlayerInventory;
import me.fep310.creativeutils.outcome.SavePlayerInventory;
import me.fep310.creativeutils.gui.CustomGUITest;
import me.fep310.creativeutils.values.StringValues;
import me.fep310.peticovapi.commandblueprint.CommandBlueprint;
import me.fep310.peticovapi.commandblueprint.HelpType;
import me.fep310.peticovapi.outcome.OutcomeUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InventoryCmdBlueprint extends CommandBlueprint {

    /*
        /inventory save <name>
        /inventory save public <name>

        /inventory load <name>
        /inventory load public <name>

        TODO ->

            * List available inventories to load (public or not) ->
                On PeticovAPI: .addPossibleCompletions() on ArgOptionString

            * /inventory list
            * /inventory list public

            * /inventory delete
            * /inventory delete public
    */

    public InventoryCmdBlueprint(@NotNull String name) {
        super(name);

        setRightUsage("/inventory <save/load> <public/name> [name]");
        setPermission(StringValues.PERMISSION_BEGINNING+"inventory");
        setPlayerExclusive(true);
        setHelpType(HelpType.AS_ARGUMENT);

        setFirstOutcome((sender, args) -> {
            Player player = (Player) sender;
            new CustomGUITest().open(player);
            OutcomeUtils.success(player, null);
        });

        InventoriesDataFile inventoriesDataFile = CreativeUtils.getInstance().getInventoriesDataFile();

        String[] savePrev = new String[1];
        savePrev[0] = "save";

        String[] savePublicPrev = new String[2];
        savePublicPrev[0] = "save";
        savePublicPrev[1] = "public";

        String[] loadPrev = new String[1];
        loadPrev[0] = "load";

        String[] loadPublicPrev = new String[2];
        loadPublicPrev[0] = "load";
        loadPublicPrev[1] = "public";

        // inventory save <name>

        addArgOptionLabel(
                0, "save",
                (sender, args) -> OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to save.",
                        getRightUsage()))
                .setPermission(getPermission())
                .setDescription("Save your current inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                1,
                (sender, args) -> new SavePlayerInventory(false).onOutcome(sender, args))
                .setPrevArgs(savePrev)
                .setPermission(getPermission());

        // inventory save public <name>

        addArgOptionLabel(
                1, "public",
                (sender, args) -> OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to save.",
                        getRightUsage()))
                .setPrevArgs(savePrev)
                .setPermission(getPermission())
                .setDescription("Save your current inventory publicly.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                2,
                (sender, args) -> new SavePlayerInventory(true).onOutcome(sender, args))
                .setPrevArgs(savePublicPrev)
                .setPermission(getPermission());

        // inventory load <name>

        addArgOptionLabel(
                0, "load",
                (sender, args) ->
                        OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to load.",
                        getRightUsage()))
                .setPermission(getPermission())
                .setDescription("Load a saved inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                1,
                (sender, args) -> new LoadPlayerInventory(false).onOutcome(sender, args))
                .addPossibleCompletions((sender) -> inventoriesDataFile.getPrivateInventoryNames((Player) sender))
                .setPrevArgs(loadPrev)
                .setPermission(getPermission());

        // inventory load public <name>

        addArgOptionLabel(
                1, "public",
                (sender, args) ->
                        OutcomeUtils.showRightUsage(sender,
                                "Please specify a name for the inventory you're trying to load.",
                                getRightUsage()))
                .setPrevArgs(loadPrev)
                .setPermission(getPermission())
                .setDescription("Load a public inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                2,
                (sender, args) -> new LoadPlayerInventory(true).onOutcome(sender, args))
                .addPossibleCompletions(inventoriesDataFile.getPublicInventoryNames())
                .setPrevArgs(loadPublicPrev)
                .setPermission(getPermission());
    }
}
