package ssii;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public final class TestSparkSQLMongoDB {
	private static final String NAME = "TestWordCount";
	private static String[] path = { "C:\\Users\\practica\\Desktop\\prueba\\bbdd" };

	public static void main(String[] args) {
		String master = System.getProperty("spark master");
		SparkConf sc = SparkConfigs.create(NAME, master == null ? "local" : "master");
				//.set("spark.mongodb.input.collection", "spark").set("spark.mongodb.output.collection", "spark")
				//.set("spark.mongodb.input.uri", "mongodb://root:root@ds057816.mlab.com:57816/test_spark")
				//.set("spark.mongodb.ouput.uri", "mongodb://root:root@ds057816.mlab.com:57816/test_spark");

		JavaSparkContext ctx = new JavaSparkContext(sc);
		SQLContext sql = SQLContext.getOrCreate(ctx.sc());
		Dataset<Row> dataset = sql.read().option("inferShchema", true).json(path);
		String joinExprs= "username";
		
		Dataset<Row> manufacturas = dataset.select(dataset.col("*")).join(dataset.select(dataset.col("*")), joinExprs);
		manufacturas.show();


		System.out.println(manufacturas);
		ctx.stop();
		ctx.close();
	}
}
