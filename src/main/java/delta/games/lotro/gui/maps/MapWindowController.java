package delta.games.lotro.gui.maps;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxItem;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.data.categories.CategoriesManager;
import delta.games.lotro.maps.data.links.MapLink;
import delta.games.lotro.maps.ui.DefaultMarkerIconsProvider;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapChooserController;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.MarkerIconProvider;
import delta.games.lotro.maps.ui.filter.MapFilterPanelController;
import delta.games.lotro.maps.ui.filter.MapMarkersFilter;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;
import delta.games.lotro.maps.ui.navigation.MapViewDefinition;
import delta.games.lotro.maps.ui.navigation.NavigationListener;
import delta.games.lotro.maps.ui.navigation.NavigationSupport;

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
  private SimpleMarkersProvider _markersProvider;
  // UI controllers
  private MapPanelController _mapPanel;
  private MapFilterPanelController _filter;
  private MapChooserController _mapChooser;
  // Navigation
  private NavigationSupport _navigation;
  // Layers
  private RadarMapLayer _radarLayer;

  /**
   * Constructor.
   * @param mapsManager Maps manager.
   */
  public MapWindowController(MapsManager mapsManager)
  {
    _mapPanel=new MapPanelController(mapsManager);
    MapCanvas canvas=_mapPanel.getCanvas();
    // Radar layer
    DataFacade facade=new DataFacade();
    RadarImageProvider provider=new DatRadarImageProvider(facade);
    _radarLayer=new RadarMapLayer(1,provider);
    canvas.addLayer(_radarLayer);

    // Setup navigation
    _navigation=new NavigationSupport(canvas);
    _navigation.getNavigationListeners().addListener(this);
    // Markers filter
    MapMarkersFilter filter=new MapMarkersFilter();
    CategoriesManager categoriesManager=mapsManager.getCategories();
    _filter=new MapFilterPanelController(categoriesManager,filter,_mapPanel);
    // Markers layer
    MarkerIconProvider iconsProvider=new DefaultMarkerIconsProvider(categoriesManager);
    _markersProvider=new SimpleMarkersProvider();
    MarkersLayer markersLayer=new MarkersLayer(iconsProvider,_markersProvider);
    markersLayer.setFilter(filter);
    canvas.addLayer(markersLayer);
    // Map chooser
    _mapChooser=new MapChooserController(_navigation,mapsManager.getBasemapsManager());
  }

  /**
   * Get the managed map canvas.
   * @return the managed map canvas.
   */
  public MapCanvas getMapCanvas()
  {
    return _mapPanel.getCanvas();
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
    GeoreferencedBasemapsManager mapsManager=_mapPanel.getCanvas().getBasemapsManager();
    int key=mapViewDefinition.getMapKey();
    GeoreferencedBasemap map=mapsManager.getMapById(key);
    if (map==null)
    {
      return;
    }
    // Setup map
    _mapPanel.setMap(mapViewDefinition);
    _mapChooser.selectMap(key);
    pack();
    // Radar map
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap parchmentMap=parchmentMapsMgr.getMapById(key);
    int region=0;
    if (parchmentMap!=null)
    {
      region=parchmentMap.getRegion();
    }
    _radarLayer.setRegion(region);
    // - reset radar map cache on map change to avoid too much memory consumption
    _radarLayer.resetCache();
    // Markers
    updateMarkers(key);
    // Links
    List<MapLink> links=new MapLinksFactory().getLinks(key);
    _navigation.setLinks(links);
  }

  private void updateMarkers(int mapId)
  {
    List<Marker> markers=new MapMarkersFactory().getMarkers(mapId);
    _markersProvider.setMarkers(markers);
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
    GeoreferencedBasemapsManager mapsManager=_mapPanel.getCanvas().getBasemapsManager();
    GeoreferencedBasemap map=mapsManager.getMapById(268437716); // Bree
    setMap(map.getIdentifier());
    frame.setTitle("Middle Earth maps");
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
    // Top
    JPanel topPanel=buildTopPanel();
    panel.add(topPanel,BorderLayout.NORTH);
    // Center
    JLayeredPane layers=_mapPanel.getLayers();
    panel.add(layers,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel topPanel=GuiFactory.buildPanel(new GridBagLayout());
    // Map chooser
    JComboBox<ComboBoxItem<GeoreferencedBasemap>> mapChooserCombo=_mapChooser.getCombo();
    JPanel chooserPanel=GuiFactory.buildPanel(new BorderLayout());
    chooserPanel.add(mapChooserCombo,BorderLayout.CENTER);
    TitledBorder mapChooserBorder=GuiFactory.buildTitledBorder("Map chooser");
    chooserPanel.setBorder(mapChooserBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    topPanel.add(chooserPanel,c);
    // Markers filter
    JPanel filterPanel=_filter.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    c.gridx=2;
    topPanel.add(filterPanel,c);
    c.gridx=3;
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    JPanel emptyPanel=GuiFactory.buildPanel(new FlowLayout());
    topPanel.add(emptyPanel,c);
    return topPanel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    _markersProvider=null;
    if (_filter!=null)
    {
      _filter.dispose();
      _filter=null;
    }
    if (_mapChooser==null)
    {
      _mapChooser.dispose();
      _mapChooser=null;
    }
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
    _radarLayer=null;
    super.dispose();
  }
}
