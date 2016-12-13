package ssii;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public final class TestWordCount {
//	private static final String NAME = "JavaWordCount";
	private static final Pattern SPACE = Pattern.compile("Hola Anita Womp");

	public static void main(String[] args) throws Exception {
		SparkConf sConfig = new SparkConf().setAppName("testWordCount").setMaster("local[2]");
		// 1. Definimos un SparkContext.
//		String master = System.getProperty("spark.master");
		/*
		 * JavaSparkContext ctx = new JavaSparkContext(SparkConfigs.create(NAME,
		 * master == null ? "local":master));
		 */
		JavaSparkContext ctx = new JavaSparkContext(sConfig);

		// 2. Resolver nuestro problema.
		JavaRDD<String> lines = ctx.textFile("c:/prueba.txt");

		JavaRDD<String> words = lines.flatMap(x -> ((List<String>) (Arrays.asList(SPACE.split(x)))).iterator());

		JavaPairRDD<String, Integer> ones = words.mapToPair(x -> new Tuple2<String, Integer>(x, 1));

		JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

		System.out.println(counts.collectAsMap());

		// 3. Liberar Recursos

		ctx.stop();
		ctx.close();

	}
}