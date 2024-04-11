package com.example.todoapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todoapp.mapper.ItemMapper;
import com.example.todoapp.model.Item;

@Service
public class ItemService {

  // @Autowired
  // ItemMapper getAllItems;

  private final ItemMapper itemMapper;

  public ItemService(ItemMapper itemMapper) {
    this.itemMapper = itemMapper;
  }

  private List<Item> allItems = new ArrayList<>(Arrays.asList(
      new Item("1", "ネックレス", "ジュエリ"),
      new Item("2", "パーカー", "ファッション"),
      new Item("3", "フェイスクリーム", "ビューティ"),
      new Item("4", "サプリメント", "ヘルス"),
      new Item("5", "ブルーベリー", "フルーツ")));

  public List<Item> getAllItems() {
    // return allItems;
    return itemMapper.findAll();
  }

  public Item getItem(String itemId) {
    return itemMapper.findOneById(itemId);
    // for (int i = 0; i < allItems.size(); i++) {
    // if (allItems.get(i).getItemId().equals(itemId)) {
    // return (Item) allItems.get(i);
    // }
    // }
    // return null;
  }

  public void addItem(Item item) {
    // allItems.add(item);
    itemMapper.addOne(item);
  }

  public void updateItem(Item item, String itemId) {
    // for (int i = 0; i < allItems.size(); i++) {
    // if (allItems.get(i).getItemId().equals(itemId)) {
    // allItems.set(i, item);
    // }
    // }
    itemMapper.updateOne(item, itemId);
  }

  public void deleteItem(String itemId) {
    // for (int i = 0; i < allItems.size(); i++) {
    // if (allItems.get(i).getItemId().equals(itemId)) {
    // allItems.remove(i);
    // }
    // }
    itemMapper.deleteOneById(itemId);
  }
}
