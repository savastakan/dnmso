
# DNMSO; a Standard for Representing De Novo Sequencing Results from Tandem-MS Data

## Savaş Takan and Jens Allmer

For the identification and sequencing of proteins, mass spectrometry (MS) has become the tool of choice and as such drives proteomics. MS/MS spectra need to be assigned a peptide sequence for which two strategies exist. Either database search or de novo sequencing can be employed to establish peptide spectrum matches. For database search mzIdentML is the currently proposed standard for data representation. There is no agreed standard for representing de novo sequencing results, but we recently proposed the de novo markup language (DNML). At the moment each de novo sequencing solution uses different data representation, complicating downstream data integration which is crucial since ensemble predictions may be more useful than predictions of a single tool. We here propose De Novo MS Ontology (DNMSO) which can, for example, provide many-to-many mappings between spectra and peptides. Additionally to this and many other improvements over DNML, an all-encompassing application programming interface which supports any file operation necessary for de novo sequencing from spectra input to reading, writing, creating, of the DNMSO format as well as conversion from existing formats. This essentially removes all overhead from the production of de novo sequencing tools and allows developers to completely concentrate on algorithm development.

## Binding dnmso.jar from other Languages
## USAGE

download and compile dnmso library

start terminal

write command in terminal such as

example command for validatation : "java -jar dnmso.jar validate file.dnmso"

example command for merge : "java -jar dnmso.jar merge file1.mzxml file2.mzml file.lut pepnovo.txt output.dnmso"

example command for convert : "java -jar dnmso.jar convert file.lut output.dnmso"


## More info : http://bioinformatics.iyte.edu.tr/dnml/
