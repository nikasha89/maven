package ssii;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Utils {
	public static void disableSparkLogging() {
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
	}
}
