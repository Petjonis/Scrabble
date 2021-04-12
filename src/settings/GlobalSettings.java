package settings;

/**
 * Class for reusable stuff needed across the project
 */
public class GlobalSettings {

    /** final int for board spaces */
    public static final int row = 15 ;
    public static final int columns = 15;

    // System independent filepath to resources folder
    public static final String filepath =
            System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "resources"
                    + System.getProperty("file.separator");
}
