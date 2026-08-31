package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;

public class UserDAOImpl implements UserDAO {

    private static final String INSERT_USER_SQL = """
        INSERT INTO users (
            full_name, email, phone, password,
            address_line1, address_line2, city, state, pincode, country
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String LOGIN_USER_SQL = """
        SELECT * FROM users
        WHERE email = ? AND password = ?
    """;

    private static final String GET_USER_BY_ID_SQL = """
        SELECT * FROM users
        WHERE user_id = ?
    """;

    private static final String GET_USER_BY_EMAIL_SQL = """
        SELECT * FROM users
        WHERE email = ?
    """;

    private static final String GET_USER_BY_PHONE_SQL = """
        SELECT * FROM users
        WHERE phone = ?
    """;

    private static final String UPDATE_USER_SQL = """
        UPDATE users
        SET full_name = ?, email = ?, phone = ?, password = ?,
            address_line1 = ?, address_line2 = ?, city = ?, state = ?, pincode = ?, country = ?
        WHERE user_id = ?
    """;

    private static final String UPDATE_PASSWORD_SQL = """
        UPDATE users
        SET password = ?
        WHERE user_id = ?
    """;

    private static final String EMAIL_EXISTS_SQL = """
        SELECT 1 FROM users
        WHERE email = ?
    """;

    private static final String PHONE_EXISTS_SQL = """
        SELECT 1 FROM users
        WHERE phone = ?
    """;

    private static final String GET_ALL_USERS_SQL = """
        SELECT * FROM users
        ORDER BY user_id DESC
    """;

    @Override
    public boolean registerUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAddressLine1());
            preparedStatement.setString(6, user.getAddressLine2());
            preparedStatement.setString(7, user.getCity());
            preparedStatement.setString(8, user.getState());
            preparedStatement.setString(9, user.getPinCode());
            preparedStatement.setString(10, user.getCountry());

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public User loginUser(String email, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_USER_SQL)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(int userId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID_SQL)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL_SQL)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByPhone(String phone) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_PHONE_SQL)) {

            preparedStatement.setString(1, phone);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean updateUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
            
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAddressLine1());
            preparedStatement.setString(6, user.getAddressLine2());
            preparedStatement.setString(7, user.getCity());
            preparedStatement.setString(8, user.getState());
            preparedStatement.setString(9, user.getPinCode());
            preparedStatement.setString(10, user.getCountry());
            preparedStatement.setInt(11, user.getUserId());

            return preparedStatement.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_SQL)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean emailExists(String email) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EMAIL_EXISTS_SQL)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean phoneExists(String phone) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(PHONE_EXISTS_SQL)) {

            preparedStatement.setString(1, phone);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
    
    private User mapResultSetToUser(ResultSet resultSet) throws Exception {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setPassword(resultSet.getString("password"));
        user.setAddressLine1(resultSet.getString("address_line1"));
        user.setAddressLine2(resultSet.getString("address_line2"));
        user.setCity(resultSet.getString("city"));
        user.setState(resultSet.getString("state"));
        user.setPinCode(resultSet.getString("pincode"));
        user.setCountry(resultSet.getString("country"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        return user;
    }
}