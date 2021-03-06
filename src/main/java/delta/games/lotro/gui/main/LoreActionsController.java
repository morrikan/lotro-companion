package delta.games.lotro.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.deed.explorer.DeedsExplorerWindowController;
import delta.games.lotro.gui.emotes.explorer.EmotesExplorerWindowController;
import delta.games.lotro.gui.lore.trade.barter.explorer.BarterersExplorerWindowController;
import delta.games.lotro.gui.lore.trade.vendor.explorer.VendorsExplorerWindowController;
import delta.games.lotro.gui.mounts.explorer.MountsExplorerWindowController;
import delta.games.lotro.gui.pets.explorer.PetsExplorerWindowController;
import delta.games.lotro.gui.quests.explorer.QuestsExplorerWindowController;
import delta.games.lotro.gui.recipes.explorer.RecipesExplorerWindowController;
import delta.games.lotro.gui.titles.explorer.TitlesExplorerWindowController;
import delta.games.lotro.gui.utils.SharedUiUtils;

/**
 * Controller for the 'lore' actions (toolbar and menu).
 * @author DAM
 */
public class LoreActionsController implements ActionListener,Disposable
{
  private static final String DEEDS_ID="deeds";
  private static final String QUESTS_ID="quests";
  private static final String RECIPES_ID="recipes";
  private static final String TITLES_ID="titles";
  private static final String EMOTES_ID="emotes";
  private static final String MOUNTS_ID="mounts";
  private static final String PETS_ID="pets";
  private static final String VENDORS_ID="vendors";
  private static final String BARTERERS_ID="barterers";

  private WindowController _parent;
  private WindowsManager _windowsManager;
  private ToolbarController _loreToolbar;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param windowsManager Windows manager.
   */
  public LoreActionsController(WindowController parent, WindowsManager windowsManager)
  {
    _parent=parent;
    _windowsManager=windowsManager;
  }

  /**
   * Build the 'compendium' menu.
   * @return a new menu.
   */
  public JMenu buildCompendiumMenu()
  {
    // Compendium
    JMenu compendiumMenu=GuiFactory.buildMenu("Compendium");
    // - deeds
    JMenuItem deedsExplorer=GuiFactory.buildMenuItem("Deeds");
    deedsExplorer.setActionCommand(DEEDS_ID);
    deedsExplorer.addActionListener(this);
    compendiumMenu.add(deedsExplorer);
    // - quests
    JMenuItem questsExplorer=GuiFactory.buildMenuItem("Quests");
    questsExplorer.setActionCommand(QUESTS_ID);
    questsExplorer.addActionListener(this);
    compendiumMenu.add(questsExplorer);
    // - recipes
    JMenuItem recipesExplorer=GuiFactory.buildMenuItem("Recipes");
    recipesExplorer.setActionCommand(RECIPES_ID);
    recipesExplorer.addActionListener(this);
    compendiumMenu.add(recipesExplorer);
    // - titles
    JMenuItem titlesExplorer=GuiFactory.buildMenuItem("Titles");
    titlesExplorer.setActionCommand(TITLES_ID);
    titlesExplorer.addActionListener(this);
    compendiumMenu.add(titlesExplorer);
    // - emotes
    JMenuItem emotesExplorer=GuiFactory.buildMenuItem("Emotes");
    emotesExplorer.setActionCommand(EMOTES_ID);
    emotesExplorer.addActionListener(this);
    compendiumMenu.add(emotesExplorer);
    // - mounts
    JMenuItem mountsExplorer=GuiFactory.buildMenuItem("Mounts");
    mountsExplorer.setActionCommand(MOUNTS_ID);
    mountsExplorer.addActionListener(this);
    compendiumMenu.add(mountsExplorer);
    // - pets
    JMenuItem petsExplorer=GuiFactory.buildMenuItem("Pets");
    petsExplorer.setActionCommand(PETS_ID);
    petsExplorer.addActionListener(this);
    compendiumMenu.add(petsExplorer);
    // - vendors
    JMenuItem vendorsExplorer=GuiFactory.buildMenuItem("Vendors");
    vendorsExplorer.setActionCommand(VENDORS_ID);
    vendorsExplorer.addActionListener(this);
    compendiumMenu.add(vendorsExplorer);
    // - barterers
    JMenuItem barterersExplorer=GuiFactory.buildMenuItem("Barterers");
    barterersExplorer.setActionCommand(BARTERERS_ID);
    barterersExplorer.addActionListener(this);
    compendiumMenu.add(barterersExplorer);

    return compendiumMenu;
  }

  /**
   * Build the 'lore' toolbar.
   * @return a new toobar.
   */
  public ToolbarController buildToolbarLore()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Deeds icon
    String deedsIconPath=SharedUiUtils.getToolbarIconPath("deeds");
    ToolbarIconItem deedsIconItem=new ToolbarIconItem(DEEDS_ID,deedsIconPath,DEEDS_ID,"Deeds...","Deeds");
    model.addToolbarIconItem(deedsIconItem);
    // Quests icon
    String questsIconPath=SharedUiUtils.getToolbarIconPath("quests");
    ToolbarIconItem questsIconItem=new ToolbarIconItem(QUESTS_ID,questsIconPath,QUESTS_ID,"Quests...","Quests");
    model.addToolbarIconItem(questsIconItem);
    // Recipes icon
    String recipesIconPath=SharedUiUtils.getToolbarIconPath("recipes");
    ToolbarIconItem recipesIconItem=new ToolbarIconItem(RECIPES_ID,recipesIconPath,RECIPES_ID,"Recipes...","Recipes");
    model.addToolbarIconItem(recipesIconItem);
    // Titles icon
    String titlesIconPath=SharedUiUtils.getToolbarIconPath("titles");
    ToolbarIconItem titlesIconItem=new ToolbarIconItem(TITLES_ID,titlesIconPath,TITLES_ID,"Titles...","Titles");
    model.addToolbarIconItem(titlesIconItem);
    // Emotes icon
    String emotesIconPath=SharedUiUtils.getToolbarIconPath("emotes");
    ToolbarIconItem emotesIconItem=new ToolbarIconItem(EMOTES_ID,emotesIconPath,EMOTES_ID,"Emotes...","Emotes");
    model.addToolbarIconItem(emotesIconItem);
    // Mounts icon
    String mountsIconPath=SharedUiUtils.getToolbarIconPath("mounts");
    ToolbarIconItem mountsIconItem=new ToolbarIconItem(MOUNTS_ID,mountsIconPath,MOUNTS_ID,"Mounts...","Mounts");
    model.addToolbarIconItem(mountsIconItem);
    // Pets icon
    String petsIconPath=SharedUiUtils.getToolbarIconPath("pets");
    ToolbarIconItem petsIconItem=new ToolbarIconItem(PETS_ID,petsIconPath,PETS_ID,"Pets...","Pets");
    model.addToolbarIconItem(petsIconItem);
    // Vendors icon
    String vendorsIconPath=SharedUiUtils.getToolbarIconPath("vendors");
    ToolbarIconItem vendorsIconItem=new ToolbarIconItem(VENDORS_ID,vendorsIconPath,VENDORS_ID,"Vendors...","Vendors");
    model.addToolbarIconItem(vendorsIconItem);
    // Barterers icon
    String barterersIconPath=SharedUiUtils.getToolbarIconPath("barterers");
    ToolbarIconItem barterersIconItem=new ToolbarIconItem(BARTERERS_ID,barterersIconPath,BARTERERS_ID,"Barterers...","Barterers");
    model.addToolbarIconItem(barterersIconItem);
    // Border
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder("Lore Compendium"));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (DEEDS_ID.equals(cmd))
    {
      doDeeds();
    }
    else if (QUESTS_ID.equals(cmd))
    {
      doQuests();
    }
    else if (RECIPES_ID.equals(cmd))
    {
      doRecipes();
    }
    else if (TITLES_ID.equals(cmd))
    {
      doTitles();
    }
    else if (EMOTES_ID.equals(cmd))
    {
      doEmotes();
    }
    else if (MOUNTS_ID.equals(cmd))
    {
      doMounts();
    }
    else if (PETS_ID.equals(cmd))
    {
      doPets();
    }
    else if (VENDORS_ID.equals(cmd))
    {
      doVendors();
    }
    else if (BARTERERS_ID.equals(cmd))
    {
      doBarterers();
    }
  }

  private void doDeeds()
  {
    WindowController controller=_windowsManager.getWindow(DeedsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new DeedsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doQuests()
  {
    WindowController controller=_windowsManager.getWindow(QuestsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new QuestsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doRecipes()
  {
    WindowController controller=_windowsManager.getWindow(RecipesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new RecipesExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doTitles()
  {
    WindowController controller=_windowsManager.getWindow(TitlesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new TitlesExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doEmotes()
  {
    WindowController controller=_windowsManager.getWindow(EmotesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new EmotesExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doMounts()
  {
    WindowController controller=_windowsManager.getWindow(MountsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new MountsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doPets()
  {
    WindowController controller=_windowsManager.getWindow(PetsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new PetsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doVendors()
  {
    WindowController controller=_windowsManager.getWindow(VendorsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new VendorsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doBarterers()
  {
    WindowController controller=_windowsManager.getWindow(BarterersExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new BarterersExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_loreToolbar!=null)
    {
      _loreToolbar.dispose();
      _loreToolbar=null;
    }
  }
}
