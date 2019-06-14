package wst.dao;

import wst.entity.Shop;
import wst.query.BuildQuery;
import wst.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ShopDAO {
    private final String TABLE_NAME = "shops";
    private final String ID_COLUMN = "id";
    private final String NAME_COLUMN = "name";
    private final String IS_ACTIVE_COLUMN = "is_active";
    private final String TYPE_COLUMN = "type";
    private final String ADDRESS_COLUMN = "address";
    private final String CITY_COLUMN = "city";

    private final Connection connection;

    public ShopDAO(Connection connection) {
        this.connection = connection;
    }

    public ShopDAO() {
        this.connection = ConnectionUtil.getConnection();
    }

    public List<Shop> findAll() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM shops");
            List<Shop> result = rsToEntities(statement.getResultSet());
            System.out.println(result);
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Shop> findById(int id) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM shops where id = " + id);

            List<Shop> result = rsToEntities(statement.getResultSet());

            if (result.isEmpty())
                return null;

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public long create(String name, String city, String address, Boolean isActive, String type) {
        try {
            try (PreparedStatement stmnt = connection.prepareStatement(
                    "INSERT INTO shops(name, is_active, address, city, type) " +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

                stmnt.setString(1, name);
                stmnt.setBoolean(2, isActive);
                stmnt.setString(3, address);
                stmnt.setString(4, city);
                stmnt.setString(5, type);

                int newId = stmnt.executeUpdate();
                
                return newId;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public List<Shop> read(String name, String city, String address, Boolean isActive, String type) throws SQLException {
        if (Stream.of(name, address, isActive, city, type).allMatch(Objects::isNull)) {
            return findAll();
        }

        Query query = new BuildQuery()
            .tableName(TABLE_NAME)
            .selectColumns(ID_COLUMN, NAME_COLUMN, ADDRESS_COLUMN, IS_ACTIVE_COLUMN, TYPE_COLUMN, CITY_COLUMN)
            .condition(new Condition(NAME_COLUMN, name, String.class))
            .condition(new Condition(ADDRESS_COLUMN, address, String.class))
            .condition(new Condition(IS_ACTIVE_COLUMN, isActive, Boolean.class))
            .condition(new Condition(TYPE_COLUMN, type, String.class))
            .condition(new Condition(CITY_COLUMN, city, String.class))
            .buildPreparedStatementQuery();

        try {
            PreparedStatement ps = connection.prepareStatement(query.getQueryString());
            query.initPreparedStatement(ps);
            ResultSet rs = ps.executeQuery();
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE,rs.toString());
            return rsToEntities(rs);
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int update(long id, String name, String city, String address, Boolean isActive, String type) {
        try {
            connection.setAutoCommit(true);
            String updateStr = "UPDATE shops SET name = ?, is_active = ?, address = ?, city = ?, type = ? WHERE id = ?";
            try (PreparedStatement stmnt = connection.prepareStatement(updateStr)) {
                stmnt.setString(1, name);
                stmnt.setBoolean(2, isActive);
                stmnt.setString(3, address);
                stmnt.setString(4, city);
                stmnt.setString(5, type);
                stmnt.setLong(6, id);

                int updated = stmnt.executeUpdate();
                return updated;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int delete(long id) {
        try {
            connection.setAutoCommit(true);
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM shops WHERE id = ?")) {
                ps.setLong(1, id);
                return ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShopDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    private List<Shop> rsToEntities(ResultSet rs) throws SQLException {
        List<Shop> result = new ArrayList<>();

        while (rs.next()) {
            result.add(resultSetToEntity(rs));
        }

        return result;
    }

    private Shop resultSetToEntity(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String type = rs.getString("type");
        String city = rs.getString("city");
        Boolean isActive = rs.getBoolean("is_active");

        return new Shop(id, name, address, isActive, type, city);
    }
}