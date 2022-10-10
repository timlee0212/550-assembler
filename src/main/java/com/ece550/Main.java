package com.ece550;

import com.ece550.gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{
    private static Stage stage;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage s) throws Exception {
        stage = s;
        GUI gui = new GUI(s);
    }
}
