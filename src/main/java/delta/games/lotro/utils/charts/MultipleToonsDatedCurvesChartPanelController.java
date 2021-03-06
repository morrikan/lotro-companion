package delta.games.lotro.utils.charts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.chooser.CharacterSelectionChangedListener;
import delta.games.lotro.gui.character.chooser.CharactersSelectorPanelController;
import delta.games.lotro.gui.character.chooser.CharactersSelectorWindowController;
import delta.games.lotro.gui.stats.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.gui.stats.curves.DatedCurveProvider;
import delta.games.lotro.gui.stats.curves.DatedCurvesChartController;
import delta.games.lotro.gui.stats.curves.MultipleToonsDatedCurvesProvider;
import delta.games.lotro.stats.MultipleToonsStats;

/**
 * Controller for a panel to show a chart with curves for a series of characters.
 * @param <T> Type of managed stats.
 * @author DAM
 */
public class MultipleToonsDatedCurvesChartPanelController<T> implements CharacterSelectionChangedListener
{
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parentController;
  private DatedCurvesChartController _chartController;
  private CharactersSelectorPanelController _toonSelectionController;
  // Data
  private MultipleToonsStats<T> _stats;

  /**
   * Constructor.
   * @param parentController Parent window controller.
   * @param stats Stats to display.
   * @param curveProvider Curve provider.
   * @param configuration Chart configuration.
   */
  public MultipleToonsDatedCurvesChartPanelController(WindowController parentController, MultipleToonsStats<T> stats, DatedCurveProvider<T> curveProvider, DatedCurvesChartConfiguration configuration)
  {
    _parentController=parentController;
    _stats=stats;
    MultipleToonsDatedCurvesProvider<T> provider=new MultipleToonsDatedCurvesProvider<T>(stats,curveProvider);
    _chartController=new DatedCurvesChartController(provider,configuration);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());

    JPanel chartPanel=_chartController.getPanel();
    panel.add(chartPanel,BorderLayout.CENTER);

    JPanel toonsControlPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      // Toons show/hide
      List<CharacterFile> toons=_stats.getToonsList();
      _toonSelectionController=new CharactersSelectorPanelController(toons);
      for(CharacterFile toon : toons)
      {
        _toonSelectionController.setToonSelected(toon,true);
        _toonSelectionController.setToonEnabled(toon,true);
      }

      _toonSelectionController.setGridConfiguration(1,10);
      JPanel selectionPanel=_toonSelectionController.getPanel();
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      toonsControlPanel.add(selectionPanel,c);
      _toonSelectionController.getListenersManager().addListener(this);

      // Choose toons button
      JButton chooser=GuiFactory.buildButton("Choose characters...");
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          doChooseToons();
        }
      };
      chooser.addActionListener(al);
      chooser.setAlignmentY(Component.CENTER_ALIGNMENT);
      c.gridy++;
      toonsControlPanel.add(chooser,c);
    }
    panel.add(toonsControlPanel,BorderLayout.EAST);
    return panel;
  }

  private void doChooseToons()
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> selectedToons=_stats.getToonsList();
    List<CharacterFile> enabledToons=new ArrayList<CharacterFile>();
    for(CharacterFile toon : toons)
    {
      //if (toon.hasLog())
      {
        enabledToons.add(toon);
      }
    }
    List<CharacterFile> newSelectedToons=CharactersSelectorWindowController.selectToons(_parentController,toons,selectedToons,enabledToons);
    if (newSelectedToons!=null)
    {
      for(CharacterFile toon : newSelectedToons)
      {
        if (selectedToons.contains(toon))
        {
          selectedToons.remove(toon);
        }
        else
        {
          _stats.addToon(toon);
          _toonSelectionController.addToon(toon,true);
          _toonSelectionController.setToonEnabled(toon,true);
        }
      }
      for(CharacterFile removedToon : selectedToons)
      {
        _stats.removeToon(removedToon);
        _toonSelectionController.removeToon(removedToon);
      }
      _toonSelectionController.refresh();
      _chartController.refresh();
    }
  }

  /**
   * Called when the selection of characters has changed.
   * @param toonId Targeted character identifier.
   * @param selected New state for this character (visible if selected, hidden otherwise).
   */
  @Override
  public void selectionChanged(String toonId, boolean selected)
  {
    if (_chartController!=null)
    {
      _chartController.setVisible(toonId,selected);
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
    _parentController=null;
    if (_chartController!=null)
    {
      _chartController.dispose();
      _chartController=null;
    }
    if (_toonSelectionController!=null)
    {
      _toonSelectionController.dispose();
      _toonSelectionController=null;
    }
    _stats=null;
  }
}
