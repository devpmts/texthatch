package org.loststone.toodledo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Static classes and singletons are bad. God kills a kitten every time you code
 * one of those. Really. What's the problem in instating an object?
 * 
 * @author lant
 *
 */
public class TextEncoder {

	public String encode(String original) {
		String replace = original.replace("&", "%26").replace(";", "%3B");
		try {
			return URLEncoder.encode(replace, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return replace;
	}
}
