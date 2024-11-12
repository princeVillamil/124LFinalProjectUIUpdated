package com.example;

public class Item {

    private String name;
    private String equipmentType;
    private int defaultStatValue;
    private int maxStatValue;
    private String statType;
    private double dropRate;
    private String imageUrl;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getDefaultStatValue() {
        return defaultStatValue;
    }

/*************  ✨ Codeium Command ⭐  *************/
    /**
     * Sets the default stat value for this item. The default stat value is the
     * initial value of the stat that this item provides when equipped.
     *
     * @param defaultStatValue the default stat value for this item
     */
/******  5f038810-cfc1-4789-827d-2797a6139bd6  *******/
    public void setDefaultStatValue(int defaultStatValue) {
        this.defaultStatValue = defaultStatValue;
    }

    public int getMaxStatValue() {
        return maxStatValue;
    }

    public void setMaxStatValue(int maxStatValue) {
        this.maxStatValue = maxStatValue;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

    public double getDropRate() {
        return dropRate;
    }

    public void setDropRate(double dropRate) {
        this.dropRate = dropRate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}