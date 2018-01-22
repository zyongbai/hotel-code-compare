package com.derby.util;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * œµÕ≥≈‰÷√¿‡
 * @author ZHANGYONG415
 *
 */
public class SystemMessage {
	private static final String BUNDLE_NAME = "system";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
