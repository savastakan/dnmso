//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.07 at 10:34:54 AM EEST 
//


package domain.pepxml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type. <p>The following schema fragment specifies the expected content contained within this class. <pre> &lt;complexType> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;sequence> &lt;element name="search_score_summary" minOccurs="0"> &lt;complexType> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;sequence> &lt;element name="parameter" type="{http://regis-web.systemsbiology.net/pepXML}nameValueType" maxOccurs="unbounded" minOccurs="2"/> &lt;/sequence> &lt;/restriction> &lt;/complexContent> &lt;/complexType> &lt;/element> &lt;/sequence> &lt;attribute name="probability" use="required" type="{http://www.w3.org/2001/XMLSchema}float" /> &lt;attribute name="all_ntt_prob" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;attribute name="analysis" type="{http://www.w3.org/2001/XMLSchema}string" /> &lt;/restriction> &lt;/complexContent> &lt;/complexType> </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "searchScoreSummary"
})
@XmlRootElement(name = "peptideprophet_result")
public class PeptideprophetResult {

    /**
	 * @uml.property  name="searchScoreSummary"
	 * @uml.associationEnd  
	 */
    @XmlElement(name = "search_score_summary")
    protected SearchScoreSummary searchScoreSummary;
    /**
	 * @uml.property  name="probability"
	 */
    @XmlAttribute(required = true)
    protected float probability;
    /**
	 * @uml.property  name="allNttProb"
	 */
    @XmlAttribute(name = "all_ntt_prob")
    protected String allNttProb;
    /**
	 * @uml.property  name="analysis"
	 */
    @XmlAttribute
    protected String analysis;

    /**
	 * Gets the value of the searchScoreSummary property.
	 * @return  possible object is {@link SearchScoreSummary  }
	 * @uml.property  name="searchScoreSummary"
	 */
    public SearchScoreSummary getSearchScoreSummary() {
        return searchScoreSummary;
    }

    /**
	 * Sets the value of the searchScoreSummary property.
	 * @param value  allowed object is {@link SearchScoreSummary  }
	 * @uml.property  name="searchScoreSummary"
	 */
    public void setSearchScoreSummary(SearchScoreSummary value) {
        this.searchScoreSummary = value;
    }

    /**
	 * Gets the value of the probability property.
	 * @uml.property  name="probability"
	 */
    public float getProbability() {
        return probability;
    }

    /**
	 * Sets the value of the probability property.
	 * @uml.property  name="probability"
	 */
    public void setProbability(float value) {
        this.probability = value;
    }

    /**
	 * Gets the value of the allNttProb property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="allNttProb"
	 */
    public String getAllNttProb() {
        return allNttProb;
    }

    /**
	 * Sets the value of the allNttProb property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="allNttProb"
	 */
    public void setAllNttProb(String value) {
        this.allNttProb = value;
    }

    /**
	 * Gets the value of the analysis property.
	 * @return  possible object is {@link String  }  
	 * @uml.property  name="analysis"
	 */
    public String getAnalysis() {
        return analysis;
    }

    /**
	 * Sets the value of the analysis property.
	 * @param value  allowed object is {@link String  }  
	 * @uml.property  name="analysis"
	 */
    public void setAnalysis(String value) {
        this.analysis = value;
    }


    /**
	 * <p>Java class for anonymous complex type. <p>The following schema fragment specifies the expected content contained within this class. <pre> &lt;complexType> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;sequence> &lt;element name="parameter" type="{http://regis-web.systemsbiology.net/pepXML}nameValueType" maxOccurs="unbounded" minOccurs="2"/> &lt;/sequence> &lt;/restriction> &lt;/complexContent> &lt;/complexType> </pre>
	 */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "parameter"
    })
    public static class SearchScoreSummary {

        /**
		 * @uml.property  name="parameter"
		 */
        @XmlElement(required = true)
        protected List<NameValueType> parameter;

        /**
		 * Gets the value of the parameter property. <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the parameter property. <p> For example, to add a new item, do as follows: <pre> getParameter().add(newItem); </pre> <p> Objects of the following type(s) are allowed in the list {@link NameValueType  } 
		 * @uml.property  name="parameter"
		 */
        public List<NameValueType> getParameter() {
            if (parameter == null) {
                parameter = new ArrayList<NameValueType>();
            }
            return this.parameter;
        }

    }

}