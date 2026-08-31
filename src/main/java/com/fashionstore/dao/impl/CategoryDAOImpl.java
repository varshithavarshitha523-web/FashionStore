package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.model.Category;
import com.fashionstore.util.DBConnection;

public class CategoryDAOImpl implements CategoryDAO {

    private static final String INSERT_CATEGORY_SQL = """
        INSERT INTO categories (category_name, description)
        VALUES (?, ?)
    	""";

    private static final String UPDATE_CATEGORY_SQL = """
            UPDATE categories
            SET category_name = ?, description = ?
            WHERE category_id = ?
            """;

    private static final String DELETE_CATEGORY_SQL = """
            DELETE FROM categories
            WHERE category_id = ?
            """;

    private static final String GET_CATEGORY_BY_ID_SQL = """
            SELECT * FROM categories
            WHERE category_id = ?
            """;
    
    private static final String GET_CATEGORY_BY_NAME_SQL = """
    	    SELECT * FROM categories
    	    WHERE category_name = ?
    	""";

    private static final String GET_ALL_CATEGORIES_SQL = """
    	    SELECT * FROM categories
    	    ORDER BY category_name ASC
    	""";
       
    @Override
    public boolean addCategory(Category category) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL)) {

            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, category.getDescription());

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }   
        
    @Override
    public boolean updateCategory(Category category) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY_SQL)) {

            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, category.getCategoryId());

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
   

    @Override
    public boolean deleteCategory(int categoryId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY_SQL)) {

            preparedStatement.setInt(1, categoryId);

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public Category getCategoryById(int categoryId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_ID_SQL)) {
             
            preparedStatement.setInt(1, categoryId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Category getCategoryByName(String categoryName) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_NAME_SQL)) {
            
            preparedStatement.setString(1, categoryName);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CATEGORIES_SQL );
             ResultSet resultSet = preparedStatement.executeQuery()) {
        	
        	while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
        	
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    private Category mapResultSetToCategory(ResultSet resultSet) throws Exception {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setDescription(resultSet.getString("description"));
        return category;
    }
} 
    
    
  