package wst.dao;

import wst.entity.Shop;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimplePostgresSQLDAO {
    private Connection connection;

    public SimplePostgresSQLDAO(Connection connection) {
        this.connection = connection;
    }

    public SimplePostgresSQLDAO() {
       this.connection = ConnectionUtil.getConnection();

    }
    public static Shop getShopInfo(ResultSet rs) {
        try {
            String name = rs.getString("name");
            String address = rs.getString("address");
            String type = rs.getString("type");
            String city = rs.getString("city");
            Boolean isActive = rs.getBoolean("isActive");

            return new Shop(name, address, isActive, type, city);
        } catch (SQLException ex) {
            Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static List<Shop> getShopsByQuery(Connection connection, String query) {
        List<Shop> shops = new ArrayList<Shop>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Shop shop = SimplePostgresSQLDAO.getShopInfo(rs);
                shops.add(shop);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SimplePostgresSQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return shops;
    }


    public List<Shop> getAllShops() {
        return SimplePostgresSQLDAO.getShopsByQuery(connection, "SELECT * FROM shops");
    }

    public Shop getShopByName(String name) {
        Shop shop = SimplePostgresSQLDAO.getShopsByQuery(connection, "SELECT * FROM shops WHERE name = " + name).get(0);
        return shop;
    }

    public List<Shop> getShopsByLine(int line) {
        return SimplePostgresSQLDAO.getShopsByQuery(connection, "SELECT * FROM shops WHERE line = " + line);
    }

    public List<Shop> getShopsBySmth(String parameters) {
        return SimplePostgresSQLDAO.getShopsByQuery(connection, "SELECT * FROM shops WHERE " + parameters + "=" + parameters);
    }
}
