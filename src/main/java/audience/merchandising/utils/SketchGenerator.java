package audience.merchandising.utils;

import audience.merchandising.sketch.AudienceAttributeSketch;
import audience.merchandising.sketch.DataSketchJava;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import org.apache.datasketches.tuple.aninteger.IntegerSketch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SketchGenerator {

  public static AudienceAttributeSketch generateSketch(String filename, int id, boolean random)
      throws IOException, ParseException {
    final Random randomGenerator = new Random(System.currentTimeMillis());
    AudienceAttributeSketch sketch = new AudienceAttributeSketch();
    JSONParser parser = new JSONParser();
    JSONArray arr = (JSONArray) parser.parse(new FileReader(filename));
    for (Object o : arr) {
      if (random && randomGenerator.nextDouble() > 0.5) {
        continue;
      }
      JSONObject profile = (JSONObject) o;

      Long sessionDuration = (Long) profile.get("sessionDuration");
      if (sessionDuration >= DataSketchJava.MAX_SESION_DURATION) {
        System.out.println("exceeded sessionTime " + sessionDuration);
        continue;
      }

      String epoch = (String) profile.get("_ts");
      Long givenEpoch = Long.parseLong(epoch);
      Long day =
          Instant.ofEpochSecond(givenEpoch).until(DataSketchJava.CURRENT_INSTANT, ChronoUnit.DAYS);
      if (day >= DataSketchJava.DAYS) {
        System.out.println("exceeded day " + day);
        continue;
      }

      JSONObject et = (JSONObject) profile.get("et");
      JSONObject ex = (JSONObject) et.get("ex");
      String location = (String) ex.get("0");
      if (DataSketchJava.locationList.indexOf(location.toUpperCase()) == -1) {
        System.out.println("not found location " + location);
        continue;
      }

      JSONArray xids = (JSONArray) profile.get("xids");
      for (Object obj : xids) {
        String xid = (String) obj;
        /*System.out.println(
        "id is "
            + xid
            + " sessionDuration is "
            + sessionDuration
            + " day is "
            + day.intValue()
            + " locaton is "
            + location);*/
        sketch
            .updateSketchesLocation[day.intValue()][
            DataSketchJava.locationList.indexOf(location.toUpperCase())]
            .update(xid);

        sketch.sessionDurationDailySketch[day.intValue()].update(xid, sessionDuration.intValue());
        sketch.updateSketchesSessionDuration[day.intValue()][sessionDuration.intValue()].update(
            xid);
        sketch.count++;
      }
    }
    if (random) {
      int total = (id == 1) ? 1000000 : 2000000;
      for (int i = 0; i < total; i++) {
        int day = (int) (Math.random() * (DataSketchJava.DAYS));
        int sessionDuration = (int) (Math.random() * (DataSketchJava.MAX_SESION_DURATION - 5) + 5);
        String xid = "ih9uhimFk93dXPqzPsRYWbfiju" + new Random();
        sketch.updateSketchesSessionDuration[day][sessionDuration].update(xid);
        sketch.sessionDurationDailySketch[day].update(xid, sessionDuration);
        sketch
            .updateSketchesLocation[day][
            (int) (Math.random() * (DataSketchJava.locationList.size() - 5) + 5)]
            .update(xid);
      }
    }
    for (IntegerSketch isk : sketch.sessionDurationDailySketch) {
      sketch.union.union(isk);
    }
    return sketch;
  }
}
