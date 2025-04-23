package src.database;

public class PlayerData {
    public int health, maxHealth, attack, defense, experience, gold;
    public int wood, fish, salmon, shark;
    public int grilledTrout, bakedSalmon, sharkSteak;
    public int swordLevel, armorLevel;
    public double attackBonus = 0.0;
    public double defenseBonus = 0.0;
    public int copper, iron, coal, silver, goldOre;


    public void heal(int amount) {
        health = Math.min(health + amount, maxHealth);
    }

    public int getAttackWithWeapon() {
        return attack + swordLevel * 5;
    }

    public int getDefenseWithArmor() {
        return armorLevel * 3;
    }

    public void loadFromFile() {
    }
}

