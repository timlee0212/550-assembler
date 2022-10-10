package com.ece550.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * @author Matthew Dickson
 *
 *         Class of static methods used to create dialog boxes
 */
public class DialogFactory {
    //kept in program since storing it in a file could cause a FileNotFound exception that
    //need this string to display the error
    //needs these strings to display the error
    public static final String ERROR_MESSAGE = "I'm sorry Dave, I'm afraid I can't do that.";
    public static final String ERROR_TITLE = "Error";
    public static final String STACKTRACE_LABEL = "The exception stacktrace was:";

    public static final int PREF_WIDTH = 750;
    public static final int PREF_HEIGHT = 200;
    public static final String BAD_FILE = "Invalid file selected";


    /**
     * Show an error from somewhere in the program,
     * taking the exception thrown as the argument.
     *
     *
     * @param e Exception thrown by the error
     */
    public static void showError(Exception e){
        Alert alert = getAlert(e);
        String exceptionText = getStacktraceString(e);
        System.err.println(exceptionText);
        formatAlert(alert, exceptionText);
        alert.show();
    }

    private static void formatAlert(Alert alert, String exceptionText) {
        Label label = new Label(STACKTRACE_LABEL);
        TextArea textArea = initTextArea(exceptionText);
        GridPane expContent = initExceptionArea(textArea);
        setStacktraceText(label, textArea, expContent);
        alert.getDialogPane().setExpandableContent(expContent);
    }

    private static void setStacktraceText(Label label, TextArea textArea, GridPane expContent) {
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
    }

    private static GridPane initExceptionArea(TextArea textArea) {
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        return new GridPane();
    }

    private static TextArea initTextArea(String exceptionText) {
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        return textArea;
    }

    private static String getStacktraceString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private static Alert getAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText(ERROR_MESSAGE);
        alert.setContentText(e.getMessage());
        return alert;
    }

    /**
     * Show an error from somewhere in the program,
     * taking a string describing the error as the argument
     *
     * @param error Error message to be displayed
     */
    public static void showError(String error){
        showError(new Exception(error));
    }

    /**
     *
     * Get a file by creating a file choose dialog. If the user chooses nothing,
     * null is returned and therefore must be checked for
     *
     * @param typeArgs list of window title and allowed file extensions. Must be ordered as title first,
     *                 then pairs of 'file type name' (image, text file, css document, etc) and file extension
     *                 (.png, .jpg, .txt, etc). File extensions must be decimated by only semicolons without
     *                 whitespace
     * @return The user chose file. Might be null if they choose nothing. Null must be returned since callers
     *          will want to handle a lack of selection individually
     */
    public static File fileLoadChooser(String... typeArgs){
        ArrayList<String> extensions = new ArrayList<>();
        FileChooser chooser = setUpFileChooser("Read file", typeArgs, extensions);
        File f = chooser.showOpenDialog(new Stage());
        return checkValidFile(extensions, f);
    }

    /**
     *
     * Save to a file by creating a file choose dialog. If the user chooses nothing,
     * null is returned and therefore must be checked for
     *
     *
     * @return The user chose file. Might be null if they choose nothing. Null must be returned since callers
     *          will want to handle a lack of selection individually
     */
    public static File fileSaveChooser(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Save file location");
        File f = chooser.showDialog(new Stage());
        return f;
    }

    private static File checkValidFile(ArrayList<String> extensions, File f) {
        if(f != null && extensions.contains(getExtension(f))) {
            return f;
        }
        else if(f!= null)
            showError(BAD_FILE);
        return null;
    }

    private static FileChooser setUpFileChooser(String title, String[] typeArgs, List<String> extensions){
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        if(typeArgs.length % 2 != 0) throw new IllegalArgumentException("Expected pairs type names and extensions");
        for(int i = 0; i < typeArgs.length; i+=2){
            String typeTitle = typeArgs[i];
            String[] typeExtensions = typeArgs[i+1].split(";");
            extensions.addAll(Arrays.asList(typeExtensions));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(typeTitle, typeExtensions));
        }
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        return chooser;
    }

    public static String getExtension(File f){
        String fname = f.getName();
        int index = fname.lastIndexOf(".");
        if(index > 0)
            return "*" + fname.substring(index);
        return "";
    }
}
