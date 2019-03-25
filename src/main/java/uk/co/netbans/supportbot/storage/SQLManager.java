package uk.co.netbans.supportbot.storage;

import uk.co.netbans.supportbot.commands.misc.remind.Reminder;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLManager {

    private Database database;
    private File dataFolder;

    public SQLManager(File dataFolder) {
        this.dataFolder = dataFolder;
        try {
            File dbFile = new File(dataFolder + "/" + "supportbot" + ".db");
            database = new SQLite(dbFile, dataFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            writeTables();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeTables() throws SQLException, ClassNotFoundException {
        String RemindTable = "CREATE TABLE IF NOT EXISTS `reminds` (" +
                "`user`      BIGINT(32)         NOT NULL," +
                "`creation`  BIGINT(64)         NOT NULL," +
                "`expiry`    BIGINT(64)         NOT NULL," +
                "`message`   VARCHAR(512)       NOT NULL" +
                ");";

        database.openConnection();
        database.updateSQL(RemindTable);
        database.closeConnection();
    }

    public boolean addNewRemind(Reminder reminder) {
        try (Connection c = database.openConnection()) {
            String statement = "INSERT INTO reminds (user,creation,expiry,message) VALUES(?,?,?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)){
                ps.setLong(1, reminder.getUserID());
                ps.setLong(2, reminder.getCreation());
                ps.setLong(3, reminder.getExpiry());
                ps.setString(4, reminder.getMessage());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeRemind(Reminder reminder) {
        try (Connection c = database.openConnection()) {
            String statement = "DELETE FROM reminds WHERE user='" + reminder.getUserID() + "' AND creation='" + reminder.getCreation() + "' AND expiry='" + reminder.getExpiry() + "' AND message='" + reminder.getMessage() + "';";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.executeQuery();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Reminder> getRemindersForUser(long userID) {
        ArrayList<Reminder> reminders = new ArrayList<>();
        try (Connection c = database.openConnection()) {
            String statement = "SELECT * FROM reminds WHERE user='" + userID + "';";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                 while (rs.next()) {
                     reminders.add(new Reminder(rs.getLong("user"), rs.getLong("creation"), rs.getLong("expiry"), rs.getString("message")));
                 }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reminders;
    }
}