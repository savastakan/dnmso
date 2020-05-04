from jpype import (
    JClass, JArray, getDefaultJVMPath, java, shutdownJVM, startJVM)

startJVM(
        getDefaultJVMPath(),
        '-ea',
        f'-Djava.class.path=dnmso.jar',
        convertStrings=False
    )

DnmsoFactory: JClass = JClass('domain.DnmsoFactory')
LutefiskXPService: JClass = JClass('service.LutefiskXPService')
DNMSO: JClass = JClass('domain.DNMSO')

dnmsoFactory: DnmsoFactory = DnmsoFactory()
targetDNMSO: DNMSO = dnmsoFactory.createDnmso()

lutefiskXPService: LutefiskXPService = LutefiskXPService()

lutefiskXPArgs: JArray(java.lang.String) = ["read", "-p", "Qtof_ELVISLIVESK.lut", "-n", "2"]

targetDNMSO: DNMSO = lutefiskXPService.run(targetDNMSO, lutefiskXPArgs)

print(targetDNMSO.getPredictions().get(0).getPrediction().size())

