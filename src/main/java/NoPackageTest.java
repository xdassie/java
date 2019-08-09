import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import org.testng.annotations.Test;
public class NoPackageTest {
@Test
  public void testThis() throws Exception {
	SoccerLeague soccerLeague = new SoccerLeague();
	throw new Exception("this hasn't been done yet");
  }
}
