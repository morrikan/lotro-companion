package delta.games.lotro.gui.items.legendary.non_imbued;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;

/**
 * Simple test class for the non-imbued legendary attributes edition panel.
 * @author DAM
 */
public class MainTestNonImbuedLegendaryAttrsEdition
{
  private void doIt()
  {
    ItemInstance<? extends Item> itemInstance=LegendariesTestUtils.loadItemInstance("CaptainEmblemSecondAge75NonImbued.xml");
    LegendaryInstanceAttrs attrs=LegendariesTestUtils.getLegendaryAttrs(itemInstance);
    final NonImbuedLegendaryInstanceAttrs nonImbuedLegAttrs=attrs.getNonImbuedAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.CLASS_SLOT);
    final NonImbuedLegendaryAttrsEditionPanelController controller=new NonImbuedLegendaryAttrsEditionPanelController(null,nonImbuedLegAttrs,constraints);
    int itemLevel=itemInstance.getEffectiveItemLevel().intValue();
    Item item=itemInstance.getReference();
    controller.setReferenceData(itemLevel,item);

    DefaultFormDialogController<NonImbuedLegendaryInstanceAttrs> dialog=new DefaultFormDialogController<NonImbuedLegendaryInstanceAttrs>(null,nonImbuedLegAttrs)
    {
      @Override
      protected JPanel buildFormPanel()
      {
        return controller.getPanel();
      }

      @Override
      protected void okImpl()
      {
        super.okImpl();
        controller.getData(nonImbuedLegAttrs);
      }
    };
    NonImbuedLegendaryInstanceAttrs result=dialog.editModal();
    System.out.println("Result: "+result);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestNonImbuedLegendaryAttrsEdition().doIt();
  }
}
