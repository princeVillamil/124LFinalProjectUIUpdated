package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class mainMenuController {
    private MediaPlayer bgMusicPlayer;

    @FXML
    private Button mainMenuStoryBtn; // Reference to your "Start" button

    @FXML
    private Button mainMenuEndlessBtn; // Reference to your "Endless" button

    @FXML
    public void initialize() { //initialize does its thing when u open
        playBackgroundMusic(); 
        // mainMenuEndlessBtn.setDisable(true);
    }

    @FXML
    private void openVisualNovel() throws IOException {
        stopBackgroundMusic();
        playClickSound();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initialVN.fxml"));
        Parent root = loader.load(); // This loads the FXML and returns the root node
        
        // Create a new scene and set it on the current stage
        Stage stage = (Stage) mainMenuStoryBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // Retrieve the DialogueController from the FXML loader
        initialVN initialVN = loader.getController();
        
        // Ensure that the controller is not null and start scene1
        if (initialVN != null) {
            initialVN.scene1(); // Start the first scene in the visual novel
        }
        else {
            System.out.println("Error: DialogueController is not found.");
        }
    }

    @FXML
    private void openEndlessMode(){
        stopBackgroundMusic();
        playClickSound();
        // mainMenuEndlessBtn.setDisable(true);
        try {
            // Calls the setRoot method with the path to instructionsScene.fxml
            App.setRoot("instructionsScene");
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, you can display an error message if the file cannot be loaded
        }
    }

    private void playClickSound() {
        String audioPath = getClass().getResource("/com/example/GameSFX/InteractSFX/ButtonClicked.mp3").toString();
        Media sound = new Media(audioPath);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void playBackgroundMusic() {
        String audioPath = getClass().getResource("/com/example/GameSFX/BGMusic/MenuBG.mp3").toString();
        Media bgMusic = new Media(audioPath);
        bgMusicPlayer = new MediaPlayer(bgMusic);
        
        //loop
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.play(); 
    }

    private void stopBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop(); 
        }
    }
}
