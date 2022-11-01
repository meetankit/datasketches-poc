package audience.merchandising.sketch;

import static org.apache.datasketches.tuple.aninteger.IntegerSummary.Mode.Max;
import static org.apache.datasketches.tuple.aninteger.IntegerSummary.Mode.Sum;

import org.apache.datasketches.theta.UpdateSketch;
import org.apache.datasketches.tuple.Union;
import org.apache.datasketches.tuple.aninteger.IntegerSketch;
import org.apache.datasketches.tuple.aninteger.IntegerSummary;
import org.apache.datasketches.tuple.aninteger.IntegerSummarySetOperations;

public class AudienceAttributeSketch {

  public int count = 0;

  public UpdateSketch[][] updateSketchesSessionDuration =
      new UpdateSketch[DataSketchJava.DAYS][DataSketchJava.MAX_SESION_DURATION];

  private int lgK = 8;
  private int K = 1 << lgK;
  public IntegerSketch[] sessionDurationDailySketch = new IntegerSketch[DataSketchJava.DAYS];
  public Union<IntegerSummary> union;
  public IntegerSummarySetOperations setOps = new IntegerSummarySetOperations(Sum, Max);

  public UpdateSketch[][] updateSketchesLocation =
      new UpdateSketch[DataSketchJava.DAYS][DataSketchJava.locationList.size()];;

  public AudienceAttributeSketch() {
    union = new Union<>(K, setOps);
    for (int i = 0; i < DataSketchJava.DAYS; i++) {
      sessionDurationDailySketch[i] = new IntegerSketch(lgK, Sum);
      for (int j = 0; j < DataSketchJava.MAX_SESION_DURATION; j++) {
        updateSketchesSessionDuration[i][j] = UpdateSketch.builder().build();
      }
    }
    for (int i = 0; i < DataSketchJava.DAYS; i++) {
      for (int j = 0; j < DataSketchJava.locationList.size(); j++) {
        updateSketchesLocation[i][j] = UpdateSketch.builder().build();
      }
    }
  }
}
