/*
 * 
 */
package service;

import org.xml.sax.SAXException;
import domain.DNMSO;
import domain.DnmsoFactory;

import javax.xml.bind.*;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
 * The Class DnmlService.
 * 
 * @author ST
 * @version $Revision: 1.0 $
 */
public class DnmlService extends AbstractService {

	/**
	 * Write.
	 *
	 * @param DNMSO the dnmso
	 * @param path the path
	 * @return the DNMSO
	 */
	private DNMSO write(DNMSO DNMSO, String path) {
		Writer fileWriter = null;
		try {
			JAXBContext context = JAXBContext.newInstance(DNMSO.class);
			fileWriter = new FileWriter(path);
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(DNMSO, fileWriter);

		} catch (JAXBException ignored) {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return DNMSO;
	}

	/**
	 * Read.
	 *
	 * @param container the container
	 * @param path the path
	 * @return the DNMSO
	 */
	private DNMSO read(DNMSO container, String path) {
		DNMSO DNMSO = null;
		
		try {
			Reader reader = new FileReader(new File(path));

			JAXBContext context = JAXBContext.newInstance(DNMSO.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
					.createXMLStreamReader(reader);

			JAXBElement<DNMSO> root = unmarshaller.unmarshal(xmlStreamReader,
					DNMSO.class);
			DNMSO = root.getValue();
		} catch (FileNotFoundException | JAXBException | XMLStreamException | FactoryConfigurationError e) {
			e.printStackTrace();
		}
		if(container!= null) {
			DnmsoFactory dnmsoFactory = new DnmsoFactory();
			return dnmsoFactory.mergeDNML(container, DNMSO);
		}
		return DNMSO;
	}

	/* (non-Javadoc)
	 * @see tr.edu.iyte.dnmso.service.Service#run(java.lang.Object, java.lang.String[])
	 */
	public DNMSO run(Object container, String[] args) {
		//System.out.println(args[1]);
		if (args[0].startsWith("write")) return write((DNMSO)container, args[1]);
		if (args[0].startsWith("read")) return read((DNMSO)container,args[1]);
		return null;
	}


	/* 
	 * @see tr.edu.iyte.dnmso.service.Service#isValid(java.io.File)
	 */
	public boolean isValid(File dnml) {
		boolean isValid = true;
		  try {
		     // define the type of schema - we use W3C:
		      String schemaLang = "http://www.w3.org/2001/XMLSchema";
		      // get validation driver:
		      SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
		      // create schema by reading it from an XSD file:

		      Schema schema = factory.newSchema(this.getClass().getResource("/dnml.xsd"));
		      Validator validator = schema.newValidator();
		      // at last perform validation:
		      validator.validate(new StreamSource(dnml));
		    } catch (Exception ex) {
		    	isValid = false;
		    }
		return isValid;
	}


	/* 
	 * @see tr.edu.iyte.dnmso.service.Service#getServiceName()
	 */
	public String getServiceName() {
		return "DNMSO Sevice";
	}
}
