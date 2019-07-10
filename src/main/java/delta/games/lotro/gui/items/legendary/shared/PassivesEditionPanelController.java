package delta.games.lotro.gui.items.legendary.shared;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.lore.items.legendary.LegendaryAttrs;

/**
 * Panel to edit passives.
 * @author DAM
 */
public class PassivesEditionPanelController
{
  private static final int NB_PASSIVES_MAX=3;

  // GUI
  private JPanel _panel;
  private List<SinglePassiveEditionController> _passiveGadgets;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param legendaryAttrs Attributes to edit.
   * @param level Item level.
   */
  public PassivesEditionPanelController(WindowController parent, LegendaryAttrs legendaryAttrs, int level)
  {
    _passiveGadgets=new ArrayList<SinglePassiveEditionController>();
    List<Effect> passives=legendaryAttrs.getPassives();
    int nbPassives=passives.size();
    for(int i=0;i<NB_PASSIVES_MAX;i++)
    {
      Effect passive=(i<nbPassives)?passives.get(i):null;
      SinglePassiveEditionController controller=new SinglePassiveEditionController(parent,passive,level);
      _passiveGadgets.add(controller);
    }
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    for(SinglePassiveEditionController controller : _passiveGadgets)
    {
      // Label
      JLabel label=controller.getValueLabel();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(label,c);
      // Choose button
      JButton chooser=controller.getChooseButton();
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(chooser,c);
      // Delete button
      JButton deleteButton=controller.getDeleteButton();
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(deleteButton,c);
    }
    return panel;
  }

  /**
   * Get the contents of the edited data into the given storage.
   * @param legendaryAttrs Storage for data.
   */
  public void getData(LegendaryAttrs legendaryAttrs)
  {
    List<Effect> passives=legendaryAttrs.getPassives();
    passives.clear();
    for(SinglePassiveEditionController controller : _passiveGadgets)
    {
      Effect passive=controller.getPassive();
      passives.add(passive);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_passiveGadgets!=null)
    {
      for(SinglePassiveEditionController controller : _passiveGadgets)
      {
        controller.dispose();
      }
      _passiveGadgets.clear();
    }
  }
}
