package javadownloader;

import java.nio.file.Path;
import java.nio.file.Paths;
import static javadownloader.Main.input;
/**
 * 
 * @author Alessio Pellerito
 *
 */

public class Constants {
	
	/**
	 * Path Constants
	 */
	protected static final Path PATH_FROM 		= 	Paths.get(input).toAbsolutePath();
	protected static final Path PATH_TO			=   Paths.get(input.split("/")[0] + "/" + input.split("/")[1] + "/" + "skript-downloads/content/stream").toAbsolutePath();
	protected static final Path PATH_TO_LOG		=   Paths.get(input.split("/")[0] + "/" + input.split("/")[1] + "/" + "skript-logs").toAbsolutePath();
	protected static final String DONE_FILE 		= 	"done";
	protected static final String _LOG_XML 		= 	"_log.xml";
	protected static final String DC_XML 			= 	"dc.xml";
	protected static final String DIR_CONTENT 	= 	"content";
	
	/**
	 * XML Tag Constants	 
	 */
	protected static final String CONTENT_URL 	=	"ContentURL";
	
	/**
	 * Thread Constants
	 */
	// Maximum number of threads equal to Number of Cores
	protected static final int MAX_THREADS 		= 	Runtime.getRuntime().availableProcessors();

}
