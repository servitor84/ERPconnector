package de.di.erpconnect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class DataWriter {

    private FileOutputStream fileOut = null;
    private ObjectOutputStream objOut = null;

    DataWriter(File outputFile) throws IOException {
        this.fileOut = new FileOutputStream(outputFile);
        this.objOut = new ObjectOutputStream(this.fileOut);
    }

    void writeObject(Object o) throws IOException {
        this.objOut.writeObject(o);
    }

    void close() throws IOException {
        this.objOut.close();
    }
}