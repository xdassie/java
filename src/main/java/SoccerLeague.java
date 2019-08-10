import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.util.List;
import java.util.Arrays; 
import org.apache.spark.sql.functions;
import org.apache.spark.sql.Column;
import java.util.regex.Pattern;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.RelationalGroupedDataset;

public class SoccerLeague
{
	public void init(){
		SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SoccerLeague")
                .getOrCreate();

		spark.sparkContext().setLogLevel("ERROR");

                Dataset<Row> df = spark.read()
                    .option("header", "false")
                    .option("delimiter",",")
                    .csv("file:///soccer_data.txt");
		df.show();

		List<String> cols = Arrays.asList(df.columns());
		cols.forEach(System.out::println); 

		String beforePattern="(?:(?!\\d+).)*"; 	//   "\\d+"
		String scorePattern="\\d+";
		final Integer winPoints = 3 ;
		df = df.withColumn("club_lhs",functions.trim(functions.regexp_extract(df.col("_c0") ,beforePattern,0)));
		df = df.withColumn("club_rhs",functions.trim(functions.regexp_extract(df.col("_c1") ,beforePattern,0)));
                df = df.withColumn("score_lhs",functions.regexp_extract(df.col("_c0") ,scorePattern,0).cast("int"));
                df = df.withColumn("score_rhs",functions.regexp_extract(df.col("_c1") ,scorePattern,0).cast("int"));
		// now the points can be computed  before grouping
		df = df.withColumn("lhs_wins",df.col("score_lhs") .gt ( df.col("score_rhs") ).cast("int") );
		df = df.withColumn("draw",df.col("score_lhs") .equalTo ( df.col("score_rhs") ).cast("int") );
		df = df.withColumn("rhs_wins",df.col("score_lhs") .lt ( df.col("score_rhs") ).cast("int") );
		df = df.withColumn("lhs_points", df.col("draw")   .plus( df.col("lhs_wins").multiply(winPoints)   )  );
		df = df.withColumn("rhs_points", df.col("draw")   .plus( df.col("rhs_wins").multiply(winPoints)   )  );

		Dataset<Row> uniqueLhsClub = df.groupBy(df.col("club_lhs")).sum("lhs_points");
		Dataset<Row> uniqueRhsClub = df.groupBy(df.col("club_rhs")).sum("rhs_points");

		uniqueLhsClub.show();
		uniqueRhsClub.show();
		Dataset<Row> ranking = (uniqueLhsClub.unionAll(uniqueRhsClub)).groupBy("club_lhs").sum("sum(lhs_points)");
		ranking.show();
		df.show();

	}
	protected static SoccerLeague mInstance=new SoccerLeague(); 

	public static void main(String[] args)
	{
		System.out.println("hello");
		SoccerLeague.getInstance().init();
	}
	public static SoccerLeague getInstance(){
		return mInstance;
	}
}
 
