/*     */ package de.di.erpconnect;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class IniFileReader
/*     */ {
/*  20 */   private BufferedReader fileReader = null;
/*     */ 
/*  27 */   private Map<String, Hashtable<String, String>> sections = null;
/*     */ 
/*  29 */   private Logger logger = null;
/*     */ 
/*  31 */   private File iniFile = null;
/*     */ 
/*     */   public IniFileReader(File iniFile)
/*     */   {
/*  35 */     this(iniFile, null);
/*     */   }
/*     */ 
/*     */   public IniFileReader(File iniFile, Logger logger)
/*     */   {
/*  40 */     this.iniFile = iniFile;
/*     */ 
/*  42 */     if (logger == null)
/*  43 */       Logger.getLogger(super.getClass().getName());
/*     */     else
/*  45 */       this.logger = logger;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getSection(String sectionName)
/*     */   {
/*  55 */     return (Map)this.sections.get(sectionName);
/*     */   }
/*     */ 
/*     */   public String getValue(String sectionName, String key)
/*     */   {
/*  66 */     Hashtable section = (Hashtable)this.sections.get(sectionName);
/*     */ 
/*  68 */     if (section == null)
/*     */     {
/*  70 */       return null;
/*     */     }
/*     */ 
/*  73 */     return (String)section.get(key);
/*     */   }
/*     */ 
/*     */   public void parse()
/*     */     throws IOException
/*     */   {
/*  82 */     String line = null;
/*  83 */     String sectionName = null;
/*  84 */     Hashtable section = null;
/*  85 */     String[] pair = null;
/*     */     try
/*     */     {
/*  89 */       this.fileReader = new BufferedReader(new FileReader(this.iniFile));
/*  90 */       this.sections = new HashMap();
/*  91 */       line = this.fileReader.readLine().trim();
/*  92 */       while (line != null)
/*     */       {
/*  95 */         if (line.length() != 0)
/*     */         {
/* 100 */           if ((line.startsWith("[")) && (line.endsWith("]")))
/*     */           {
/* 102 */             sectionName = line.substring(1, line.length() - 1);
/* 103 */             section = new Hashtable();
/* 104 */             this.sections.put(sectionName, section);
/*     */           }
/* 107 */           else if ((line.indexOf("=") > 0) && (section != null))
/*     */           {
/* 109 */             pair = line.split("=", 2);
/*     */ 
/* 111 */             if (pair.length == 2)
/*     */             {
/* 113 */               section.put(pair[0].trim(), pair[1].trim());
/*     */             }
/*     */           }
/*     */         }
/* 116 */         line = this.fileReader.readLine();
/*     */       }
/*     */ 
/* 120 */       this.fileReader.close();
/* 121 */       this.fileReader = null;
/*     */     }
/*     */     catch (IOException ioex)
/*     */     {
/* 126 */       this.fileReader.close();
/* 127 */       this.fileReader = null;
/*     */ 
/* 129 */       throw ioex;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLogger(Logger logger)
/*     */   {
/* 135 */     this.logger = logger;
/*     */   }
/*     */ }

/* Location:           E:\PARTNER\Poresta\erp-BARCODE\WEB-INF\lib\ERP-Connect\
 * Qualified Name:     de.di.erpconnect.IniFileReader
 * JD-Core Version:    0.5.4
 */