package delta.games.lotro.gui.items.essences;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.games.lotro.gui.items.AbstractItemFilterPanelController;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.utils.gui.combobox.ComboBoxController;
import delta.games.lotro.utils.gui.combobox.ItemSelectionListener;
import delta.games.lotro.utils.gui.text.DynamicTextEditionController;
import delta.games.lotro.utils.gui.text.TextListener;

/**
 * Controller for a essence filter edition panel.
 * @author DAM
 */
public class EssenceFilterController extends AbstractItemFilterPanelController
{
  // Data
  private EssenceItemFilter _filter;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  // Controllers
  private ComboBoxController<Integer> _tier;
  private ComboBoxController<ItemQuality> _quality;
  private DynamicTextEditionController _textController;

  /**
   * Constructor.
   * @param filter Item filter.
   */
  public EssenceFilterController(EssenceItemFilter filter)
  {
    _filter=filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      updateFilter();
    }
    return _panel;
  }

  private void setFilter()
  {
    // Name
    String contains=_filter.getNameFilter().getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Quality
    ItemQuality quality=_filter.getQualityFilter().getQuality();
    _quality.selectItem(quality);
    // Tier
    Integer tier=_filter.getEssenceTierFilter().getTier();
    _tier.selectItem(tier);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Tier
    {
      JPanel tierPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      tierPanel.add(GuiFactory.buildLabel("Tier:"));
      _tier=buildTierCombo();
      ItemSelectionListener<Integer> tierListener=new ItemSelectionListener<Integer>()
      {
        public void itemSelected(Integer tier)
        {
          _filter.getEssenceTierFilter().setTier(tier);
          updateFilter();
        }
      };
      _tier.addListener(tierListener);
      tierPanel.add(_tier.getComboBox());
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(tierPanel,c);
    }
    // Quality
    {
      JPanel qualityPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      qualityPanel.add(GuiFactory.buildLabel("Quality:"));
      _quality=ItemUiTools.buildQualityCombo();
      ItemSelectionListener<ItemQuality> qualityListener=new ItemSelectionListener<ItemQuality>()
      {
        public void itemSelected(ItemQuality quality)
        {
          _filter.getQualityFilter().setQuality(quality);
          updateFilter();
        }
      };
      _quality.addListener(qualityListener);
      qualityPanel.add(_quality.getComboBox());
      GridBagConstraints c=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(qualityPanel,c);
    }
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      containsPanel.add(GuiFactory.buildLabel("Label filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          _filter.getNameFilter().setPattern(newText);
          updateFilter();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      GridBagConstraints c=new GridBagConstraints(0,1,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(containsPanel,c);
    }
    return panel;
  }

  /**
   * Build a controller for a combo box to choose an essence tier.
   * @return A new controller.
   */
  private ComboBoxController<Integer> buildTierCombo()
  {
    JComboBox combo=GuiFactory.buildComboBox();
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>(combo);
    ctrl.addEmptyItem("");
    for(int tier=1;tier<=8;tier++)
    {
      ctrl.addItem(Integer.valueOf(tier),"Tier "+tier);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _filter=null;
    // Controllers
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
    if (_quality!=null)
    {
      _quality.dispose();
      _quality=null;
    }
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}