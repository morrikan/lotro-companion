package delta.games.lotro.gui.character.tomes;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.tomes.TomesSet;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a panel to edit tomes.
 * @author DAM
 */
public class TomesEditionPanelController
{
  // Data
  private CharacterData _toon;
  // UI
  private List<TomeIconController> _tomeControllers;
  private JPanel _panel;
  private JPanel _iconsPanel;

  /**
   * Constructor.
   * @param character Targeted character.
   */
  public TomesEditionPanelController(CharacterData character)
  {
    _toon=character;
    _tomeControllers=new ArrayList<TomeIconController>();
    build();
    updateIconsPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void build()
  {
    _panel=GuiFactory.buildPanel(new FlowLayout());
    _iconsPanel=GuiFactory.buildBackgroundPanel(new FlowLayout(FlowLayout.LEFT));
    buildTomesControllers(_panel);
    _panel.add(_iconsPanel);
  }

  private void updateIconsPanel()
  {
    _iconsPanel.removeAll();
    for(TomeIconController controller : _tomeControllers)
    {
      JLabel label=controller.getLabel();
      _iconsPanel.add(label);
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildTomesControllers(JPanel panel)
  {
    TomesSet tomes=_toon.getTomes();
    for(STAT stat : TomesSet.AVAILABLE_TOMES)
    {
      TomeIconController controller=buildTomeController(tomes,stat);
      _tomeControllers.add(controller);
    }
  }

  private TomeIconController buildTomeController(TomesSet tomes, STAT stat)
  {
    Font font=_iconsPanel.getFont();
    TomeIconController controller=new TomeIconController(tomes,stat,font);
    JLabel label=controller.getLabel();
    MouseListener listener=buildLeftClickListener();
    label.addMouseListener(listener);
    return controller;
  }

  private MouseListener buildLeftClickListener()
  {
    class LeftClickListener extends MouseAdapter
    {
      public void mouseReleased(MouseEvent e)
      {
        if (e.getButton()==MouseEvent.BUTTON1)
        {
          updateTier(e);
        }
      }
    }
    return new LeftClickListener();
  }

  private void updateTier(MouseEvent e)
  {
    // Straight click
    Object invoker=e.getSource();
    int index=getIndex(invoker);
    if (index!=-1)
    {
      // Update tier
      updateTier(index);
    }
  }

  private int getIndex(Object invoker)
  {
    int index=0;
    for(TomeIconController controller : _tomeControllers)
    {
      JLabel label=controller.getLabel();
      if (label==invoker)
      {
        return index;
      }
      index++;
    }
    return -1;
  }

  private void updateTier(int index)
  {
    TomesSet tomes=_toon.getTomes();
    STAT stat=TomesSet.AVAILABLE_TOMES[index];
    int currentTierIndex=tomes.getTomeRank(stat);
    currentTierIndex++;
    if (currentTierIndex>TomesSet.MAX_RANK)
    {
      currentTierIndex=0;
    }
    tomes.setTomeRank(stat,currentTierIndex);
    _tomeControllers.get(index).update();
    // Broadcast toon update event...
    CharacterEvent event=new CharacterEvent(null,_toon);
    CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
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
    if (_iconsPanel!=null)
    {
      _iconsPanel.removeAll();
      _iconsPanel=null;
    }
    _tomeControllers.clear();
    _toon=null;
  }
}