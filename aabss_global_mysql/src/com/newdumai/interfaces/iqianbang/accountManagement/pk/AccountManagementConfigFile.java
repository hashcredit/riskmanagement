package com.newdumai.interfaces.iqianbang.accountManagement.pk;

public class AccountManagementConfigFile {
	public static String getFilePath(){
		String FILE_NAME = "gmd_private_key.pem";
		return AccountManagementConfigFile.class.getResource(FILE_NAME).getFile();
	}
}
