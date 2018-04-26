package com.ThreadTormentor.test;

import org.junit.Test;

import com.ThreadTormentor.Main.Main;

public class TestMain {
	
	@Test
	public void testMain () {
		Main.main(new String[] {"inputParams.properties"});
	}
}
