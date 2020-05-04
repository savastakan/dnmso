//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.07 at 10:34:54 AM EEST 
//


package domain.pepxml;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * <p>Java class for anonymous complex type. <p>The following schema fragment specifies the expected content contained within this class. <pre> &lt;complexType> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="author" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="elution" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /> &lt;attribute name="labeled_residues" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="area_flag" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /> &lt;attribute name="static_quant" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;/restriction> &lt;/complexContent> &lt;/complexType> </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "asapratio_summary")
public class AsapratioSummary {

    /**
	 * @uml.property  name="version"
	 */
    @XmlAttribute(required = true)
    protected String version;
    /**
	 * @uml.property  name="author"
	 */
    @XmlAttribute(required = true)
    protected String author;
    /**
	 * @uml.property  name="elution"
	 */
    @XmlAttribute(required = true)
    protected BigInteger elution;
    /**
	 * @uml.property  name="labeledResidues"
	 */
    @XmlAttribute(name = "labeled_residues", required = true)
    protected String labeledResidues;
    /**
	 * @uml.property  name="areaFlag"
	 */
    @XmlAttribute(name = "area_flag", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger areaFlag;
    /**
	 * @uml.property  name="staticQuant"
	 */
    @XmlAttribute(name = "static_quant", required = true)
    protected String staticQuant;

    /**
	 * Gets the value of the version property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="version"
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * Sets the value of the version property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="version"
	 */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
	 * Gets the value of the author property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="author"
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * Sets the value of the author property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="author"
	 */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
	 * Gets the value of the elution property.
	 * @return  possible object is {@link BigInteger  }  
	 * @uml.property  name="elution"
	 */
    public BigInteger getElution() {
        return elution;
    }

    /**
	 * Sets the value of the elution property.
	 * @param value  allowed object is {@link BigInteger  }  
	 * @uml.property  name="elution"
	 */
    public void setElution(BigInteger value) {
        this.elution = value;
    }

    /**
	 * Gets the value of the labeledResidues property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="labeledResidues"
	 */
    public String getLabeledResidues() {
        return labeledResidues;
    }

    /**
	 * Sets the value of the labeledResidues property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="labeledResidues"
	 */
    public void setLabeledResidues(String value) {
        this.labeledResidues = value;
    }

    /**
	 * Gets the value of the areaFlag property.
	 * @return  possible object is {@link BigInteger  }  
	 * @uml.property  name="areaFlag"
	 */
    public BigInteger getAreaFlag() {
        return areaFlag;
    }

    /**
	 * Sets the value of the areaFlag property.
	 * @param value  allowed object is {@link BigInteger  }  
	 * @uml.property  name="areaFlag"
	 */
    public void setAreaFlag(BigInteger value) {
        this.areaFlag = value;
    }

    /**
	 * Gets the value of the staticQuant property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="staticQuant"
	 */
    public String getStaticQuant() {
        return staticQuant;
    }

    /**
	 * Sets the value of the staticQuant property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="staticQuant"
	 */
    public void setStaticQuant(String value) {
        this.staticQuant = value;
    }

}