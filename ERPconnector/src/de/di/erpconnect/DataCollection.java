package de.di.erpconnect;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

class DataCollection {

    private ConcurrentHashMap<String, Long> data = new ConcurrentHashMap<String, Long>();
    private long timeout = 15552000000L;

    void clean() {
        long system = System.currentTimeMillis();
        for (String key : this.data.keySet()) {
            Long l = (Long) this.data.get(key);

            if (system - l.longValue() > this.timeout) {
                this.data.remove(key);
            }
        }
    }

    void put(String d, Long t) {
        this.data.put(d, t);
    }

    int getSize() {
        return this.data.size();
    }

    void setTimeout(long t) {
        this.timeout = t;
    }

    void save(File outFile) throws IOException {
        DataWriter writer = new DataWriter(outFile);
        try {
            writer.writeObject(this.data);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

    static DataCollection fromFile(File inputFile) throws IOException, ClassNotFoundException {
        DataReader reader = new DataReader(inputFile);
        DataCollection c = new DataCollection();

        if (inputFile.length() == 0L) {
            return c;
        }
        try {
            if (reader.readObject() instanceof ConcurrentHashMap<?, ?>){
                c.data = ((ConcurrentHashMap<String, Long>) reader.readObject());
            }            
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
        return c;
    }
 }