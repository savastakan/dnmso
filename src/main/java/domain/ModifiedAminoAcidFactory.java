package domain;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class ModifiedAminoAcidFactory {

    public ModifiedAminoAcid createModifiedAminoAcid(String singleLetterCode,Double diffMono, Double diffAvg){
        AminoAcidFactory aminoAcidFactory = new AminoAcidFactory();
        AminoAcid aminoAcid = aminoAcidFactory.createAminoAcid(singleLetterCode);

        Model model = ModelFactory.createDefaultModel();
        model.read(FileManager.get().open("mod.owl"),"RDF/XML");
        String queryString =""
                + "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#>\n"
                + "PREFIX obo: <http://purl.obolibrary.org/obo/>\n"
                + "PREFIX mod: <http://purl.obolibrary.org/obo/mod#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                +"SELECT DISTINCT  ?subclass ?synonym ?id ?DiffAvg ?DiffMono ?TermSpec ?Origin\n" +
                "WHERE { \n" +
                "?subject owl:annotatedSource ?subclass  .\n" +
                "?subject owl:annotatedTarget \"DiffMono:\" .\n" +
                "?subject rdfs:label ?DiffMono .\n";
        if (diffMono != null){
            queryString += "FILTER ( xsd:double(?DiffMono) > "+(diffMono - 0.1)+" && xsd:double(?DiffMono) < "+(diffMono + 0.1)+")\n";
        }

        queryString += "{\n" +
                "SELECT ?subclass  ?synonym ?id\n" +
                "WHERE {\n" +
                "?subclass oboInOwl:hasRelatedSynonym ?synonym .\n" +
                "?subclass oboInOwl:id ?id .\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "{\n" +
                "SELECT ?subclass  ?TermSpec\n" +
                "WHERE {\n" +
                "?subject owl:annotatedSource ?subclass  .\n" +
                " ?subject owl:annotatedTarget \"TermSpec:\" .\n" +
                "?subject rdfs:label ?TermSpec  .\n" +
                //"FILTER (?TermSpec ='none' )\n" +
                "}\n" +
                "}\n" +
                "{\n" +
                "SELECT ?subclass  ?DiffAvg\n" +
                "WHERE {\n" +
                "?subclass rdfs:subClassOf* obo:MOD_01157 .\n" +
                "?subject owl:annotatedSource ?subclass  .\n" +
                " ?subject owl:annotatedTarget \"DiffAvg:\" .\n" +
                "?subject rdfs:label ?DiffAvg .\n";
        if (diffAvg != null){
            queryString += "FILTER ( xsd:double(?DiffAvg) > "+(diffAvg- 0.1)+" && xsd:double(?DiffAvg) < "+(diffAvg + 0.1)+")\n";
        }
        queryString += "FILTER NOT EXISTS {\n" +
                " ?class rdfs:subClassOf ?subclass  . \n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "{\n" +
                "SELECT ?subclass  ?Origin\n" +
                "WHERE {\n" +
                "?subclass rdfs:subClassOf* obo:MOD_01157 .\n" +
                "?subject owl:annotatedSource ?subclass  .\n" +
                " ?subject owl:annotatedTarget \"Origin:\" .\n" +
                "?subject rdfs:label ?Origin .\n";
                if (!aminoAcid.getAminoAcidName().equals("Unknown")){
                    queryString +=  "FILTER ( ?Origin = \""+aminoAcid.getSingleLetterCode()+"\")\n";
                }
        queryString += "FILTER NOT EXISTS {\n" +
                " ?class rdfs:subClassOf ?subclass  . \n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";
        //System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        ObjectFactory objectFactory = new ObjectFactory();
        ModifiedAminoAcid modifiedAminoAcid = objectFactory.createModifiedAminoAcid();

        if (results.hasNext()) {
            QuerySolution sol = results.nextSolution();
            Modification modification = new Modification();
            modification.setPsiModRef(sol.getLiteral("?id").getString());
            modification.setModificationName(sol.getLiteral("?synonym").getString());
            modification.setMonoModificationMass(sol.getLiteral("?DiffMono").getDouble());
            modification.setAverageModificationMass(sol.getLiteral("?DiffAvg").getDouble());
            String terminal = sol.getLiteral("?TermSpec").getString();
            if (terminal.equals("none")){
                modification.setIsTerminalModification(false);
            }
            modifiedAminoAcid.setModification(modification);
            modifiedAminoAcid.setModAAId( aminoAcid.getSingleLetterCode() + modifiedAminoAcid.getModification().getPsiModRef());
            modifiedAminoAcid.setAminoAcid(aminoAcid);
            return modifiedAminoAcid;
        } else {
            modifiedAminoAcid = objectFactory.createModifiedAminoAcid();
            modifiedAminoAcid.setAminoAcid(aminoAcid);
            Modification modification = new Modification();
            String id = "";
            if (diffMono != null){
                if (diffMono > 0){
                    id = aminoAcid.getSingleLetterCode() +"+" + Math.round(diffMono);
                } else {
                    id = aminoAcid.getSingleLetterCode() + Math.round(diffMono);
                }

            } else if ( diffAvg != null) {
                if (diffAvg > 0){
                    id = aminoAcid.getSingleLetterCode() +"+" + Math.round(diffAvg);
                } else {
                    id = aminoAcid.getSingleLetterCode() + Math.round(diffAvg);
                }
            }
            modification.setModificationName(id);
            modification.setPsiModRef(id);
            if (diffMono != null) modification.setMonoModificationMass(diffMono);
            if (diffAvg != null) modification.setAverageModificationMass(diffAvg);
            modification.setCvParam(objectFactory.createCVParam());
            modifiedAminoAcid.setModification(modification);
            modifiedAminoAcid.setModAAId(aminoAcid.getSingleLetterCode() + id);
            return modifiedAminoAcid;
        }
    }
}
