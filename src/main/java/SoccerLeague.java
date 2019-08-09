import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.util.List;
import java.util.Arrays; 

public class SoccerLeague
{
	public void init(){
		SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SoccerLeague")
                .getOrCreate();

                Dataset<Row> df = spark.read()
                    .option("header", "false")
                    .option("delimiter",",")
                    .csv("file:///soccer_data.txt");

		List<String> cols = Arrays.asList(df.columns());
		cols.forEach(System.out::println); 

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
 
