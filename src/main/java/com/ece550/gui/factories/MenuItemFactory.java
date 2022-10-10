package com.ece550.gui.factories;

import com.ece550.gui.GUI;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;


public class MenuItemFactory {

    public static final String RADIO_MENU = "RadioMenuItem";
    public static final String CHECK_MENU = "CheckMenuItem";
    public static final String DEFAULT_MENU = "MenuItem";

    @Deprecated
    public static MenuItem getInstance(String type, String label, Object actionHandler){
        switch (type) {
            case RADIO_MENU:
            case CHECK_MENU:
                if(actionHandler instanceof ChangeListener)
                    return getInstance(label, "Default", GUI.FALSE);
                else
                    throw new IllegalArgumentException(String.format(MenuBarFactory.ERROR, actionHandler.getClass().toString(), "ChangeListener"));
            case DEFAULT_MENU:
                if(actionHandler instanceof EventHandler)
                    return getInstance(label, (EventHandler) actionHandler);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized menu item type: " + type);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static MenuItem getInstance(String label, EventHandler action) {
        MenuItem mi = new MenuItem(label);
        mi.setOnAction(action);
        return mi;
    }

    @SuppressWarnings("unchecked")
    public static MenuItem getInstance(String label, String defaultState) {
        CheckMenuItem mi = new CheckMenuItem(label);

        mi.setSelected(defaultState.equals(GUI.TRUE));
        return mi;
    }

}
