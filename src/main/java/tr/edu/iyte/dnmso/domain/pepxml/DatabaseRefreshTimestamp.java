//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.07 at 10:34:54 AM EEST 
//


package tr.edu.iyte.dnmso.domain.pepxml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type. <p>The following schema fragment specifies the expected content contained within this class. <pre> &lt;complexType> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;attribute name="database" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="min_num_enz_term" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /> &lt;/restriction> &lt;/complexContent> &lt;/complexType> </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "database_refresh_timestamp")
public class DatabaseRefreshTimestamp {

    /**
	 * @uml.property  name="database"
	 */
    @XmlAttribute(required = true)
    protected String database;
    /**
	 * @uml.property  name="minNumEnzTerm"
	 */
    @XmlAttribute(name = "min_num_enz_term")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger minNumEnzTerm;

    /**
	 * Gets the value of the database property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="database"
	 */
    public String getDatabase() {
        return database;
    }

    /**
	 * Sets the value of the database property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="database"
	 */
    public void setDatabase(String value) {
        this.database = value;
    }

    /**
	 * Gets the value of the minNumEnzTerm property.
	 * @return  possible object is {@link BigInteger  }  
	 * @uml.property  name="minNumEnzTerm"
	 */
    public BigInteger getMinNumEnzTerm() {
        return minNumEnzTerm;
    }

    /**
	 * Sets the value of the minNumEnzTerm property.
	 * @param value  allowed object is {@link BigInteger  }  
	 * @uml.property  name="minNumEnzTerm"
	 */
    public void setMinNumEnzTerm(BigInteger value) {
        this.minNumEnzTerm = value;
    }

}
