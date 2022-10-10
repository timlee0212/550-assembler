package com.ece550.gui.factories;

import javafx.scene.control.TextField;

public class TextFieldFactory {

    public static TextField getInstance(){
        return getInstance("");
    }

    public static TextField getInstance(String promptText){
        TextField tf = new TextField();
        tf.setPromptText(promptText);
        return tf;
    }
}
