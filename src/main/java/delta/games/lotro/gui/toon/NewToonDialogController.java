package delta.games.lotro.gui.toon;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.OKCancelPanelController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;

/**
 * Controller for the "new toon" dialog.
 * @author DAM
 */
public class NewToonDialogController extends DefaultDialogController implements ActionListener
{
  private static final int TOON_NAME_SIZE=32;
  private OKCancelPanelController _okCancelController;
  private JTextField _toonName;
  private ComboBoxController<String> _server;
  private ComboBoxController<CharacterClass> _class;
  private ComboBoxController<Race> _race;
  private ComboBoxController<CharacterSex> _sex;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public NewToonDialogController(WindowController parentController)
  {
    super(parentController);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("New character...");
    dialog.setResizable(false);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel dataPanel=buildNewToonPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Character");
    dataPanel.setBorder(pathsBorder);
    panel.add(dataPanel,BorderLayout.CENTER);
    _okCancelController=new OKCancelPanelController();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().addActionListener(this);
    _okCancelController.getCancelButton().addActionListener(this);
    return panel;
  }

  private JPanel buildNewToonPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Toon name
    _toonName=GuiFactory.buildTextField("");
    _toonName.setColumns(TOON_NAME_SIZE);
    // Server
    _server=CharacterUiUtils.buildServerCombo();
    TypedProperties props=Config.getInstance().getParameters();
    String defaultServer=props.getStringProperty("default.server",null);
    if (defaultServer!=null)
    {
      _server.selectItem(defaultServer);
    }
    // Class
    _class=CharacterUiUtils.buildClassCombo();
    // Race
    _race=CharacterUiUtils.buildRaceCombo();
    // Sex
    _sex=CharacterUiUtils.buildSexCombo();

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc);
    gbc.gridx=0; gbc.gridy=1;
    panel.add(GuiFactory.buildLabel("Server:"),gbc);
    gbc.gridx=0; gbc.gridy=2;
    panel.add(GuiFactory.buildLabel("Race:"),gbc);
    gbc.gridx=0; gbc.gridy=3;
    panel.add(GuiFactory.buildLabel("Class:"),gbc);
    gbc.gridx=0; gbc.gridy=4;
    panel.add(GuiFactory.buildLabel("Sex:"),gbc);
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_toonName,gbc);
    gbc.gridx=1; gbc.gridy=1;
    panel.add(_server.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=2;
    panel.add(_race.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=3;
    panel.add(_class.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=4;
    panel.add(_sex.getComboBox(),gbc);
    return panel;
  }

  public void actionPerformed(ActionEvent event)
  {
    String action=event.getActionCommand();
    if (OKCancelPanelController.OK_COMMAND.equals(action))
    {
      ok();
    }
    else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
    {
      cancel();
    }
  }

  private void ok()
  {
    String toonName=_toonName.getText();
    String server=_server.getSelectedItem();
    CharacterClass cClass=_class.getSelectedItem();
    Race race=_race.getSelectedItem();
    CharacterSex sex=_sex.getSelectedItem();
    String errorMsg=checkData();
    if (errorMsg==null)
    {
      CharacterData info=new CharacterData();
      info.setName(toonName);
      info.setServer(server);
      info.setCharacterClass(cClass);
      info.setCharacterSex(sex);
      info.setRace(race);
      info.setLevel(1);
      info.setDate(Long.valueOf(System.currentTimeMillis()));
      // Compute stats
      CharacterStatsComputer computer=new CharacterStatsComputer();
      info.getStats().setStats(computer.getStats(info));
      CharactersManager manager=CharactersManager.getInstance();
      CharacterFile toon=manager.addToon(info);
      if (toon!=null)
      {
        dispose();
      }
      else
      {
        showErrorMessage("Character creation failed!");
      }
    }
    else
    {
      showErrorMessage(errorMsg);
    }
  }

  private String checkData()
  {
    String errorMsg=null;
    String toonName=_toonName.getText();
    if ((toonName==null) || (toonName.trim().length()==0))
    {
      errorMsg="Invalid toon name!";
    }
    String server=_server.getSelectedItem();
    if ((server==null) || (server.trim().length()==0))
    {
      errorMsg="Invalid server name!";
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Character creation";
    JDialog dialog=getDialog();
    GuiFactory.showErrorDialog(dialog,errorMsg,title);
  }

  private void cancel()
  {
    dispose();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    if (_server!=null)
    {
      _server.dispose();
      _server=null;
    }
    if (_class!=null)
    {
      _class.dispose();
      _class=null;
    }
    if (_race!=null)
    {
      _race.dispose();
      _race=null;
    }
    if (_sex!=null)
    {
      _sex.dispose();
      _sex=null;
    }
    _toonName=null;
    _server=null;
  }
}
