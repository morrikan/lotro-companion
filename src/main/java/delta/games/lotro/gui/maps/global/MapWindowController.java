package delta.games.lotro.gui.maps.global;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.DatRadarImageProvider;
import delta.games.lotro.gui.maps.MarkerSelectionListener;
import delta.games.lotro.gui.maps.RadarMapLayer;
import delta.games.lotro.gui.maps.basemap.DatBasemapImageProvider;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.categories.CategoriesConstants;
import delta.games.lotro.maps.data.categories.CategoriesManager;
import delta.games.lotro.maps.data.links.MapLink;
import delta.games.lotro.maps.data.markers.filters.MarkerCategoryFilter;
import delta.games.lotro.maps.ui.BasemapPanelController;
import delta.games.lotro.maps.ui.DefaultMarkerIconsProvider;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.filter.MapFilterPanelController;
import delta.games.lotro.maps.ui.layers.BasemapLayer;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.maps.ui.navigation.MapViewDefinition;
import delta.games.lotro.maps.ui.navigation.NavigationListener;
import delta.games.lotro.maps.ui.navigation.NavigationSupport;
import delta.games.lotro.maps.ui.selection.SelectionManager;
import delta.games.lotro.utils.dat.DatInterface;

/**
 * Controller for a map window.
 * @author DAM
 */
public class MapWindowController extends DefaultWindowController implements NavigationListener
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="MAP";

  // Data
  private MapsManager _mapsManager;
  private SimpleMarkersProvider _markersProvider;
  // UI controllers
  private BasemapPanelController _mapPanel;
  // Navigation
  private NavigationSupport _navigation;
  private NavigationMenuController _navigationMenuController;
  // Layers
  private RadarMapLayer _radarLayer;

  /**
   * Constructor.
   * @param mapsManager Maps manager.
   */
  public MapWindowController(MapsManager mapsManager)
  {
    _mapsManager=mapsManager;
    _mapPanel=new BasemapPanelController(mapsManager.getBasemapsManager());
    MapCanvas canvas=_mapPanel.getCanvas();
    // Radar layer
    DataFacade facade=DatInterface.getInstance().getFacade();
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    _radarLayer=new RadarMapLayer(1,provider);
    canvas.addLayer(_radarLayer);
    // Basemap layer
    BasemapLayer basemapLayer=_mapPanel.getBasemapLayer();
    DatBasemapImageProvider imageProvider=new DatBasemapImageProvider(facade);
    basemapLayer.setBasemapImageProvider(imageProvider);

    // Setup navigation
    _navigation=new NavigationSupport(_mapPanel);
    _navigation.getNavigationListeners().addListener(this);
    // Markers filter UI
    CategoriesManager categoriesManager=mapsManager.getCategories();
    MapFilterPanelController mapFilterCtrl=new MapFilterPanelController(categoriesManager,_mapPanel.getCanvas());
    removeCategories(mapFilterCtrl.getFilter().getCategoryFilter());
    _mapPanel.getMapPanelController().addFilterButton(mapFilterCtrl);
    // Markers layer
    MarkerIconProvider iconsProvider=new DefaultMarkerIconsProvider(categoriesManager);
    _markersProvider=new SimpleMarkersProvider();
    MarkersLayer markersLayer=new MarkersLayer(iconsProvider,_markersProvider);
    markersLayer.setFilter(mapFilterCtrl.getFilter());
    canvas.addLayer(markersLayer);
    // Setup selection manager
    SelectionManager selectionMgr=_mapPanel.getMapPanelController().getSelectionManager();
    selectionMgr.addListener(new MarkerSelectionListener(this));
  }

  private void removeCategories(MarkerCategoryFilter filter)
  {
    filter.removeCategory(CategoriesConstants.CROP);
    filter.removeCategory(CategoriesConstants.CRITTER);
    filter.removeCategory(CategoriesConstants.MONSTER);
    filter.removeCategory(CategoriesConstants.RESOURCE_NODE);
  }

  private GeoreferencedBasemapsManager getBasemapsManager()
  {
    return _mapsManager.getBasemapsManager();
  }

  @Override
  public void mapChangeRequest(MapViewDefinition mapViewDefinition)
  {
    setupMap(mapViewDefinition);
  }

  private void setMap(int mapKey)
  {
    MapViewDefinition newMapView=new MapViewDefinition(mapKey,null,null);
    setupMap(newMapView);
  }

  private void setupMap(MapViewDefinition mapViewDefinition)
  {
    GeoreferencedBasemapsManager basemapsManager=getBasemapsManager();
    int mapId=mapViewDefinition.getMapKey();
    GeoreferencedBasemap map=basemapsManager.getMapById(mapId);
    if (map==null)
    {
      return;
    }
    // Setup map
    _mapPanel.setMap(mapViewDefinition);
    pack();
    // Radar map
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(mapId);
    int region=0;
    if (parchmentMap!=null)
    {
      region=parchmentMap.getRegion();
    }
    _radarLayer.setRegion(region);
    // - reset radar map cache on map change to avoid too much memory consumption
    _radarLayer.resetCache();
    // Markers
    List<Marker> markers=new MapMarkersFactory().getMarkers(mapId);
    _markersProvider.setMarkers(markers);
    // Links
    List<MapLink> links=new MapLinksFactory().getLinks(mapId);
    _navigation.setLinks(links);
    // Title
    String title=map.getName();
    setTitle(title);
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Set initial map (Middle-earth or Bree)
    GeoreferencedBasemapsManager basemapsManager=getBasemapsManager();
    ParchmentMap rootMap=ParchmentMapsManager.getInstance().getRootMap();
    int initialMapId=(rootMap!=null)?rootMap.getIdentifier():268437716;
    GeoreferencedBasemap map=basemapsManager.getMapById(initialMapId);
    setMap(map.getIdentifier());
    // Menu
    _navigationMenuController=new NavigationMenuController(_navigation);
    JMenuBar menuBar=_navigationMenuController.getMenuBar();
    frame.setJMenuBar(menuBar);
    // Size & position
    frame.setLocation(100,100);
    frame.pack();
    frame.setResizable(false);
    frame.getContentPane().setBackground(GuiFactory.getBackgroundColor());
    return frame;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    // Center
    Component mapComponent=_mapPanel.getComponent();
    panel.add(mapComponent,BorderLayout.CENTER);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    _markersProvider=null;
    if (_mapPanel!=null)
    {
      _mapPanel.dispose();
      _mapPanel=null;
    }
    if (_navigation!=null)
    {
      _navigation.dispose();
      _navigation=null;
    }
    if (_navigationMenuController!=null)
    {
      _navigationMenuController.dispose();
      _navigationMenuController=null;
    }
    _radarLayer=null;
    super.dispose();
  }
}
