package delta.games.lotro.gui.deed.table;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.ChallengeLevel;
import delta.games.lotro.common.ChallengeLevelComparator;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.requirements.table.RequirementsColumnsBuilder;
import delta.games.lotro.gui.common.rewards.table.RewardsColumnsBuilder;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Controller for a table that shows deeds.
 * @author DAM
 */
public class DeedsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<DeedDescription> _deeds;
  // GUI
  private JTable _table;
  private GenericTableController<DeedDescription> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public DeedsTableController(TypedProperties prefs, Filter<DeedDescription> filter)
  {
    _prefs=prefs;
    _deeds=new ArrayList<DeedDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<DeedDescription> buildTable()
  {
    ListDataProvider<DeedDescription> provider=new ListDataProvider<DeedDescription>(_deeds);
    GenericTableController<DeedDescription> table=new GenericTableController<DeedDescription>(provider);
    List<TableColumnController<DeedDescription,?>> columns=buildColumns();
    for(TableColumnController<DeedDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<DeedDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a deeds table.
   * @return A list of columns for a deeds table.
   */
  public static List<TableColumnController<DeedDescription,?>> buildColumns()
  {
    List<TableColumnController<DeedDescription,?>> ret=new ArrayList<TableColumnController<DeedDescription,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<DeedDescription,Integer> idCell=new CellDataProvider<DeedDescription,Integer>()
      {
        @Override
        public Integer getData(DeedDescription deed)
        {
          return Integer.valueOf(deed.getIdentifier());
        }
      };
      DefaultTableColumnController<DeedDescription,Integer> idColumn=new DefaultTableColumnController<DeedDescription,Integer>(DeedColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(100,100,100);
      ret.add(idColumn);
    }
    // Key column
    {
      CellDataProvider<DeedDescription,String> keyCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getKey();
        }
      };
      DefaultTableColumnController<DeedDescription,String> keyColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.KEY.name(),"Key",String.class,keyCell);
      keyColumn.setWidthSpecs(100,200,200);
      ret.add(keyColumn);
    }
    // Name column
    {
      CellDataProvider<DeedDescription,String> nameCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getName();
        }
      };
      DefaultTableColumnController<DeedDescription,String> nameColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,300,200);
      ret.add(nameColumn);
    }
    // Type column
    {
      CellDataProvider<DeedDescription,DeedType> typeCell=new CellDataProvider<DeedDescription,DeedType>()
      {
        @Override
        public DeedType getData(DeedDescription deed)
        {
          return deed.getType();
        }
      };
      DefaultTableColumnController<DeedDescription,DeedType> typeColumn=new DefaultTableColumnController<DeedDescription,DeedType>(DeedColumnIds.TYPE.name(),"Type",DeedType.class,typeCell);
      typeColumn.setWidthSpecs(80,100,80);
      ret.add(typeColumn);
    }
    // Category column
    {
      CellDataProvider<DeedDescription,String> categoryCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getCategory();
        }
      };
      DefaultTableColumnController<DeedDescription,String> categoryColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(80,350,80);
      ret.add(categoryColumn);
    }
    // Challenge level column
    {
      CellDataProvider<DeedDescription,ChallengeLevel> levelCell=new CellDataProvider<DeedDescription,ChallengeLevel>()
      {
        @Override
        public ChallengeLevel getData(DeedDescription deed)
        {
          return deed.getChallengeLevel();
        }
      };
      DefaultTableColumnController<DeedDescription,ChallengeLevel> levelColumn=new DefaultTableColumnController<DeedDescription,ChallengeLevel>(DeedColumnIds.LEVEL.name(),"Level",ChallengeLevel.class,levelCell);
      levelColumn.setWidthSpecs(100,100,100);
      levelColumn.setComparator(new ChallengeLevelComparator());
      ret.add(levelColumn);
    }
    // Obsolete column
    {
      CellDataProvider<DeedDescription,Boolean> obsoleteCell=new CellDataProvider<DeedDescription,Boolean>()
      {
        @Override
        public Boolean getData(DeedDescription deed)
        {
          return Boolean.valueOf(deed.isObsolete());
        }
      };
      DefaultTableColumnController<DeedDescription,Boolean> obsoleteColumn=new DefaultTableColumnController<DeedDescription,Boolean>(DeedColumnIds.OBSOLETE.name(),"Obsolete",Boolean.class,obsoleteCell);
      obsoleteColumn.setWidthSpecs(100,100,100);
      ret.add(obsoleteColumn);
    }
    // Requirements
    {
      List<DefaultTableColumnController<UsageRequirement,?>> requirementColumns=RequirementsColumnsBuilder.buildRequirementsColumns();
      CellDataProvider<DeedDescription,UsageRequirement> dataProvider=new CellDataProvider<DeedDescription,UsageRequirement>()
      {
        @Override
        public UsageRequirement getData(DeedDescription deed)
        {
          return deed.getUsageRequirement();
        }
      };
      for(DefaultTableColumnController<UsageRequirement,?> requirementColumn : requirementColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<UsageRequirement,Object> c=(TableColumnController<UsageRequirement,Object>)requirementColumn;
        TableColumnController<DeedDescription,Object> proxiedColumn=new ProxiedTableColumnController<DeedDescription,UsageRequirement,Object>(c,dataProvider);
        ret.add(proxiedColumn);
      }
    }
    // Rewards
    {
      List<DefaultTableColumnController<Rewards,?>> rewardColumns=RewardsColumnsBuilder.buildRewardColumns();
      CellDataProvider<DeedDescription,Rewards> dataProvider=new CellDataProvider<DeedDescription,Rewards>()
      {
        @Override
        public Rewards getData(DeedDescription deed)
        {
          return deed.getRewards();
        }
      };
      for(DefaultTableColumnController<Rewards,?> rewardColumn : rewardColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Rewards,Object> c=(TableColumnController<Rewards,Object>)rewardColumn;
        TableColumnController<DeedDescription,Object> proxiedColumn=new ProxiedTableColumnController<DeedDescription,Rewards,Object>(c,dataProvider);
        ret.add(proxiedColumn);
      }
    }
    return ret;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(DeedColumnIds.NAME.name());
      columnIds.add(DeedColumnIds.TYPE.name());
      columnIds.add(DeedColumnIds.CATEGORY.name());
      columnIds.add(DeedColumnIds.LEVEL.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<DeedDescription> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of deeds.
   * @return A number of deeds.
   */
  public int getNbItems()
  {
    return _deeds.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  private void reset()
  {
    _deeds.clear();
  }

  /**
   * Refresh table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  /**
   * Refresh table.
   * @param deed Deed to refresh.
   */
  public void refresh(DeedDescription deed)
  {
    if (_table!=null)
    {
      _tableController.refresh(deed);
    }
  }

  private void init()
  {
    reset();
    DeedsManager manager=DeedsManager.getInstance();
    List<DeedDescription> deeds=manager.getAll();
    for(DeedDescription deed : deeds)
    {
      _deeds.add(deed);
    }
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _deeds=null;
  }
}
