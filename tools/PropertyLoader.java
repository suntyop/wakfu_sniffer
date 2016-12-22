package wakfuSniffer.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader
{

 
   public static Properties load(String filename) throws IOException, FileNotFoundException
   {
      Properties properties = new Properties();

      FileInputStream input = new FileInputStream(filename); 
      try{
    	  properties.load(input);
          return properties;
      	}
      	finally{
      		input.close();
        }

   	}
}
