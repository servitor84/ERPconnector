/*    */ package de.di.erp.gui;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class ClientCountFile
/*    */ {
/*    */   private int count;
/*    */ 
/*    */   public ClientCountFile()
/*    */   {
/* 15 */     this.count = 0;
/*    */   }
/*    */   public int getClientCount() {
/* 18 */     return this.count;
/*    */   }
/*    */ 
/*    */   void setClientCount(int c) {
/* 22 */     this.count = c;
/*    */   }
/*    */ 
/*    */   void save(File outputFile) throws IOException {
/* 26 */     Properties p = new Properties();
/* 27 */     p.setProperty("count", Integer.toString(this.count));
/* 28 */     p.store(new FileOutputStream(outputFile), "ERPconnector - do not modify");
/*    */   }
/*    */ 
/*    */   public static ClientCountFile readFromFile(File inputFile) throws IOException {
/* 32 */     ClientCountFile countFile = new ClientCountFile();
/*    */ 
/* 34 */     Properties p = new Properties();
/* 35 */     p.load(new FileInputStream(inputFile));
/* 36 */     String count = p.getProperty("count", "0");
/*    */     try
/*    */     {
/* 39 */       countFile.count = Integer.parseInt(count);
/*    */     } catch (NumberFormatException nfe) {
/* 41 */       countFile.count = 0;
/*    */     }
/*    */ 
/* 44 */     return countFile;
/*    */   }
/*    */ }