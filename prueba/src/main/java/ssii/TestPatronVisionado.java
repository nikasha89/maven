package ssii;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;

public class TestPatronVisionado {

	private static final double MIN_SUPPORT = 1;
	private static final int NUM_PARTITIONS = 3;
	private static final String NAME = "JavaSQLMLlib2";
	private static final String URL = "jdbc:mysql://localhost:3306/videoclub";
	private static final String TABLE = "rates";
	@SuppressWarnings("unused")
	private static final double MIN_CONFIDENCE = 0.3;
	private static final String TABLE_USERS = null;
	private static final int MAX_ITER = 2;
	private static final long SEED = (long) 0.5;
	private static final int K = 1;
	public TestPatronVisionado() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		FPGrowth fp = new FPGrowth().setMinSupport(MIN_SUPPORT).setNumPartitions(NUM_PARTITIONS);

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
		
		Dataset<Row> ds = sql.read().jdbc(URL, TABLE, properties);
		Dataset<Row> completa = ds.select(ds.col("idUser"),ds.col("idFilm"));
		
		
		JavaRDD<Iterable<String>> aux = completa
				.javaRDD()
				.mapToPair(
						x -> new Tuple2<String, String>("" + x.getInt(0) ,""
								+x.getInt(1)))
				.reduceByKey((z,y)-> (String)(z+" "+y))
				.map(x-> Arrays.asList(x._2.split(" ")));
		
		
		FPGrowthModel<String> model = fp.run(aux);
		for (FPGrowth.FreqItemset<String> itemset : model.freqItemsets().toJavaRDD().collect()) {
			System.out.println("[" + itemset.javaItems() + "], " + itemset.freq());
		}
		/*
		AssociationRules arules = new AssociationRules().setMinConfidence(MIN_CONFIDENCE);
		JavaRDD<AssociationRules.Rule<String>> results = arules.run(
		new JavaRDD<>(model.freqItemsets(),model.freqItemsets().org$apache$spark$rdd$RDD$$evidence$1));
		for (AssociationRules.Rule<String> rule : results.collect()) {
		System.out.println(rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " +
		rule.confidence());
		}
		*/
		Dataset<Row> users = sql.read().jdbc(URL, TABLE_USERS, properties);
		List<Integer> works = users.select("occupation").distinct().toJavaRDD().collect().stream().map(x ->
		x.get(0).hashCode()).collect(Collectors.toList());
		List<Integer> zips = users.select("zip").distinct().toJavaRDD().collect().stream().map(x ->
		x.getString(0).substring(0, 2).hashCode()).collect(Collectors.toList());
		List<Integer> ages = users.select("age").distinct().toJavaRDD().collect().stream().map(x ->
		x.getInt(0)).collect(Collectors.toList());
		JavaRDD<Vector> jrdd = new JavaRDD<Row>(users.rdd(),
		users.org$apache$spark$sql$Dataset$$classTag())
		.map(x ->Vectors.dense(new double[]{(double) x.get(0)}));
		// Cluster the data into two classes using KMeans
		KMeansModel clusters = new KMeans().setSeed(SEED).setK(K).
		setMaxIterations(MAX_ITER).run(jrdd.rdd());
		
		ctx.stop();
		ctx.close();

	}

}
