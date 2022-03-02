package lubridate;

import java.util.TimeZone;

public class TimeZoneUtil {

  public static String localTimeZone() {
    return TimeZone.getDefault().getID();
  }

  public boolean validTimeZone(String tz) {
    for (String id : TimeZone.getAvailableIDs()) {
      if (id.equals(tz)) {
        return true;
      }
    }
    return false;
  }
}
