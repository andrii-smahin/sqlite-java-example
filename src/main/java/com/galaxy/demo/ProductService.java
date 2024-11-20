package com.galaxy.demo;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final String url = "jdbc:sqlite:mydb.db";

    public ProductService() {
        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM products");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public int addProduct(Product product) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO products (name, price) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateProduct(int id, Product product) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE products SET name = ?, price = ? WHERE id = ?")
        ) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, id);
            statement.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM products WHERE id = ?")
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
