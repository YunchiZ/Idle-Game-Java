import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

class CombatPanel extends JPanel {
    private final GameFrame frame;
    private final JTextArea logArea;
    private final JLabel playerStats;
    private final JLabel enemyStats;
    private final JLabel enemyInfo;
    private final JProgressBar enemyHealthBar;
    private final JButton startButton, stopButton, backButton;
    private final JComboBox<String> enemySelector;

    private final PlayerData player;
    private int enemyHealth;
    private Enemy currentEnemy;
    private final Timer timer;
    private boolean inCombat = false;

    private final Map<String, Enemy> enemyTypes;

    public CombatPanel(GameFrame frame) {
        this.frame = frame;
        this.setLayout(new BorderLayout());

        player = new PlayerData();
        player.loadFromFile();

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);

        playerStats = new JLabel();
        enemyStats = new JLabel("No enemy currently engaged.");
        enemyInfo = new JLabel("Select an enemy to view stats.");
        enemyHealthBar = new JProgressBar(0, 100);
        enemyHealthBar.setValue(100);
        enemyHealthBar.setStringPainted(true);

        // Enemy types
        enemyTypes = new HashMap<>();
        enemyTypes.put("Slime", new Enemy("Slime", 50, 5, 3, 0));
        enemyTypes.put("Goblin", new Enemy("Goblin", 80, 10, 5, 2));
        enemyTypes.put("Orc", new Enemy("Orc", 120, 20, 10, 5));

        enemySelector = new JComboBox<>(enemyTypes.keySet().toArray(new String[0]));
        enemySelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !inCombat) {
                String selected = (String) enemySelector.getSelectedItem();
                Enemy preview = enemyTypes.get(selected);
                enemyInfo.setText("<html><b>" + preview.name + "</b> | HP: " + preview.maxHealth
                        + " | ATK: " + preview.attack + " | DEF: " + preview.defense
                        + " | XP: +" + preview.rewardXP + " | Gold: +" + preview.rewardGold + "</html>");
                enemyHealthBar.setValue(100);
            }
        });

        startButton = new JButton("Start Combat");
        startButton.addActionListener(e -> startCombat());

        stopButton = new JButton("Stop Combat");
        stopButton.addActionListener(e -> stopCombat());

        backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            stopCombat();
            frame.switchPanel("MainMenu");
        });

        JPanel topPanel = new JPanel(new GridLayout(5, 1));
        topPanel.add(playerStats);
        topPanel.add(enemySelector);
        topPanel.add(enemyInfo);
        topPanel.add(enemyStats);
        topPanel.add(enemyHealthBar);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(startButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(logScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        timer = new Timer();
        updateStats();
    }

    private void startCombat() {
        if (inCombat) return;

        String selected = (String) enemySelector.getSelectedItem();
        if (selected == null) return;

        currentEnemy = enemyTypes.get(selected);
        enemyHealth = currentEnemy.maxHealth;
        inCombat = true;
        enemySelector.setEnabled(false);
        log("A " + currentEnemy.name + " appears with " + currentEnemy.maxHealth + " HP!");
        updateEnemyBar();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!inCombat) return;

                int damage = Math.max(0, player.attack - currentEnemy.defense);
                enemyHealth -= damage;
                enemyHealth = Math.max(0, enemyHealth);
                log("You deal " + damage + " damage to the " + currentEnemy.name + "!");

                if (enemyHealth <= 0) {
                    log(currentEnemy.name + " defeated! +" + currentEnemy.rewardXP + " XP, +" + currentEnemy.rewardGold + " gold.");
                    player.experience += currentEnemy.rewardXP;
                    player.gold += currentEnemy.rewardGold;
                    player.saveToFile();
                    updateStats();
                    stopCombat();
                } else {
                    updateStats();
                    updateEnemyBar();
                }
            }
        }, 0, 2000); // every 2 seconds
    }

    private void stopCombat() {
        if (!inCombat) return;

        inCombat = false;
        enemySelector.setEnabled(true);
        timer.purge();
        log("Combat stopped.");
        updateStats();
    }

    private void updateStats() {
        playerStats.setText("Player - HP: " + player.health + ", ATK: " + player.attack +
                ", XP: " + player.experience + ", Gold: " + player.gold);

        if (inCombat && currentEnemy != null) {
            enemyStats.setText("Enemy - " + currentEnemy.name + " HP: " + enemyHealth + "/" + currentEnemy.maxHealth);
        } else {
            enemyStats.setText("No enemy currently engaged.");
        }
    }

    private void updateEnemyBar() {
        if (currentEnemy != null) {
            int percent = (int) ((enemyHealth / (double) currentEnemy.maxHealth) * 100);
            enemyHealthBar.setMaximum(currentEnemy.maxHealth);
            enemyHealthBar.setValue(enemyHealth);
            enemyHealthBar.setString(enemyHealth + " / " + currentEnemy.maxHealth);
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    static class Enemy {
        String name;
        int maxHealth;
        int rewardXP;
        int rewardGold;
        int defense;
        int attack;

        Enemy(String name, int maxHealth, int rewardXP, int rewardGold, int defense) {
            this.name = name;
            this.maxHealth = maxHealth;
            this.rewardXP = rewardXP;
            this.rewardGold = rewardGold;
            this.defense = defense;
        }
    }
}
