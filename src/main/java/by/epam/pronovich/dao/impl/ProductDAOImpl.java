package by.epam.pronovich.dao.impl;

import by.epam.pronovich.dao.ProductDAO;
import by.epam.pronovich.exception.DAOException;
import by.epam.pronovich.model.Brand;
import by.epam.pronovich.model.Catalog;
import by.epam.pronovich.model.Product;
import by.epam.pronovich.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ProductDAOImpl implements ProductDAO {

    private final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

    private final String GET_ALL = "SELECT p.id as p_id, catalog_id, brand_id, model, p.description as p_description, " +
            "price, product_img, quantity_in_stock, parent_id, c.description as c_description, " +
            " name from shop.product as p inner join shop.catalog c on p.catalog_id = c.id inner join shop.brand " +
            "b on p.brand_id = b.id";

    private final String FIND_BY_CATEGORY = GET_ALL + " where catalog_id=? ";
    private final String FIND_BY_ID = GET_ALL + " where p.id=?";
    private final String SAVE = "INSERT INTO shop.product " +
            "(catalog_id, brand_id, model, description, price, product_img, quantity_in_stock) values (?,?,?,?,?,?,?)";
    private final String UPDATE = "UPDATE shop.product SET catalog_id=?, brand_id=?, model=?, description=?, price=?," +
            " product_img=?, quantity_in_stock=? where id=?";

    private final String SEARCH = GET_ALL + " where to_tsvector(b.name) || to_tsvector(model) " +
            "|| to_tsvector(c.description) " +
            "@@ plainto_tsquery(?)";

    @Override
    public List<Product> seach(String text) throws DAOException {
        List<Product> productList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH)) {
            preparedStatement.setString(1, text);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productList.add(getProductFrom(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return productList;
    }


    @Override
    public void update(Product product) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            prepareProductForUpdating(product, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Failed update product info", e);
            throw new DAOException(e);
        }
    }

    @Override
    public Product save(Product product) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            prepareProductForSaving(product, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getInt("id"));
            }
        } catch (SQLException e) {
            logger.warn("Failed save product", e);
            throw new DAOException(e);
        }
        return product;
    }


    @Override
    public List<Product> getByCategoryId(Integer id) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFrom(resultSet));
            }
        } catch (SQLException e) {
            logger.warn("Failed get product by category id", e);
            throw new DAOException(e);
        }
        return products;
    }


    @Override
    public Product getById(Integer id) {
        Product product = null;
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                product = getProductFrom(resultSet);
            }
        } catch (SQLException e) {
            logger.warn("Failed get product by id", e);
            throw new DAOException(e);
        }
        return product;
    }

    @Override
    public List<Product> getAll() {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFrom(resultSet));
            }
        } catch (SQLException e) {
            logger.warn("Failed get all product", e);
            throw new DAOException(e);
        }
        return products;
    }

    private void prepareProductForUpdating(Product product, PreparedStatement preparedStatement) throws SQLException {
        prepareProductForSaving(product, preparedStatement);
        preparedStatement.setInt(8, product.getId());
    }

    private void prepareProductForSaving(Product product, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, product.getCatalog().getId());
        preparedStatement.setInt(2, product.getBrand().getId());
        preparedStatement.setString(3, product.getModel());
        preparedStatement.setString(4, product.getDescription());
        preparedStatement.setDouble(5, product.getPrice());
        preparedStatement.setString(6, product.getImg());
        preparedStatement.setInt(7, product.getQuantity());
    }

    private Product getProductFrom(ResultSet resultSet) throws SQLException {
        Product product;
        product = Product.builder()
                .id(resultSet.getInt("p_id"))
                .catalog(Catalog.builder()
                        .id(resultSet.getInt("catalog_id"))
                        .parentId(resultSet.getInt("parent_id"))
                        .description(resultSet.getString("c_description"))
                        .build())
                .brand(Brand.builder()
                        .id(resultSet.getInt("brand_id"))
                        .name(resultSet.getString("name")).build())
                .model(resultSet.getString("model"))
                .description(resultSet.getString("p_description"))
                .price(resultSet.getDouble("price"))
                .img(resultSet.getString("product_img"))
                .quantity(resultSet.getInt("quantity_in_stock"))
                .build();
        return product;
    }
}
