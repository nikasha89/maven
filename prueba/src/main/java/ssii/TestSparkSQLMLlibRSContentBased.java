package ssii;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import java.util.Properties;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

public class TestSparkSQLMLlibRSContentBased {

	private static final String NAME = "JavaSQLMLlib2";
	private static final String URL = "jdbc:mysql://localhost:3306/videoclub";
	private static final String TABLE = "films";
	
	public static void main(String[] args) throws AnalysisException {
		String master = System.getProperty("spark.master");
		JavaSparkContext ctx = new JavaSparkContext(SparkConfigs.create(NAME, master == null ? "local[*]" : master).set("spark.sql.crossJoin.enabled",
		"true"));
		SQLContext sql = SQLContext.getOrCreate(ctx.sc());
		Properties properties = new Properties();
		properties.setProperty("driver", "com.mysql.jdbc.Driver");
		properties.setProperty("user", "root");
		properties.setProperty("password", "");
		properties.setProperty("allowMultiQueries", "true");
		properties.setProperty("rewriteBatchedStatements", "true");
		Dataset<Row> dataset = sql.read().jdbc(URL, TABLE, properties);
		JavaRDD<RatioCB> aux = dataset.select(dataset.col("idUser").as("user1"),dataset.col("idFilm").as("film1"))
		.join(dataset.select(dataset.col("idUser").as("user2"), dataset.col("idFilm").as("film2"))).where("user1 = user2")
		.where("film1 <> film2").select("film1", "film2").javaRDD().mapToPair(x -> new Tuple2<Row, Integer>(x, 1))
		.reduceByKey((x, y) -> x + y).map(x->new RatioCB(x._1,x._2));
		sql.createDataFrame(aux, RatioCB.class).write().jdbc(URL, TABLE + "CB", properties);
		ctx.stop();
		ctx.close();
		}

}
