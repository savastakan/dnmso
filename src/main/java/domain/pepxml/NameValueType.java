//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.07 at 10:34:54 AM EEST 
//


package domain.pepxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nameValueType complex type. <p>The following schema fragment specifies the expected content contained within this class. <pre> &lt;complexType name="nameValueType"> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;/restriction> &lt;/complexContent> &lt;/complexType> </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nameValueType")
public class NameValueType {

    /**
	 * @uml.property  name="name"
	 */
    @XmlAttribute(required = true)
    protected String name;
    /**
	 * @uml.property  name="value"
	 */
    @XmlAttribute(required = true)
    protected String value;
    /**
	 * @uml.property  name="type"
	 */
    @XmlAttribute
    protected String type;

    /**
	 * Gets the value of the name property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="name"
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the value of the name property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="name"
	 */
    public void setName(String value) {
        this.name = value;
    }

    /**
	 * Gets the value of the value property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="value"
	 */
    public String getValue() {
        return value;
    }

    /**
	 * Sets the value of the value property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="value"
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * Gets the value of the type property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="type"
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets the value of the type property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="type"
	 */
    public void setType(String value) {
        this.type = value;
    }

}