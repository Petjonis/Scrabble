package settings;

/**
 * Class for reusable stuff needed across the project
 */
public class GlobalSettings {
    // System independent filepath to resources folder
    public static final String filepath =
            System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "resources"
                    + System.getProperty("file.separator");
}
