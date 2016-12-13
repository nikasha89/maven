package ssii;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class TestSQL {

	private static final String NAME = "TestSQL";

	public static void main(String[] args) {
		// 1. Definir un SparkContext
		String master = System.getProperty("spark.master");
		JavaSparkContext ctx = new JavaSparkContext(SparkConfigs.create(NAME,
							master == null ? "local" : master));
		SQLContext sql = SQLContext.getOrCreate(ctx.sc());
		String path = "C:\\Users\\practica\\Desktop\\troleandoAndo.csv";
		// 2. Resolver nuestro problema
		Dataset<Row> dataset = sql.read().option("inferSchema", true).csv(path);
		//dataset.printSchema();
		Dataset<Row> manufacturas = dataset.select(dataset.col("_c1")).distinct();
		
		manufacturas.show();
		// 3. Liberar recursos
		ctx.stop();
		ctx.close();
	}

}
