package com.example.khataregister.DB;

public interface fireBaseInterface {

    public void loadProducts();
    public void loadUsers(String tableName, String userEmail, String password);
    public void loadCustomers();
    public void updateCustomers();
    public void updateProducts() ;

}
