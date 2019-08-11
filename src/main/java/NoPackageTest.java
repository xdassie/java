import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.SparkSession;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.apache.spark.sql.functions.*;
import java.util.Map;
import java.util. Collections;

public class NoPackageTest {
@Test
  public void testThis() throws Exception {
	SoccerLeague soccerLeague = SoccerLeague.getInstance();
	// the test file is built into the Docker container for execution of the test by Maven
	soccerLeague.init("file:///soccer_data.txt");
	System.out.println(soccerLeague.toString());

	SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SoccerLeague")
                .getOrCreate();
	Dataset<Row> ranking = soccerLeague.getResult();
	ranking.show();
	Dataset<Row>  workTable = soccerLeague.getWorkTable();
	// check the rules against the calculated dataset
	// read the file to count the lines and  check all are acccounted for
	Dataset<Row>  rawData = spark.read()
                    .option("header", "false")
                    .option("delimiter",",")
                    .csv("file:///soccer_data.txt");

	Assert.assertEquals(rawData.count(),workTable.count());
	// check that the team with the highest number of wins has the highest rank
	Row winner = ranking.first();
	//Dataset<Row> highestScoreAgg = ranking.rollUp("sum(sum(lhs_points))").max("sum(sum(lhs_points))");
	Map<String,String> expressions = Collections.singletonMap("sum(sum(lhs_points))", "max");
	Dataset<Row> highestScoreAgg = ranking.agg(expressions);
	highestScoreAgg.show();
	Long highestScore = highestScoreAgg.first().getLong(0);
	Assert.assertEquals((Long)winner.getLong(1),(Long)highestScore ); 
	throw new Exception("don't forget to make it read various files from the filesystem or stdin");
  }
}
