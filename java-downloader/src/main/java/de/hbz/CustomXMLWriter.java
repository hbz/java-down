package de.hbz;

import java.io.IOException;

import java.io.Writer;

import org.dom4j.Attribute;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 
 * @author Alessio Pellerito
 *
 */

public class CustomXMLWriter extends XMLWriter {
	
	/** The format used by this writer */
    private OutputFormat format;
    
    /**
     * The initial number of indentations (so you can print a whole document
     * indented, if you like)
     */
    private int indentLevel = 0;
	
	public CustomXMLWriter(Writer writer) {
		this.writer = writer;
        this.format = DEFAULT_FORMAT;
	}
	
	protected void writeNode(Node node) throws IOException {
        int nodeType = node.getNodeType();

        switch (nodeType) {
            case Node.ELEMENT_NODE:
                writeElement((Element) node);

                break;

            case Node.ATTRIBUTE_NODE:
                writeAttribute((Attribute) node);

                break;

            case Node.TEXT_NODE:
                writeNodeText(node);

                // write((Text) node);
                break;

            case Node.CDATA_SECTION_NODE:
                writeCDATA(node.getText());

                break;

            case Node.ENTITY_REFERENCE_NODE:
                writeEntity((Entity) node);

                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                writeProcessingInstruction((ProcessingInstruction) node);

                break;

            case Node.COMMENT_NODE:
                writeComment(node.getText());

                break;

            case Node.DOCUMENT_NODE:
                write((Document) node);

                break;

            case Node.DOCUMENT_TYPE_NODE:
                writeDocType((DocumentType) node);

                break;

            case Node.NAMESPACE_NODE:

                // Will be output with attributes
                // write((Namespace) node);
                break;

            default:
                throw new IOException("Invalid node type: " + node);
        }
    }
	
	
	protected void writeElement(Element element) throws IOException {
        int size = element.nodeCount();
        String qualifiedName = element.getQualifiedName();

        writePrintln();
        indent();

        writer.write("<");
        writer.write(qualifiedName);
        
        // Print out additional namespace declarations
        boolean textOnly = true;

        for (int i = 0; i < size; i++) {
            Node node = element.node(i);

            if (node instanceof Namespace) {
                Namespace additional = (Namespace) node;

                if (isNamespaceDeclaration(additional)) {
                    writeNamespace(additional);
                }
            } else if (node instanceof Element) {
                textOnly = false;
            } else if (node instanceof Comment) {
                textOnly = false;
            }
        }

        writeAttributes(element);

        lastOutputNodeType = Node.ELEMENT_NODE;

        if (size <= 0) {
            writeEmptyElementClose(qualifiedName);
        } else {
            writer.write(">");

            if (textOnly) {
                // we have at least one text node so lets assume
                // that its non-empty
                writeElementContent(element);
            } else {
                // we know it's not null or empty from above
                ++indentLevel;

                writeElementContent(element);

                --indentLevel;

                writePrintln();
                indent();
            }

            writer.write("</");
            writer.write(qualifiedName);
            writer.write(">");
        }

        lastOutputNodeType = Node.ELEMENT_NODE;
    }

	/**
     * Writes the attributes of the given element
     * 
     * @param element
     *            DOCUMENT ME!
     * 
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected void writeAttributes(Element element) throws IOException {
        
        for (int i = 0, size = element.attributeCount(); i < size; i++) {
            Attribute attribute = element.attribute(i);

            String attName = attribute.getName();

            if (attName.startsWith("xmlns:")) {
                String prefix = attName.substring(6);
                String uri = attribute.getValue();
                writeNamespace(prefix, uri);
               
            } else if (attName.equals("xmlns")) {
            	String uri = attribute.getValue();
                writeNamespace(null, uri);
            } else {
                char quote = format.getAttributeQuoteCharacter();
                writer.write(" ");
                writer.write(attribute.getQualifiedName());
                writer.write("=");
                writer.write(quote);
                writeEscapeAttributeEntities(attribute.getValue());
                writer.write(quote);
            }
        }
    }
	
    // Getter and Setter

	protected OutputFormat getFormat() {
		return format;
	}
	
	/**
     * Set the initial indentation level. This can be used to output a document
     * (or, more likely, an element) starting at a given indent level, so it's
     * not always flush against the left margin. Default: 0
     * 
     * @param indentLevel
     *            the number of indents to start with
     */
    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }
    
	protected int getIndentLevel() {
		return indentLevel;
	}

}
