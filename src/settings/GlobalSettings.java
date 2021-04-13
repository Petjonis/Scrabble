/** Class for reusable stuff needed across the project
 *
 * @author fpetek
 * @version 1.0
 * */
package settings;

public class GlobalSettings {

  /** final int for board spaces **/
  public static final int ROWS = 15;
  public static final int COLUMNS = 15;
  /** final int for maxium tiles in a tilebag **/
  public static final int MAXTILES = 100;

  /* System independent filepath to resources folder */
  public static final String filepath =
      System.getProperty("user.dir")
          + System.getProperty("file.separator")
          + "resources"
          + System.getProperty("file.separator");
}
