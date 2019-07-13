package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;

/**
 * Controller for the UI gadgets of a class points reward.
 * @author DAM
 */
public class ClassPointRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param count Class points count.
   */
  public ClassPointRewardGadgetsController(int count)
  {
    // Label
    String text="Class Point";
    if (count>1)
    {
      text=String.valueOf(count)+" Class Points";
    }
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon lpIcon=IconsManager.getIcon("/resources/gui/icons/ClassPoint.png");
    _labelIcon=GuiFactory.buildIconLabel(lpIcon);
  }
}
