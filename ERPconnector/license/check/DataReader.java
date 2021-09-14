 package de.di.license.check;
 

 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 
 class DataReader
 {
   private FileInputStream fileIn = null;
 
   private ObjectInputStream objIn = null;
 
   DataReader(File inputFile) throws IOException {
     this.fileIn = new FileInputStream(inputFile);
     this.objIn = new ObjectInputStream(this.fileIn);
   }
 
   Object readObject() throws IOException, ClassNotFoundException {
     return this.objIn.readObject();
   }
 
   void close() throws IOException {
     this.objIn.close();
   }
 }