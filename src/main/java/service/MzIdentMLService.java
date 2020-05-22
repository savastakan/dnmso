package service;

import domain.DNMSO;
import domain.ObjectFactory;

import java.io.File;

public class MzIdentMLService extends AbstractService {
    @Override
    public String getServiceName() {
        return null;
    }

    private DNMSO read() {
        ObjectFactory objectFactory = new ObjectFactory();
        DNMSO dnmso = objectFactory.createDNMSO("2.0", null);
        return null;
    }

    private DNMSO write() {
        DNMSO dnmso = getDNMSO();
        return null;
    }

    @Override
    public DNMSO run(DNMSO dnmso, String[] args) {
        processSettings(dnmso, args);
        if (getProperties().get(ServiceTag.COMMAND.toString()).equals("write")) return write();
        if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
        return null;
    }




    @Override
    public boolean isValid(File file) {
        return false;
    }
}
