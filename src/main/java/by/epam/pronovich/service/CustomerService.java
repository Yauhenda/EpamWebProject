package by.epam.pronovich.service;

import by.epam.pronovich.model.Customer;

public interface CustomerService {

    void registr(String login, Integer password);

    Customer autorize(String login, String password);

    void save(Customer customer);

    void update (Customer customer);
}