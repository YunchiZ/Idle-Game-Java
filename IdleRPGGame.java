import javax.swing.*;
import java.awt.*;

public class IdleRPGGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame().setVisible(true));
    }
}

class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GameFrame() {
        setTitle("Idle RPG Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add all game panels
        mainPanel.add(new MainMenuPanel(this), "MainMenu");
        mainPanel.add(new CombatPanel(this), "Combat");
        mainPanel.add(new GatheringPanel(this), "Gathering");
        mainPanel.add(new CraftingPanel(this), "Crafting");
        mainPanel.add(new StatsPanel(this), "Stats");

        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }

    public void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
}

// ---------- Panels ----------

class MainMenuPanel extends JPanel {
    public MainMenuPanel(GameFrame frame) {
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton combatButton = new JButton("Enter Combat");
        JButton gatheringButton = new JButton("Gather Resources");
        JButton craftingButton = new JButton("Craft Items");
        JButton statsButton = new JButton("View Stats");

        combatButton.addActionListener(e -> frame.switchPanel("Combat"));
        gatheringButton.addActionListener(e -> frame.switchPanel("Gathering"));
        craftingButton.addActionListener(e -> frame.switchPanel("Crafting"));
        statsButton.addActionListener(e -> frame.switchPanel("Stats"));

        add(combatButton);
        add(gatheringButton);
        add(craftingButton);
        add(statsButton);
    }
}


class CraftingPanel extends JPanel {
    public CraftingPanel(GameFrame frame) {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Crafting Screen - Crafting logic will be added soon", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.switchPanel("MainMenu"));

        add(label, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}

class StatsPanel extends JPanel {
    public StatsPanel(GameFrame frame) {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Stats Screen - Player stats, XP, and resources will be displayed", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.switchPanel("MainMenu"));

        add(label, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
