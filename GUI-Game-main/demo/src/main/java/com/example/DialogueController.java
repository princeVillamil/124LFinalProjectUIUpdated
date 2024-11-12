package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogueController {
    private MediaPlayer bgMusicPlayer;
    
    @FXML
    private BorderPane vnBorderPane; // Reference to your VN BorderPane
    @FXML
    private TextArea vnTextArea;  // Text area for dialogue
    @FXML
    private Label vnNameLabel;     // Label for character names
    @FXML
    private ImageView vnLeftHboxImageView; // Image view for left character
    @FXML
    private ImageView vnCenterHboxImageView; // Image view for center character
    @FXML
    private ImageView vnRightHboxImageView; // Image view for right character
    @FXML
    private BorderPane root;       // Reference to the root layout

    private Dialogue[] currentDialogue;
    private int currentLineIndex = 0;

    private final Map<String, String> characterImages = new HashMap<>();

    // This method will be called automatically after the FXML is loaded
    @FXML
    private void initialize() {
        // CHAR IMAGES
        characterImages.put("Young Eli", "com/example/Visual Novel Characters/VN_EliYoung.png");
        characterImages.put("Eli", "com/example/Visual Novel Characters/VN_Eli.png");
        characterImages.put("Sleepy Eli", "com/example/assets/Empty.png");
        characterImages.put("Weak Eli", "com/example/Visual Novel Characters/VN_Eli.png");
        characterImages.put("Corrupted Knight", "com/example/Visual Novel Characters/VN_CorruptedKing.png");
        characterImages.put("Weak Corrupted Knight", "com/example/Visual Novel Characters/VN_CorruptedKingDamaged.png");
        characterImages.put("Crimson Talon", "com/example/Visual Novel Characters/VN_CrimsonTalon.png");
        characterImages.put("Weak Crimson Talon", "com/example/Visual Novel Characters/VN_CrimsonTalon.png");
        characterImages.put("Eli\'s Father", "com/example/Visual Novel Characters/VN_EliFather.png");
        characterImages.put("Eli\'s Mother", "com/example/Visual Novel Characters/VN_EliMother.png");
        characterImages.put("Alchemist", "com/example/Visual Novel Characters/AlchemistPortrait_VN.png");

        vnTextArea.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    // Starts the dialogue
    public void startDialogue(Dialogue[] dialogue) {
        if (dialogue == null || dialogue.length == 0) {
            System.out.println("Error: No dialogue provided.");
            return;
        }
        currentDialogue = dialogue;
        currentLineIndex = 0;

        // Debugging output
        System.out.println("Starting dialogue with " + currentDialogue.length + " lines.");

        // Set the first dialogue line
        vnTextArea.setText(currentDialogue[currentLineIndex].getText());
        vnNameLabel.setText(currentDialogue[currentLineIndex].getCharacterName());
        updateCharacterState(currentDialogue[currentLineIndex]);
        vnTextArea.requestFocus();  // Set focus to TextArea for key events
    }

    private void updateCharacterState(Dialogue dialogue) {
        // Clear all previous character images
        clearCharacterImages();
    
        String characterName = dialogue.getCharacterName();
        vnNameLabel.setText(characterName);
    
        String imagePath = characterImages.get(characterName);
        if (imagePath != null) {
            try {
                System.out.println("Loading image for character: " + characterName);
                System.out.println("Image path from characterImages map: " + imagePath);
    
                // Construct full path to the image resource
                String fullPath = "/" + imagePath; // Only prefix with '/' once
                System.out.println("Full path to image: " + fullPath);
    
                // Load the image using getResourceAsStream
                Image characterImage = new Image(getClass().getResourceAsStream(fullPath));
                
                if (characterImage.isError()) {
                    System.out.println("Error loading image: " + fullPath);
                } else {
                    // Set the appropriate ImageView based on character name
                    switch (characterName) {
                        case "Young Eli":
                            vnLeftHboxImageView.setImage(characterImage);
                            break;
                        case "Eli":
                            vnLeftHboxImageView.setImage(characterImage);
                            break;
                        case "Sleepy Eli":
                            vnLeftHboxImageView.setImage(characterImage);
                            break;
                        case "Weak Eli":
                            vnLeftHboxImageView.setImage(characterImage);
                            break;
                        case "Eli\'s Father":
                            vnLeftHboxImageView.setImage(characterImage);
                            break;
                        case "Crimson Talon":
                            vnRightHboxImageView.setImage(characterImage);
                            break;
                        case "Weak Crimson Talon":
                            vnRightHboxImageView.setImage(characterImage);
                        case "Corrupted Knight":
                            vnRightHboxImageView.setImage(characterImage);
                            break;
                        case "Weak Corrupted Knight":
                            vnRightHboxImageView.setImage(characterImage);
                            break;
                        case "Eli\'s Mother":
                            vnCenterHboxImageView.setImage(characterImage);
                            break;
                        case "Alchemist":
                            vnRightHboxImageView.setImage(characterImage);
                            break;
                        default:
                            System.out.println("Warning: No ImageView for character " + characterName);
                            break;
                    }
                }
    
            } catch (Exception e) {
                System.out.println("Error loading image for character " + characterName + ": " + e.getMessage());
            }
        } else {
            System.out.println("Warning: No image found for character " + characterName);
        }
    }
    

    // Clears all character images
    private void clearCharacterImages() {
        // Clear all character images to avoid showing previous ones
        vnLeftHboxImageView.setImage(null);
        vnCenterHboxImageView.setImage(null);
        vnRightHboxImageView.setImage(null);
    }

    // Handle key press event
    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
            case ENTER:
                if (currentLineIndex < currentDialogue.length - 1) {
                    nextDialogueLine();
                } else {
                    stopBG();
                    closeVN();
                    transitionToCombatScene2();
                }
            default:
                break;
        }
    }

    private void transitionToCombatScene2 () {
        try {
            Stage currentStage = (Stage) vnBorderPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("combatScene.fxml")); 
            Parent combatRoot = loader.load();
            Scene combatScene = new Scene(combatRoot);
            currentStage.setScene(combatScene);            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transitionToCombatScene() {
        try {
            Stage currentStage = (Stage) vnTextArea.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("combatScene.fxml")); 
            Parent newWindowRoot = loader.load();
            
            Stage newWindowStage = new Stage();
            Scene newWindowScene = new Scene(newWindowRoot);
            newWindowStage.setScene(newWindowScene);
            
            newWindowStage.initModality(Modality.APPLICATION_MODAL);  
            currentStage.close();          
            newWindowStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // // End the dialogue
    // private void endDialogue() {
    //     // Logic to end the dialogue or trigger the next part of the game
    //     vnTextArea.clear();
    //     // Example: root.setCenter(newScene);
    // }


    // Triggered when the game starts or after an introductory event
    public void scene1() {
        changeBackground("scene1"); // Change background to Scene 1's background
        scene1BG();
        startDialogue(Script.SCENE_1_DIALOGUE); // Use Script class for Scene 1 dialogue
        // if (currentLineIndex > currentDialogue.length - 1) {
        //     transitionToCombatScene();
        // }
        // if (currentLineIndex >= currentDialogue.length - 1) {
        //     scene2();
        // } 
    }

    public void scene2() {
        changeBackground("scene2"); // Change background to Scene 2's background
        scene2_4BG();
        startDialogue(Script.SCENE_2_DIALOGUE); // Use Script class for Scene 2 dialogue
    }

    public void scene2B() {
        changeBackground("scene2B"); // Change background to Scene 2B's background
        scene2_4BG();
        startDialogue(Script.SCENE_2B_DIALOGUE); // Use Script class for Scene 2B dialogue
    }

    public void scene3() {
        changeBackground("scene3"); // Change background to Scene 3's background
        scene2_4BG();
        startDialogue(Script.SCENE_3_DIALOGUE); // Use Script class for Scene 3 dialogue
    }

    public void scene4() {
        changeBackground("scene4"); // Change background to Scene 4's background
        scene2_4BG();
        startDialogue(Script.SCENE_4_DIALOGUE); // Use Script class for Scene 4 dialogue
    }

    public void ending1() {
        changeBackground("ending1"); // Change background to Ending 1's background
        endingBG();
        startDialogue(Script.ENDING_1_DIALOGUE); // Use Script class for Ending 1 dialogue
    }

    public void ending2() {
        changeBackground("ending2"); // Change background to Ending 2's background
        endingBG();
        startDialogue(Script.ENDING_2_DIALOGUE); // Use Script class for Ending 2 dialogue
    }

    public void ending3() {
        changeBackground("ending3"); // Change background to Ending 3's background
        endingBG();
        startDialogue(Script.ENDING_3_DIALOGUE); // Use Script class for Ending 3 dialogue
    }

    // Method to change the VN background based on the scene
    public void changeBackground(String sceneName) {
        // Remove any previously applied background class
        vnBorderPane.getStyleClass().removeAll("vnBackground1", "vnBackground2", "vnBackground2B", "vnBackground3", "vnBackground4", "vnEnding1Background", "vnEnding2Background", "vnEnding3Background");
    
        // Add the new background class based on the scene name
        switch (sceneName) {
            case "scene1":
                vnBorderPane.getStyleClass().add("vnBackground1");
                break;
            case "scene2":
                vnBorderPane.getStyleClass().add("vnBackground2");
                break;
            case "scene2B":
                vnBorderPane.getStyleClass().add("vnBackground2B");
                break;
            case "scene3":
                vnBorderPane.getStyleClass().add("vnBackground3");
                break;
            case "scene4":
                vnBorderPane.getStyleClass().add("vnBackground4");
                break;
            case "ending1":
                vnBorderPane.getStyleClass().add("vnEnding1Background");
                break;
            case "ending2":
                vnBorderPane.getStyleClass().add("vnEnding2Background");
                break;
            case "ending3":
                vnBorderPane.getStyleClass().add("vnEnding3Background");
                break;
            default:
                // Optionally handle an invalid scene name
                break;
        }
    }
    
    

    // Move to the next dialogue line
    private void nextDialogueLine() {
        if (currentLineIndex < currentDialogue.length - 1) {
            currentLineIndex++;  // Move to the next line in the dialogue
            System.out.println("Current line index: " + currentLineIndex); // Debugging output
            System.out.println("Dialogue text: " + currentDialogue[currentLineIndex].getText()); // Debugging output
            vnTextArea.setText(currentDialogue[currentLineIndex].getText());  // Update the text in the TextArea
            updateCharacterState(currentDialogue[currentLineIndex]);  // Update the character's image and name
        } else {
            System.out.println("Dialogue finished. Moving to the next scene.");
            // scene2();  // Transition to the next scene
        }
    }
    
    private void closeVN(){
        Stage stage = (Stage) vnTextArea.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    /////////////////////////////////////////////SCENE SOUNDS/////////////////////////////////////////////////
    private void stopBG() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop(); // Stop the music
        }
    }
    
    private void scene1BG() {
        String audioPath = getClass().getResource("/com/example/GameSFX/BGMusic/Scene1.mp3").toString();
        Media bgMusic = new Media(audioPath);
        bgMusicPlayer = new MediaPlayer(bgMusic); 
        
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.play(); 
    }

    private void scene2_4BG() {
        String audioPath = getClass().getResource("/com/example/GameSFX/BGMusic/Scene2-4.mp3").toString();
        Media bgMusic = new Media(audioPath);
        bgMusicPlayer = new MediaPlayer(bgMusic); 
        
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.play(); 
    }

    private void endingBG() {
        String audioPath = getClass().getResource("/com/example/GameSFX/BGMusic/Ending.mp3").toString();
        Media bgMusic = new Media(audioPath);
        bgMusicPlayer = new MediaPlayer(bgMusic); 
        
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.play(); 
    }
     
}
