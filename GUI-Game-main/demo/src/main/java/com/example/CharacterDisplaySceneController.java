package com.example;

import java.util.Collections;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CharacterDisplaySceneController {

    @FXML
    private ImageView characterDisplayLeftCharacter;
    @FXML
    private Label characterDisplayLeftCharacterNameLabel;
    @FXML
    private Label characterDisplayLeftCharacterAttackLabel;
    @FXML
    private Label characterDisplayLeftCharacterDefenceLabel;
    @FXML
    private Label characterDisplayLeftCharacterHealthLabel;

    @FXML
    private ImageView characterDisplayRightCharacter;
    @FXML
    private Label characterDisplayRightCharacterNameLabel;
    @FXML
    private Label characterDisplayRightCharacterAttackLabel;
    @FXML
    private Label characterDisplayRightCharacterDefenceLabel;
    @FXML
    private Label characterDisplayRightCharacterHealthLabel;

    private int mainCharacterHealth;
    private int mainCharacterAttack;
    private int mainCharacterDefence;

    private Enemy enemy;
    private List<Item> lootItems;
    private boolean victory = false;

    private MediaPlayer bgMusicPlayer;

    public void setMainCharacterData(int health, int attack, int defence) {
        this.mainCharacterHealth = health;
        this.mainCharacterAttack = attack;
        this.mainCharacterDefence = defence;

        characterDisplayLeftCharacterNameLabel.setText("Eli");
        characterDisplayLeftCharacterHealthLabel.setText(String.valueOf(health));
        characterDisplayLeftCharacterAttackLabel.setText(String.valueOf(attack));
        characterDisplayLeftCharacterDefenceLabel.setText(String.valueOf(defence));
        
        // Load main character image if needed
        characterDisplayLeftCharacter.setImage(new Image(getClass().getResource("/enemies/Sprite_ELI.gif").toExternalForm()));

        startCombat();
        playBackgroundMusic();
    }

    public void setEnemyData(Enemy enemy) {
        this.enemy = enemy;

        characterDisplayRightCharacterNameLabel.setText(String.valueOf(enemy.getName()));
        characterDisplayRightCharacterHealthLabel.setText(String.valueOf(enemy.getHealth()));
        characterDisplayRightCharacterAttackLabel.setText(String.valueOf(enemy.getAttack()));
        characterDisplayRightCharacterDefenceLabel.setText(String.valueOf(enemy.getDefence()));
        
        // Load enemy image
        characterDisplayRightCharacter.setImage(new Image(getClass().getResource("/" + enemy.getImageUrl()).toExternalForm()));
    }

    private void logCombatEvent(String event) {
        System.out.println(event);  // Print to the terminal
    }

    public void setLootItems(List<Item> lootItems) {
        this.lootItems = lootItems;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public boolean isVictory() {
        return victory;
    }


    private void giveLoot() {
        if (lootItems != null && !lootItems.isEmpty()) {
            Collections.shuffle(lootItems);  // Shuffle the loot to get random items
            List<Item> itemsToGive = lootItems.subList(0, Math.min(3, lootItems.size())); // Give up to 3 items
            for (Item item : itemsToGive) {
                // Process the loot (add to the player's inventory, etc.)
                System.out.println("You have received: " + item.getName());
            }
        }
    }

    private void startCombat() {
        // Main character attacks first
        PauseTransition combatTurn = new PauseTransition(Duration.seconds(1.5));
        combatTurn.setOnFinished(event -> {
            int damageToEnemy = Math.max(0, mainCharacterAttack - enemy.getDefence());
            enemy.setHealth(enemy.getHealth() - damageToEnemy);
            characterDisplayRightCharacterHealthLabel.setText(String.valueOf(enemy.getHealth()));
            logCombatEvent("Eli attacks for " + damageToEnemy + " damage. Enemy health: " + enemy.getHealth());
    
            if (enemy.getHealth() <= 0) {
                logCombatEvent("Enemy has been defeated!");
                logCombatEvent("Combat Result: Victory!");
                victory = true;
                giveLoot();
                closeCombatWindow();
                return;
            }
    
            // Enemy's turn
            PauseTransition enemyTurn = new PauseTransition(Duration.seconds(1.5));
            enemyTurn.setOnFinished(enemyEvent -> {
                int damageToMainCharacter = Math.max(0, enemy.getAttack() - mainCharacterDefence);
                mainCharacterHealth -= damageToMainCharacter;
                characterDisplayLeftCharacterHealthLabel.setText(String.valueOf(mainCharacterHealth));
                logCombatEvent(enemy.getName() + " attacks for " + damageToMainCharacter + " damage. Eli's health: " + mainCharacterHealth);
    
                if (mainCharacterHealth <= 0) {
                    logCombatEvent("Eli has been defeated!");
                    logCombatEvent("Combat Result: Defeat!");
                    victory = false;
                    closeCombatWindow();
                    return;
                }
    
                // Continue combat
                startCombat();
            });
            enemyTurn.play();
        });
        combatTurn.play();
    }

    private void closeCombatWindow() {
        stopBackgroundMusic();
        Stage stage = (Stage) characterDisplayLeftCharacter.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
        logCombatEvent("Combat Ended.");  // Log combat closure
    }

    //////////////SOUNDS/////////////////////
    private void playBackgroundMusic() {
        // Define the path to the music file
        String audioPath = getClass().getResource("/com/example/CombatSFX/SwordFX/MergedSword.mp3").toString();
        System.out.println("Audio Path: " + audioPath);  // Debug print
    
        // Ensure the resource exists
        if (audioPath == null) {
            System.out.println("Error: Audio file not found!");
            return;
        }
    
        Media bgMusic = new Media(audioPath);
        bgMusicPlayer = new MediaPlayer(bgMusic);
    
        bgMusicPlayer.setOnError(() -> {
            System.out.println("Error playing media: " + bgMusicPlayer.getError().getMessage());
        });
    
        // Start the background music in a new thread to avoid blocking UI operations
        new Thread(() -> {
            bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music indefinitely
            bgMusicPlayer.setVolume(1.0);  // Ensure the volume is at max
            bgMusicPlayer.play();
            System.out.println("Music should be playing now...");
        }).start();
    }
    

    // Stop the background music (e.g., when closing the combat window or transitioning to another scene)
    private void stopBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop();
        }
    }
}
