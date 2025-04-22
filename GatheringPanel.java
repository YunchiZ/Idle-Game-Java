import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

class GatheringPanel extends JPanel {
    private final GameFrame frame;
    private final JPanel resourcePanel;
    private final PlayerData player;
    private Timer gatheringTimer;
    private boolean isGathering = false;
    private JButton activeStartButton = null;
    private JProgressBar activeProgressBar = null;

    public GatheringPanel(GameFrame frame) {
        this.frame = frame;
        this.setLayout(new BorderLayout());

        player = new PlayerData();
        player.loadFromFile();

        // 🔼 顶部按钮区
        JPanel activityButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton fishingButton = new JButton("Fishing");
        JButton woodcuttingButton = new JButton("Woodcutting");
        JButton miningButton = new JButton("Mining");

        Dimension buttonSize = new Dimension(100, 30);
        fishingButton.setPreferredSize(buttonSize);
        woodcuttingButton.setPreferredSize(buttonSize);
        miningButton.setPreferredSize(buttonSize);

        activityButtonPanel.add(fishingButton);
        activityButtonPanel.add(woodcuttingButton);
        activityButtonPanel.add(miningButton);

        // ⚙️ 采集内容区（可滚动）
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resourcePanel);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 提升滚动速度

        // 🔽 底部按钮
        JPanel bottomPanel = new JPanel();
        JButton stopButton = new JButton("Stop Gathering");
        JButton backButton = new JButton("Back to Main Menu");

        stopButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        stopButton.addActionListener(e -> stopGathering());
        backButton.addActionListener(e -> {
            stopGathering();
            frame.switchPanel("MainMenu");
        });

        bottomPanel.add(stopButton);
        bottomPanel.add(backButton);

        // 🔄 事件绑定
        fishingButton.addActionListener(e -> showResources("Fishing"));
        woodcuttingButton.addActionListener(e -> showResources("Woodcutting"));
        miningButton.addActionListener(e -> showResources("Mining"));

        add(activityButtonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showResources(String type) {
        resourcePanel.removeAll();

        if (type.equals("Fishing")) {
            addResourceOption("River Trout", 3000, "fish");
            addResourceOption("Salmon", 5000, "fish");
        } else if (type.equals("Woodcutting")) {
            addResourceOption("Oak Tree", 2000, "wood");
            addResourceOption("Maple Tree", 4000, "wood");
            addResourceOption("Yew Tree", 6000, "wood");
        } else if (type.equals("Mining")) {
            addResourceOption("Copper Ore", 2500, "ore");
            addResourceOption("Iron Ore", 5000, "ore");
            addResourceOption("Coal", 5500, "ore");
            addResourceOption("Silver", 6000, "ore");
            addResourceOption("Gold", 7000, "ore");
        }

        resourcePanel.revalidate();
        resourcePanel.repaint();
    }

    private void addResourceOption(String name, int duration, String resourceType) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        JLabel label = new JLabel(name);
        JButton startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(80, 25));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(300, 20));

        panel.add(label, BorderLayout.WEST);
        panel.add(startButton, BorderLayout.EAST);
        panel.add(progressBar, BorderLayout.SOUTH);
        panel.setMaximumSize(new Dimension(600, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        startButton.addActionListener(e -> {
            if (isGathering) return;
            isGathering = true;

            // 设置当前激活组件
            activeStartButton = startButton;
            activeProgressBar = progressBar;

            startButton.setEnabled(false);
            startGatheringLoop(duration, resourceType, progressBar, startButton);
        });

        resourcePanel.add(panel);
    }

    private void startGatheringLoop(int duration, String type, JProgressBar progressBar, JButton button) {
        gatheringTimer = new Timer();
        int interval = 100;

        gatheringTimer.scheduleAtFixedRate(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                if (!isGathering) {
                    gatheringTimer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(0);
                        button.setEnabled(true);
                    });
                    return;
                }

                progress += (int) (100.0 * interval / duration);
                SwingUtilities.invokeLater(() -> progressBar.setValue(Math.min(progress, 100)));

                if (progress >= 100) {
                    addResourceToPlayer(type);
                    player.saveToFile();
                    progress = 0; // 重置继续采集
                }
            }
        }, 0, interval);
    }

    private void stopGathering() {
        if (gatheringTimer != null) {
            gatheringTimer.cancel();
        }
        isGathering = false;

        if (activeStartButton != null) {
            activeStartButton.setEnabled(true);
        }
        if (activeProgressBar != null) {
            activeProgressBar.setValue(0);
        }

        activeStartButton = null;
        activeProgressBar = null;
    }

    private void addResourceToPlayer(String type) {
        switch (type) {
            case "wood" -> player.wood++;
            case "fish" -> player.fish++;
            case "ore" -> player.ore++;
        }
        System.out.println("Collected 1 " + type + ". Total: wood=" + player.wood + ", fish=" + player.fish + ", ore=" + player.ore);
    }
}
