package com.example.gui.controllers;


import java.util.ResourceBundle;

/**
 * Interface for controllers of windows from which language of the app can be change
 */
public interface LanguageChangeWindow {
    /**
     * Method for reloading window with new captions in right selected language
     * @param bundle resource bundle for right selected language
     */
    void reload(ResourceBundle bundle);
}
