package com.example.mydictionary;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

public class DictionaryApplication extends Application {
    private final Group titleGroup = new Group();
    private static  ListView<String> listView;
    int xLine=20;
    int yLine=20;
    int yLine2 =100;
    private static DictionaryMap dmap;
    private static Connection connection;

    private Pane createContain(){
        Pane root = new Pane();
        root.setPrefSize(500,500);
        root.getChildren().addAll(titleGroup);
        TextField wordText = new TextField("");
        wordText.setTranslateX(xLine);
        wordText.setTranslateY(yLine);
        wordText.setPrefWidth(200);
        wordText.setPrefHeight(40);

        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(100);
        searchButton.setPrefHeight(30);
        searchButton.setTranslateX(xLine+250);
        searchButton.setTranslateY(yLine);


        listView.setTranslateX(xLine);
        listView.setTranslateY(yLine+50);
        listView.setPrefHeight(100);
        listView.setPrefWidth(400);



        TextArea meaning = new TextArea("i am meaning");
        meaning.setTranslateX(xLine);
        meaning.setTranslateY(yLine2+100);
        meaning.setPrefWidth(400);
        meaning.setPrefHeight(200);
        meaning.setEditable(false);

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String word = wordText.getText();
                dmap.findExact(word,connection);
                if(word.length() ==0){
                    meaning.setText("Please Eneter the Word !");
                }else {
                    meaning.setText(dmap.findWord(word));
                }
            }
        });

        wordText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                 String word = wordText.getText();
                 dmap.findWordSimilar(word,connection);
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String word =listView.getSelectionModel().getSelectedItem();
                String  wordMean = dmap.findWord(word);
                meaning.setText(wordMean);
            }
        });

        titleGroup.getChildren().addAll(wordText,searchButton,listView,meaning);
        return root;
    }

    public static ListView<String> getListView(){
        return listView;
    }

    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(createContain());
        stage.setTitle("My Dictionary");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        listView = new ListView<>();
        dmap = new DictionaryMap(getListView());
        connection = dmap.getConnection();
        launch();
    }

}