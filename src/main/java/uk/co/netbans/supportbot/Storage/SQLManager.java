package uk.co.netbans.supportbot.Storage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String GroupsTable = "CREATE TABLE IF NOT EXISTS `groups` (" +
                "`name`      VARCHAR(16)         NOT NULL," +
                "`child`     VARCHAR(16)         DEFAULT 'default'," +
                "`role`      VARCHAR(16)         DEFAULT '0'," +
                "FOREIGN KEY (`child`) REFERENCES `groups` (`name`)," +
                "PRIMARY KEY (`name`));";

        String GroupsIndex = "CREATE INDEX IF NOT EXISTS `groups_child` ON `groups` (`child`);" +
                "CREATE INDEX IF NOT EXISTS `groups_role` ON `groups` (`role`);";

        String GroupPerms = "CREATE TABLE IF NOT EXISTS `group_perms` (" +
                "`id`        INTEGER PRIMARY KEY NOT NULL," +
                "`grp`       VARCHAR(16)         NOT NULL," +
                "`perm`      VARCHAR(16)         NOT NULL," +
                "FOREIGN KEY (`grp`) REFERENCES `groups` (`name`)" +
                ");";

        String GroupPermsIndex = "CREATE INDEX IF NOT EXISTS `group_perms_grp` ON `group_perms` (`grp`);" +
                "CREATE INDEX IF NOT EXISTS `group_perms_perm` ON `group_perms` (`perm`);";

        String UserGroups = "CREATE TABLE IF NOT EXISTS `user_groups` (" +
                "`id`        INTEGER PRIMARY KEY NOT NULL," +
                "`usr`       BIGINT(32)          NOT NULL," +
                "`grp`       VARCHAR(16)         NOT NULL DEFAULT 'default'," +
                "FOREIGN KEY (`grp`) REFERENCES `groups` (`name`)" +
                ");";

        String UserGroupsIndex = "CREATE INDEX IF NOT EXISTS `user_groups_usr` ON `user_groups` (`usr`);" +
                "CREATE INDEX IF NOT EXISTS `user_groups_grp` ON `user_groups` (`grp`);";

        String UserPerms = "CREATE TABLE IF NOT EXISTS `user_perms` (" +
                "    `id`        INTEGER PRIMARY KEY NOT NULL," +
                "    `usr`       BIGINT(32)          NOT NULL," +
                "    `perm`      VARCHAR(16)         NOT NULL" +
                ");";

        String UserPermsIndex = "CREATE INDEX IF NOT EXISTS `user_perms_usr` ON `user_perms` (`usr`);" +
                "CREATE INDEX IF NOT EXISTS `user_perms_perm` ON `user_perms` (`perm`);";


        database.openConnection();
        database.updateSQL(GroupsTable);
        database.updateSQL(GroupsIndex);
        database.updateSQL(GroupPerms);
        database.updateSQL(GroupPermsIndex);
        database.updateSQL(UserGroups);
        database.updateSQL(UserGroupsIndex);
        database.updateSQL(UserPerms);
        database.updateSQL(UserPermsIndex);
        database.closeConnection();
    }

    public void addNewGroup(String groupName) {
        try (Connection c = database.openConnection()){
            String statement = "INSERT INTO groups ('name') VALUES(?);";
            try (PreparedStatement ps = c.prepareStatement(statement)){
                ps.setString(1, groupName);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNewGroup(String groupName, String child) {
        try (Connection c = database.openConnection()){
            String statement = "INSERT INTO groups ('name','child') VALUES(?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)){
                ps.setString(1, groupName);
                ps.setString(2, child);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNewGroup(String groupName, long roleID) {
        try (Connection c = database.openConnection()){
            String statement = "INSERT INTO groups ('name','role') VALUES(?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)){
                ps.setString(1, groupName);
                ps.setString(2, String.valueOf(roleID));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNewGroup(String groupName, String child, String roleID) {
        try (Connection c = database.openConnection()){
            String statement = "INSERT INTO groups ('name','child','role') VALUES(?,?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)){
                ps.setString(1, groupName);
                ps.setString(2, child);
                ps.setString(3, roleID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNewGroupPerm(String group, String perm) {
        try (Connection c = database.openConnection()) {
            String statement = "INSERT INTO group_perms ('grp','perm') VALUES(?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.setString(1, group);
                ps.setString(2, perm);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeGroupPerm(String group, String perm) {
        try (Connection c = database.openConnection()) {
            String statement = "DELETE FROM group_perms WHERE grp='" + group + "' AND perm='" + perm + "';";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addUserToGroup(long userID, String group) {
        try (Connection c = database.openConnection()) {
            String statement = "INSERT INTO user_groups ('usr','grp') VALUES(?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.setLong(1, userID);
                ps.setString(2, group);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeUserFromGroup(long userID, String group) {
        try (Connection c = database.openConnection()) {
            String statement = "DELETE FROM user_groups WHERE usr='" + userID + "' AND grp='" + group + "';";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNewPermToUser(long userID, String perm) {
        try (Connection c = database.openConnection()) {
            String statement = "INSERT INTO user_perms ('usr','perm') VALUES(?,?);";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.setLong(1, userID);
                ps.setString(2, perm);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removePermFromUser(long userID, String perm) {
        try (Connection c = database.openConnection()) {
            String statement = "DELETE FROM user_perms WHERE usr='" + userID + "' AND perm='" + perm + "';";
            try (PreparedStatement ps = c.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}