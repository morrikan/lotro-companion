package delta.games.lotro.gui.stats.warbands;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.Config;
import delta.games.lotro.Preferences;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;
import delta.games.lotro.utils.TypedProperties;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "warbands statistics" window.
 * @author DAM
 */
public class WarbandsWindowController extends DefaultWindowController
{
  private static final String WARBANDS_PREFERENCES_NAME="warbands";
  private static final String TOON_NAME_PREFERENCE="warbands.registered.toon";

  private WarbandsPanelController _warbandsStatisticsPanelController;
  private MultipleToonsWarbandsStats _stats;

  /**
   * Constructor.
   * @param stats Underlying warbands statistics.
   */
  public WarbandsWindowController(MultipleToonsWarbandsStats stats)
  {
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(WARBANDS_PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(TOON_NAME_PREFERENCE);
    CharactersManager manager=CharactersManager.getInstance();
    if ((toonIds!=null) && (toonIds.size()>0))
    {
      for(String toonID : toonIds)
      {
        CharacterFile toon=manager.getToonById(toonID);
        if (toon!=null)
        {
          stats.addToon(toon);
        }
      }
    }
    _stats=stats;
    _warbandsStatisticsPanelController=new WarbandsPanelController(this,stats);
  }

  /**
   * Get the window identifier for a given toon.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "WARBANDS";
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_warbandsStatisticsPanelController.getPanel();
    return panel;
  }
  
  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String title="Warbands statistics";
    frame.setTitle(title);
    frame.pack();
    //frame.setResizable(false);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    CharacterFile[] toons=_stats.getToons();
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(WARBANDS_PREFERENCES_NAME);
    List<String> toonIds=new ArrayList<String>();
    for(CharacterFile toon : toons)
    {
      toonIds.add(toon.getIdentifier());
    }
    props.setStringList(TOON_NAME_PREFERENCE,toonIds);
    _stats=null;
    if (_warbandsStatisticsPanelController!=null)
    {
      _warbandsStatisticsPanelController.dispose();
      _warbandsStatisticsPanelController=null;
    }
  }
}
