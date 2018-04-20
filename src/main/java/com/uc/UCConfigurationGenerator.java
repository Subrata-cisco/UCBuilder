package com.uc;

import java.io.File;

import com.uc.core.service.IBuilderService;
import com.uc.core.service.UCBuildService;
/**
 * Main client class to initiate the updating the required configuration files.
 * @author Subrata Saha (ssaha2)
 *
 */
public class UCConfigurationGenerator {
	
	public static void main(String[] args) {
		
		String filePath = null;//"C:\\Users\\ssaha2\\Desktop\\buildtool\\ucbuild.txt";
		IBuilderService service = null;
		
		if(args != null && args.length > 0) {
			filePath = args[0];
		}
		
		
		if(filePath != null && new File(filePath).exists()) {
			service = new UCBuildService(filePath);
		}else {
			service = new UCBuildService();
		}
		
		service.startService();
	}
}
