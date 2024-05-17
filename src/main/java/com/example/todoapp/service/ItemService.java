package com.example.todoapp.service;

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

  public List<Item> getAllItems(String loginId) {
    // return allItems;
    return itemMapper.findAll(loginId);
  }

  public Item getItem(String itemId, String loginId) {
    return itemMapper.findOneById(itemId, loginId);
    // for (int i = 0; i < allItems.size(); i++) {
    // if (allItems.get(i).getItemId().equals(itemId)) {
    // return (Item) allItems.get(i);
    // }
    // }
    // return null;
  }

  public void addItem(Item item, String loginId) {
    // allItems.add(item);
    itemMapper.addOne(item, loginId);
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
