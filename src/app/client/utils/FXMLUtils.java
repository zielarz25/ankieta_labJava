package app.client.utils;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FXMLUtils {
    // Class to use if no controller instance needed
    public static Parent fxmlLoad(String path){
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(path));
      //  loader.setResources(getResourceBundle());
        try {
            System.out.println(FXMLUtils.class.getResource(path));
            return loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Cant return");
        return null;
    }

    // if controller instance needed, use this
    public static FXMLLoader getLoader(String path) {
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(path));
      //  loader.setResources(getResourceBundle());
        return loader;
    }


}