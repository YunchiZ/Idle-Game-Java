package src;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import src.database.PlayerData;
import src.database.PlayerDataDAO;

public class StatsPanel extends JPanel {
    private final PlayerDataDAO dao;
    private final JLabel baseStatsLabel;
    private final JPanel inventoryPanel;

    public StatsPanel(GameFrame frame) {
        this.setLayout(new BorderLayout());

        dao = new PlayerDataDAO();

        baseStatsLabel = new JLabel();
        baseStatsLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        baseStatsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(inventoryPanel);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.switchPanel("MainMenu"));

        add(baseStatsLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // 定时刷新状态
        Timer timer = new Timer(1000, e -> refreshStats());
        timer.start();

        refreshStats();
    }

    private void refreshStats() {
        PlayerData player = dao.loadPlayer();

        // 顶部状态
        String statsText = String.format("""
            Health: %d / %d    Attack: %d + %.1f    Defense: %.1f
            EXP: %d    Gold: %d
            """,
                player.health, player.maxHealth,
                player.attack, player.attackBonus,
                player.defenseBonus,
                player.experience, player.gold
        );
        baseStatsLabel.setText("<html>" + statsText.replace("\n", "<br>") + "</html>");

        // 中间库存
        inventoryPanel.removeAll();

        Map<String, Integer> inventory = new LinkedHashMap<>();
        inventory.put("fish", player.fish);
        inventory.put("salmon", player.salmon);
        inventory.put("shark", player.shark);
        inventory.put("grilled_trout", player.grilledTrout);
        inventory.put("baked_salmon", player.bakedSalmon);
        inventory.put("shark_steak", player.sharkSteak);
        inventory.put("copper", player.copper);
        inventory.put("iron", player.iron);
        inventory.put("coal", player.coal);
        inventory.put("silver", player.silver);
        inventory.put("gold_ore", player.goldOre);
        inventory.put("wood", player.wood);
        inventory.put("sword_level", player.swordLevel);
        inventory.put("armor_level", player.armorLevel);

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            JLabel itemLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            itemLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
            inventoryPanel.add(itemLabel);
        }

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }
}

