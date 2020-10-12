package delta.games.lotro.gui.maps.resources;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.utils.maps.Maps;

/**
 * Test class for the resources map panel controller.
 * @author DAM
 */
public class MainTestResourcesMapPanelController
{
  private DataFacade _facade=new DataFacade();

  private void doIt()
  {
    ResourcesMapsManager mapsMgr=ResourcesMapsManager.getInstance();
    for(ResourcesMapDescriptor mapDescriptor : mapsMgr.getResourcesMaps())
    {
      if (doMap(mapDescriptor))
      {
        doResourcesMap(mapDescriptor);
      }
    }
  }

  private boolean doMap(ResourcesMapDescriptor map)
  {
    CraftingLevel level=map.getLevel();
    if (!"PROSPECTOR".equals(level.getProfession().getKey())) return false;
    //if (level.getTier()!=6) return false;
    return true;
  }

  private void doResourcesMap(ResourcesMapDescriptor map)
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    GeoreferencedBasemapsManager basemapsMgr=Maps.getMaps().getMapsManager().getBasemapsManager();
    JLayeredPane mapPanel=null;
    for(Integer mapId : map.getMapIds())
    {
      ResourcesMapPanelController ctrl=new ResourcesMapPanelController(_facade,map,mapId.intValue());
      MapPanelController panelCtrl=ctrl.getMapPanelController();
      JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      mapPanel=panelCtrl.getLayers();
      GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(mapPanel,c);
      GeoreferencedBasemap basemap=basemapsMgr.getMapById(mapId.intValue());
      String title=basemap.getName();
      tabbedPane.add(title,panel);
    }
    JFrame f=new JFrame();
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.getContentPane().add(tabbedPane,BorderLayout.CENTER);
    f.setTitle(map.getLevel().getName());
    f.pack();
    f.setResizable(false);
    f.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestResourcesMapPanelController().doIt();
  }
}
