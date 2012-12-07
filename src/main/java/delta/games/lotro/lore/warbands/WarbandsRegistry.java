package delta.games.lotro.lore.warbands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.Config;
import delta.games.lotro.lore.warbands.io.xml.WarbandsRegistryXMLParser;

/**
 * Registry for warbands.
 * @author DAM
 */
public class WarbandsRegistry
{
  private static WarbandsRegistry _registry=null;
  private HashMap<String,WarbandDefinition> _mapByName;

  /**
   * Constructor.
   */
  public WarbandsRegistry()
  {
    _mapByName=new HashMap<String,WarbandDefinition>();
  }

  public static WarbandsRegistry getWarbandsRegistry()
  {
    if (_registry==null)
    {
      Config cfg=Config.getInstance();
      File configDir=cfg.getConfigDir();
      File warbandsFile=new File(configDir,"warbands.xml");
      WarbandsRegistryXMLParser parser=new WarbandsRegistryXMLParser();
      _registry=parser.parseXML(warbandsFile);
    }
    return _registry;
  }

  public void addWarband(WarbandDefinition warband)
  {
    String name=warband.getName();
    WarbandDefinition old=_mapByName.get(name);
    if (old==null)
    {
      _mapByName.put(name,warband);
    }
  }

  public WarbandDefinition[] getAllWarbands()
  {
    List<String> names=new ArrayList<String>(_mapByName.keySet());
    Collections.sort(names);
    List<WarbandDefinition> warbands=new ArrayList<WarbandDefinition>();
    for(String name : names)
    {
      warbands.add(getByName(name));
    }
    WarbandDefinition[] ret=warbands.toArray(new WarbandDefinition[_mapByName.size()]);
    return ret;
  }

  public WarbandDefinition getByName(String name)
  {
    return _mapByName.get(name);
  }
}
