package org.hisp.dhis.gis.util;

import java.io.*;

public class TestCoppyFile {

	private String input = ".." + File.separator + "dhis-web-gis" + File.separator + "map"
			+ File.separator + "hcmc.svg";

	private String output = "C:" + File.separator + "hcmc.svg";

	public void test() {
		FileCopy copy = new FileCopy();
		copy.copy(input, output);
		

	}

	public static void main(String[] args) {
		new TestCoppyFile().test();
	}

}
