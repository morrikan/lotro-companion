package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Controller for the UI gadgets of a emote reward.
 * @author DAM
 */
public class EmoteRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param emote Emote.
   */
  public EmoteRewardGadgetsController(EmoteDescription emote)
  {
    // Label
    String text=emote.getCommand();
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    int id=emote.getIconId();
    Icon icon=LotroIconsManager.getEmoteIcon(id+".png");
    _icon=GuiFactory.buildIconLabel(icon);
  }
}
