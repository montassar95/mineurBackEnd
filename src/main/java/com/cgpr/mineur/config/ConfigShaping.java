package com.cgpr.mineur.config;

import java.util.Vector;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
 public class ConfigShaping {

	
	
	  public ConfigShaping() {
	     
	  }
	  
	  
	 
	 
	  
 
 
	  private String reverseString(String inputString){
	      String outputString="";
	      for(int i=inputString.length();i>0;i--){
	          outputString=outputString+inputString.substring(i-1,i);
	      }
	      return outputString;
	  }
	  public String format(String inputString) throws ArabicShapingException{

	      if(inputString == null || inputString.equals(""))
	          return "";
	      String outputString = "";
	      String arabicString = "";
	      String notArabicString = "";
	      Vector allStringVect = new Vector();
	      for(int i = 0; i < inputString.length(); i++)
	      {                                                             
	          char car = inputString.charAt(i);
	          if((car >= '\u0600' || car <= '\u06FF' || car == '\n') && (car > '9' || car < '0'))
	          {
	              allStringVect.add(notArabicString);
	              notArabicString = "";
	              arabicString = arabicString + car;
	          } else
	          {
	              allStringVect.add(reverseString(arabicString));
	              notArabicString = notArabicString + car;
	              arabicString = "";
	          }
	      }

	      allStringVect.add(notArabicString);
	      allStringVect.add(reverseString(arabicString));
	      for(int k = allStringVect.size() - 1; k >= 0; k--)
	          outputString = outputString + allStringVect.get(k);

	      outputString = traitPara(outputString);
	      ArabicShaping arabic = new ArabicShaping(124);
	      return arabic.shape(outputString);



	  }

	  public Font getFontForArabic(int size){
	    Font font=null ;

	    try{
		BaseFont bfComic = BaseFont.createFont("font/TIMESBD.TTF",   BaseFont.IDENTITY_H, BaseFont.EMBEDDED   );
		font = new Font(bfComic, size);
	    }
	    catch(Exception exp){
	      exp.printStackTrace();
	    }
	    return font;
	  }
	  
	  //font/VIP-Hakm-Regular-2016.ttf for number
	  
	  
	  public Font getFontForArabicAmiri(int size){
		    Font font=null ;

		    try{
			BaseFont bfComic = BaseFont.createFont("font/times0.ttf",   BaseFont.IDENTITY_H, BaseFont.EMBEDDED   );
			font = new Font(bfComic, size);
		    }
		    catch(Exception exp){
		      exp.printStackTrace();
		    }
		    return font;
		  }
	  
	  public Font getFontForArabicAmiri1(int size){
		    Font font=null ;

		    try{
			BaseFont bfComic = BaseFont.createFont("font/Amiri-BoldItalic.ttf",   BaseFont.IDENTITY_H, BaseFont.EMBEDDED   );
			font = new Font(bfComic, size);
		    }
		    catch(Exception exp){
		      exp.printStackTrace();
		    }
		    return font;
		  }
	  public Font getFontForArabicArr(int size){
		    Font font=null ;

		    try{
			BaseFont bfComic = BaseFont.createFont("font/GE_SS_Unique_Light.otf",   BaseFont.IDENTITY_H, BaseFont.EMBEDDED   );
			font = new Font(bfComic, size);
		    }
		    catch(Exception exp){
		      exp.printStackTrace();
		    }
		    return font;
		  }
	  
	  
	  
	  
	  
	  
	  
	  public String traitPara(String inputString){
	      String outputString="";
	      for(int i=0;i<inputString.length();i++){
	          char car=inputString.charAt(i);
	          if(car=='('){
	              outputString=outputString+')';//inputString.substring(i-1,i);
	          }else if(car==')'){
	              outputString=outputString+'(';
	          }else{
		      outputString=outputString+car;
	          }
	      }
	      return outputString;

	  }
	  public String getIso(String inputString){
	      String outputString="";
	      try{
	          outputString=new String(inputString.getBytes(),"ISO8859_6");
	      }catch(Exception exp){
	          exp.printStackTrace();
	      }
	      return outputString;
	      
	      
	  }
	  
	  
}
