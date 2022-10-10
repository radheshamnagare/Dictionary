package com.example.mydictionary;

import javafx.scene.control.ListView;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.sql.*;

public class DictionaryMap {

    private static ListView<String> listView;
    private static HashMap<String,String> dictMap ;

    DictionaryMap(ListView<String> listView){
        this.listView = listView;
        dictMap = new HashMap<>();
    }

    public void addWord(String word,String meaning){
        dictMap.put(word,meaning);
    }

    public String findWord(String word){
        if(dictMap.containsKey(word))
            return dictMap.get(word);
        return "Word meaning not found...!";
    }

    public static Connection getConnection(){
        Connection connection = null;
        String url = "jdbc:postgresql://localhost:5432/book";
        String password = "spring123";
        String user = "spring_jpa";
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("Connected...");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }


    public static void findWordSimilar(String word,Connection connection){
         dictMap.clear();
         listView.getItems().clear();
         String query = "select * from words where word like ?";
         try{

             PreparedStatement statement = connection.prepareStatement(query);
             statement.setString(1,"%"+word+"%");
             ResultSet res = statement.executeQuery();
             while(res.next()){
                 dictMap.put(res.getString("word"),res.getString("meaning"));
                 listView.getItems().add(res.getString("word"));
             }
         }catch (Exception e){
             e.printStackTrace();
         }
    }
    public static void  findExact(String word,Connection connection){
        dictMap.clear();
        listView.getItems().clear();
        String query = "select * from words where word=?";
        try{

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,word);
            ResultSet res = statement.executeQuery();
            while(res.next()){
                dictMap.put(res.getString("word"),res.getString("meaning"));
                listView.getItems().add(res.getString("word"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
