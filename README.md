
# DNMSO; a Standard for Representing De Novo Sequencing Results from Tandem-MS Data

## Savaş Takan and Jens Allmer

For the identification and sequencing of proteins, mass spectrometry (MS) has become the tool of choice and as such drives proteomics. MS/MS spectra need to be assigned a peptide sequence for which two strategies exist. Either database search or de novo sequencing can be employed to establish peptide spectrum matches. For database search mzIdentML is the currently proposed standard for data representation. There is no agreed standard for representing de novo sequencing results, but we recently proposed the de novo markup language (DNML). At the moment each de novo sequencing solution uses different data representation, complicating downstream data integration which is crucial since ensemble predictions may be more useful than predictions of a single tool. We here propose De Novo MS Ontology (DNMSO) which can, for example, provide many-to-many mappings between spectra and peptides. Additionally to this and many other improvements over DNML, an all-encompassing application programming interface which supports any file operation necessary for de novo sequencing from spectra input to reading, writing, creating, of the DNMSO format as well as conversion from existing formats. This essentially removes all overhead from the production of de novo sequencing tools and allows developers to completely concentrate on algorithm development.

If you want to learn more about DNMSO ontology, click [here](https://savastakan.github.io/dnmso/).

## Binding dnmso.jar from other Languages

The reference implementation we provide in Java can often be directly used in other programming languages. This binding of libraries can be achieved by first importing dnmso.jar (either download here or build from source) and then calling the methods provided in dnmso.jar. Have a look at the following python code for an example.

### Python Example

```python
# The jpype (https://jpype.readthedocs.io/) python library is used to run the dnmso library in python.
from jpype import (
    JClass, JArray, getDefaultJVMPath, java, shutdownJVM, startJVM)

# Starting JVM. Here, it is necessary to show the location of the dnmso library
startJVM(
        getDefaultJVMPath(),
        '-ea',
        f'-Djava.class.path=dnmso.jar',
        convertStrings=False
    )

# DnmsoFactory, LutefiskXPService and DNMSO classes are created.
DnmsoFactory: JClass = JClass('domain.DnmsoFactory')
LutefiskXPService: JClass = JClass('service.LutefiskXPService')
DNMSO: JClass = JClass('domain.DNMSO')

# dnmsoFActory, targetDNMSO and lutefiskXPService instances are created by using DnmsoFactory, LutefiskXPService
dnmsoFactory: DnmsoFactory = DnmsoFactory()
targetDNMSO: DNMSO = dnmsoFactory.createDnmso()
lutefiskXPService: LutefiskXPService = LutefiskXPService()

# create parameters
lutefiskXPArgs: JArray(java.lang.String) = ["read", "-p", "Qtof_ELVISLIVESK.lut", "-n", "2"]

#read Lutefisk file using lutefiskXPService with parameters
targetDNMSO: DNMSO = lutefiskXPService.run(targetDNMSO, lutefiskXPArgs)

#print result
print(targetDNMSO.getPredictions().get(0).getPrediction().size())

# shutdown JVM.
shutdownJVM()
```

## USAGE

Download the dnmso.jar file or compile the dnmso library.

Start terminal or console.

Validatation: "java -jar dnmso.jar validate file.dnmso"

Merge multiple DNMSO files: "java -jar dnmso.jar merge file1.mzxml file2.mzml file.lut pepnovo.txt output.dnmso"

Convert (for example from Lutefisk output to DNMSO): "java -jar dnmso.jar convert file.lut output.dnmso"


## Information on the previous format which is superseeded by this one

https://github.com/savastakan/DNML
