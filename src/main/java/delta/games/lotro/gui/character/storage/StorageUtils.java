package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.Chest;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.Vault;
import delta.games.lotro.character.storage.Wallet;
import delta.games.lotro.character.storage.io.xml.StorageIO;
import delta.games.lotro.character.storage.location.SimpleStorageLocation;
import delta.games.lotro.character.storage.location.SimpleStorageLocation.LocationType;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.common.owner.AccountOwner;
import delta.games.lotro.common.owner.AccountServerOwner;
import delta.games.lotro.common.owner.CharacterOwner;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.lore.items.CountedItem;

/**
 * Utility methods for storage management.
 * @author DAM
 */
public class StorageUtils
{
  /**
   * Build character items.
   * @param toon Targeted character.
   * @param characterStorage Storage for this character.
   * @return A list of stored items.
   */
  public static List<StoredItem> buildCharacterItems(CharacterFile toon, CharacterStorage characterStorage)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    // Build owner
    AccountOwner accountOwner=new AccountOwner("???");
    String server=toon.getServerName();
    AccountServerOwner accountServer=new AccountServerOwner(accountOwner,server);
    String characterName=toon.getName();
    CharacterOwner owner=new CharacterOwner(accountServer,characterName);

    // Own bags
    {
      Vault container=characterStorage.getBags();
      List<StoredItem> storedItems=getAllItems(owner,container,LocationType.BAG);
      items.addAll(storedItems);
    }
    // Own vault
    {
      Vault container=characterStorage.getOwnVault();
      List<StoredItem> storedItems=getAllItems(owner,container,LocationType.VAULT);
      items.addAll(storedItems);
    }
    // Own wallet
    {
      Wallet ownWallet=characterStorage.getWallet();
      List<StoredItem> storedItems=getAllItems(owner,ownWallet,LocationType.WALLET);
      items.addAll(storedItems);
    }
    return items;
  }

  /**
   * Build account/server items.
   * @param account Targeted account.
   * @param serverName Targeted server.
   * @return A list of stored items.
   */
  public static List<StoredItem> buildAccountItems(Account account, String serverName)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();

    String accountName=account.getName();
    AccountOwner accountOwner=new AccountOwner(accountName);
    AccountServerOwner accountServer=new AccountServerOwner(accountOwner,serverName);

    // Characters
    List<CharacterFile> characters=AccountUtils.getCharacters(accountName,serverName);
    for(CharacterFile character : characters)
    {
      // Build owner
      String characterName=character.getName();
      CharacterOwner owner=new CharacterOwner(accountServer,characterName);
  
      CharacterStorage characterStorage=StorageIO.loadCharacterStorage(character);
      if (characterStorage!=null)
      {
        // Own bags
        {
          Vault container=characterStorage.getBags();
          List<StoredItem> storedItems=getAllItems(owner,container,LocationType.BAG);
          items.addAll(storedItems);
        }
        // Own vault
        {
          Vault container=characterStorage.getOwnVault();
          List<StoredItem> storedItems=getAllItems(owner,container,LocationType.VAULT);
          items.addAll(storedItems);
        }
        // Own wallet
        {
          Wallet ownWallet=characterStorage.getWallet();
          List<StoredItem> storedItems=getAllItems(owner,ownWallet,LocationType.WALLET);
          items.addAll(storedItems);
        }
      }
    }
    // Account/server storage
    if (account!=null)
    {
      // Shared vault
      Vault sharedVault=StorageIO.loadAccountSharedVault(account,serverName);
      if (sharedVault!=null)
      {
        List<StoredItem> storedItems=getAllItems(accountServer,sharedVault,LocationType.SHARED_VAULT);
        items.addAll(storedItems);
      }
      // Shared wallet
      Wallet sharedWallet=StorageIO.loadAccountSharedWallet(account,serverName);
      if (sharedWallet!=null)
      {
        List<StoredItem> storedItems=getAllItems(accountServer,sharedWallet,LocationType.SHARED_WALLET);
        items.addAll(storedItems);
      }
    }
    return items;
  }

  private static List<StoredItem> getAllItems(Owner owner, Wallet container, LocationType type)
  {
    StorageLocation location=new SimpleStorageLocation(owner,type,null);
    List<StoredItem> items=new ArrayList<StoredItem>();
    for(CountedItem countedItem : container.getAllItemsByName())
    {
      StoredItem storedItem=new StoredItem(countedItem.getProxy(),countedItem.getQuantity());
      storedItem.setOwner(owner);
      storedItem.setLocation(location);
      items.add(storedItem);
    }
    return items;
  }

  private static List<StoredItem> getAllItems(Owner owner, Vault container, LocationType type)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    int chests=container.getChestCount();
    for(int i=0;i<chests;i++)
    {
      Chest chest=container.getChest(i);
      if (chest!=null)
      {
        String chestName=chest.getName();
        StorageLocation location=new SimpleStorageLocation(owner,type,chestName);
        List<CountedItem> chestItems=chest.getAllItemsByName();
        for(CountedItem chestItem : chestItems)
        {
          StoredItem storedItem=new StoredItem(chestItem.getProxy(),chestItem.getQuantity());
          storedItem.setOwner(owner);
          storedItem.setLocation(location);
          items.add(storedItem);
        }
      }
    }
    return items;
  }
}
