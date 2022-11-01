package audience.merchandising.sketch;

import audience.merchandising.utils.SketchGenerator;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.datasketches.theta.Intersection;
import org.apache.datasketches.theta.SetOperation;
import org.apache.datasketches.theta.Sketch;
import org.apache.datasketches.theta.Union;
import org.apache.datasketches.tuple.CompactSketch;
import org.apache.datasketches.tuple.Filter;
import org.apache.datasketches.tuple.Summary;
import org.apache.datasketches.tuple.aninteger.IntegerSummary;
import org.json.simple.parser.ParseException;
import org.springframework.util.ObjectUtils;

public class DataSketchJava {

  public static final Integer DAYS = 30;
  public static final Integer MAX_SESION_DURATION = 101;
  public static final String PUBLISHER_DATA = "AepAudienceMerchandising.json";
  public static final String ADVERTISER_DATA = "AepAudienceMerchandising.json";
  public static final Instant CURRENT_INSTANT = Instant.now();
  public static List<String> locationList = new ArrayList<>();
  public static AudienceAttributeSketch audienceAttributeSketchPublisher1;
  private static AudienceAttributeSketch audienceAttributeSketchAdvertiser;
  public static AudienceAttributeSketch audienceAttributeSketchPublisher2;

  public static void trySketch(List<String> locationList) throws IOException, ParseException {
    DataSketchJava.locationList = locationList;
    System.out.println("Sketching Test Starts");

    Long generateSketchAdvertiser = System.currentTimeMillis();
    audienceAttributeSketchAdvertiser = SketchGenerator.generateSketch(ADVERTISER_DATA, 0, false);
    Long endSketchAdvertiser = System.currentTimeMillis();
    System.out.println(
        "\nTime Taken: \n Advertiser Generate Skech:"
            + (endSketchAdvertiser - generateSketchAdvertiser)
            + "ms");

    Long generateSketchPublisher1 = System.currentTimeMillis();
    audienceAttributeSketchPublisher1 = SketchGenerator.generateSketch(PUBLISHER_DATA, 1, true);
    Long endSketchPublisher1 = System.currentTimeMillis();
    System.out.println(
        "\nTime Taken: \n Publisher Generate Skech:"
            + (endSketchPublisher1 - generateSketchPublisher1)
            + "ms");

    Long generateSketchPublisher2 = System.currentTimeMillis();
    audienceAttributeSketchPublisher2 = SketchGenerator.generateSketch(PUBLISHER_DATA, 2, true);
    Long endSketchPublisher2 = System.currentTimeMillis();
    System.out.println(
        "\nTime Taken: \n Publisher Generate Skech:"
            + (endSketchPublisher2 - generateSketchPublisher2)
            + "ms");

    System.out.println("Sketching Test Ends\n\n\n");
  }

  public static Sketch querySketch(List<String> locations, Long sessionDuration, int id) {
    if (id == 1) {
      return queryPublisher1Sketch(locations, sessionDuration);
    }
    return queryPublisher2Sketch(locations, sessionDuration);
  }

  // this section deserializes the sketches, produces union and prints some results
  public static Sketch queryPublisher1Sketch(List<String> locations, Long sessionDuration) {
    Long generateIntersection = System.currentTimeMillis();

    Union locationUnionPublisher = SetOperation.builder().buildUnion();
    Union sessionDurationUnionPublisher = SetOperation.builder().buildUnion();

    Union locationUnionAdvertiser = SetOperation.builder().buildUnion();
    Union sessionDurationUnionAdvertiser = SetOperation.builder().buildUnion();

    // int size = 0;

    for (int i = DAYS; i > 0; i--) {
      locations = ObjectUtils.isEmpty(locations) ? locationList : locations;
      for (String location : locations) {
        locationUnionPublisher.union(
            audienceAttributeSketchPublisher1
                .updateSketchesLocation[DAYS - i][locationList.indexOf(location.toUpperCase())]);

        locationUnionAdvertiser.union(
            audienceAttributeSketchAdvertiser
                .updateSketchesLocation[DAYS - i][locationList.indexOf(location.toUpperCase())]);

        /*size +=
        audienceAttributeSketchAdvertiser
            .updateSketchesLocation[DAYS - i][locationList.indexOf(location.toUpperCase())]
            .compact()
            .toByteArray()
            .length;*/
      }

      sessionDuration = ObjectUtils.isEmpty(sessionDuration) ? 0L : sessionDuration;
      for (Long j = sessionDuration; j < MAX_SESION_DURATION; j++) {
        sessionDurationUnionPublisher.union(
            audienceAttributeSketchPublisher1
                .updateSketchesSessionDuration[DAYS - i][j.intValue()]);
      }

      for (Long j = 0L; j < MAX_SESION_DURATION; j++) {
        sessionDurationUnionAdvertiser.union(
            audienceAttributeSketchAdvertiser
                .updateSketchesSessionDuration[DAYS - i][j.intValue()]);
      }

      /*size +=
      audienceAttributeSketchAdvertiser
          .updateSketchesSessionDuration[DAYS - i][j.intValue()]
          .compact()
          .toByteArray()
          .length;*/
    }

    // System.out.println("size is " + size);

    /*System.out.println(
        "sessionTime union is " + sessionDurationUnionPublisher.getResult().getEstimate());
    System.out.println("location union is " + locationUnionPublisher.getResult().getEstimate());*/

    Intersection intersectionPublisher = SetOperation.builder().buildIntersection();
    intersectionPublisher.intersect(locationUnionPublisher.getResult());
    intersectionPublisher.intersect(sessionDurationUnionPublisher.getResult());

    Intersection intersectionAdvertiser = SetOperation.builder().buildIntersection();
    intersectionAdvertiser.intersect(locationUnionAdvertiser.getResult());
    intersectionAdvertiser.intersect(sessionDurationUnionAdvertiser.getResult());

    Intersection intersection = SetOperation.builder().buildIntersection();
    intersection.intersect(intersectionAdvertiser.getResult());
    intersection.intersect(intersectionPublisher.getResult());
    Sketch intersectionResult = intersection.getResult();

    Long endIntersection = System.currentTimeMillis();

    // System.out.println("Intersection unique count estimate: " +
    // intersectionResult.getEstimate());
    System.out.println(
        "\nTime Taken: \n Intersection Query Skech:"
            + (endIntersection - generateIntersection)
            + "ms");

    return intersectionResult;
  }

  public static Sketch queryPublisher2Sketch(List<String> locations, Long sessionDuration) {
    Long generateIntersection = System.currentTimeMillis();

    Union locationUnionPublisher = SetOperation.builder().buildUnion();
    Union sessionDurationUnionPublisher = SetOperation.builder().buildUnion();

    Union locationUnionAdvertiser = SetOperation.builder().buildUnion();
    Union sessionDurationUnionAdvertiser = SetOperation.builder().buildUnion();

    for (int i = DAYS; i > 0; i--) {
      locations = ObjectUtils.isEmpty(locations) ? locationList : locations;
      for (String location : locations) {
        locationUnionPublisher.union(
            audienceAttributeSketchPublisher2
                .updateSketchesLocation[DAYS - i][locationList.indexOf(location.toUpperCase())]);

        locationUnionAdvertiser.union(
            audienceAttributeSketchAdvertiser
                .updateSketchesLocation[DAYS - i][locationList.indexOf(location.toUpperCase())]);
      }

      sessionDuration = ObjectUtils.isEmpty(sessionDuration) ? 0L : sessionDuration;
      for (Long j = sessionDuration; j < MAX_SESION_DURATION; j++) {
        sessionDurationUnionPublisher.union(
            audienceAttributeSketchPublisher2
                .updateSketchesSessionDuration[DAYS - i][j.intValue()]);
      }
      for (Long j = 0L; j < MAX_SESION_DURATION; j++) {
        sessionDurationUnionAdvertiser.union(
            audienceAttributeSketchAdvertiser
                .updateSketchesSessionDuration[DAYS - i][j.intValue()]);
      }
    }

    /*System.out.println(
        "sessionTime union is " + sessionDurationUnionPublisher.getResult().getEstimate());
    System.out.println("location union is " + locationUnionPublisher.getResult().getEstimate());*/

    Intersection intersectionPublisher = SetOperation.builder().buildIntersection();
    intersectionPublisher.intersect(locationUnionPublisher.getResult());
    intersectionPublisher.intersect(sessionDurationUnionPublisher.getResult());

    Intersection intersectionAdvertiser = SetOperation.builder().buildIntersection();
    intersectionAdvertiser.intersect(locationUnionAdvertiser.getResult());
    intersectionAdvertiser.intersect(sessionDurationUnionAdvertiser.getResult());

    Intersection intersection = SetOperation.builder().buildIntersection();
    intersection.intersect(intersectionAdvertiser.getResult());
    intersection.intersect(intersectionPublisher.getResult());
    Sketch intersectionResult = intersection.getResult();

    Long endIntersection = System.currentTimeMillis();

    // System.out.println("Intersection unique count estimate: " +
    // intersectionResult.getEstimate());
    System.out.println(
        "\nTime Taken: \n Intersection Query Skech:"
            + (endIntersection - generateIntersection)
            + "ms");

    return intersectionResult;
  }

  public static CompactSketch<Summary> getAverageSessionTimeIntersection(
      Long averageSessionTime, int id) {
    if (id == 1) {
      return getPublisher1AverageSessionTimeIntersection(averageSessionTime);
    }
    return getPublisher2AverageSessionTimeIntersection(averageSessionTime);
  }

  public static CompactSketch<Summary> getPublisher1AverageSessionTimeIntersection(
      Long averageSessionTime) {
    Long generateIntersection = System.currentTimeMillis();

    Filter<IntegerSummary> filter =
        new Filter<>(
            integerSummary -> {
              if (integerSummary.getValue() >= averageSessionTime * DAYS) {
                return true;
              }
              return false;
            });

    CompactSketch<IntegerSummary> sketch =
        filter.filter(audienceAttributeSketchPublisher1.union.getResult());

    org.apache.datasketches.tuple.Intersection intersection =
        new org.apache.datasketches.tuple.Intersection(audienceAttributeSketchAdvertiser.setOps);
    CompactSketch<Summary> intersectionResult =
        intersection.intersect(audienceAttributeSketchAdvertiser.union.getResult(), sketch);
    System.out.println("intersection result estimate " + intersectionResult.getEstimate());
    Long endIntersection = System.currentTimeMillis();

    System.out.println(
        "\nTime Taken: \n Average Intersection Query Skech:"
            + (endIntersection - generateIntersection)
            + "ms");

    return intersectionResult;
  }

  public static CompactSketch<Summary> getPublisher2AverageSessionTimeIntersection(
      Long averageSessionTime) {
    Long generateIntersection = System.currentTimeMillis();

    Filter<IntegerSummary> filter =
        new Filter<>(
            integerSummary -> {
              if (integerSummary.getValue() >= averageSessionTime * DAYS) {
                return true;
              }
              return false;
            });

    CompactSketch<IntegerSummary> sketch =
        filter.filter(audienceAttributeSketchPublisher2.union.getResult());

    org.apache.datasketches.tuple.Intersection intersection =
        new org.apache.datasketches.tuple.Intersection(audienceAttributeSketchAdvertiser.setOps);
    CompactSketch<Summary> intersectionResult =
        intersection.intersect(audienceAttributeSketchAdvertiser.union.getResult(), sketch);
    Long endIntersection = System.currentTimeMillis();

    System.out.println(
        "\nTime Taken: \n Average Intersection Query Skech:"
            + (endIntersection - generateIntersection)
            + "ms");

    return intersectionResult;
  }
}
