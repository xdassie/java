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
  public void testCoreLogic() throws Exception {
	SoccerLeague soccerLeague = SoccerLeague.getInstance();
	// the test file is built into the Docker container for execution of the test by Maven
	soccerLeague.ingest("file:///soccer_data.txt");
	soccerLeague.process();
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
	Map<String,String> expressions = Collections.singletonMap("sum(sum(lhs_points))", "max");
	Dataset<Row> highestScoreAgg = ranking.agg(expressions);
	highestScoreAgg.show();
	Long highestScore = highestScoreAgg.first().getLong(0);
	Assert.assertEquals((Long)winner.getLong(1),(Long)highestScore );
  }

@Test
	public void testMultipleInputs() throws Exception{
        // invoke the class via the main method with multiple inputs
		String  files [] = {"file:///soccer_data.txt","file:///soccer_data.expanded.txt"};
		SoccerLeague.main(files);
	}
@Test
	public void testOutputFormat() throws Exception{
		throw new Exception("this isn't done yet");
	}
}

