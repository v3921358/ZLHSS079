package handling.login.handler;

import client.LoginCrypto;
import constants.ServerConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutoRegister {

    private static final int ACCOUNTS_PER_MAC = 1;
    public static boolean autoRegister = ServerConstants.getAutoReg();
    public static boolean success = false, mac = true;

    public static boolean getAccountExists(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();;
            ps.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return accountExists;
    }

    public static void createAccount(String login, String pwd, String eip, String macs) {
        String sockAddr = eip;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, SessionIP) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, login);
            ps.setString(2, LoginCrypto.hexSha1(pwd));
            ps.setString(3, "autoregister@mail.com");
            ps.setString(4, "2016-04-10");
            ps.setString(5, macs);
            ps.setString(6, "/" + sockAddr.substring(1, sockAddr.lastIndexOf(':')));
            ps.executeUpdate();
            success = true;
            mac = true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
