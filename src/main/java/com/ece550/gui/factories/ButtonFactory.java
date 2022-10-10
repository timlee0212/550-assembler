package com.ece550.gui.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ButtonFactory {

    public static Button getInstance(){
        return getInstance("My Button");
    }

    public static Button getInstance(String label){
        return new Button(label);
    }

    public static Button getInstance(String label, EventHandler<ActionEvent> handler){
        Button b = getInstance(label);
        b.setOnAction(handler);
        return b;
    }

    public static Button getInstance(String label, EventHandler<ActionEvent> handler, Image img){
        Button b = new Button(label, new ImageView(img));
        b.setOnAction(handler);
        return b;
    }
}
