package settings;

import java.util.HashSet;
import java.util.Set;

public class IdGenerator {
  private static Set<Integer> idSet = new HashSet<>();

  public static Integer createID(){
    Integer id;
    do {
      id = Integer.valueOf((int)( Math.random()* Integer.MAX_VALUE) + 1);
    } while (!(idSet.add(id)));
    return id;
  }

}
