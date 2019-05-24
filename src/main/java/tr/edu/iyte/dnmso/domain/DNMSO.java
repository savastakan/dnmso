//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.07.06 at 11:32:37 AM EEST 
//


package tr.edu.iyte.dnmso.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import mslib.peak.Peak;
import mslib.spectrum.MassSpectrum;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Spectra">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}Spectrum" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Predictions" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}Prediction" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="Software" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Publication" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="Settings" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Setting" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                               &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="threshold" type="{http://www.w3.org/2001/XMLSchema}double" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Modifications" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}Modification" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "spectra",
    "predictions",
    "modifications"
})
@XmlRootElement(name = "DNML")
public class DNMSO {

    @XmlElement(name = "Spectra", required = true)
    protected DNMSO.Spectra spectra;
    @XmlElement(name = "Predictions")
    protected List<DNMSO.Predictions> predictions;
    @XmlElement(name = "Modifications")
    protected DNMSO.Modifications modifications;
    @XmlAttribute(name = "version", required = true)
    protected String version;

    /**
     * Gets the value of the spectra property.
     * 
     * @return
     *     possible object is
     *     {@link DNMSO.Spectra }
     *     
     */
    public DNMSO.Spectra getSpectra() {
        return spectra;
    }

    /**
     * Sets the value of the spectra property.
     * 
     * @param value
     *     allowed object is
     *     {@link DNMSO.Spectra }
     *     
     */
    public void setSpectra(DNMSO.Spectra value) {
        this.spectra = value;
    }

    /**
     * Gets the value of the predictions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the predictions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPredictions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DNMSO.Predictions }
     * 
     * 
     */
    public List<DNMSO.Predictions> getPredictions() {
        if (predictions == null) {
            predictions = new ArrayList<DNMSO.Predictions>();
        }
        return this.predictions;
    }

    /**
     * Gets the value of the modifications property.
     * 
     * @return
     *     possible object is
     *     {@link DNMSO.Modifications }
     *     
     */
    public DNMSO.Modifications getModifications() {
        return modifications;
    }

    /**
     * Sets the value of the modifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link DNMSO.Modifications }
     *     
     */
    public void setModifications(DNMSO.Modifications value) {
        this.modifications = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}Modification" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "modification"
    })
    public static class Modifications {

        @XmlElement(name = "Modification")
        protected List<Modification> modification;

        /**
         * Gets the value of the modification property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the modification property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getModification().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Modification }
         * 
         * 
         */
        public List<Modification> getModification() {
            if (modification == null) {
                modification = new ArrayList<Modification>();
            }
            return this.modification;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}Prediction" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="Software" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Publication" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="Settings" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Setting" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                     &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="threshold" type="{http://www.w3.org/2001/XMLSchema}double" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "prediction",
        "software"
    })
    public static class Predictions {

        @XmlElement(name = "Prediction")
        protected List<Prediction> prediction;
        @XmlElement(name = "Software")
        protected DNMSO.Predictions.Software software;
        @XmlAttribute(name = "threshold")
        protected Double threshold;

        /**
         * Gets the value of the prediction property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the prediction property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPrediction().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Prediction }
         * 
         * 
         */
        public List<Prediction> getPrediction() {
            if (prediction == null) {
                prediction = new ArrayList<Prediction>();
            }
            return this.prediction;
        }

        /**
         * Gets the value of the software property.
         * 
         * @return
         *     possible object is
         *     {@link DNMSO.Predictions.Software }
         *     
         */
        public DNMSO.Predictions.Software getSoftware() {
            return software;
        }

        /**
         * Sets the value of the software property.
         * 
         * @param value
         *     allowed object is
         *     {@link DNMSO.Predictions.Software }
         *     
         */
        public void setSoftware(DNMSO.Predictions.Software value) {
            this.software = value;
        }

        /**
         * Gets the value of the threshold property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getThreshold() {
            return threshold;
        }

        /**
         * Sets the value of the threshold property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setThreshold(Double value) {
            this.threshold = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Publication" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="Settings" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Setting" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                           &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "publication",
            "settings"
        })
        public static class Software {

            @XmlElement(name = "Publication")
            protected List<String> publication;
            @XmlElement(name = "Settings")
            protected DNMSO.Predictions.Software.Settings settings;
            @XmlAttribute(name = "version", required = true)
            protected String version;
            @XmlAttribute(name = "name", required = true)
            protected String name;

            /**
             * Gets the value of the publication property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the publication property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPublication().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getPublication() {
                if (publication == null) {
                    publication = new ArrayList<String>();
                }
                return this.publication;
            }

            /**
             * Gets the value of the settings property.
             * 
             * @return
             *     possible object is
             *     {@link DNMSO.Predictions.Software.Settings }
             *     
             */
            public DNMSO.Predictions.Software.Settings getSettings() {
                return settings;
            }

            /**
             * Sets the value of the settings property.
             * 
             * @param value
             *     allowed object is
             *     {@link DNMSO.Predictions.Software.Settings }
             *     
             */
            public void setSettings(DNMSO.Predictions.Software.Settings value) {
                this.settings = value;
            }

            /**
             * Gets the value of the version property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getVersion() {
                return version;
            }

            /**
             * Sets the value of the version property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setVersion(String value) {
                this.version = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Setting" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "setting"
            })
            public static class Settings {

                @XmlElement(name = "Setting")
                protected List<DNMSO.Predictions.Software.Settings.Setting> setting;

                /**
                 * Gets the value of the setting property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the setting property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getSetting().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link DNMSO.Predictions.Software.Settings.Setting }
                 * 
                 * 
                 */
                public List<DNMSO.Predictions.Software.Settings.Setting> getSetting() {
                    if (setting == null) {
                        setting = new ArrayList<DNMSO.Predictions.Software.Settings.Setting>();
                    }
                    return this.setting;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Setting {

                    @XmlAttribute(name = "name")
                    @XmlSchemaType(name = "anySimpleType")
                    protected String name;
                    @XmlAttribute(name = "value")
                    @XmlSchemaType(name = "anySimpleType")
                    protected String value;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                }

            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}Spectrum" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "spectrum"
    })
    public static class Spectra {

        @XmlElement(name = "Spectrum", required = true)
        protected List<Spectrum> spectrum;
        
        protected HashMap<String, MassSpectrum> massSpectra = new HashMap<String, MassSpectrum>();
        
        public int size(){
        	return massSpectra.size();
        	
        }
        public MassSpectrum findSpectrum(DNMSO dnmso,String fileName,String scanId){
    		return dnmso.getSpectra().massSpectra.get(fileName + ":" + scanId);
    	}
        public Collection<MassSpectrum> getSpectrum(){
        	return Collections.unmodifiableCollection(massSpectra.values());
        }
    	
  
        public void addSpectrum(MassSpectrum massSpectrum){
        	Spectrum spectrum = new Spectrum();
        	ArrayList<Peak> spectrumexp = massSpectrum.getSpectrum();
            String spe="";
            for(Peak p: spectrumexp){
                 spe+=p.getX()+","+p.getY()+";";
            }
           spectrum.setCsv(spe);
           spectrum.setFileName(massSpectrum.getFileName());
           spectrum.setPrecursorIntensity(massSpectrum.getPrecursorIntensity());
           spectrum.setScanId(String.valueOf(massSpectrum.getScanNumber()));
           spectrum.setPrecursorMZ(massSpectrum.getPrecursorMass());
           spectrum.setLink(massSpectrum.getLink());

           if (this.spectrum == null){
        	   this.spectrum = new ArrayList<Spectrum>();
           }
           this.spectrum.add(spectrum);
           massSpectra.put(massSpectrum.getFileName()+":"+String.valueOf(massSpectrum.getScanNumber()),massSpectrum);
        }
        
        /**
         * Gets the value of the spectrum property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the spectrum property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSpectrum().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Spectrum }
         * 
         * 
         */
        /*
        public List<Spectrum> getSpectrum() {
            if (spectrum == null) {
                spectrum = new ArrayList<Spectrum>();
            }
            return this.spectrum;
        }
        */
		protected HashMap<String, MassSpectrum> getMassSpectra() {
			return massSpectra;
		}
		protected void setMassSpectra(HashMap<String, MassSpectrum> massSpectra) {
			this.massSpectra = massSpectra;
		}

    }

}
