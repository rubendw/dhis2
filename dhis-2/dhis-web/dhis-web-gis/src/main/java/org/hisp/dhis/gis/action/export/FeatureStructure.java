package org.hisp.dhis.gis.action.export;
/*
 * Copyright (c) 2004-2007, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class FeatureStructure implements Comparable<FeatureStructure>{
	
	private String featureCode;
	
	private String color;
	
	private double value;
	
	private int orgunit;
	
	

	public FeatureStructure(String featureCode, String color, double value,
			int orgunit) {
		super();
		this.featureCode = featureCode;
		this.color = color;
		this.value = value;
		this.orgunit = orgunit;
	}

	public FeatureStructure() {
		// TODO Auto-generated constructor stub
	}

	public FeatureStructure(String line) {
		String[] array = line.split(":");
		this.featureCode = array[0];
		array = array[1].split("-");
		this.color = array[0];
		this.value = Double.parseDouble(array[1]);
		this.orgunit = Integer.parseInt(array[2]);
		
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getOrgunit() {
		return orgunit;
	}

	public void setOrgunit(int orgunit) {
		this.orgunit = orgunit;
	}

	public int compareTo(FeatureStructure other) {
		// TODO Auto-generated method stub
		if(this.value > other.value) return 1;
		if(this.value < other.value) return -1;		
		return 0;
	}
	
	

}
