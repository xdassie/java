import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import org.testng.annotations.Test;
public class NoPackageTest {
@Test
  public void testThis() throws Exception {
	SoccerLeague soccerLeague = SoccerLeague.getInstance();
	soccerLeague.init();
	System.out.println(soccerLeague.toString());
	throw new Exception("this hasn't been done yet");
  }
}
