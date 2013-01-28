package delta.games.lotro.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.games.lotro.Config;
import delta.games.lotro.Preferences;
import delta.games.lotro.gui.stats.warbands.WarbandsWindowController;
import delta.games.lotro.gui.toon.ToonsManagementController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController
{
  private ToonsManagementController _toonsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("LOTRO Companion");
    frame.setSize(500,400);
    frame.setLocation(100,100);
    frame.getContentPane().setBackground(GuiFactory.getBackgroundColor());

    return frame;
  }

  @Override
  protected JMenuBar buildMenuBar()
  {
    JMenu fileMenu=GuiFactory.buildMenu("File");
    JMenuItem quit=GuiFactory.buildMenuItem("Quit");
    ActionListener alQuit=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doQuit();
      }
    };
    quit.addActionListener(alQuit);
    fileMenu.add(quit);

    JMenu statsMenu=GuiFactory.buildMenu("Statistics");
    JMenuItem warbandsStats=GuiFactory.buildMenuItem("Warbands");
    ActionListener alWarbands=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doWarbands();
      }
    };
    warbandsStats.addActionListener(alWarbands);
    statsMenu.add(warbandsStats);
    
    JMenuBar menuBar=GuiFactory.buildMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(statsMenu);
    return menuBar;
  }

  @Override
  protected JComponent buildContents()
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    JPanel toonsPanel=_toonsManager.getPanel();
    tabbedPane.add("Toons",toonsPanel);
    return tabbedPane;
  }

  @Override
  protected void doWindowClosing()
  {
    doQuit();
  }

  private void doWarbands()
  {
    MultipleToonsWarbandsStats stats=new MultipleToonsWarbandsStats();
    WarbandsWindowController warbandsController=new WarbandsWindowController(stats);
    warbandsController.show();
  }

  private void doQuit()
  {
    int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to quit?","Quit?",JOptionPane.YES_NO_OPTION);
    if (result==JOptionPane.OK_OPTION)
    {
      dispose();
    }
    Preferences preferences=Config.getInstance().getPreferences();
    preferences.saveAllPreferences();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_toonsManager!=null)
    {
      _toonsManager.dispose();
      _toonsManager=null;
    }
  }
}
