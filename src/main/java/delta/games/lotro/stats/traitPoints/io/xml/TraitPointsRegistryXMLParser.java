package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.io.xml.ItemXMLConstants;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointsRegistry;

/**
 * Parser for the trait point registry stored in XML.
 * @author DAM
 */
public class TraitPointsRegistryXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed registry or <code>null</code>.
   */
  public TraitPointsRegistry parseXML(File source)
  {
    TraitPointsRegistry registry=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      registry=parseRegistry(root);
    }
    return registry;
  }

  private TraitPointsRegistry parseRegistry(Element root)
  {
    TraitPointsRegistry registry=new TraitPointsRegistry();
    List<Element> pointTags=DOMParsingTools.getChildTagsByName(root,TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG,false);
    for(Element pointTag : pointTags)
    {
      TraitPoint item=parsePoint(pointTag);
      registry.registerTraitPoint(item);
    }
    return registry;
  }

  /**
   * Build a trait point from an XML tag.
   * @param pointTag Point tag.
   * @return A trait point.
   */
  public TraitPoint parsePoint(Element pointTag)
  {
    TraitPoint ret=null;
    NamedNodeMap attrs=pointTag.getAttributes();

    // Identifier
    String id=DOMParsingTools.getStringAttribute(attrs,TraitPointsRegistryXMLConstants.TRAIT_POINT_ID_ATTR,null);
    if (id!=null)
    {
      ret=new TraitPoint(id);
      // Label
      String label=DOMParsingTools.getStringAttribute(attrs,TraitPointsRegistryXMLConstants.TRAIT_POINT_LABEL_ATTR,null);
      if (label!=null)
      {
        ret.setLabel(label);
      }
      // Category
      String category=DOMParsingTools.getStringAttribute(attrs,TraitPointsRegistryXMLConstants.TRAIT_POINT_CATEGORY_ATTR,null);
      if (category!=null)
      {
        ret.setCategory(category);
      }
      // Required class
      String requiredClassesStr=DOMParsingTools.getStringAttribute(attrs,ItemXMLConstants.ITEM_REQUIRED_CLASS_ATTR,null);
      if (requiredClassesStr!=null)
      {
        String[] requiredClasses=requiredClassesStr.split(",");
        for(String requiredClass : requiredClasses)
        {
          CharacterClass characterClass=CharacterClass.getByKey(requiredClass);
          ret.addRequiredClass(characterClass);
        }
      }
    }
    return ret;
  }
}
