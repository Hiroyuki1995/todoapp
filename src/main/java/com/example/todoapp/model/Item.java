package com.example.todoapp.model;

public class Item {

  private String itemId;
  private String itemName;
  private String itemCategory;

  public Item() {
  }

  public Item(String itemId, String itemName, String itemCategory) {
    this.itemId = itemId;
    this.itemName = itemName;
    this.itemCategory = itemCategory;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemCategory() {
    return itemCategory;
  }

  public void setItemCategory(String itemCategory) {
    this.itemCategory = itemCategory;
  }

}
