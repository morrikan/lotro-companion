package delta.games.lotro.gui.lore.instances;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.instances.SkirmishPrivateEncounter;
import delta.games.lotro.lore.instances.filters.PrivateEncounterCategoryFilter;
import delta.games.lotro.lore.instances.filters.PrivateEncounterNameFilter;

/**
 * Instance filter.
 * @author DAM
 */
public class InstancesFilter implements Filter<SkirmishPrivateEncounter>
{
  private Filter<SkirmishPrivateEncounter> _filter;

  private PrivateEncounterNameFilter _nameFilter;
  private PrivateEncounterCategoryFilter _categoryFilter;

  /**
   * Constructor.
   */
  public InstancesFilter()
  {
    List<Filter<SkirmishPrivateEncounter>> filters=new ArrayList<Filter<SkirmishPrivateEncounter>>();
    // Name
    _nameFilter=new PrivateEncounterNameFilter();
    filters.add(_nameFilter);
    // Category
    _categoryFilter=new PrivateEncounterCategoryFilter(null);
    filters.add(_categoryFilter);
    _filter=new CompoundFilter<SkirmishPrivateEncounter>(Operator.AND,filters);
  }

  /**
   * Get the filter on instance name.
   * @return an instance name filter.
   */
  public PrivateEncounterNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on instance category.
   * @return an instance category filter.
   */
  public PrivateEncounterCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  @Override
  public boolean accept(SkirmishPrivateEncounter item)
  {
    return _filter.accept(item);
  }
}
