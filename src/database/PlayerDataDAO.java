package src.database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataDAO {
    private static final String DB_URL = "jdbc:sqlite:idle_rpg_game.db";
    private static final int PLAYER_ID = 1; // 单人游戏默认主键 ID

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS player_data (
                id INTEGER PRIMARY KEY,
                health INTEGER,
                max_health INTEGER,
                attack INTEGER,
                defense INTEGER,
                experience INTEGER,
                gold INTEGER,
                wood INTEGER,
                fish INTEGER,
                salmon INTEGER,
                shark INTEGER,
                copper INTEGER,
                iron INTEGER,
                coal INTEGER,
                silver INTEGER,
                gold_ore INTEGER,
                grilled_trout INTEGER,
                baked_salmon INTEGER,
                shark_steak INTEGER,
                sword_level INTEGER,
                armor_level INTEGER,
                attack_bonus REAL,
                defense_bonus REAL
            );
            """);



            // 插入一条默认记录，如果不存在
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM player_data");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("""
                INSERT INTO player_data (
                    id, health, max_health, attack, defense, experience, gold,
                    wood, fish, salmon, shark, 
                    copper, iron, coal, silver, gold_ore,
                    grilled_trout, baked_salmon, shark_steak,
                    sword_level, armor_level,
                    attack_bonus, defense_bonus
                ) VALUES (
                    1, 100, 100, 10, 0, 0, 0,
                    0, 0, 0, 0, 
                    0, 0, 0, 0, 0,
                    0, 0, 0,
                    0, 0,
                    0.0, 0.0
                );
                """);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public PlayerData loadPlayer() {
        PlayerData player = new PlayerData();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM player_data WHERE id = ?")) {

            stmt.setInt(1, PLAYER_ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                player.health = rs.getInt("health");
                player.maxHealth = rs.getInt("max_health");
                player.attack = rs.getInt("attack");
                player.experience = rs.getInt("experience");
                player.gold = rs.getInt("gold");
                player.defense = rs.getInt("defense");

                player.wood = rs.getInt("wood");
                player.fish = rs.getInt("fish");
                player.salmon = rs.getInt("salmon");
                player.shark = rs.getInt("shark");

                player.copper = rs.getInt("copper");
                player.iron = rs.getInt("iron");
                player.coal = rs.getInt("coal");
                player.silver = rs.getInt("silver");
                player.goldOre = rs.getInt("gold_ore");

                player.grilledTrout = rs.getInt("grilled_trout");
                player.bakedSalmon = rs.getInt("baked_salmon");
                player.sharkSteak = rs.getInt("shark_steak");

                player.swordLevel = rs.getInt("sword_level");
                player.armorLevel = rs.getInt("armor_level");

                player.attackBonus = rs.getDouble("attack_bonus");
                player.defenseBonus = rs.getDouble("defense_bonus");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return player;
    }

    public void savePlayer(PlayerData player) {
        String sql = """
            UPDATE player_data SET
              health=?, max_health=?, attack=?, defense=?,
              experience=?, gold=?,
              wood=?, fish=?, salmon=?, shark=?,
              copper=?, iron=?, coal=?, silver=?, gold_ore=?,
              grilled_trout=?, baked_salmon=?, shark_steak=?,
              sword_level=?, armor_level=?,
              attack_bonus=?, defense_bonus=?
            WHERE id=?
        """;


        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, player.health);
            stmt.setInt(2, player.maxHealth);
            stmt.setInt(3, player.attack);
            stmt.setInt(4, player.defense);
            stmt.setInt(5, player.experience);
            stmt.setInt(6, player.gold);

            stmt.setInt(7, player.wood);
            stmt.setInt(8, player.fish);
            stmt.setInt(9, player.salmon);
            stmt.setInt(10, player.shark);

            stmt.setInt(11, player.copper);
            stmt.setInt(12, player.iron);
            stmt.setInt(13, player.coal);
            stmt.setInt(14, player.silver);
            stmt.setInt(15, player.goldOre);

            stmt.setInt(16, player.grilledTrout);
            stmt.setInt(17, player.bakedSalmon);
            stmt.setInt(18, player.sharkSteak);

            stmt.setInt(19, player.swordLevel);
            stmt.setInt(20, player.armorLevel);

            stmt.setDouble(21, player.attackBonus);  // 插入顺序依表字段
            stmt.setDouble(22, player.defenseBonus);

            stmt.setInt(23, PLAYER_ID);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateResource(String type, int delta) {
        String sql = "UPDATE player_data SET " + type + " = " + type + " + ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, PLAYER_ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getResource(String type) {
        String sql = "SELECT " + type + " FROM player_data WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, PLAYER_ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void updateHealth(int delta) {
        String sql = """
        UPDATE player_data
        SET health = MIN(max_health, health + ?)
        WHERE id = ?
    """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, PLAYER_ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFloat(String field, double delta) {
        String sql = "UPDATE player_data SET " + field + " = " + field + " + ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, delta);
            stmt.setInt(2, PLAYER_ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getHealth() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT health FROM player_data WHERE id = ?")) {
            stmt.setInt(1, PLAYER_ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("health");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}

