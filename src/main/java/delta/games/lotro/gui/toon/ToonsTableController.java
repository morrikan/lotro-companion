package delta.games.lotro.gui.toon;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.details.CharacterDetails;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Duration;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.gui.items.chooser.ItemsTableBuilder;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a table that shows all available toons.
 * @author DAM
 */
public class ToonsTableController implements GenericEventsListener<CharacterEvent>
{
  private static final String NAME="NAME";
  private static final String SERVER="SERVER";

  // Data
  private List<CharacterFile> _toons;
  // GUI
  private JTable _table;
  private GenericTableController<CharacterFile> _tableController;

  /**
   * Constructor.
   */
  public ToonsTableController()
  {
    _toons=new ArrayList<CharacterFile>();
    _tableController=buildTable();
    EventsManager.addListener(CharacterEvent.class,this);
  }

  private GenericTableController<CharacterFile> buildTable()
  {
    ListDataProvider<CharacterFile> provider=new ListDataProvider<CharacterFile>(_toons);
    GenericTableController<CharacterFile> table=new GenericTableController<CharacterFile>(provider);

    // Name column
    {
      CellDataProvider<CharacterFile,String> nameCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getName();
        }
      };
      DefaultTableColumnController<CharacterFile,String> nameColumn=new DefaultTableColumnController<CharacterFile,String>(NAME,"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      table.addColumnController(nameColumn);
    }
    // Race column
    {
      CellDataProvider<CharacterFile,Race> raceCell=new CellDataProvider<CharacterFile,Race>()
      {
        @Override
        public Race getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getRace();
        }
      };
      DefaultTableColumnController<CharacterFile,Race> raceColumn=new DefaultTableColumnController<CharacterFile,Race>("Race",Race.class,raceCell);
      raceColumn.setWidthSpecs(100,100,100);
      table.addColumnController(raceColumn);
    }
    // Class column
    {
      CellDataProvider<CharacterFile,CharacterClass> classCell=new CellDataProvider<CharacterFile,CharacterClass>()
      {
        @Override
        public CharacterClass getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getCharacterClass();
        }
      };
      DefaultTableColumnController<CharacterFile,CharacterClass> classColumn=new DefaultTableColumnController<CharacterFile,CharacterClass>("Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(100,100,100);
      table.addColumnController(classColumn);
    }
    // Sex column
    {
      CellDataProvider<CharacterFile,CharacterSex> sexCell=new CellDataProvider<CharacterFile,CharacterSex>()
      {
        @Override
        public CharacterSex getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getCharacterSex();
        }
      };
      DefaultTableColumnController<CharacterFile,CharacterSex> sexColumn=new DefaultTableColumnController<CharacterFile,CharacterSex>("Sex",CharacterSex.class,sexCell);
      sexColumn.setWidthSpecs(80,80,80);
      table.addColumnController(sexColumn);
    }
    // Region column
    {
      CellDataProvider<CharacterFile,String> regionCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getRegion();
        }
      };
      DefaultTableColumnController<CharacterFile,String> regionColumn=new DefaultTableColumnController<CharacterFile,String>("Region",String.class,regionCell);
      regionColumn.setWidthSpecs(100,100,100);
      table.addColumnController(regionColumn);
    }
    // Level column
    {
      CellDataProvider<CharacterFile,Integer> levelCell=new CellDataProvider<CharacterFile,Integer>()
      {
        @Override
        public Integer getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return Integer.valueOf(data.getLevel());
        }
      };
      DefaultTableColumnController<CharacterFile,Integer> serverColumn=new DefaultTableColumnController<CharacterFile,Integer>("Level",Integer.class,levelCell);
      serverColumn.setWidthSpecs(80,80,80);
      table.addColumnController(serverColumn);
    }
    // Server column
    {
      CellDataProvider<CharacterFile,String> serverCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getServer();
        }
      };
      DefaultTableColumnController<CharacterFile,String> serverColumn=new DefaultTableColumnController<CharacterFile,String>(SERVER,"Server",String.class,serverCell);
      serverColumn.setWidthSpecs(100,100,100);
      table.addColumnController(serverColumn);
    }
    // Account column
    {
      CellDataProvider<CharacterFile,String> accountCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          return item.getAccountName();
        }
      };
      DefaultTableColumnController<CharacterFile,String> accountColumn=new DefaultTableColumnController<CharacterFile,String>("Account",String.class,accountCell);
      accountColumn.setWidthSpecs(100,100,100);
      table.addColumnController(accountColumn);
    }
    // Last update time
    /*
    {
      CellDataProvider<CharacterFile,Date> lastUpdateCell=new CellDataProvider<CharacterFile,Date>()
      {
        public Date getData(CharacterFile item)
        {
          return item.getLastInfoUpdate();
        }
      };
      TableColumnController<CharacterFile,Date> lastUpdateColumn=new TableColumnController<CharacterFile,Date>("Last update",Date.class,lastUpdateCell);
      lastUpdateColumn.setWidthSpecs(100,-1,100);
      lastUpdateColumn.setCellRenderer(new DateRenderer(Formats.DATE_PATTERN));
      table.addColumnController(lastUpdateColumn);
    }
    */
    List<DefaultTableColumnController<CharacterFile,?>> detailsColumns=getDetailsColumns();
    for(DefaultTableColumnController<CharacterFile,?> column : detailsColumns)
    {
      table.addColumnController(column);
    }
    String sort=Sort.SORT_ASCENDING+NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+SERVER;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  private List<DefaultTableColumnController<CharacterFile,?>> getDetailsColumns()
  {
    List<DefaultTableColumnController<CharacterFile,?>> ret=new ArrayList<DefaultTableColumnController<CharacterFile,?>>();
    // XP column
    {
      CellDataProvider<CharacterFile,Long> xpCell=new CellDataProvider<CharacterFile,Long>()
      {
        @Override
        public Long getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return Long.valueOf(data.getXp());
        }
      };
      DefaultTableColumnController<CharacterFile,Long> xpColumn=new DefaultTableColumnController<CharacterFile,Long>("XP","XP",Long.class,xpCell);
      xpColumn.setWidthSpecs(80,80,80);
      ret.add(xpColumn);
    }
    // In-game time column
    {
      CellDataProvider<CharacterFile,Integer> cooldownCell=new CellDataProvider<CharacterFile,Integer>()
      {
        @Override
        public Integer getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return Integer.valueOf(data.getIngameTime());
        }
      };
      DefaultTableColumnController<CharacterFile,Integer> cooldownColumn=new DefaultTableColumnController<CharacterFile,Integer>("INGAME_TIME","In-game Time",Integer.class,cooldownCell);
      cooldownColumn.setWidthSpecs(120,120,120);
      DefaultTableCellRenderer renderer=new DefaultTableCellRenderer()
      {
        @Override
        public void setValue(Object value)
        {
          setHorizontalAlignment(SwingConstants.CENTER);
          setText((value == null) ? "" : Duration.getDurationString(((Integer)value).intValue()));
        }
      };
      cooldownColumn.setCellRenderer(renderer);
      ret.add(cooldownColumn);
    }
    // Money
    {
      CellDataProvider<CharacterFile,Money> moneyCell=new CellDataProvider<CharacterFile,Money>()
      {
        @Override
        public Money getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return data.getMoney();
        }
      };
      DefaultTableColumnController<CharacterFile,Money> moneyColumn=new DefaultTableColumnController<CharacterFile,Money>("MONEY","Money",Money.class,moneyCell);
      moneyColumn.setWidthSpecs(120,180,180);
      moneyColumn.setCellRenderer(ItemsTableBuilder.buildMoneyCellRenderer());
      moneyColumn.setComparator(new MoneyComparator());
      ret.add(moneyColumn);
    }
    // Last logout time
    {
      CellDataProvider<CharacterFile,Long> lastLogoutCell=new CellDataProvider<CharacterFile,Long>()
      {
        public Long getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return data.getLastLogoutDate();
        }
      };
      DefaultTableColumnController<CharacterFile,Long> lastLogoutColumn=new DefaultTableColumnController<CharacterFile,Long>("LAST_LOGOUT_DATE","Last logout",Long.class,lastLogoutCell);
      lastLogoutColumn.setWidthSpecs(120,120,120);
      lastLogoutColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(lastLogoutColumn);
    }
    // Title column
    {
      CellDataProvider<CharacterFile,String> titleCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile file)
        {
          String titleName=null;
          Integer titleId=file.getDetails().getCurrentTitleId();
          if (titleId!=null)
          {
            TitleDescription title=TitlesManager.getInstance().getTitle(titleId.intValue());
            if (title!=null)
            {
              titleName=title.getName();
            }
          }
          return titleName;
        }
      };
      DefaultTableColumnController<CharacterFile,String> titleColumn=new DefaultTableColumnController<CharacterFile,String>("TITLE","Title",String.class,titleCell);
      titleColumn.setWidthSpecs(100,-1,200);
      ret.add(titleColumn);
    }

    return ret;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<CharacterFile> getTableController()
  {
    return _tableController;
  }

  private CharacterSummary getDataForToon(CharacterFile toon)
  {
    CharacterSummary summary=toon.getSummary();
    if (summary==null)
    {
      summary=new CharacterSummary();
    }
    return summary;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if ((type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
        || (type==CharacterEventType.CHARACTER_DETAILS_UPDATED))
    {
      CharacterFile toon=event.getToonFile();
      _tableController.refresh(toon);
    }
  }

  /**
   * Set the characters to show.
   * @param toons List of characters to show.
   */
  public void setToons(List<CharacterFile> toons)
  {
    _toons.clear();
    for(CharacterFile toon : toons)
    {
      CharacterSummary summary=toon.getSummary();
      if (summary!=null)
      {
        _toons.add(toon);
      }
    }
    _tableController.refresh();
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
    // Listeners
    EventsManager.removeListener(CharacterEvent.class,this);
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _toons=null;
  }
}
