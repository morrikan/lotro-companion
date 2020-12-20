package delta.games.lotro.gui.stats.deeds.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.lore.quests.objectives.Objective;

/**
 * Controller for a panel to edit the status of an objective of an achievable.
 * @author DAM
 */
public class ObjectiveStatusEditionPanelController
{
  private AchievableObjectiveStatus _objectiveStatus;
  private AchievableElementStateEditionController _stateCtrl;
  private List<ObjectiveConditionStatusEditionPanelController> _conditionStatusEditors;
  private JLabel _label;
  private JPanel _panel;

  /**
   * Constructor.
   * @param objectiveStatus Status to edit.
   */
  public ObjectiveStatusEditionPanelController(AchievableObjectiveStatus objectiveStatus)
  {
    _objectiveStatus=objectiveStatus;
    _panel=build();
    setStatus();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    // Head panel
    JPanel headPanel=buildHeadPanel();
    // Condition editors
    _conditionStatusEditors=new ArrayList<ObjectiveConditionStatusEditionPanelController>();
    for(ObjectiveConditionStatus conditionStatus : _objectiveStatus.getConditionStatuses())
    {
      ObjectiveConditionStatusEditionPanelController editor=new ObjectiveConditionStatusEditionPanelController(conditionStatus);
      _conditionStatusEditors.add(editor);
    }
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
    panel.add(headPanel,c);
    c.gridy++;
    for(ObjectiveConditionStatusEditionPanelController editor : _conditionStatusEditors)
    {
      c.gridy++;
      JPanel editorPanel=editor.getPanel();
      panel.add(editorPanel,c);
    }
    return panel;
  }

  private JPanel buildHeadPanel()
  {
    // State
    _stateCtrl=new AchievableElementStateEditionController();
    // Label
    String label=getLabel();
    _label=GuiFactory.buildLabel(label);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_label,c);
    return panel;
  }

  private void setStatus()
  {
    // State
    _stateCtrl.setState(_objectiveStatus.getState());
  }

  private String getLabel()
  {
    Objective objective=_objectiveStatus.getObjective();
    //String objectiveOverride=objective.getProgressOverride();
    int index=objective.getIndex();
    String label="Objective #"+index;
    return label;
  }
}
