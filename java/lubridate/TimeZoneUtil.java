package lubridate;

import org.renjin.sexp.*;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class TimeZoneUtil {

  private static final StringArrayVector classDefinition;
  static {
    StringArrayVector.Builder builder = StringArrayVector.newBuilder();
    builder.add("POSIXct");
    builder.add("POSIXt");
    classDefinition = builder.build();
  }

  public static List<String> TIME_ZONES = Arrays.asList(TimeZone.getAvailableIDs());

  public static String localTimeZone() {
    return TimeZone.getDefault().getID();
  }

  public static boolean validTimeZone(String tz) {
    return TIME_ZONES.contains(tz);
  }

  /**
   * a POSIXct date has the class set to c("POSIXct", "POSIXt")
   * and timezone set as attr(xx, "tzone")
   *
   * @param dtSEXP e.g. 0.0, 9.50493599E8, c(1.266112799E9, 1.266199199E9, 1.266285599E9)
   * @param yearSEXP e.g. 2000.0
   * @param monthSEXP e,g, 1.0
   * @param ydaySEXP
   * @param mdaySEXP e.g 14.0, c(14, 15, 16), c(1L)
   * @param wdaySEXP
   * @param hourSEXP e.g. 1.0, c(0L)
   * @param minuteSEXP e.g. 59.0, c(59, 59, 59), c(0L)
   * @param secondSEXP e.g. 59.0, c(1, 1, 1)
   * @param tzSEXP e.g. GMT, UTC
   * @param rollSEXP e.g. FALSE
   * @param weekStartSEXP e.g. c(7L)
   * @return a POSIXct date
   */
  public static SEXP updateDt(SEXP dtSEXP, SEXP yearSEXP, SEXP monthSEXP,
                              SEXP ydaySEXP, SEXP mdaySEXP, SEXP wdaySEXP,
                              SEXP hourSEXP, SEXP minuteSEXP, SEXP secondSEXP,
                              SEXP tzSEXP, SEXP rollSEXP, SEXP weekStartSEXP) {
    double[] dt;
    if (dtSEXP instanceof DoubleArrayVector) {
      dt = ((DoubleArrayVector)dtSEXP).toDoubleArray();
    } else {
      dt = new double[]{dtSEXP.asReal()};
    }
    Integer year = yearSEXP.length() == 0 ? null : yearSEXP.asInt();
    Double month = monthSEXP.length() == 0 ? null : monthSEXP.asReal();
    Integer yday = ydaySEXP.length() == 0 ? null : ydaySEXP.asInt();
    Integer mday = mdaySEXP.length() == 0 ? null : mdaySEXP.asInt();
    Integer wday = wdaySEXP.length() == 0 ? null : wdaySEXP.asInt();
    Integer hour = hourSEXP.length() == 0 ? null : hourSEXP.asInt();
    Integer minute = minuteSEXP.length() == 0 ? null : minuteSEXP.asInt();
    Double second = secondSEXP.length() == 0 ? null : secondSEXP.asReal();
    String tz = tzSEXP.asString();
    Boolean roll = asBoolean(rollSEXP.asLogical());
    Integer weekStart = weekStartSEXP.length() == 0 ? null : weekStartSEXP.asInt();

    System.out.println("dtSEXP = " + dtSEXP + ", dt = " + Arrays.toString(dt));
    System.out.println("yearSEXP = " + yearSEXP + ", year = " + year);
    System.out.println("monthSEXP = " + monthSEXP + ", month = " + month);
    System.out.println("ydaySEXP = " + ydaySEXP + ", yday = " + yday);
    System.out.println("mdaySEXP = " + mdaySEXP + ", mday = " + mday);
    System.out.println("wdaySEXP = " + wdaySEXP + ", wday = " + wday);
    System.out.println("hourSEXP = " + hourSEXP + ", hour = " + hour);
    System.out.println("minuteSEXP = " + minuteSEXP + ", minute = " + minute);
    System.out.println("secondSEXP = " + secondSEXP + ", second = " + second);
    System.out.println("tzSEXP = " + tzSEXP + ", tz = " + tz);
    System.out.println("rollSEXP = " + rollSEXP + ", roll = " + roll);
    System.out.println("weekStartSEXP = " + weekStartSEXP + ", weekStart = " + weekStart);

    ZonedDateTime zonedDateTime = ZonedDateTime.now(); // TODO Fix!
    if (dt.length == 1 && dt[0] > 0) {
      Instant instant = Instant.ofEpochSecond(Double.valueOf(dt[0]).longValue());
      ZoneId zoneId = ZoneId.of(tz);
      zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
      System.out.println("converted dt to " + zonedDateTime);
    }
    DoubleArrayVector.Builder posixCt = new DoubleArrayVector.Builder();
    posixCt.add(zonedDateTime.toEpochSecond());
    posixCt.setAttribute("class", classDefinition);
    StringVector.Builder tzVal = StringArrayVector.newBuilder();
    tzVal.add(tz);
    posixCt.setAttribute("tzone", tzVal.build());
    return posixCt.build();
  }

  private static Boolean asBoolean(Logical logical) {
    switch (logical) {
      case TRUE: return Boolean.TRUE;
      case FALSE:return Boolean.FALSE;
      case NA: default: return null;
    }
  }
}
