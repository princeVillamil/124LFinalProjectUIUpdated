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


public class instructionsController {
    private MediaPlayer bgMusicPlayer;

    @FXML
    private Button instructionsMainMenuBtn;

    @FXML
    public void initialize() { //initialize does its thing when u open
        playBackgroundMusic(); 
        // mainMenuEndlessBtn.setDisable(true);
    }

    public void openMainMenu() throws IOException{
        stopBackgroundMusic();
        playClickSound();
        App.setRoot("mainMenuScene");
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
