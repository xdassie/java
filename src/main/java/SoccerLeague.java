import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;


public class SoccerLeague
{
	public void init(){
		SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SoccerLeague")
                .getOrCreate();

                Dataset<Row> df = spark.read()
                    .option("header", "true")
                    .option("delimiter","\t")
                    .csv("file://soccer_data.txt");

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
 
