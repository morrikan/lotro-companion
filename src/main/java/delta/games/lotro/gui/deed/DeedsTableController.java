package delta.games.lotro.gui.deed;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Controller for a table that shows deeds.
 * @author DAM
 */
public class DeedsTableController
{
  /**
   * Double-click action command.
   */
  public static final String DOUBLE_CLICK="double click";
  // Data
  private List<DeedDescription> _toons;
  // GUI
  private JTable _table;
  private GenericTableController<DeedDescription> _tableController;

  /**
   * Constructor.
   */
  public DeedsTableController()
  {
    _toons=new ArrayList<DeedDescription>();
    init();
    _tableController=buildTable();
  }

  private GenericTableController<DeedDescription> buildTable()
  {
    ListDataProvider<DeedDescription> provider=new ListDataProvider<DeedDescription>(_toons);
    GenericTableController<DeedDescription> table=new GenericTableController<DeedDescription>(provider);

    // Identifier column
    {
      CellDataProvider<DeedDescription,Integer> idCell=new CellDataProvider<DeedDescription,Integer>()
      {
        public Integer getData(DeedDescription deed)
        {
          return Integer.valueOf(deed.getIdentifier());
        }
      };
      TableColumnController<DeedDescription,Integer> idColumn=new TableColumnController<DeedDescription,Integer>("ID",Integer.class,idCell);
      idColumn.setWidthSpecs(100,100,100);
      table.addColumnController(idColumn);
    }
    // Key column
    {
      CellDataProvider<DeedDescription,String> keyCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          return deed.getKey();
        }
      };
      TableColumnController<DeedDescription,String> keyColumn=new TableColumnController<DeedDescription,String>("Key",String.class,keyCell);
      keyColumn.setWidthSpecs(100,200,200);
      table.addColumnController(keyColumn);
    }
    // Name column
    {
      CellDataProvider<DeedDescription,String> nameCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          deed.getObjectives();
          deed.getDescription();
          deed.getRewards();
          return deed.getName();
        }
      };
      TableColumnController<DeedDescription,String> nameColumn=new TableColumnController<DeedDescription,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,300,200);
      table.addColumnController(nameColumn);
    }
    // Type column
    {
      CellDataProvider<DeedDescription,DeedType> typeCell=new CellDataProvider<DeedDescription,DeedType>()
      {
        public DeedType getData(DeedDescription deed)
        {
          return deed.getType();
        }
      };
      TableColumnController<DeedDescription,DeedType> typeColumn=new TableColumnController<DeedDescription,DeedType>("Type",DeedType.class,typeCell);
      typeColumn.setWidthSpecs(80,100,80);
      table.addColumnController(typeColumn);
    }
    // Category column
    {
      CellDataProvider<DeedDescription,String> categoryCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          return deed.getCategory();
        }
      };
      TableColumnController<DeedDescription,String> categoryColumn=new TableColumnController<DeedDescription,String>("Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(80,100,80);
      table.addColumnController(categoryColumn);
    }
    // Min level column
    {
      CellDataProvider<DeedDescription,Integer> minLevelCell=new CellDataProvider<DeedDescription,Integer>()
      {
        public Integer getData(DeedDescription deed)
        {
          return deed.getMinLevel();
        }
      };
      TableColumnController<DeedDescription,Integer> minLevelColumn=new TableColumnController<DeedDescription,Integer>("Min Level",Integer.class,minLevelCell);
      minLevelColumn.setWidthSpecs(40,40,40);
      table.addColumnController(minLevelColumn);
    }
    // Objectives column
    {
      CellDataProvider<DeedDescription,String> objectivesCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          return deed.getObjectives();
        }
      };
      TableColumnController<DeedDescription,String> objectivesColumn=new TableColumnController<DeedDescription,String>("Objectives",String.class,objectivesCell);
      objectivesColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(objectivesColumn);
    }
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<DeedDescription> getTableController()
  {
    return _tableController;
  }

  private void reset()
  {
    _toons.clear();
  }

  /**
   * Refresh toons table.
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
   * Refresh toons table.
   * @param toon Toon to refresh.
   */
  public void refresh(DeedDescription toon)
  {
    if (_table!=null)
    {
      _tableController.refresh(toon);
    }
  }

  private void init()
  {
    reset();
    DeedsManager manager=DeedsManager.getInstance();
    List<DeedDescription> toons=manager.getAll();
    for(DeedDescription toon : toons)
    {
      _toons.add(toon);
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
    // GUI
    if (_table!=null)
    {
      _table=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _toons=null;
  }
}
