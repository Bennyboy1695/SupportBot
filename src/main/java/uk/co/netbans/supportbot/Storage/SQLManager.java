package uk.co.netbans.supportbot.Storage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class SQLManager {

    private Database database;
    private File dataFolder;
    private String commandPermsTable = "commandPermsTable";
    private String permRolesTable = "permsRolesTable";
    private String userPermsInfoTable = "userPermsInfoTable";

    public SQLManager(File dataFolder) {
        this.dataFolder = dataFolder;
        try {
            File dbFile = new File(dataFolder + "/" + "supportbot" + ".db");
            database = new SQLite(dbFile, dataFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            writeTables(commandPermsTable, permRolesTable, userPermsInfoTable);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeTables(String commandPerms, String permRoles, String UserPermsInfo) throws SQLException, ClassNotFoundException {
        String CreateCommandPermsTable = "CREATE TABLE IF NOT EXISTS `" + this.commandPermsTable + "` (" +
                "`command` varchar(64)," +
                "`permrole` varchar(64) NOT NULL DEFAULT 0" +
                ");";

        String CreatePermRolesTable = "CREATE TABLE IF NOT EXISTS `" + this.permRolesTable + "` (" +
                "`permrole` varchar(64)," +
                "`linkeddiscordrole` varchar(64) NOT NULL DEFAULT 0" +
                ");";

        String CreateUserPermsInfoTable = "CREATE TABLE IF NOT EXISTS `" + this.userPermsInfoTable + "` (" +
                "`userlongid` varchar(64)," +
                "`permrole` varchar(64)" +
                ");";


        database.openConnection();
        database.updateSQL(CreateCommandPermsTable);
        database.updateSQL(CreatePermRolesTable);
        database.updateSQL(CreateUserPermsInfoTable);
        database.closeConnection();
    }

    public void createCommandWithPerms(String commandName, Optional<String> perm) {
        try (Connection c = database.openConnection()) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + this.commandPermsTable + " (command, permrole) VALUES(?,?)")) {
                ps.setString(1, commandName);
                if (perm.isPresent()) ps.setString(2, perm.get());
                ps.executeUpdate();
                database.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateCommandWithPerms(String commandName, String perm) {
        try (Connection c = database.openConnection()) {
            try (PreparedStatement ps = c.prepareStatement("UPDATE " + this.commandPermsTable + " SET perm=? WHERE command='" + commandName + "';")) {
                ps.setString(1, perm);
                ps.executeUpdate();
                database.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createPermRoleWithLinkedRole(String permRoleName, Optional<Long> roleID) {
        try (Connection c = database.openConnection()) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + this.permRolesTable + " (permrole, linkeddiscordrole) VALUES(?,?)")) {
                ps.setString(1, permRoleName);
                if (roleID.isPresent()) ps.setString(2, roleID.get().toString());
                ps.executeUpdate();
                database.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updatePermRoleWithLinkedRole(String permRoleName, long roleID) {
        try (Connection c = database.openConnection()) {
            try (PreparedStatement ps = c.prepareStatement("UPDATE " + this.permRolesTable + " SET linkeddiscordrole=? WHERE permrole='" + permRoleName + "';")) {
                ps.setString(1, String.valueOf(roleID));
                ps.executeUpdate();
                database.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createUserWithPerms(long userLong, Optional<String> permRoleName) {
        try (Connection c = database.openConnection()) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + this.userPermsInfoTable + " (userlongid, permrole) VALUES(?,?)")) {
                ps.setString(1, String.valueOf(userLong));
                if (permRoleName.isPresent()) ps.setString(2, permRoleName.get());
                ps.executeUpdate();
                database.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateUserWithPerms(long userLong, String permRoleName, String oldPermRoleName) {
        try (Connection c = database.openConnection()) {
            try (PreparedStatement ps = c.prepareStatement("UPDATE " + this.userPermsInfoTable + " SET permrole=? WHERE userlongid='" + userLong + "' AND permrole='" + oldPermRoleName +"';")) {
                ps.setString(1, String.valueOf(permRoleName));
                ps.executeUpdate();
                database.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}