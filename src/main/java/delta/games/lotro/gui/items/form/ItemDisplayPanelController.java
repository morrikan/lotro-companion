package delta.games.lotro.gui.items.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemSturdiness;

/**
 * Controller for an item display panel.
 * @author DAM
 */
public class ItemDisplayPanelController implements NavigablePanelController
{
  // Data
  private Item _item;
  // GUI
  private JPanel _panel;
  // Controllers
  private ItemReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item to show.
   */
  public ItemDisplayPanelController(NavigatorWindowController parent, Item item)
  {
    //_parent=parent;
    _item=item;
    _references=new ItemReferencesDisplayController(parent);
    _references.setItem(item.getIdentifier());
  }

  @Override
  public String getTitle()
  {
    return "Item: "+_item.getName();
  }

  @Override
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // References panel
    JEditorPane references=_references.getComponent();
    JScrollPane referencesPane=GuiFactory.buildScrollPane(references);
    referencesPane.setBorder(GuiFactory.buildTitledBorder("References"));
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(referencesPane,c);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      ImageIcon icon=LotroIconsManager.getItemIcon(_item.getIcon());
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      // Name
      String name=_item.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Category
    String category=_item.getSubCategory();
    if ((category!=null) && (category.length()>0))
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Category: "));
      JLabel categoryLabel=GuiFactory.buildLabel(category);
      panelLine.add(categoryLabel);
    }
    // Slots
    int nbSlots=_item.getEssenceSlots();
    if (nbSlots>0)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Essence slots: "));
      JLabel slotsLabel=GuiFactory.buildLabel(String.valueOf(nbSlots));
      panelLine.add(slotsLabel);
    }
    // Durability
    Integer durability=_item.getDurability();
    if (durability!=null)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Durability: "));
      JLabel durabilityLabel=GuiFactory.buildLabel(durability.toString());
      panelLine.add(durabilityLabel);
    }
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(_item.getUsageRequirements());
    if (requirements.length()>0)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Requirements: "));
      JLabel requirementsLabel=GuiFactory.buildLabel(requirements);
      panelLine.add(requirementsLabel);
    }
    // Attributes
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      String attributes=buildAttributesString();
      JLabel attributesLabel=GuiFactory.buildLabel(attributes);
      panelLine.add(attributesLabel);
    }

    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    panel.add(paddingPanel,c);

    return panel;
  }

  /**
   * Build an attributes string.
   * @return A string, empty if no requirement.
   */
  private String buildAttributesString()
  {
    StringBuilder sb=new StringBuilder();
    // Unique?
    boolean unique=_item.isUnique();
    if (unique)
    {
      sb.append("Unique");
    }
    // Binding
    ItemBinding binding=_item.getBinding();
    if (binding!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(binding.toString());
    }
    // Sturdiness
    ItemSturdiness sturdiness=_item.getSturdiness();
    if (sturdiness!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(sturdiness.toString());
    }
    // Stacks
    Integer stack=_item.getStackMax();
    if ((stack!=null) && (stack.intValue()>1))
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Stacks to ");
      sb.append(stack);
    }
    String ret=sb.toString();
    return ret;
  }

  @Override
  public void dispose()
  {
    // Data
    _item=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    //_parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}