package ch.ludin.dbcompare.model;

import java.util.HashMap;
import java.util.Map;

public class DescriptorItem {

	public enum FolderKey {
		dev("doc/tomcat/dev"), 
		Local("doc/tomcat/local"), 
		Test("doc/tomcat/test"), 
		Prod("doc/tomcat/prod"),
		dev2("env/tomcat/dev"),
		Local2("env/tomcat/local"),
		Test2("env/tomcat/test"),
		Prod2("env/tomcat/prod");
	
		private String folderKey;
		
		FolderKey(String folderKey){
			this.folderKey = folderKey;
		}

		public String getFolderKey() {
			return folderKey;
		}
	    //****** Reverse Lookup Implementation************//
		 
	    //Lookup table
	    private static final Map<String, FolderKey> lookup = new HashMap<>();
	  
	    //Populate the lookup table on loading time
	    static
	    {
	        for(FolderKey env : FolderKey.values())
	        {
	            lookup.put(env.getFolderKey(), env);
	        }
	    }
	  
	    //This method can be used for reverse lookup purpose
	    public static FolderKey get(String key) 
	    {
	        return lookup.get(key);
	    }

	}


}
