package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.io.xml.CharacterDataIO;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.gui.character.buffs.BuffEditionPanelController;
import delta.games.lotro.gui.character.essences.AllEssencesEditionWindowController;
import delta.games.lotro.gui.character.essences.EssencesSummaryWindowController;
import delta.games.lotro.gui.character.gear.EquipmentPanelController;
import delta.games.lotro.gui.character.tomes.TomesEditionPanelController;
import delta.games.lotro.gui.character.virtues.VirtuesDisplayPanelController;
import delta.games.lotro.gui.character.virtues.VirtuesEditionDialogController;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "character data" window.
 * @author DAM
 */
public class CharacterDataWindowController extends DefaultFormDialogController<CharacterData> implements GenericEventsListener<CharacterEvent>
{
  private CharacterMainAttrsEditionPanelController _attrsController;
  private CharacterStatsSummaryPanelController _statsController;
  private EquipmentPanelController _equipmentController;
  private VirtuesDisplayPanelController _virtuesController;
  private BuffEditionPanelController _buffsController;
  private TomesEditionPanelController _tomesController;
  private CharacterFile _toonFile;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param toon Parent toon.
   * @param toonData Managed toon.
   */
  public CharacterDataWindowController(WindowController parent, CharacterFile toon, CharacterData toonData)
  {
    super(parent,toonData);
    _toonFile=toon;
    _windowsManager=new WindowsManager();
    _attrsController=new CharacterMainAttrsEditionPanelController(toon,toonData);
    _attrsController.set();
    _statsController=new CharacterStatsSummaryPanelController(this,toonData);
    _equipmentController=new EquipmentPanelController(this,toon,toonData);
    _virtuesController=new VirtuesDisplayPanelController();
    updateVirtues();
    _buffsController=new BuffEditionPanelController(this,toonData);
    _tomesController=new TomesEditionPanelController(toonData);
  }

  /**
   * Get the window identifier for a given toon.
   * @param data Data to use.
   * @return A window identifier.
   */
  public static String getIdentifier(CharacterData data)
  {
    String id="DATA#"+data.getFile();
    return id;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    // North: attributes panel
    JPanel attrsPanel=_attrsController.getPanel();

    // Center: equipment and stats
    // Stats panel
    JPanel statsPanel=_statsController.getPanel();
    // Equipment panel
    JPanel equipmentPanel=_equipmentController.getPanel();
    TitledBorder equipmentBorder=GuiFactory.buildTitledBorder("Equipment");
    equipmentPanel.setBorder(equipmentBorder);
    // Essences panel
    JPanel essencesPanel=buildEssencesPanel();
    TitledBorder essencesBorder=GuiFactory.buildTitledBorder("Essences");
    essencesPanel.setBorder(essencesBorder);

    JPanel gearingPanel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;
    c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    gearingPanel.add(equipmentPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(3,3,0,0),0,0);
    gearingPanel.add(essencesPanel,c);

    // Center panel
    JPanel centerPanel;
    {
      centerPanel=GuiFactory.buildPanel(new GridBagLayout());
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
      centerPanel.add(gearingPanel,c);
      c=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      centerPanel.add(GuiFactory.buildPanel(new BorderLayout()),c);
      c=new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
      centerPanel.add(statsPanel,c);
    }

    // Bottom panel
    JPanel bottomPanel=GuiFactory.buildPanel(new GridBagLayout());
    JPanel bottomPanel1=GuiFactory.buildPanel(new GridBagLayout());
    // - virtues
    {
      JPanel virtuesPanel=buildVirtuesPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Virtues");
      virtuesPanel.setBorder(border);
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      bottomPanel1.add(virtuesPanel,c);
    }
    // - tomes
    {
      JPanel tomesPanel=_tomesController.getPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Tomes");
      tomesPanel.setBorder(border);
      c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      bottomPanel1.add(tomesPanel,c);
    }
    // Space on right
    c=new GridBagConstraints(2,0,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    bottomPanel1.add(GuiFactory.buildPanel(new GridBagLayout()),c);
    c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    bottomPanel.add(bottomPanel1,c);

    JPanel bottomPanel2=GuiFactory.buildPanel(new GridBagLayout());
    // - buffs
    {
      JPanel buffsPanel=_buffsController.getPanel();
      TitledBorder border=GuiFactory.buildTitledBorder("Buffs");
      buffsPanel.setBorder(border);
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
      bottomPanel2.add(buffsPanel,c);
    }
    // Space on right
    c=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    bottomPanel2.add(GuiFactory.buildPanel(new GridBagLayout()),c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    bottomPanel.add(bottomPanel2,c);

    JPanel fullPanel=GuiFactory.buildPanel(new BorderLayout());
    fullPanel.add(attrsPanel,BorderLayout.NORTH);
    fullPanel.add(centerPanel,BorderLayout.CENTER);
    fullPanel.add(bottomPanel,BorderLayout.SOUTH);

    // Register to events
    EventsManager.addListener(CharacterEvent.class,this);
    return fullPanel;
  }

  private JPanel buildEssencesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    // Edition
    {
      JButton edit=GuiFactory.buildButton("Edit...");
      ActionListener alEssences=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          doEssencesEdition();
        }
      };
      edit.addActionListener(alEssences);
      panel.add(edit);
    }
    // Summary
    {
      JButton summary=GuiFactory.buildButton("Summary...");
      ActionListener alEssences=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          doEssencesSummary();
        }
      };
      summary.addActionListener(alEssences);
      panel.add(summary);
    }
    return panel;
  }

  private JPanel buildVirtuesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    JPanel virtuesPanel=_virtuesController.getPanel();
    panel.add(virtuesPanel);
    JButton button=GuiFactory.buildButton("Edit...");
    panel.add(button);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        VirtuesSet virtuesToEdit=_data.getVirtues();
        int characterLevel=_data.getLevel();
        VirtuesSet virtues=VirtuesEditionDialogController.editVirtues(CharacterDataWindowController.this,virtuesToEdit,characterLevel);
        if (virtues!=null)
        {
          virtuesToEdit.copyFrom(virtues);
          updateVirtues();
          // Broadcast virtues update event...
          CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_data);
          EventsManager.invokeEvent(event);
        }
      }
    };
    button.addActionListener(al);
    return panel;
  }

  @Override
  public void configureWindow()
  {
    JDialog dialog=getDialog();
    // Title
    String name=_data.getName();
    String serverName=_data.getServer();
    String title="Character: "+name+" @ "+serverName;
    dialog.setTitle(title);
    // Set values
    _statsController.update();
    // Size
    dialog.pack();
    dialog.setResizable(false);
  }

  @Override
  public String getWindowIdentifier()
  {
    String id=getIdentifier(_data);
    return id;
  }

  /**
   * Get a set of user properties.
   * @param id of the properties set.
   * @return Some properties or <code>null</code> if not managed.
   */
  @Override
  public TypedProperties getUserProperties(String id)
  {
    return CharacterPreferencesManager.getUserProperties(_toonFile,id);
  }

  private void doEssencesEdition()
  {
    AllEssencesEditionWindowController editionController=(AllEssencesEditionWindowController)_windowsManager.getWindow(AllEssencesEditionWindowController.IDENTIFIER);
    if (editionController==null)
    {
      editionController=new AllEssencesEditionWindowController(this,_data);
      _windowsManager.registerWindow(editionController);
      editionController.getWindow().setLocationRelativeTo(this.getWindow());
    }
    editionController.bringToFront();
  }

  private void doEssencesSummary()
  {
    EssencesSummaryWindowController summaryController=(EssencesSummaryWindowController)_windowsManager.getWindow(EssencesSummaryWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new EssencesSummaryWindowController(this,_data);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(this.getWindow());
    }
    summaryController.bringToFront();
  }

  private void updateVirtues()
  {
    BasicStatsSet buffs=_data.getBuffs().getBuffs(_data);
    VirtuesSet virtues=_data.getVirtues();
    virtues.setBuffs(buffs);
    _virtuesController.setVirtues(virtues);
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_DATA_UPDATED)
    {
      CharacterData data=event.getToonData();
      if (data==_data)
      {
        // Compute new stats
        CharacterStatsComputer computer=new CharacterStatsComputer();
        BasicStatsSet stats=computer.getStats(data);
        BasicStatsSet toonStats=_data.getStats();
        toonStats.clear();
        toonStats.setStats(stats);
        // Update stats display
        _statsController.update();
        // Update buffs display
        _buffsController.update();
        // Update virtues display
        updateVirtues();
      }
    }
    if (type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
    {
      CharacterFile toonFile=event.getToonFile();
      if (toonFile==_toonFile)
      {
        // Update sex
        _attrsController.updateSexDisplay();
      }
    }
  }

  @Override
  public void okImpl()
  {
    _attrsController.get();
    boolean ok=CharacterDataIO.saveInfo(_data.getFile(),_data);
    if (ok)
    {
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_data);
      EventsManager.invokeEvent(event);
    }
    else
    {
      // TODO warn
    }
  }

  @Override
  public void cancelImpl()
  {
    _data.revert();
    _data.getSummary().setSummary(_toonFile.getSummary());
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    EventsManager.removeListener(CharacterEvent.class,this);
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    if (_statsController!=null)
    {
      _statsController.dispose();
      _statsController=null;
    }
    if (_equipmentController!=null)
    {
      _equipmentController.dispose();
      _equipmentController=null;
    }
    if (_buffsController!=null)
    {
      _buffsController.dispose();
      _buffsController=null;
    }
    if (_tomesController!=null)
    {
      _tomesController.dispose();
      _tomesController=null;
    }
    if (_toonFile!=null)
    {
      _toonFile.getPreferences().saveAllPreferences();
      _toonFile=null;
    }
  }
}
