package delta.games.lotro.gui.stats.deeds.statistics;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.stats.deeds.DeedsStatistics;

/**
 * Controller for a panel to show the summary of the statistics about some deeds.
 * @author DAM
 */
public class DeedStatisticsSummaryPanelController
{
  // Data
  private DeedsStatistics _statistics;
  // UI
  private JPanel _panel;
  private JPanel _statsPanel;
  private JLabel _completed;
  private JLabel _lotroPoints;
  private JLabel _classPoints;
  private JLabel _marks;
  private JLabel _medallions;
  private JLabel _titlesCount;
  private JLabel _reputation;
  private JLabel _virtues;
  private JLabel _itemsCount;
  private JLabel _emotesCount;
  private JLabel _traitsCount;

  /**
   * Constructor.
   * @param statistics Statistics to show.
   */
  public DeedStatisticsSummaryPanelController(DeedsStatistics statistics)
  {
    _statistics=statistics;
    _panel=buildPanel();
    update();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Stats panel
    _statsPanel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Statistics");
    _statsPanel.setBorder(border);
    panel.add(_statsPanel,BorderLayout.CENTER);
    GridBagConstraints cLabels=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    GridBagConstraints cValues=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);

    // Completion
    _statsPanel.add(GuiFactory.buildLabel("Completion:"),cLabels);
    _completed=GuiFactory.buildLabel("");
    _statsPanel.add(_completed,cValues);
    cLabels.gridy++;cValues.gridy++;
    // LOTRO points
    _statsPanel.add(GuiFactory.buildLabel("LOTRO Points:"),cLabels);
    _lotroPoints=GuiFactory.buildLabel("");
    _statsPanel.add(_lotroPoints,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Class points
    _statsPanel.add(GuiFactory.buildLabel("Class Points:"),cLabels);
    _classPoints=GuiFactory.buildLabel("");
    _statsPanel.add(_classPoints,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Marks
    _statsPanel.add(GuiFactory.buildLabel("Marks:"),cLabels);
    _marks=GuiFactory.buildLabel("");
    _statsPanel.add(_marks,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Medallions
    _statsPanel.add(GuiFactory.buildLabel("Medallions:"),cLabels);
    _medallions=GuiFactory.buildLabel("");
    _statsPanel.add(_medallions,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Titles
    _statsPanel.add(GuiFactory.buildLabel("Titles:"),cLabels);
    _titlesCount=GuiFactory.buildLabel("");
    _statsPanel.add(_titlesCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Reputation
    _statsPanel.add(GuiFactory.buildLabel("Reputation:"),cLabels);
    _reputation=GuiFactory.buildLabel("");
    _statsPanel.add(_reputation,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Virtues
    _statsPanel.add(GuiFactory.buildLabel("Virtues:"),cLabels);
    _virtues=GuiFactory.buildLabel("");
    _statsPanel.add(_virtues,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Items
    _statsPanel.add(GuiFactory.buildLabel("Items:"),cLabels);
    _itemsCount=GuiFactory.buildLabel("");
    _statsPanel.add(_itemsCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Emotes
    _statsPanel.add(GuiFactory.buildLabel("Emotes:"),cLabels);
    _emotesCount=GuiFactory.buildLabel("");
    _statsPanel.add(_emotesCount,cValues);
    cLabels.gridy++;cValues.gridy++;
    // Traits
    _statsPanel.add(GuiFactory.buildLabel("Traits:"),cLabels);
    _traitsCount=GuiFactory.buildLabel("");
    _statsPanel.add(_traitsCount,cValues);
    cLabels.gridy++;cValues.gridy++;

    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Completion
    int completed=_statistics.getCompletedCount();
    int total=_statistics.getTotalCount();
    double percentage=(100.0*completed)/total;
    String completionStr=String.format("%d / %d (%.1f%%)",Integer.valueOf(completed),Integer.valueOf(total),Double.valueOf(percentage));
    _completed.setText(completionStr);
    // LOTRO points
    int lotroPoints=_statistics.getAcquiredLP();
    _lotroPoints.setText(String.valueOf(lotroPoints));
    // Class points
    int classPoints=_statistics.getClassPoints();
    _classPoints.setText(String.valueOf(classPoints));
    // Marks
    int marks=_statistics.getMarksCount();
    _marks.setText(String.valueOf(marks));
    // Medallions
    int medallions=_statistics.getMedallionsCount();
    _medallions.setText(String.valueOf(medallions));
    // Titles count
    int nbTitles=_statistics.getTitles().size();
    _titlesCount.setText(String.valueOf(nbTitles));
    // Reputation
    int nbFactions=_statistics.getReputation().size();
    int nbReputationPoints=_statistics.getTotalReputationPoints();
    String reputationStr=String.format("%d points, %d factions",Integer.valueOf(nbReputationPoints),Integer.valueOf(nbFactions));
    _reputation.setText(reputationStr);
    // Virtues
    int nbVirtues=_statistics.getVirtues().size();
    int nbVirtuePoints=_statistics.getTotalVirtuePoints();
    String virtuesStr=String.format("%d points, %d virtues",Integer.valueOf(nbVirtuePoints),Integer.valueOf(nbVirtues));
    _virtues.setText(virtuesStr);
    // Items count
    int nbItems=_statistics.getItems().size();
    _itemsCount.setText(String.valueOf(nbItems));
    // Emotes count
    int nbEmotes=_statistics.getEmotes().size();
    _emotesCount.setText(String.valueOf(nbEmotes));
    // Traits count
    int nbTraits=_statistics.getTraits().size();
    _traitsCount.setText(String.valueOf(nbTraits));
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _statistics=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _completed=null;
    _lotroPoints=null;
    _classPoints=null;
    _marks=null;
    _medallions=null;
    _titlesCount=null;
    _reputation=null;
    _virtues=null;
    _itemsCount=null;
    _emotesCount=null;
    _traitsCount=null;
  }
}
