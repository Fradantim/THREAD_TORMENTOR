package com.ThreadTormentor.test;

import java.util.HashMap;
import java.util.Map;

public class StringInterpolationTest {
	public static void main (String args[]) {
		Map<String,String> vars = new HashMap<String, String>();
		vars.put("$var1$", "huehuehue");
		
		String str="jajaja$var1$";
		System.out.println("str: "+str);
		for(String var: vars.keySet()) {
			if(str.contains(var)) {
					str=str.replace(var, "%s");
					System.out.println("remplazo: " + str);
					str= String.format(str, vars.get(var));
					System.out.println("formateado: " + str);
			}
			
		}
	}
}
