package delta.games.lotro.gui.stats.crafting.synopsis;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.crafting.GuildStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.character.reputation.FactionStatus;
import delta.games.lotro.crafting.CraftingLevel;
import delta.games.lotro.crafting.CraftingLevelTier;
import delta.games.lotro.crafting.Profession;
import delta.games.lotro.crafting.ProfessionComparator;
import delta.games.lotro.crafting.ProfessionFilter;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.stats.reputation.synopsis.ReputationSynopsisTableController;
import delta.games.lotro.lore.reputation.FactionLevelComparator;
import delta.games.lotro.utils.gui.Gradients;

/**
 * Controller for a table that shows crafting status for several toons.
 * @author DAM
 */
public class CraftingSynopsisTableController
{
  // Data
  private List<CharacterFile> _toons;
  private List<CraftingSynopsisItem> _rowItems;
  private CraftingSynopsisItemFilter _filter;
  // GUI
  private GenericTableController<CraftingSynopsisItem> _table;

  /**
   * Constructor.
   * @param filter Profession filter.
   */
  public CraftingSynopsisTableController(ProfessionFilter filter)
  {
    _filter=new CraftingSynopsisItemFilter(filter);
    _toons=new ArrayList<CharacterFile>();
    _rowItems=new ArrayList<CraftingSynopsisItem>();
    _table=buildTable();
    configureTable(_table.getTable());
  }

  /**
   * Get the managed toons.
   * @return the managed toons.
   */
  public List<CharacterFile> getToons()
  {
    return _toons;
  }

  private void updateRowItems()
  {
    _rowItems.clear();
    for(CharacterFile toon : _toons)
    {
      CraftingStatus craftingStatus=toon.getCraftingStatus();
      Profession[] professions=craftingStatus.getProfessions();
      GuildStatus guildStatus=craftingStatus.getGuildStatus();
      Profession guild=guildStatus.getProfession();
      for(Profession profession : professions)
      {
        ProfessionStatus professionStatus=craftingStatus.getProfessionStatus(profession,true);
        GuildStatus displayedStatus=(profession==guild)?guildStatus:null;
        CraftingSynopsisItem item=new CraftingSynopsisItem(toon,professionStatus,displayedStatus);
        _rowItems.add(item);
      }
    }
  }

  private DataProvider<CraftingSynopsisItem> buildDataProvider()
  {
    DataProvider<CraftingSynopsisItem> ret=new ListDataProvider<CraftingSynopsisItem>(_rowItems);
    return ret;
  }

  private GenericTableController<CraftingSynopsisItem> buildTable()
  {
    DataProvider<CraftingSynopsisItem> provider=buildDataProvider();
    GenericTableController<CraftingSynopsisItem> table=new GenericTableController<CraftingSynopsisItem>(provider);
    table.setFilter(_filter);
    // Row header column
    TableColumnController<CraftingSynopsisItem,String> rowHeaderColumn=buildRowHeaderColumn();
    table.addColumnController(rowHeaderColumn);
    // Profession column
    TableColumnController<CraftingSynopsisItem,Profession> professionColumn=buildProfessionColumn();
    table.addColumnController(professionColumn);
    // Proficiency
    TableColumnController<CraftingSynopsisItem,CraftingLevel> proficiencyColumn=buildCraftingTierColumn(false);
    table.addColumnController(proficiencyColumn);
    // Mastery
    TableColumnController<CraftingSynopsisItem,CraftingLevel> masteryColumn=buildCraftingTierColumn(true);
    table.addColumnController(masteryColumn);
    // Guild
    TableColumnController<CraftingSynopsisItem,FactionStatus> guildColumn=buildGuildColumn();
    table.addColumnController(guildColumn);
    return table;
  }

  private TableCellRenderer buildSimpleCellRenderer(final JPanel panel)
  {
    TableCellRenderer renderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        return panel;
      }
    };
    return renderer;
  }

  private TableCellRenderer buildCharacterNameCellRenderer()
  {
    final JLabel label=GuiFactory.buildLabel("");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    TableCellRenderer renderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        label.setText((String)value);
        return label;
      }
    };
    return renderer;
  }

  private TableColumnController<CraftingSynopsisItem,String> buildRowHeaderColumn()
  {
    CellDataProvider<CraftingSynopsisItem,String> cell=new CellDataProvider<CraftingSynopsisItem,String>()
    {
      public String getData(CraftingSynopsisItem item)
      {
        CharacterFile character=item.getCharacter();
        String name=character.getName();
        return name;
      }
    };
    TableColumnController<CraftingSynopsisItem,String> column=new TableColumnController<CraftingSynopsisItem,String>("Name",String.class,cell);

    // Init widths
    column.setMinWidth(100);
    column.setPreferredWidth(150);
    column.setMaxWidth(300);

    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    column.setHeaderCellRenderer(headerRenderer);

    // Cell renderer
    TableCellRenderer renderer=buildCharacterNameCellRenderer();
    column.setCellRenderer(renderer);

    return column;
  }

  private TableCellRenderer buildProfessionCellRenderer()
  {
    TableCellRenderer renderer=new DefaultTableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        JLabel label=(JLabel)super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        Profession profession=(Profession)value;
        Icon icon=LotroIconsManager.getProfessionIcon(profession);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setIcon(icon);
        label.setToolTipText(profession.getLabel());
        label.setText("");
        return label;
      }
    };
    return renderer;
  }

  private TableColumnController<CraftingSynopsisItem,Profession> buildProfessionColumn()
  {
    CellDataProvider<CraftingSynopsisItem,Profession> professionCell=new CellDataProvider<CraftingSynopsisItem,Profession>()
    {
      public Profession getData(CraftingSynopsisItem item)
      {
        return item.getProfession();
      }
    };
    TableColumnController<CraftingSynopsisItem,Profession> professionColumn=new TableColumnController<CraftingSynopsisItem,Profession>("Profession",Profession.class,professionCell);
    professionColumn.setWidthSpecs(50,50,50);
    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    professionColumn.setHeaderCellRenderer(headerRenderer);
    // Cell renderer
    TableCellRenderer cellRenderer=buildProfessionCellRenderer();
    professionColumn.setCellRenderer(cellRenderer);
    // Comparator
    ProfessionComparator comparator=new ProfessionComparator();
    professionColumn.setComparator(comparator);
    return professionColumn;
  }

  private JPanel buildCraftingTierPanel(boolean mastery)
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new FlowLayout());
    Icon tierIcon=LotroIconsManager.getCraftingTierIcon(mastery);
    panel.add(GuiFactory.buildIconLabel(tierIcon));
    String label=mastery?"Mastery":"Proficiency";
    panel.add(GuiFactory.buildLabel(label));
    return panel;
  }

  private TableCellRenderer buildCraftingLevelCellRenderer(final boolean mastery)
  {
    final JLabel label=GuiFactory.buildLabel("");
    TableCellRenderer renderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        CraftingLevel data=(CraftingLevel)value;
        configureCraftingLevelLabel(mastery,label,data);
        return label;
      }
    };
    return renderer;
  }

  private TableColumnController<CraftingSynopsisItem,CraftingLevel> buildCraftingTierColumn(final boolean mastery)
  {
    CellDataProvider<CraftingSynopsisItem,CraftingLevel> cell=new CellDataProvider<CraftingSynopsisItem,CraftingLevel>()
    {
      public CraftingLevel getData(CraftingSynopsisItem item)
      {
        return item.getLevel(mastery);
      }
    };
    String columnName=mastery?"Mastery":"Proficiency";
    String id=columnName;
    TableColumnController<CraftingSynopsisItem,CraftingLevel> column=new TableColumnController<CraftingSynopsisItem,CraftingLevel>(id,columnName,CraftingLevel.class,cell);
    // Header cell renderer
    JPanel panel=buildCraftingTierPanel(mastery);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(panel);
    column.setHeaderCellRenderer(headerRenderer);
    // Cell renderer
    TableCellRenderer renderer=buildCraftingLevelCellRenderer(mastery);
    column.setCellRenderer(renderer);

    // Init widths
    column.setMinWidth(150);
    column.setPreferredWidth(200);
    column.setMaxWidth(300);

    // Comparator
    Comparator<CraftingLevel> statsComparator=new Comparator<CraftingLevel>()
    {
      public int compare(CraftingLevel data1, CraftingLevel data2)
      {
        return Integer.compare(data1.getTier(),data2.getTier());
      }
    };
    column.setComparator(statsComparator);
    return column;
  }

  private JPanel buildGuildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Guild"));
    return panel;
  }

  private TableColumnController<CraftingSynopsisItem,FactionStatus> buildGuildColumn()
  {
    CellDataProvider<CraftingSynopsisItem,FactionStatus> cell=new CellDataProvider<CraftingSynopsisItem,FactionStatus>()
    {
      public FactionStatus getData(CraftingSynopsisItem item)
      {
        return item.getGuildFaction();
      }
    };
    String columnName="Guild";
    String id=columnName;
    TableColumnController<CraftingSynopsisItem,FactionStatus> column=new TableColumnController<CraftingSynopsisItem,FactionStatus>(id,columnName,FactionStatus.class,cell);
    // Header cell renderer
    JPanel panel=buildGuildPanel();
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(panel);
    column.setHeaderCellRenderer(headerRenderer);
    // Cell renderer
    TableCellRenderer renderer=ReputationSynopsisTableController.buildStatCellRenderer();
    column.setCellRenderer(renderer);

    // Init widths
    column.setMinWidth(150);
    column.setPreferredWidth(200);
    column.setMaxWidth(300);

    // Comparator
    final FactionLevelComparator factionLevelComparator=new FactionLevelComparator();
    Comparator<FactionStatus> statsComparator=new Comparator<FactionStatus>()
    {
      public int compare(FactionStatus data1, FactionStatus data2)
      {
        return factionLevelComparator.compare(data1.getFactionLevel(),data2.getFactionLevel());
      }
    };
    column.setComparator(statsComparator);
    return column;
  }

  /**
   * Set the displayed toons.
   * @param toons Toons to display.
   */
  public void setToons(List<CharacterFile> toons)
  {
    _toons.clear();
    _toons.addAll(toons);
    updateRowItems();
    _table.refresh();
  }

  /**
   * Update data for a toon.
   * @param toon Targeted toon.
   */
  public void updateToon(CharacterFile toon)
  {
    updateRowItems();
    _table.refresh();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _table.getTable();
  }

  private void configureTable(final JTable table)
  {
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setShowGrid(false);
    table.getTableHeader().setReorderingAllowed(false);
    // Adjust table row height for icons (30 pixels)
    table.setRowHeight(30);
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _table.filterUpdated();
  }

  private Color getColorForCraftingLevel(CraftingLevel level)
  {
    int index=level.getTier();
    if (index==0) return Color.GRAY;
    // Gradient from orange to green
    int nbSteps=CraftingLevel.getMaximumLevel().getTier();
    Color[] gradient=Gradients.getOrangeToGreen(nbSteps);
    Color ret=null;
    if (gradient!=null)
    {
      ret=gradient[index-1];
    }
    else
    {
      ret=Color.WHITE;
    }
    return ret;
  }

  private void configureCraftingLevelLabel(boolean mastery, JLabel label, CraftingLevel level)
  {
    Color backgroundColor=null;
    String text="";
    if (level!=null)
    {
      backgroundColor=getColorForCraftingLevel(level);
      CraftingLevelTier tier=mastery?level.getMastery():level.getProficiency();
      text=tier.getLabel();
    }
    label.setForeground(Color.BLACK);
    if (backgroundColor!=null)
    {
      label.setOpaque(true);
      backgroundColor=new Color(backgroundColor.getRed(),backgroundColor.getGreen(),backgroundColor.getBlue(),128);
      label.setBackground(backgroundColor);
    }
    else
    {
      label.setOpaque(false);
    }
    label.setText(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table.dispose();
      _table=null;
    }
    // Data
    _toons=null;
    _filter=null;
  }
}
