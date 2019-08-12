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
	private String output1="";
	private String output2="";
        private SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SoccerLeague")
                .getOrCreate();

@Test
  public void testCoreLogic() throws Exception {
	SoccerLeague soccerLeague = SoccerLeague.getInstance();
	// the test file is built into the Docker container for execution of the test by Maven
	soccerLeague.init();
	soccerLeague.ingest("file:///soccer_data.txt");
	soccerLeague.process();
	output1=soccerLeague.getOutputFileName();
	System.out.println(soccerLeague.toString());

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
	soccerLeague.init();
	// check that the team with the highest number of wins has the highest rank
	Row winner = ranking.first();
	Map<String,String> expressions = Collections.singletonMap("sum(sum(lhs_points))", "max");
	Dataset<Row> highestScoreAgg = ranking.agg(expressions);
	highestScoreAgg.show();
	Long highestScore = highestScoreAgg.first().getLong(0);
	Assert.assertEquals((Long)winner.getLong(2),(Long)highestScore );
	rawData=null;
  }

@Test
	public void testMultipleInputs() throws Exception{
        // invoke the class via the main method with multiple inputs
		String  files [] = {"file:///soccer_data.txt","file:///soccer_data.expanded.txt"};
		SoccerLeague.main(files);
		output2=SoccerLeague.getInstance().getOutputFileName();
	}
@Test
	public void testOutputFormat() throws Exception{
		// is there a file at location stored in output1 and output2?
		String fileUrl="file://" +  output1;
		Dataset<Row> outputData = spark.read()
                    .option("header", "false")
                    .option("delimiter",",")
                    .csv(fileUrl);
		fileUrl="file://" +  output2;
                outputData = spark.read()
                    .option("header", "false")
                    .option("delimiter",",")
                    .csv(fileUrl);
		// is the correct data in the most recent file?
		Dataset<Row> mostRecentResult = SoccerLeague.getInstance().getResult();
		Assert.assertEquals(mostRecentResult.count(),outputData.count());

		Assert.assertEquals(mostRecentResult.col("ranking").plus(". ") , outputData.col("_c0") );
		Assert.assertEquals(mostRecentResult.col("sum(sum(lhs_points))").plus(" pts") , outputData.col("_c2") );
		Assert.assertEquals(mostRecentResult.col("lhs_club") , outputData.col("_c1") );
	}
}

