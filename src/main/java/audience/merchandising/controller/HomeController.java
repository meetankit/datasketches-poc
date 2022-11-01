package audience.merchandising.controller;

import audience.merchandising.sketch.DataSketchJava;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.datasketches.theta.Sketch;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  private static Map<String, String> locationMap;
  private static DecimalFormat decimalFormat = new DecimalFormat("###,###.##");

  static {
    locationMap = new HashMap<>();
    String[] isoCountries = Locale.getISOCountries();
    for (String country : isoCountries) {
      Locale locale = new Locale("en", country);
      locationMap.put(locale.getDisplayCountry(), locale.getCountry());
    }
    try {
      DataSketchJava.trySketch(Lists.newArrayList(locationMap.values()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @RequestMapping("/")
  public String home(Publisher publisher) throws IOException, ParseException {
    publisher.setCountries(Lists.newArrayList(locationMap.keySet()));
    if (!StringUtils.isEmpty(publisher.getSessionTime())) {
      List<String> locations = new ArrayList<>();
      if (publisher.getRegionList() != null) {
        for (String location : publisher.getRegionList().split(",")) {
          locations.add(locationMap.get(location));
        }
      }
      if (publisher.getName() != null) {
        Long generateIntersection = System.currentTimeMillis();
        if ("avg".equalsIgnoreCase(publisher.getTimeRadio())) {
          org.apache.datasketches.tuple.Sketch sketch =
              DataSketchJava.getAverageSessionTimeIntersection(
                  Long.valueOf(publisher.getPeriod()),
                  publisher.getName().equalsIgnoreCase("cnbc") ? 1 : 2);
          publisher.setOverlap(String.valueOf(decimalFormat.format(sketch.getEstimate())));
          publisher.setOverlapPercent(
              Double.valueOf(decimalFormat.format(sketch.getEstimate() / 1000)));
        } else {
          Sketch sketch =
              DataSketchJava.querySketch(
                  locations,
                  publisher.getTimeRadio() == null
                      ? null
                      : Long.valueOf(publisher.getSessionTime()),
                  publisher.getName().equalsIgnoreCase("cnbc") ? 1 : 2);
          publisher.setOverlap(String.valueOf(decimalFormat.format(sketch.getEstimate())));
          publisher.setOverlapPercent(
              Double.valueOf(decimalFormat.format(sketch.getEstimate() / 1000)));
          publisher.setAccuracy(
              publisher.getName().equalsIgnoreCase("cnbc")
                  ? String.valueOf(
                      decimalFormat.format(
                          sketch.getEstimate()
                              * 100
                              / DataSketchJava.audienceAttributeSketchPublisher1.count))
                  : String.valueOf(
                      decimalFormat.format(
                          sketch.getEstimate()
                              * 100
                              / DataSketchJava.audienceAttributeSketchPublisher2.count)));
        }
        Long endIntersection = System.currentTimeMillis();
        publisher.setTime((endIntersection - generateIntersection) + "ms");
      }
    }
    return "home";
  }

  @RequestMapping("/publisher")
  public String publisher(Publisher publisher) {
    return "publisher";
  }
}
