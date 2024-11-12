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
import javafx.application.Platform;
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

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
        loadEnemyData(null);
        loadLootItems();
        buttonStage1_1.setDisable(false);
        buttonStage1_2.setDisable(true);
        buttonStage1_3.setDisable(true);
        buttonStage2_1.setDisable(true);
        buttonStage2_2.setDisable(true);
        buttonStage2_3.setDisable(true);
        buttonStage3_1.setDisable(true);
        buttonStage3_2.setDisable(true);
        buttonStage3_3.setDisable(true);
        unequipButton.setDisable(true);
        craftButton.setDisable(true);
        hoverPause = new PauseTransition(Duration.seconds(3));
        hoverPause.setOnFinished(event -> showItemStats(selectedItemIndex));
    }

    private Enemy loadEnemyData(String stage) {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/enemies/enemies.json"))) {
            Gson gson = new Gson();
            Type enemyListType = new TypeToken<List<Enemy>>() {}.getType();
            List<Enemy> enemies = gson.fromJson(reader, enemyListType);
            
            // Find the enemy for the selected stage
            for (Enemy enemy : enemies) {
                if (Objects.equals(enemy.getStage(), stage)) {
                    return enemy;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // or a default enemy if needed
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
    
    ////////////////////////////////////////////////////////////SOUNDS/////////////////////////////////////////////////////////////////
    private void playClickSound() {
        String audioPath = getClass().getResource("/com/example/GameSFX/InteractSFX/ButtonClicked.mp3").toString();
        Media sound = new Media(audioPath);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void DismantleSound() {
        String audioPath = getClass().getResource("/com/example/GameSFX/InteractSFX/DismantleItem.mp3").toString();
        Media sound = new Media(audioPath);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    ////////////////////////////////////////////////HANDLERS OF BUTTON-CLICK PER STAGE/////////////////////////////////////////////////////////////////
    @FXML
    private void handleStage1_1ButtonClick() {
        playClickSound();
        handleStageButtonClick("1-1");
    }

    @FXML
    private void handleStage1_2ButtonClick() {
        playClickSound();
        handleStageButtonClick("1-2");
    }

    @FXML
    private void handleStage1_3ButtonClick() {
        playClickSound();
        VN_StageButtonClick("1-3");
        handleStageButtonClick("1-3");
    }

    @FXML
    private void handleStage2_1ButtonClick() {
        playClickSound();
        handleStageButtonClick("2-1");
    }

    @FXML
    private void handleStage2_2ButtonClick() {
        playClickSound();
        handleStageButtonClick("2-2");
    }

    @FXML
    private void handleStage2_3ButtonClick() {
        playClickSound();
        VN_StageButtonClick("2-3");
        handleStageButtonClick("2-3");
    }

    @FXML
    private void handleStage3_1ButtonClick() {
        playClickSound();
        handleStageButtonClick("3-1");
    }

    @FXML
    private void handleStage3_2ButtonClick() {
        playClickSound();
        handleStageButtonClick("3-2");
    }

    @FXML
    private void handleStage3_3ButtonClick() {
        playClickSound();
        VN_StageButtonClick("3-3");
        handleStageButtonClick("3-3");
    }

    private void VN_StageButtonClick (String stage) {
        try {
            // Stage currentStage = (Stage) combatArmorLabel.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("visualNovelScene.fxml")); 
            Parent newWindowRoot = loader.load();

            DialogueController dialogueController = loader.getController();
            switch (stage) {
                case "1-3":
                    dialogueController.scene2();
                    break;
                case "2-3":
                    dialogueController.scene3();
                    break;
                case "3-3":
                    dialogueController.scene4();
                    break;
                default:
                    System.out.println("Unknown stage!");
                    return; 
            }
            
            Stage newWindowStage = new Stage();
            Scene newWindowScene = new Scene(newWindowRoot);
            newWindowStage.setScene(newWindowScene);
            newWindowStage.setTitle("Combat Stage " + stage);
            
            
            
            newWindowStage.initModality(Modality.APPLICATION_MODAL);
            newWindowStage.showAndWait();    
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Ending (String stage) {// CHANGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("visualNovelScene.fxml")); 
            Parent newWindowRoot = loader.load();
            
            
            DialogueController dialogueController = loader.getController();
           
            if (calculateTotalStat("Attack") == 500 && calculateTotalStat("Defence") == 100 && 
            calculateTotalStat("Health") == 1000){
                dialogueController.ending1();
            } else if (calculateTotalStat("Attack") != 500 && calculateTotalStat("Defence") != 100 && 
            calculateTotalStat("Health") != 1000) {
                dialogueController.ending2();
            }
            
            Stage newWindowStage = new Stage();
            Scene newWindowScene = new Scene(newWindowRoot);
            newWindowStage.setScene(newWindowScene);
            newWindowStage.setTitle("Combat Stage " + stage);
            newWindowStage.initModality(Modality.APPLICATION_MODAL);
            newWindowStage.showAndWait();    
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoseEnding (String stage) {// CHANGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("visualNovelScene.fxml")); 
            Parent newWindowRoot = loader.load();
            
            
            DialogueController dialogueController = loader.getController();
           
           dialogueController.ending3();
            
            Stage newWindowStage = new Stage();
            Scene newWindowScene = new Scene(newWindowRoot);
            newWindowStage.setScene(newWindowScene);
            newWindowStage.setTitle("Combat Stage " + stage);
            newWindowStage.initModality(Modality.APPLICATION_MODAL);
            newWindowStage.showAndWait();    
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // actions for the stage button click
    private void handleStageButtonClick(String stage) {
        Enemy enemy = loadEnemyData(stage);

        if (enemy == null) {
            System.out.println("Enemy data not found for this stage!");
            return;
        }
        
        //opens a the combat fxml "characterDisplayScene"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("characterDisplayScene.fxml")); 
            Parent newWindowRoot = loader.load();

            CharacterDisplaySceneController controller = loader.getController();
            controller.setEnemyData(enemy); // Create a method in CharacterDisplaySceneController to handle enemy data
            controller.setLootItems(lootItems);

            // Calculate and pass the main character’s total stats
            int mainCharacterHP = calculateTotalStat("Health");
            int mainCharacterAttack = calculateTotalStat("Attack");
            int mainCharacterArmor = calculateTotalStat("Defence");
            controller.setMainCharacterData(mainCharacterHP, mainCharacterAttack, mainCharacterArmor);
            
            Stage newWindowStage = new Stage();
            Scene newWindowScene = new Scene(newWindowRoot);
            newWindowStage.setScene(newWindowScene);
            
            newWindowStage.initModality(Modality.APPLICATION_MODAL);
            newWindowStage.setTitle("Combat Stage " + stage);
            newWindowStage.getIcons().add(new Image(getClass().getResourceAsStream("assets/bg.png")));


            newWindowStage.showAndWait();

            if (controller.isVictory()) {
                // Victory handling
                showVictoryAlert();
                enableNextStageButton(stage);  // Enable the next stage button
                giveLoot();
                if (stage == "1-3"){
                    FXMLLoader loaderVN = new FXMLLoader(getClass().getResource("visualNovelScene.fxml")); 
                    Parent newWindowRootVN = loaderVN.load();
                    
                    DialogueController dialogueController = loaderVN.getController();
                    dialogueController.scene2B();

                    Stage newWindowStageVN = new Stage();
                    Scene newWindowSceneVN = new Scene(newWindowRootVN);
                    newWindowStageVN.setScene(newWindowSceneVN);
                    newWindowStageVN.initModality(Modality.APPLICATION_MODAL);

                    newWindowStageVN.setTitle("Combat Stage " + stage);
                    newWindowStageVN.showAndWait();    
                    
                    
                } else if (stage == "3-3"){
                    Ending(stage);
                    gameWinAlert();
                    Platform.exit();

                }
                
            } else {
                if (stage == "3-3"){
                    LoseEnding(stage);
                    gameOverAlert();   
                    Platform.exit();

                } else {
                    showDefeatAlert();
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String iconPath, String imagePath, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(null);
        alert.setGraphic(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(250);
        imageView.setFitHeight(100);
        imageView.setSmooth(true);
        ScaleTransition bounce = new ScaleTransition(Duration.seconds(1), imageView);
        bounce.setFromX(1);
        bounce.setToX(1.2);
        bounce.setCycleCount(2);
        bounce.setAutoReverse(true);
        bounce.play();
        Label textLabel = new Label(message);
        textLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-padding: 10px;");
        textLabel.setFont(new Font("Arial", 16));
        HBox hbox = new HBox(imageView, textLabel);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-background-radius: 10px;");
        alert.getDialogPane().setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        alert.getDialogPane().setContent(hbox);
        alert.getDialogPane().setPadding(new Insets(5));
        alert.getDialogPane().setMinSize(300, 80);
        alert.showAndWait();
    }

    private void showVictoryAlert() {
        showAlert("Victory", "assets/trophy.png", "assets/victory.png", "Enemy Vanquished!");
    }
    
    private void showDefeatAlert() {
        showAlert("Defeat", "assets/def.png", "assets/defeat.png", "Farm easier stages to boost your equipment stats!");
    }

    private void gameOverAlert() {
        showAlert("YOU LOST!", "assets/def.png", "assets/defeat.png", "The Crimson Talon has taken over your kingdom. Game Over!");
    }

    private void gameWinAlert() {
        showAlert("You won!", "assets/trophy.png", "assets/Trophy.png", "Eli had a happy ending. Congratulations!");
    }

    private void enableNextStageButton(String currentStage) {
        String nextStage = getNextStage(currentStage); // Assuming you have a method to determine the next stage
        Button nextStageButton = getStageButton(nextStage); // Get the button for the next stage
        if (nextStageButton != null) {
            nextStageButton.setDisable(false); // Enable the next stage button
        }
    }

    private void giveLoot() {
        int availableSlots = (int) inventorySlots.stream().filter(Objects::isNull).count();
        if (availableSlots <= 0) {
            showAlert("Inventory Full", "assets/warning.png", "assets/inventoryfull.png", "No Empty Slots Available In The Inventory!");
            unequipButton.setDisable(false);
            return;
        }
        Collections.shuffle(lootItems);
        int itemsToAdd = Math.min(2, availableSlots);
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

    private String getNextStage(String currentStage) {
        // Define your stage progression logic here, for example:
        String nextStage = "";
        switch (currentStage) {
            case "1-1":
                nextStage = "1-2";
                break;
            case "1-2":
                nextStage = "1-3";
                break;
            case "1-3":
                nextStage = "2-1";
                break;
            case "2-1":
                nextStage = "2-2";
                break;
            case "2-2":
                nextStage = "2-3";
                break;
            case "2-3":
                nextStage = "3-1";
                break;
            case "3-1":
                nextStage = "3-2";
                break;
            case "3-2":
                nextStage = "3-3";
                break;
        }
        return nextStage;
    }

    private Button getStageButton(String stage) {
        // Implement logic to return the button for the next stage, for example:
        switch (stage) {
            case "1-2":
                return buttonStage1_2;
            case "1-3":
                return buttonStage1_3;
            case "2-1":
                return buttonStage2_1;
            case "2-2":
                return buttonStage2_2;
            case "2-3":
                return buttonStage2_3;
            case "3-1":
                return buttonStage3_1;
            case "3-2":
                return buttonStage3_2;
            case "3-3":
                return buttonStage3_3;
            default: return null;
        }
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
        playClickSound();
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
        DismantleSound();
        if (selectedSlotIndex >= 0 && selectedSlotIndex < equippedItems.length) {
            unequipItem(selectedSlotIndex);
            selectedSlotIndex = -1; 
        } else if (selectedItem1 != null) {
            removeSelectedItemFromInventory();
            selectedItem1 = null; 
        } else {
             showAlert("No Item Selected", "assets/warning.png", "assets/statsicon.png", "Please Select An Item To Unequip.!");
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
            showAlert("Item Not Found", "assets/warning.png", "assets/notfound.png", "Item Not Found!");
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
                showAlert("Inventory Full", "assets/warning.png", "assets/inventoryfull.png", "No Empty Slots Available In The Inventory!");
                return;
            }
        } else {
            showAlert("Invalid Equipment type", "assets/warning.png", "assets/brokensword.png", "Invalid Equipment Type!");
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
                showAlert("Inventory Full", "assets/warning.png", "assets/inventoryfull.png", "No Empty Slots Available In The Inventory!");
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
                showAlert("Inventory Full", "assets/warning.png", "assets/inventoryfull.png", "Inventory Is Full!");
            }
        } else {
            showAlert("Empty Slot", "assets/warning.png", "assets/notfound.png", "No Item Equipped In This Slot!");
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
        playClickSound();
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
                    showAlert("Crafting Success", "assets/statsicon.png", "assets/craft_button.png", selectedItem1.getName() + " crafted successfully with " + additionalStat + " additional stat!");
                } else {
                    showAlert("Inventory Full", "assets/warning.png", "assets/inventoryfull.png", "No Empty Slots Available In The Inventory!");
                }
                clearSelections();
                craftButton.setDisable(true);
            } else {
                showAlert("Crafting Failed", "assets/warning.png", "asstes/craft.png", "Select Two Items To Craft");
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
                showAlert("Item Removed", "assets/warning.png", "assets/itemremoved.png", "Item Removed From Inventory!");

            } else {
                showAlert("Item Not Found", "assets/warning.png", "assets/notfound.png", "Item Not Found in Inventory!");
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
                                  "Current Stat Value: " + item.getDefaultStatValue() + "\n" +
                                  "Max Stat Value: " + item.getMaxStatValue() + "\n" +
                                  "Stat Type: " + item.getStatType();
            if (item.getStatType().equalsIgnoreCase("Health")) {
                showAlert("Item Stats", "assets/statsicon.png", "assets/ring.png", statsMessage);
            }else if (item.getStatType().equalsIgnoreCase("Attack")) {
                showAlert("Item Stats", "assets/statsicon.png", "assets/sword.png", statsMessage);
            }else {
                showAlert("Item Stats", "assets/statsicon.png", "assets/wood.png", statsMessage);
            }
        }
    }


    private int calculateTotalStat(String statType) {
        int total = 0;

        //og values: 100,20,10
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

