package com.example.khataregister;

import java.util.ArrayList;
import java.util.Hashtable;

public interface sqlInterface {

    public void save(Hashtable<String,String> attributes, String tableName);
    public void save(ArrayList<Hashtable<String,String>> objects, String tableName);
    public ArrayList<Hashtable<String,String>> loadALL(String tableName);
    public Hashtable<String, String> loadById(String id, String tableName);
    public Integer getCustomerId();
    public void updateBalance(Hashtable<String, String> customer);
    public void truncateDb();
}
