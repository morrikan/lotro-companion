package delta.games.lotro.stats.crafting;

import java.util.List;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.CraftingStatus;

/**
 * Test for crafting stats.
 * @author DAM
 */
public class MainTestCraftingStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    for(CharacterFile toon : toons)
    {
      //CharacterFile toon=utils.getMainToon();
      //CharacterFile toon=utils.getToonByName("Feroce");
      CraftingStatus stats=toon.getCraftingMgr().getCraftingStatus();
      stats.dump(System.out);
    }
  }
}
