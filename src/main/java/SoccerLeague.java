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

public class SoccerLeague
{
	private static final Pattern SPACE = Pattern.compile(" ");
	public void init(){
		SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SoccerLeague")
                .getOrCreate();

                Dataset<Row> df = spark.read()
                    .option("header", "false")
                    .option("delimiter",",")
                    .csv("file:///soccer_data.txt");
		df.show();

		List<String> cols = Arrays.asList(df.columns());
		cols.forEach(System.out::println); 

		String beforePattern="(?:(?!\\d+).)*"; 	//   "\\d+"
		String scorePattern="\\d+";
		df = df.withColumn("club_lhs",functions.regexp_extract(df.col("_c0") ,beforePattern,0));
		df = df.withColumn("club_rhs",functions.regexp_extract(df.col("_c1") ,beforePattern,0));
                df = df.withColumn("score_lhs",functions.regexp_extract(df.col("_c0") ,scorePattern,0));
                df = df.withColumn("score_rhs",functions.regexp_extract(df.col("_c1") ,scorePattern,0));
		
               // newDs = df.withColumn("regexp2",functions.regexp_extract(df.col("_c1") ,".*",1));

//		Dataset<String> newDs = df.flatMap(row -> Arrays.asList(row.getString(1).trim().split(" ")).iterator(), Encoders.STRING() );

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
 
