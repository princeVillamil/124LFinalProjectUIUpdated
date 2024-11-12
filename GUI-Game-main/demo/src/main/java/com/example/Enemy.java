package com.example;

public class Enemy {
    private String name;
    private String stage;
    private int attack;
    private int defence;
    private int health;
    private String imageUrl;

    
    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getStage() { 
        return stage; 
    }
    public void setStage(String stage) { 
        this.stage = stage; 
    }

    public int getAttack() { 
        return attack; 
    }

    public void setAttack(int attack) { 
        this.attack = attack; 
    }

    public int getDefence() { 
        return defence; 
    }

    public void setDefence(int defence) { 
        this.defence = defence; 
    }

    public int getHealth() { 
        return health; 
    }

    public void setHealth(int health) { 
        this.health = health; 
    }

    public String getImageUrl() { 
        return imageUrl; 
    }
    
    public void setImageUrl(String imageUrl) { 
        this.imageUrl = imageUrl; 
    }
}
