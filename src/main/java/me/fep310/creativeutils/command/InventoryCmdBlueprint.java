package me.fep310.creativeutils.command;

import me.fep310.creativeutils.CreativeUtils;
import me.fep310.creativeutils.datafile.InventoriesDataFile;
import me.fep310.creativeutils.outcome.LoadPlayerInventory;
import me.fep310.creativeutils.outcome.SavePlayerInventory;
import me.fep310.creativeutils.gui.InventoriesGUI;
import me.fep310.creativeutils.values.StringValues;
import me.fep310.peticovapi.commandblueprint.CommandBlueprint;
import me.fep310.peticovapi.commandblueprint.HelpType;
import me.fep310.peticovapi.outcome.OutcomeUtils;
import me.fep310.peticovapi.util.PeticovUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class InventoryCmdBlueprint extends CommandBlueprint {

    /*
        /inventory save <name>
        /inventory save public <name>

        /inventory load <name>
        /inventory load public <name>

        /inventory list
        /inventory list public

        /inventory delete <name>
        /inventory delete public <name>
    */

    public InventoryCmdBlueprint(@NotNull String name) {
        super(name);

        String INVENTORY_PERM = StringValues.PERMISSION_BEGINNING+"inventory";

        String INVENTORY_SAVE_PERM = StringValues.PERMISSION_BEGINNING+"inventory.save";
        String INVENTORY_SAVE_PUBLIC_PERM = StringValues.PERMISSION_BEGINNING+"inventory.save_public";

        String INVENTORY_LOAD_PERM = StringValues.PERMISSION_BEGINNING+"inventory.load";
        String INVENTORY_LOAD_PUBLIC_PERM = StringValues.PERMISSION_BEGINNING+"inventory.load_public";

        String INVENTORY_LIST_PERM = StringValues.PERMISSION_BEGINNING+"inventory.list";
        String INVENTORY_LIST_PUBLIC_PERM = StringValues.PERMISSION_BEGINNING+"inventory.list_public";

        String INVENTORY_DELETE_PERM = StringValues.PERMISSION_BEGINNING+"inventory.delete";
        String INVENTORY_DELETE_PUBLIC_PERM = StringValues.PERMISSION_BEGINNING+"inventory.delete_public";

        setRightUsage("/inventory <save/load/delete/list> <public/name> [name]");
        setPermission(INVENTORY_PERM);
        setPlayerExclusive(true);
        setHelpType(HelpType.AS_ARGUMENT);

        setFirstOutcome((sender, args) -> {
            Player player = (Player) sender;
            new InventoriesGUI().open(0, player);
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

        String[] listPrev = new String[1];
        listPrev[0] = "list";

        String[] deletePrev = new String[1];
        deletePrev[0] = "delete";

        String[] deletePublicPrev = new String[2];
        deletePublicPrev[0] = "delete";
        deletePublicPrev[1] = "public";

        // inventory save <name>

        addArgOptionLabel(
                0, "save",
                (sender, args) -> OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to save.",
                        getRightUsage()))
                .setPermission(INVENTORY_SAVE_PERM)
                .setDescription("Save your current inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                1,
                (sender, args) -> new SavePlayerInventory(false).onOutcome(sender, args))
                .setPrevArgs(savePrev)
                .setPermission(INVENTORY_SAVE_PERM);

        // inventory save public <name>

        addArgOptionLabel(
                1, "public",
                (sender, args) -> OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to save.",
                        getRightUsage()))
                .setPrevArgs(savePrev)
                .setPermission(INVENTORY_SAVE_PUBLIC_PERM)
                .setDescription("Save your current inventory publicly.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                2,
                (sender, args) -> new SavePlayerInventory(true).onOutcome(sender, args))
                .setPrevArgs(savePublicPrev)
                .setPermission(INVENTORY_SAVE_PUBLIC_PERM);

        // inventory load <name>

        addArgOptionLabel(
                0, "load",
                (sender, args) ->
                        OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to load.",
                        getRightUsage()))
                .setPermission(INVENTORY_LOAD_PERM)
                .setDescription("Load a saved inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                1,
                (sender, args) -> new LoadPlayerInventory(false).onOutcome(sender, args))
                .addPossibleCompletions((sender) -> inventoriesDataFile.getPrivateInventoryNames((Player) sender))
                .setPrevArgs(loadPrev)
                .setPermission(INVENTORY_LOAD_PERM);

        // inventory load public <name>

        addArgOptionLabel(
                1, "public",
                (sender, args) ->
                        OutcomeUtils.showRightUsage(sender,
                                "Please specify a name for the inventory you're trying to load.",
                                getRightUsage()))
                .setPrevArgs(loadPrev)
                .setPermission(INVENTORY_LOAD_PUBLIC_PERM)
                .setDescription("Load a public inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(
                2,
                (sender, args) -> new LoadPlayerInventory(true).onOutcome(sender, args))
                .addPossibleCompletions(inventoriesDataFile.getPublicInventoryNames())
                .setPrevArgs(loadPublicPrev)
                .setPermission(INVENTORY_LOAD_PUBLIC_PERM);

        // inventory list

        addArgOptionLabel(
                0, "list",
                (sender, args) -> {

                    List<String> inventories = inventoriesDataFile.getPrivateInventoryNames((Player) sender);
                    String joinedString = String.join(", ", inventories + ".");

                    OutcomeUtils.success(sender, null);
                    PeticovUtil.sendMessage(sender, "&6Your saved inventories:");
                    PeticovUtil.sendMessage(sender,"&e"+joinedString);

                })
                .setPermission(INVENTORY_LIST_PERM)
                .setDescription("Lists all your private inventories.");

        // inventory list public

        addArgOptionLabel(
                1, "public",
                (sender, args) -> {

                    List<String> inventories = inventoriesDataFile.getPublicInventoryNames();
                    String joinedString = String.join(", ", inventories + ".");

                    OutcomeUtils.success(sender, null);
                    PeticovUtil.sendMessage(sender, "&6All publicly saved inventories:");
                    PeticovUtil.sendMessage(sender,"&e"+joinedString);

                })
                .setPrevArgs(listPrev)
                .setPermission(INVENTORY_LIST_PUBLIC_PERM)
                .setDescription("Lists all publicly saved inventories.");

        // inventory delete

        addArgOptionLabel(
                0, "delete",
                (sender, args) -> OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to delete.",
                        getRightUsage()))
                .setPermission(INVENTORY_DELETE_PERM)
                .setDescription("Delete a private inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(1, (sender, args) -> {
            if (args[1] == null)
                return;
            String inventoryName = args[1].toLowerCase(Locale.ROOT);
            inventoriesDataFile.deletePrivateInventory((Player) sender, inventoryName);
            OutcomeUtils.success(sender, inventoryName+" inventory should be deleted.");
        })
                .addPossibleCompletions(sender -> inventoriesDataFile.getPrivateInventoryNames((Player) sender))
                .setPrevArgs(deletePrev)
                .setPermission(INVENTORY_DELETE_PERM);

        // inventory delete public

        addArgOptionLabel(
                1, "public",
                (sender, args) -> OutcomeUtils.showRightUsage(sender,
                        "Please specify a name for the inventory you're trying to delete.",
                        getRightUsage()))
                .setPrevArgs(deletePrev)
                .setPermission(INVENTORY_DELETE_PUBLIC_PERM)
                .setDescription("Delete a public inventory.")
                .setNextPossibleArgs("inventory_name");

        addArgOptionString(2, (sender, args) -> {
            if (args[2] == null)
                return;
            String inventoryName = args[2].toLowerCase(Locale.ROOT);
            inventoriesDataFile.deletePublicInventory(inventoryName);
            OutcomeUtils.success(sender, inventoryName+" inventory should be deleted.");
        })
                .addPossibleCompletions(inventoriesDataFile.getPublicInventoryNames())
                .setPrevArgs(deletePublicPrev)
                .setPermission(INVENTORY_DELETE_PUBLIC_PERM);
    }
}
