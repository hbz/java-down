package javadownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;


/**
 * 
 * @author Alessio Pellerito
 *
 */

public class XMLParser {
	
	// reading
	private SAXReader reader;
	
	//writing
	private OutputFormat format;

	private File file;
	
	public XMLParser(File file) {
   
	    reader = new SAXReader();
		
		format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8"); 
		
		this.file = file;
	}
	
	public Document readDocument() {
		try {
			return reader.read(this.file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void replaceURLinDcXML(Document doc, Node n, String replaceText) {
		try {
			XMLWriter writer = new XMLWriter(new FileOutputStream(this.file), this.format);
			writer.setEscapeText(false);
			n.setText(replaceText);
			writer.write(doc);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	
}
