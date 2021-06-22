/**
 * 
 * This program looks for the first <dc:identifier> tag with the attribute key named ContentURL
 * in dc.xml (file name must be dc.xml) from a user specified Path and downloads the Ressource from
 * the given URL to a predefined custom location. This URL will be replaced with the new location  
 * Path.
 * 
 */
package javadownloader;

import static javadownloader.Constants.*;
import static javadownloader.LogManager.logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.String;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * 
 * @author Alessio Pellerito
 *
 */
public class Main {
	
	protected static String input;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		if(args.length == 1) {

			input = args[0];
			
		    File[] recordList = new File(PATH_FROM.toString()).listFiles(File::isDirectory);
		    
		    if(recordList != null) {
		    
		    	ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
		    	
		        for(File f : recordList) {
		        	
		        	if(!doneFileExists(f)) {
		      
						XMLParser parser = new XMLParser(Paths.get(f.toString(), DIR_CONTENT, DC_XML).toFile());
						Document docDcXML = parser.readDocument();
				  
						List<Node> nodesInRecord = docDcXML.selectNodes("/dc:records/dc:record");
						
						File logFile = new File(PATH_TO_LOG.toString(), f.getName().concat(_LOG_XML)); 
	
						for(Node n : nodesInRecord) {
									
							pool.execute(new TaskExecutor(n, parser, docDcXML, logFile));
							
						}							        		
					}
			    }
		        pool.shutdown();
		        while(!pool.isTerminated()) {}
		        logger.info("Program finished!");
		    }
		    
		    if(recordList == null)
		    	logger.warn("Specified Path does not exist!");
		}
		
		if(args.length == 0 || args.length > 1)
			logger.warn("Argument required! Insert Path in this format:\n /rootdirectory/nextdirectory/nextdirectory, i.e. /ingest-storage/buw/oai-dnb ");
	}
	
	/**
	 * 
	 * @param file: Checks if Done file exists
	 * @return true or false
	 */
	
	public static boolean doneFileExists(File file) {
		File [] fi = file.listFiles((fil,name) -> name.equals(DONE_FILE) && fil.isDirectory());
		return fi.length > 0;
	}
	
	

}
