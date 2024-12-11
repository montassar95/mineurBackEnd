package com.cgpr.mineur.config;




import java.io.* ;

public class Simplification {
  String st1 = "" ;
  String st2 = "" ;
  String st3 = "" ;
  String st4 = "" ;
  String st5 = "" ;
  public Simplification() {
    try {
      //st1 = new String( ( "" + '\u0627' ).getBytes( "ISO8859_6" ) ) ;
      //st2 = new String( ( "" + '\u0644' ).getBytes( "ISO8859_6" ) ) ;
      st1="\u0627";
      st2="\u0644";
    }
    catch( Exception exp ) {
      exp.printStackTrace() ;
    }
  }

 
  public String simplifySearch( String inputString ) {

	    /*try {
	      inputString = new String( ( inputString ).getBytes( "ISO-8859-6" ) ) ;
	    }
	    catch( UnsupportedEncodingException ex ) {
	    }*/

		//inputString =  chechInput( inputString); 
		  
		inputString = convertSynonime( inputString)  ;
	    inputString = deleteBlanck( inputString ) ;
	    inputString = remplacerElEl( inputString ) ;

	    inputString = convertLee( inputString ) ;
	    inputString = convertElif( inputString ) ;

	    inputString = convertChadda( inputString ) ;
	    inputString = convertHee_Tee( inputString ) ;

	    inputString = convertIbnete( inputString ) ;
	    inputString = convertIbn( inputString ) ;

	    inputString = standarisationChar( inputString ) ;
	    inputString = supressionChar( inputString ) ;
	    inputString = supressionChar_Double( inputString ) ;
	    if( inputString.equals( "" ) ) {
	      inputString = " " ;
	    }
	    /*try {
	      inputString = new String( inputString.getBytes( "ISO8859_1" ) , "ISO8859_6" ) ;
	    }
	    catch( UnsupportedEncodingException ex1 ) {
	    }*/
	    return inputString ;
	  }
  
  public String simplify( String inputString ) {

    /*try {
      inputString = new String( ( inputString ).getBytes( "ISO-8859-6" ) ) ;
    }
    catch( UnsupportedEncodingException ex ) {
    }*/

	inputString =  chechInput( inputString); 
	  
	inputString = convertSynonime( inputString)  ;
    inputString = deleteBlanck( inputString ) ;
    inputString = remplacerElEl( inputString ) ;

    inputString = convertLee( inputString ) ;
    inputString = convertElif( inputString ) ;

    inputString = convertChadda( inputString ) ;
    inputString = convertHee_Tee( inputString ) ;

    inputString = convertIbnete( inputString ) ;
    inputString = convertIbn( inputString ) ;

    inputString = standarisationChar( inputString ) ;
    inputString = supressionChar( inputString ) ;
    inputString = supressionChar_Double( inputString ) ;
    if( inputString.equals( "" ) ) {
      inputString = " " ;
    }
    /*try {
      inputString = new String( inputString.getBytes( "ISO8859_1" ) , "ISO8859_6" ) ;
    }
    catch( UnsupportedEncodingException ex1 ) {
    }*/
    return inputString ;
  }

  ///////////////////////////////////////////////////////////

  
  private boolean isArabic(char character){
      
	  String text = Character.toString(character);
	  String textWithoutSpace = text.trim().replaceAll(" ","");
      for (int i = 0; i < textWithoutSpace.length();) {
          int c = textWithoutSpace.codePointAt(i);
        
          if (c >= 0x0600 && c <=0x06FF || (c >= 0xFE70 && c<=0xFEFF)) 
              i += Character.charCount(c);   
          else                
              return false;

      } 
      return true;
    }
  
  private String chechInput(String inputString){
	  String outPut="";
	  char charString[] = inputString.toCharArray() ;
	    for( int i = 0 ; i < charString.length ; i++ ) {
	      char x = charString[i] ;
	      if (Character.isLetter(x)){
	    	  
	    	  if(isArabic(charString[i])){
	    	  outPut=outPut+charString[i];
	    	  
	    	  }
	      }
	    }
	  
	 return outPut;
  }
  
  private String  convertSynonime(String inputString){
	  if(inputString!=null && !inputString.equals("")){
	  inputString=inputString.replaceAll("\u0649", "\u064a");
	  }
	  return inputString;
  }

  //Supprimer les espaces et \u0640\u0640\u0640\u0640 (etape 0)
  public String deleteBlanck( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      if( x != ' ' && x != '\u0640' ) {
        outPutString = outPutString + charString[i] ;
      }
      else {
        //System.out.println( charString[i] ) ;
      }
    }
    return outPutString ;
  }

  //Supprimer les (\u0622) (etape 3)
  public String convertElif( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      if( x == '\u0622' ) {
        //st1
        outPutString = outPutString + st1 ;
      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //remplacer les (\u0627\u0644\u0622 )(etape 1)
  public String remplacerElEl( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      //if (Integer.toHexString(ascii).equals("627") &&(charString.length - i > 2)) {
      if( x == '\u0627' && ( charString.length - i > 2 ) ) {
        int ascii1 = charString[i + 1] ;
        int ascii2 = charString[i + 2] ;
        char x1 = charString[i + 1] ;
        char x2 = charString[i + 2] ;
        //test1 \u0627\u0644\u0622
        //if (Integer.toHexString(ascii1).equals("e4") &&Integer.toHexString(ascii2).equals("c2")) {
        if( x1 == '\u0644' && x2 == '\u0622' ) {
          //st2
          outPutString = outPutString + st2 ;
          i = i + 2 ;
        }
        //else if( Integer.toHexString( ascii1 ).equals( "e4" ) && Integer.toHexString( ascii2 ).equals( "c3" ) ) {
        else if( x1 == '\u0644' && x2 == '\u0623' ) {
          //st2
          outPutString = outPutString + st2 ;
          i = i + 2 ;
        }
        //else if( Integer.toHexString( ascii1 ).equals( "e4" ) && Integer.toHexString( ascii2 ).equals( "627" ) ) {
        else if( x1 == '\u0644' && x2 == '\u0627' ) {
          //st2
          outPutString = outPutString + st2 ;
          i = i + 2 ;
        }
        else {
          outPutString = outPutString + charString[i] ;
        }

      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les \u0644\u0622 (etape 2)
  public String convertLee( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      //if( Integer.toHexString( ascii ).equals( "e4" ) && ( charString.length - i > 1 ) ) {
      if( x == '\u0644' && ( charString.length - i > 1 ) ) {
        int ascii1 = charString[i + 1] ;
        char x1 = charString[i + 1] ;
        // if( Integer.toHexString( ascii1 ).equals( "c2" ) ) {
        if( x1 == '\u0622' ) {
          //st2
          outPutString = outPutString + st2 ;
          i = i + 1 ;
        }
        //else if( Integer.toHexString( ascii1 ).equals( "c3" ) ) {
        else if( x1 == '\u0623' ) {
          //st2
          outPutString = outPutString + st2 ;
          i = i + 1 ;
        }
        else if( x1 == '\u0627' ) {
          //st2
          outPutString = outPutString + st2 ;
          i = i + 1 ;
        }
        else {
          outPutString = outPutString + charString[i] ;
        }
      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les \u0644\u0622 (etape 4)
  public String convertChadda( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      //if( !Integer.toHexString( ascii ).equals( "f1" ) ) {
      if( x != '\u0651' ) {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les \u0647\u062A (etape5)
  public String convertHee_Tee( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      if( i == charString.length - 1 ) {
        // if( !Integer.toHexString( ascii ).equals( "e7" ) && !Integer.toHexString( ascii ).equals( "c9" ) && !Integer.toHexString( ascii ).equals( "ca" ) ) {
        if( x != '\u0647' && x != '\u0629' && x != '\u062A' ) {

          outPutString = outPutString + charString[i] ;
        }
      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les \u0627\u0628\u0646,\u0628\u0646\u0629 \u0628\u0646 \u0628\u0646\u062A \u0628\u0627\u0644 (etape6)
  public String convertIbnete( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;

      //if( Integer.toHexString( ascii ).equals( "c7" ) && ( charString.length - i > 4 ) ) {
      if( x == '\u0627' && ( charString.length - i > 4 ) ) {
        int ascii1 = charString[i + 1] ;
        int ascii2 = charString[i + 2] ;
        int ascii3 = charString[i + 3] ;
        char x1 = charString[i + 1] ;
        char x2 = charString[i + 2] ;
        char x3 = charString[i + 3] ;

        if( x1 == '\u0628' && x2 == '\u0646' && x3 == '\u0629' ) {
          i = i + 3 ;
        }
        else {
          outPutString = outPutString + charString[i] ;
        }
      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

//Supprimer les \u0627\u0628\u0646, \u0628\u0646 \u0628\u0646\u062A \u0628\u0627\u0644 (etape6)
  public String convertIbn( String inPutString ) {
    boolean exist = false ;
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      exist = false ;
      char x = charString[i] ;
      int ascii = x ;

      //if( Integer.toHexString( ascii ).equals( "c7" ) ) {
      if( x == '\u0627' ) {
        if( charString.length - i > 3 ) {
          int ascii1 = charString[i + 1] ;
          int ascii2 = charString[i + 2] ;
          char x1 = charString[i + 1] ;
          char x2 = charString[i + 2] ;

          //if( Integer.toHexString( ascii1 ).equals( "c8" ) && Integer.toHexString( ascii2 ).equals( "e6" ) ) {
          if( x1=='\u0628' && x2=='\u0646' ) {
            i = i + 2 ;
            exist = true ;
          }
          //else if( Integer.toHexString( ascii1 ).equals( "c8" ) && Integer.toHexString( ascii2 ).equals( "e8" ) ) {
            if( x1=='\u0628' && x2=='\u0648' ) {
            i = i + 2 ;
            exist = true ;
          }
        }
        if( charString.length - i > 2 ) {
          int ascii1 = charString[i + 1] ;
          char x1= charString[i + 1] ;
          //if( Integer.toHexString( ascii1 ).equals( "e4" ) ) {
            if( x1=='\u0644' ) {
            i = i + 1 ;
            exist = true ;
          }
        }
      }
      //else if( Integer.toHexString( ascii ).equals( "c8" ) ) {
        else if( x=='\u0628' ) {
        if( charString.length - i > 3 ) {
          int ascii1 = charString[i + 1] ;
          int ascii2 = charString[i + 2] ;
          char x1= charString[i + 1] ;
          char x2= charString[i + 2] ;
          //if( Integer.toHexString( ascii1 ).equals( "c7" ) && Integer.toHexString( ascii2 ).equals( "e4" ) ) {
            if( x1=='\u0627' && x2=='\u0644' ) {
            i = i + 2 ;
            exist = true ;
          }
          //else if( Integer.toHexString( ascii1 ).equals( "e6" ) && Integer.toHexString( ascii2 ).equals( "ca" ) ) {
            else  if( x1=='\u0646' && x2=='\u062A' ) {
            i = i + 2 ;
            exist = true ;
          }
        }
        if( charString.length - i > 2 ) {
          int ascii1 = charString[i + 1] ;
          char x1= charString[i + 1] ;
          //if( Integer.toHexString( ascii1 ).equals( "e6" ) ) {
            if( x1=='\u0646' ) {
            i = i + 1 ;
            exist = true ;
          }
          //else if( Integer.toHexString( ascii1 ).equals( "e8" ) ) {
          else if( x1=='\u0648' ) {
            i = i + 1 ;
            exist = true ;
          }

        }
      }

      if( exist == false ) {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les () (etape 7)
  public String standarisationChar( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;

      int ascii = x ;
      //if( Integer.toHexString( ascii ).equals( "c6" ) || Integer.toHexString( ascii ).equals( "c4" ) || Integer.toHexString( ascii ).equals( "c2" ) || Integer.toHexString( ascii ).equals( "c3" ) || Integer.toHexString( ascii ).equals( "c1" ) ) {
        if( x=='\u0626' ||  x=='\u0624' ||
                x=='\u0622' ||
                x=='\u0623' ||
                x=='\u0621' ) {

        //st1
        outPutString = outPutString + st1 ;
      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les () (etape 😎
  public String supressionChar( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      //if( !Integer.toHexString( ascii ).equals( "28" ) && !Integer.toHexString( ascii ).equals( "29" ) && !Integer.toHexString( ascii ).equals( "e8" ) && !Integer.toHexString( ascii ).equals( "ea" ) && !Integer.toHexString( ascii ).equals( "c7" ) ) {
        if( x!='(' &&
                 x!=')' &&
                 x!='\u0648' &&
                 x!='\u064A' &&
                 //x!='\u0649' &&
                 x!='\u0627'  ) {

        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

  //Supprimer les () (etape 9)
  public String supressionChar_Double( String inPutString ) {
    String outPutString = "" ;
    char charString[] = inPutString.toCharArray() ;
    for( int i = 0 ; i < charString.length ; i++ ) {
      char x = charString[i] ;
      int ascii = x ;
      if( charString.length - i > 1 ) {
        int ascii1 = charString[i + 1] ;
        char x1= charString[i + 1] ;
        if( x==x1 ) {
        }
        else {
          outPutString = outPutString + charString[i] ;
        }
      }
      else {
        outPutString = outPutString + charString[i] ;
      }
    }
    return outPutString ;
  }

}