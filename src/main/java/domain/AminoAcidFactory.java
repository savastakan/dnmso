package domain;

public class AminoAcidFactory {
    public AminoAcid createAminoAcid(String singleLetterCode){
        AminoAcid aminoAcid = new AminoAcid();
        if (singleLetterCode == null){
            aminoAcid.setSingleLetterCode("X");
            aminoAcid.setThreeLetterCode("UNK");
            aminoAcid.setAminoAcidName("Unknown");
            aminoAcid.setMonoIsotopicMass(10000);
            aminoAcid.setAverageMass(10000);
            return aminoAcid;
        }

        switch (singleLetterCode){
            case "A" :
                aminoAcid.setSingleLetterCode("A");
                aminoAcid.setThreeLetterCode("Ala");
                aminoAcid.setAminoAcidName("Alanine");
                aminoAcid.setMonoIsotopicMass(71.03711);
                aminoAcid.setAverageMass(71.0788);
                break;
            case "R" :
                aminoAcid.setSingleLetterCode("R");
                aminoAcid.setThreeLetterCode("Arg");
                aminoAcid.setAminoAcidName("Arginine");
                aminoAcid.setMonoIsotopicMass(156.10111);
                aminoAcid.setAverageMass(156.1875);
                break;
            case "N" :
                aminoAcid.setSingleLetterCode("N");
                aminoAcid.setThreeLetterCode("Asn");
                aminoAcid.setAminoAcidName("Asparagine");
                aminoAcid.setMonoIsotopicMass(114.04293);
                aminoAcid.setAverageMass(114.1038);
                break;
            case "D":
                aminoAcid.setSingleLetterCode("D");
                aminoAcid.setThreeLetterCode("Asp");
                aminoAcid.setAminoAcidName("AsparticAcid");
                aminoAcid.setMonoIsotopicMass(115.02694);
                aminoAcid.setAverageMass(115.02694);
                break;
            case "C":
                aminoAcid.setSingleLetterCode("C");
                aminoAcid.setThreeLetterCode("Cys");
                aminoAcid.setAminoAcidName("Cysteine");
                aminoAcid.setMonoIsotopicMass(103.00919);
                aminoAcid.setAverageMass(103.1388);
                break;
            case "E":
                aminoAcid.setSingleLetterCode("E");
                aminoAcid.setThreeLetterCode("Glu");
                aminoAcid.setAminoAcidName("GlutamicAcid");
                aminoAcid.setMonoIsotopicMass(129.04259);
                aminoAcid.setAverageMass(129.1155);
                break;
            case "Q":
                aminoAcid.setSingleLetterCode("Q");
                aminoAcid.setThreeLetterCode("Gln");
                aminoAcid.setAminoAcidName("Glutamine");
                aminoAcid.setMonoIsotopicMass(128.05858);
                aminoAcid.setAverageMass(128.1307);
                break;
            case "G":
                aminoAcid.setSingleLetterCode("G");
                aminoAcid.setThreeLetterCode("Gly");
                aminoAcid.setAminoAcidName("Glycine");
                aminoAcid.setMonoIsotopicMass(57.02146);
                aminoAcid.setAverageMass(57.0519);
                break;
            case "H":
                aminoAcid.setSingleLetterCode("H");
                aminoAcid.setThreeLetterCode("His");
                aminoAcid.setAminoAcidName("Histidine");
                aminoAcid.setMonoIsotopicMass(137.05891);
                aminoAcid.setAverageMass(137.1411);
                break;
            case "I":
                aminoAcid.setSingleLetterCode("I");
                aminoAcid.setThreeLetterCode("Ile");
                aminoAcid.setAminoAcidName("Isoleucine");
                aminoAcid.setMonoIsotopicMass(113.08406);
                aminoAcid.setAverageMass(113.1594);
                break;
            case "L":
                aminoAcid.setSingleLetterCode("L");
                aminoAcid.setThreeLetterCode("Leu");
                aminoAcid.setAminoAcidName("Leucine");
                aminoAcid.setMonoIsotopicMass(113.08406);
                aminoAcid.setAverageMass(113.1594);
                break;
            case "K":
                aminoAcid.setSingleLetterCode("K");
                aminoAcid.setThreeLetterCode("Lys");
                aminoAcid.setAminoAcidName("Lysine");
                aminoAcid.setMonoIsotopicMass(128.09496);
                aminoAcid.setAverageMass(128.1741);
                break;
            case "M":
                aminoAcid.setSingleLetterCode("M");
                aminoAcid.setThreeLetterCode("Met");
                aminoAcid.setAminoAcidName("Methionine");
                aminoAcid.setMonoIsotopicMass(131.04049);
                aminoAcid.setAverageMass(131.1926);
                break;
            case "F":
                aminoAcid.setSingleLetterCode("F");
                aminoAcid.setThreeLetterCode("Phe");
                aminoAcid.setAminoAcidName("Phenylalanine");
                aminoAcid.setMonoIsotopicMass(147.06841);
                aminoAcid.setAverageMass(147.1766);
                break;
            case "P":
                aminoAcid.setSingleLetterCode("P");
                aminoAcid.setThreeLetterCode("Pro");
                aminoAcid.setAminoAcidName("Proline");
                aminoAcid.setMonoIsotopicMass(97.05276);
                aminoAcid.setAverageMass(97.1167);
                break;
            case "S":
                aminoAcid.setSingleLetterCode("S");
                aminoAcid.setThreeLetterCode("Ser");
                aminoAcid.setAminoAcidName("Serine");
                aminoAcid.setMonoIsotopicMass(87.03203);
                aminoAcid.setAverageMass(87.0782);
                break;
            case "T":
                aminoAcid.setSingleLetterCode("T");
                aminoAcid.setThreeLetterCode("Thr");
                aminoAcid.setAminoAcidName("Threonine");
                aminoAcid.setMonoIsotopicMass(101.04768);
                aminoAcid.setAverageMass(101.1051);
                break;
            case "W":
                aminoAcid.setSingleLetterCode("W");
                aminoAcid.setThreeLetterCode("Trp");
                aminoAcid.setAminoAcidName("Tryptophan");
                aminoAcid.setMonoIsotopicMass(186.07931);
                aminoAcid.setAverageMass(186.2132);
                break;
            case "Y":
                aminoAcid.setSingleLetterCode("Y");
                aminoAcid.setThreeLetterCode("Tyr");
                aminoAcid.setAminoAcidName("Tyrosine");
                aminoAcid.setMonoIsotopicMass(163.06333);
                aminoAcid.setAverageMass(163.1760);
                break;
            case "V":
                aminoAcid.setSingleLetterCode("V");
                aminoAcid.setThreeLetterCode("Val");
                aminoAcid.setAminoAcidName("Valine");
                aminoAcid.setMonoIsotopicMass(99.06841);
                aminoAcid.setAverageMass(99.1326);
                break;
            default:
                aminoAcid.setSingleLetterCode(singleLetterCode);
                aminoAcid.setThreeLetterCode("UNK");
                aminoAcid.setAminoAcidName("Unknown");
                aminoAcid.setMonoIsotopicMass(10000);
                aminoAcid.setAverageMass(10000);
        }
        return aminoAcid;
    }
}
