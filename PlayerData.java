import java.io.*;
import java.util.Properties;

public class PlayerData {
    public int health = 100;
    public int attack = 10;
    public int experience = 0;
    public int gold = 0;

    private static final String SAVE_FILE = "player_data.properties";

    public void saveToFile() {
        try (FileOutputStream out = new FileOutputStream(SAVE_FILE)) {
            Properties props = new Properties();
            props.setProperty("health", String.valueOf(health));
            props.setProperty("attack", String.valueOf(attack));
            props.setProperty("experience", String.valueOf(experience));
            props.setProperty("gold", String.valueOf(gold));
            props.store(out, "Player Data Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        try (FileInputStream in = new FileInputStream(SAVE_FILE)) {
            Properties props = new Properties();
            props.load(in);
            health = Integer.parseInt(props.getProperty("health", "100"));
            attack = Integer.parseInt(props.getProperty("attack", "10"));
            experience = Integer.parseInt(props.getProperty("experience", "0"));
            gold = Integer.parseInt(props.getProperty("gold", "0"));
        } catch (IOException e) {
            System.out.println("No save file found. Starting fresh.");
        }
    }
}
