package delta.games.lotro.gui.items.legendary.imbued;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.LegaciesManager;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller for a "imbued legacy choice" dialog.
 * @author DAM
 */
public class ImbuedLegacyChooser
{
  private static ObjectChoiceWindowController<ImbuedLegacy> buildChooser(WindowController parent, List<ImbuedLegacy> legacies, ImbuedLegacy selectedLegacy)
  {
    // Table
    GenericTableController<ImbuedLegacy> table=ImbuedLegaciesTableBuilder.buildTable(legacies);
    // Filter
    // ... none ...

    // Build and configure chooser
    ObjectChoiceWindowController<ImbuedLegacy> chooser=new ObjectChoiceWindowController<ImbuedLegacy>(parent,null,table);
    table.getTable();
    // - selection
    table.selectItem(selectedLegacy);
    // - filter
    // ... none ...
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose imbued legacy: ");
    // - dimension
    dialog.setMinimumSize(new Dimension(600,200));
    dialog.setSize(500,dialog.getHeight());
    return chooser;
  }

  /**
   * Show the imbued legacy selection dialog.
   * @param parent Parent controller.
   * @param characterClass Character class to use.
   * @param slot Slot to use.
   * @param selectedLegacy Selected legacy.
   * @return The selected imbued legacy or <code>null</code> if the window was closed or canceled.
   */
  public static ImbuedLegacy selectImbuedLegacy(WindowController parent, CharacterClass characterClass, EquipmentLocation slot, ImbuedLegacy selectedLegacy)
  {
    LegaciesManager legaciesMgr=LegaciesManager.getInstance();
    List<ImbuedLegacy> legacies=legaciesMgr.get(characterClass,slot);
    // Build chooser
    ObjectChoiceWindowController<ImbuedLegacy> chooser=buildChooser(parent,legacies,selectedLegacy);
    // Show modal
    ImbuedLegacy chosenLegacy=chooser.editModal();
    // Dispose...
    chooser.dispose();
    return chosenLegacy;
  }
}