package de.hbz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


/**
 * 
 * @author Alessio Pellerito
 *
 */

public class XMLParser {
	
	private SAXReader reader;
	private File file;

	public XMLParser(File file) {
   
	    this.reader = new SAXReader(); 
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
				CustomXMLWriter writer;
				writer = new CustomXMLWriter(new OutputStreamWriter(new FileOutputStream(this.file),StandardCharsets.UTF_8));
				
				n.setText(replaceText);
			
				writer.write(doc);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}				
	} 	
}
