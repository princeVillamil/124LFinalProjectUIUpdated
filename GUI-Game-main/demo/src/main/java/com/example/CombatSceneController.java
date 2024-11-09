package com.example;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CombatSceneController {

    ////////////////////////////////////////////////DECLARATIONS/////////////////////////////////////////////////////////////////
    @FXML
    private ImageView itemSlotOneIMG;
    @FXML
    private ImageView itemSlotTwoIMG;
    @FXML
    private ImageView itemSlotThreeIMG;
    @FXML
    private ImageView itemSlotFourIMG;
    @FXML
    private ImageView itemSlotFiveIMG;
    @FXML
    private ImageView itemSlotSixIMG;
    @FXML
    private ImageView itemSlotSevenIMG;
    @FXML
    private ImageView itemSlotEightIMG;
    @FXML
    private ImageView itemSlotNineIMG;
    @FXML
    private ImageView itemSlotTenIMG;

    @FXML
    private HBox itemSlotOne;
    @FXML
    private HBox itemSlotTwo;
    @FXML
    private HBox itemSlotThree;
    @FXML
    private HBox itemSlotFour;
    @FXML
    private HBox itemSlotFive;
    @FXML
    private HBox itemSlotSix;
    @FXML
    private HBox itemSlotSeven;
    @FXML
    private HBox itemSlotEight;
    @FXML
    private HBox itemSlotNine;
    @FXML
    private HBox itemSlotTen;
    
    @FXML
    private ImageView itemEquipSlotOneIMG; 
    @FXML
    private ImageView itemEquipSlotTwoIMG; 
    @FXML
    private ImageView itemEquipSlotThreeIMG; 

    @FXML
    private HBox itemEquipSlotOne; 
    @FXML
    private HBox itemEquipSlotTwo; 
    @FXML
    private HBox itemEquipSlotThree;

    @FXML
    private Label combatHPLabel;
    
    @FXML
    private Label combatAttackLabel;
    
    @FXML
    private Label combatArmorLabel;

    @FXML
    private Button buttonStage1_1;
    @FXML
    private Button buttonStage1_2;
    @FXML
    private Button buttonStage1_3;
    @FXML
    private Button buttonStage2_1;
    @FXML
    private Button buttonStage2_2;
    @FXML
    private Button buttonStage2_3;
    @FXML
    private Button buttonStage3_1;
    @FXML
    private Button buttonStage3_2;
    @FXML
    private Button buttonStage3_3;
    

    @FXML
    private Button equipButton; 
    @FXML
    private Button unequipButton;
    @FXML
    private Button craftButton;

    private List<Item> lootItems;
    private Item selectedItem1;
    private Item selectedItem2;
    private int imagesLoaded = 0;
    private int equipIndex = 0; 
    private int selectedSlotIndex = 0;
    private int selectedItemIndex = 0;
    private int originalSlotIndex = 0; 
    private PauseTransition hoverPause;
    private List<Item> inventorySlots = new ArrayList<>(Collections.nCopies(10, null));
    private Item[] equippedItems = new Item[3];
    private static final int INVENTORY_SIZE = 10;
    List<Boolean> inventorySlotOccupied = new ArrayList<>(Collections.nCopies(inventorySlots.size(), false));
    

    ////////////////////////////////////////////////INITIALIZATIONS/////////////////////////////////////////////////////////////////
    public void initialize() {
        loadLootItems();
        unequipButton.setDisable(true);
        craftButton.setDisable(true);
        hoverPause = new PauseTransition(Duration.seconds(3));
        hoverPause.setOnFinished(event -> showItemStats(selectedItemIndex));
    }


    private void loadLootItems() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/loot-tables/loot-table-1.json"))) {
            Gson gson = new Gson();
            Type itemListType = new TypeToken<ArrayList<Item>>() {}.getType();
            lootItems = gson.fromJson(reader, itemListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadItemImage(int index, Item item) {
        ImageView[] itemSlots = {
            itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG, 
            itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG, 
            itemSlotSevenIMG, itemSlotEightIMG, 
            itemSlotNineIMG, itemSlotTenIMG
        };
        HBox[] itemSlotContainers = {
            itemSlotOne, itemSlotTwo, itemSlotThree, 
            itemSlotFour, itemSlotFive, itemSlotSix, 
            itemSlotSeven, itemSlotEight, itemSlotNine, 
            itemSlotTen
        };
        
        // Define a placeholder image URL
        String placeholderImageUrl = "/assets/dull-sword.png"; // Adjust the placeholder image name or path as needed
    
        if (index < itemSlots.length) {
            if (item != null) {
                String imageUrl = item.getImageUrl();
                itemSlots[index].setImage(new Image(getClass().getResource("/" + imageUrl).toExternalForm()));
            } else {
                // Set placeholder image if the item is null
                itemSlots[index].setImage(new Image(getClass().getResource("/" + placeholderImageUrl).toExternalForm()));
            }
            
            itemSlots[index].setFitHeight(80);
            itemSlots[index].setFitWidth(80);
            inventorySlots.set(index, item);
    
            itemSlots[index].setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) { 
                    selectItem(item, itemSlotContainers[index], index);
                } else if (event.getButton() == MouseButton.SECONDARY) { 
                    showItemStats(index); 
                }
            });
        }
    
        refreshInventoryDisplay();
    }
    

    ////////////////////////////////////////////////HANDLERS OF BUTTON-CLICK PER STAGE/////////////////////////////////////////////////////////////////
    @FXML
    private void handleStage1_1ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage1_2ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage1_3ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage2_1ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage2_2ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage2_3ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage3_1ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage3_2ButtonClick() {
        handleStageButtonClick();
    }

    @FXML
    private void handleStage3_3ButtonClick() {
        handleStageButtonClick();
    }

    // actions for the stage button click
    private void handleStageButtonClick() {

    //opens a the combat fxml "characterDisplayScene"
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("characterDisplayScene.fxml")); 
        Parent newWindowRoot = loader.load();
        
        Stage newWindowStage = new Stage();
        
        Scene newWindowScene = new Scene(newWindowRoot);
        newWindowStage.setScene(newWindowScene);
        
        newWindowStage.initModality(Modality.APPLICATION_MODAL);

        newWindowStage.setTitle("COMBAT!!!");
        
        newWindowStage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }

    //inventory
        int availableSlots = (int) inventorySlots.stream().filter(Objects::isNull).count();
        if (availableSlots <= 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Inventory Full");
            alert.setHeaderText(null);
            alert.setContentText("You cannot add more items. Please unequip some items to make space.");
            alert.showAndWait();
            unequipButton.setDisable(false);
            return;
        }
        Collections.shuffle(lootItems);
        int itemsToAdd = Math.min(3, availableSlots);
        for (int i = 0; i < itemsToAdd; i++) {
            int emptySlotIndex = getFirstEmptySlotIndex();
            if (emptySlotIndex != -1) {
                Item item = lootItems.get(i % lootItems.size());
                loadItemImage(emptySlotIndex, item);
                inventorySlots.set(emptySlotIndex, item);
            }
        }
        refreshInventoryDisplay();
    }

    private int getFirstEmptySlotIndex() {
        for (int j = 0; j < inventorySlots.size(); j++) {
            if (inventorySlots.get(j) == null) {
                return j;
            }
        }
        return -1;
    }


    ////////////////////////////////////////////////SELECTIONS/////////////////////////////////////////////////////////////////
    private void selectItem(Item item, HBox itemSlot, int index) {
        if (inventorySlots.get(index) == null) {
            return; 
        }
        if ((selectedItem1 != null && selectedItem1 == item && selectedItemIndex == index) ||
            (selectedItem2 != null && selectedItem2 == item && selectedItemIndex == index)) {
            return;
        }
        if (selectedItem1 != null && selectedItem2 != null) {
            clearSelections(); 
            equipButton.setDisable(true);
        }
        if (selectedItem1 == null) {
            selectedItem1 = item;
            selectedItemIndex = index;
            itemSlot.getStyleClass().add("selectedItem"); 
        } 
        
        else if (selectedItem2 == null) {
            selectedItem2 = item;
            itemSlot.getStyleClass().add("selectedItemDarkGold");
        }
        else {
            clearSelections();
            selectedItem1 = item; 
            selectedItemIndex = index;
            itemSlot.getStyleClass().add("selectedItem");
        }
        updateCraftButtonStatus(); 
    }
    

    private void clearSelections() {
        for (int i = 0; i < 10; i++) {
            HBox currentSlot = getItemSlotContainer(i);
            if (currentSlot != null) {
                currentSlot.getStyleClass().remove("selectedItem");
                currentSlot.getStyleClass().remove("selectedItemDarkGold");
            }
        }
        selectedItem1 = null;
        selectedItem2 = null;
    }


    ////////////////////////////////////////////////EQUIP-UNEQUIP ITEM/////////////////////////////////////////////////////////////////   
    @FXML
    private void handleEquipButtonClick() {
        if (selectedItem1 != null) {
            equipItem(selectedItem1); 
            clearSelections();
            refreshInventoryDisplay(); 
            updateStatsLabels();
        }
    }


    @FXML
    private void handleUnequipClick(javafx.scene.input.MouseEvent event) {
        HBox clickedSlot = (HBox) event.getSource();
        clearSlotStyles();
        if (clickedSlot == itemEquipSlotOne) {
            selectedSlotIndex = 0;
        } else if (clickedSlot == itemEquipSlotTwo) {
            selectedSlotIndex = 1;
        } else if (clickedSlot == itemEquipSlotThree) {
            selectedSlotIndex = 2;
        }
        clickedSlot.setStyle(
            "-fx-border-color: red; " +
            "-fx-border-width: 5px; " +
            "-fx-border-style: solid; " +
            "-fx-border-radius: 12px; " +
            "-fx-background-color: rgba(255, 0, 0, 0.1); " +
            "-fx-effect: dropshadow(gaussian, red, 15, 0, 0, 5); " +
            "-fx-transition: all 0.3s ease-in-out;"
        );
    }
    
    private void clearSlotStyles() {
        itemEquipSlotOne.setStyle(null); 
        itemEquipSlotTwo.setStyle(null); 
        itemEquipSlotThree.setStyle(null);
    }
    

    @FXML
    private void handleUnequipButtonClick() {
        if (selectedSlotIndex >= 0 && selectedSlotIndex < equippedItems.length) {
            unequipItem(selectedSlotIndex);
            selectedSlotIndex = -1; 
        } else if (selectedItem1 != null) {
            removeSelectedItemFromInventory();
            selectedItem1 = null; 
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Item Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item to unequip.");
            alert.showAndWait();
        }
        updateStatsLabels();
        clearSelections();
    }
    
    
    private void clearEquippedSelections() {
        HBox[] equipSlots = { itemEquipSlotOne, itemEquipSlotTwo, itemEquipSlotThree };
        for (HBox equipSlot : equipSlots) {
            equipSlot.setStyle(""); 
            equipSlot.getStyleClass().remove("selectedItemDarkGold"); 
        }
    }


    private void clearUnequippedSelections() {
        itemEquipSlotOne.setStyle("");
        itemEquipSlotTwo.setStyle("");
        itemEquipSlotThree.setStyle("");
        selectedSlotIndex = -1;
    }
    

    private void equipItem(Item item) {
        ImageView[] equipSlots = { itemEquipSlotOneIMG, itemEquipSlotTwoIMG, itemEquipSlotThreeIMG };
        ImageView[] itemSlots = {
            itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG, 
            itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG, 
            itemSlotSevenIMG, itemSlotEightIMG, 
            itemSlotNineIMG, itemSlotTenIMG
        };
        originalSlotIndex = inventorySlots.indexOf(item);
        if (originalSlotIndex == -1) {
            Alert alert = new Alert(AlertType.INFORMATION, "Item not found in inventory.", ButtonType.OK);
            alert.setTitle("Item Not Found");
            alert.setHeaderText(null);
            alert.showAndWait();
            return; 
        }
        String equipmentType = item.getEquipmentType();
        int targetEquipIndex;
        if (equipmentType.equals("Weapon")) {
            targetEquipIndex = 0;
        } else if (equipmentType.equals("Miscellaneous")) {
            if (equippedItems[1] == null) {
                targetEquipIndex = 1;
            } else if (equippedItems[2] == null) {
                targetEquipIndex = 2;
            } else {
                Alert alert = new Alert(AlertType.INFORMATION, "All miscellaneous slots are occupied.", ButtonType.OK);
                alert.setTitle("Slots Full");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION, "Invalid equipment type.", ButtonType.OK);
            alert.setTitle("Invalid Equipment Type");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        if (equippedItems[targetEquipIndex] != null) {
            Item previouslyEquippedItem = equippedItems[targetEquipIndex];
            int emptySlotIndex = inventorySlots.indexOf(null);
            if (emptySlotIndex != -1) {
                inventorySlots.set(emptySlotIndex, previouslyEquippedItem);
                itemSlots[emptySlotIndex].setImage(new Image(getClass().getResource("/" + previouslyEquippedItem.getImageUrl()).toExternalForm()));
                itemSlots[emptySlotIndex].setFitWidth(50);  
                itemSlots[emptySlotIndex].setFitHeight(50);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION, "No space available in inventory to unequip item.", ButtonType.OK);
                alert.setTitle("Inventory Full");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }
        }
        equipSlots[targetEquipIndex].setImage(new Image(getClass().getResource("/" + item.getImageUrl()).toExternalForm()));
        equipSlots[targetEquipIndex].setFitWidth(50); 
        equipSlots[targetEquipIndex].setFitHeight(50);
        equippedItems[targetEquipIndex] = item;
        inventorySlots.set(originalSlotIndex, null); 
        ImageView currentItemSlot = itemSlots[originalSlotIndex];
        currentItemSlot.setImage(null);
        currentItemSlot.setFitWidth(50);
        currentItemSlot.setFitHeight(50);
        unequipButton.setDisable(false);
    }

    
    private void unequipItem(int equipSlotIndex) {
        ImageView[] equipSlots = { itemEquipSlotOneIMG, itemEquipSlotTwoIMG, itemEquipSlotThreeIMG };
        if (equipSlotIndex < equippedItems.length && equippedItems[equipSlotIndex] != null) {
            Item item = equippedItems[equipSlotIndex];
            int emptySlotIndex = getFirstEmptyInventorySlot();
            if (emptySlotIndex != -1) {
                inventorySlots.set(emptySlotIndex, item);
                loadItemImage(emptySlotIndex, item);
                equippedItems[equipSlotIndex] = null;
                equipSlots[equipSlotIndex].setImage(null);
                equipSlots[equipSlotIndex].setFitWidth(50);  
                equipSlots[equipSlotIndex].setFitHeight(50); 
                refreshInventoryDisplay();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION, "Inventory is full.", ButtonType.OK);
                alert.setTitle("Inventory Full");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION, "No item equipped in this slot.", ButtonType.OK);
            alert.setTitle("Empty Slot");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }


    ////////////////////////////////////////////////CRAFTING/////////////////////////////////////////////////////////////////
    private void updateCraftButtonStatus() {
        if (selectedItem1 != null && selectedItem2 != null 
            && selectedItem1.getStatType().equals(selectedItem2.getStatType())) {
            craftButton.setDisable(false);
        } else {
            craftButton.setDisable(true);
        }
    }

    
    @FXML
    private void handleCraftButtonClick() {
        if (selectedItem1 != null && selectedItem2 != null) {
            boolean sameStatType = selectedItem1.getStatType().equals(selectedItem2.getStatType());
            if (sameStatType) {
                int additionalStat = selectedItem2.getDefaultStatValue() / 2;
                int newDefaultStatValue = selectedItem1.getDefaultStatValue() + additionalStat;
                if (newDefaultStatValue > selectedItem1.getMaxStatValue()) {
                    newDefaultStatValue = selectedItem1.getMaxStatValue();
                }
                selectedItem1.setDefaultStatValue(newDefaultStatValue);
                
                // Remove the second selected item from inventory
                int indexToRemove2 = inventorySlots.indexOf(selectedItem2);
                if (indexToRemove2 != -1) {
                    inventorySlots.set(indexToRemove2, null);
                    ImageView[] itemSlots = {
                        itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG, 
                        itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG, 
                        itemSlotSevenIMG, itemSlotEightIMG, 
                        itemSlotNineIMG, itemSlotTenIMG
                    };
                    itemSlots[indexToRemove2].setImage(null); 
                    itemSlots[indexToRemove2].setFitWidth(50); 
                    itemSlots[indexToRemove2].setFitHeight(50); 
                    imagesLoaded--;
                }
                
                // Remove the first selected item from inventory
                int indexToRemove1 = inventorySlots.indexOf(selectedItem1);
                if (indexToRemove1 != -1) {
                    inventorySlots.set(indexToRemove1, null); 
                    ImageView[] itemSlots = {
                        itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG, 
                        itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG, 
                        itemSlotSevenIMG, itemSlotEightIMG, 
                        itemSlotNineIMG, itemSlotTenIMG
                    };
                    itemSlots[indexToRemove1].setImage(null); 
                    itemSlots[indexToRemove1].setFitWidth(50); 
                    itemSlots[indexToRemove1].setFitHeight(50); 
                    imagesLoaded--; 
                }
    
                // Place the crafted item back into an empty slot
                boolean itemPlaced = false;
                for (int i = 0; i < inventorySlots.size(); i++) {
                    if (inventorySlots.get(i) == null) {
                        inventorySlots.set(i, selectedItem1); // Place crafted item
                        ImageView[] itemSlots = {
                            itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG, 
                            itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG, 
                            itemSlotSevenIMG, itemSlotEightIMG, 
                            itemSlotNineIMG, itemSlotTenIMG
                        };
                        itemSlots[i].setImage(new Image("file:" + selectedItem1.getImageUrl()));
                        itemSlots[i].setFitWidth(50); 
                        itemSlots[i].setFitHeight(50); 
                        imagesLoaded++;
                        itemPlaced = true;
                        break;
                    }
                }
                
                if (itemPlaced) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Crafting Success");
                    alert.setHeaderText(null);
                    alert.setContentText(selectedItem1.getName() + " crafted successfully with " + additionalStat + " additional stat!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Inventory Full");
                    alert.setHeaderText(null);
                    alert.setContentText("No empty slots available in the inventory.");
                    alert.showAndWait();
                }
    
                clearSelections();
                craftButton.setDisable(true);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Crafting Failed");
                alert.setHeaderText(null);
                alert.setContentText("You must select two items of the same stat type to craft.");
                alert.showAndWait();
            }
            refreshInventoryDisplay();
        }
    }
    


    ////////////////////////////////////////////////INVENTORY/////////////////////////////////////////////////////////////////
    private HBox getItemSlotContainer(int index) {
        switch (index) {
            case 0: return itemSlotOne;
            case 1: return itemSlotTwo;
            case 2: return itemSlotThree;
            case 3: return itemSlotFour;
            case 4: return itemSlotFive;
            case 5: return itemSlotSix;
            case 6: return itemSlotSeven;
            case 7: return itemSlotEight;
            case 8: return itemSlotNine;
            case 9: return itemSlotTen;
            default: return null;
        }
    }

    
    private int getFirstEmptyInventorySlot() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (inventorySlots.get(i) == null) return i;
        }
        return -1;
    }

    
    private void removeSelectedItemFromInventory() {
        if (selectedItem1 != null) {
            int itemIndex = inventorySlots.indexOf(selectedItem1);
            if (itemIndex != -1) {
                inventorySlots.set(itemIndex, null);
                ImageView[] itemSlots = {
                    itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG,
                    itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG,
                    itemSlotSevenIMG, itemSlotEightIMG,
                    itemSlotNineIMG, itemSlotTenIMG
                };
                if (itemIndex >= 0 && itemIndex < itemSlots.length) {
                    itemSlots[itemIndex].setImage(null);
                    itemSlots[itemIndex].setFitHeight(50);
                    itemSlots[itemIndex].setFitWidth(50);
                }
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Item Removed");
                alert.setHeaderText(null);
                alert.setContentText(selectedItem1.getName() + " has been removed from your inventory.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Item Not Found");
                alert.setHeaderText(null);
                alert.setContentText("The selected item could not be found in the inventory.");
                alert.showAndWait();
            }
        }
    }
    
    
    private void refreshInventoryDisplay() {
        ImageView[] itemSlots = {
            itemSlotOneIMG, itemSlotTwoIMG, itemSlotThreeIMG,
            itemSlotFourIMG, itemSlotFiveIMG, itemSlotSixIMG,
            itemSlotSevenIMG, itemSlotEightIMG,
            itemSlotNineIMG, itemSlotTenIMG
        };
        for (int i = 0; i < inventorySlots.size(); i++) {
            Item item = inventorySlots.get(i);
            if (item != null) {
                itemSlots[i].setImage(new Image(getClass().getResource("/" + item.getImageUrl()).toExternalForm()));
                itemSlots[i].setFitHeight(80);
                itemSlots[i].setFitWidth(80);
            } else {
                itemSlots[i].setImage(null);
                itemSlots[i].setFitHeight(80);
                itemSlots[i].setFitWidth(80);
            }
        }
    }
    

    ////////////////////////////////////////////////IN-GAME STATS/////////////////////////////////////////////////////////////////
    private void showItemStats(int index) {
        if (index >= 0 && index < inventorySlots.size() && inventorySlots.get(index) != null) { 
            Item item = inventorySlots.get(index);
            String statsMessage = "Equipment Name: " + item.getName() + "\n" +
                                "Equipment Type: " + item.getEquipmentType() + "\n" +
                                "Default Stat Value: " + item.getDefaultStatValue() + "\n" +
                                "Max Stat Value: " + item.getMaxStatValue() + "\n" +
                                "Stat Type: " + item.getStatType();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Item Stats");
            alert.setHeaderText(null);
            alert.setContentText(statsMessage);
            alert.showAndWait();
        } else {
            System.out.println("No item available in this slot.");
        }
    }


    private int calculateTotalStat(String statType) {
        int total = 0;
        if (statType.equalsIgnoreCase("Health")) {
            total = 100;
        } else if (statType.equalsIgnoreCase("Attack")) {
            total = 20;
        } else if (statType.equalsIgnoreCase("Defence")) {
            total = 10;
        }
        for (Item item : equippedItems) {
            if (item != null && item.getStatType().equalsIgnoreCase(statType)) {
                total += item.getDefaultStatValue();
            }
        }
        return total;
    }


    private void updateStatsLabels() {
        combatHPLabel.setText(String.valueOf(calculateTotalStat("Health")));
        combatAttackLabel.setText(String.valueOf(calculateTotalStat("Attack")));
        combatArmorLabel.setText(String.valueOf(calculateTotalStat("Defence")));
    }
}

