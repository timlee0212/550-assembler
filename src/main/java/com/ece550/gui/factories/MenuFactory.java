package com.ece550.gui.factories;


import javafx.scene.control.Menu;

public class MenuFactory {

    public static Menu getInstance(String label){
        return new Menu(label);
    }


}
