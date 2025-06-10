package project;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties = new Properties();

    public static void loadConfigurations() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading configuration file: " + ex.getMessage());
            // Provide default values or exit the application
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Example usage:
    // String dbUrl = Configuration.getProperty("db.url");
}
