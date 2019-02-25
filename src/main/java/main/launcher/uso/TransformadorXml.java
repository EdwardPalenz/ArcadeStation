package main.launcher.uso;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

public class TransformadorXml {
	String[] nombres= {"SnakeClassic","SnakeEvolution","FuriOut","AjedrezBeta","SpaceInavders","PongGame"};
	BufferedReader in;
    StreamResult out;
    TransformerHandler th;
	int i=0;
    
    public void begin() {
        try {
            in = new BufferedReader(new FileReader("src/main/resources/uso/UsoApps.txt"));
            out = new StreamResult("src/main/resources/uso/UsoApps.xml");
            openXml();
            String str;
            while ((str = in.readLine()) != null) {
                process(str);
                i++;
            }
            in.close();
            closeXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openXml() throws ParserConfigurationException, TransformerConfigurationException, SAXException {

        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        th = tf.newTransformerHandler();

        // pretty XML output
        Transformer serializer = th.getTransformer();
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");

        th.setResult(out);
        th.startDocument();
        th.startElement(null, null, "ReporteUso", null);
    }

    public void process(String s) throws SAXException {
    
    	if(i<6) {
    		th.startElement(null, null,nombres[i],null);
   		 th.characters(s.toCharArray(), 0, s.length());
   	        th.endElement(null, null, nombres[i]);
   	        
    	}
    		 
    	
        
    }

    public void closeXml() throws SAXException {
        th.endElement(null, null, "ReporteUso");
        th.endDocument();
    }
}
