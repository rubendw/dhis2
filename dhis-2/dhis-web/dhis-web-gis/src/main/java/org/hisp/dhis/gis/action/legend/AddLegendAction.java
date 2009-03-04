package org.hisp.dhis.gis.action.legend;
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
import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.LegendService;

import com.opensymphony.xwork.Action;
/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-04-2008 16:06:00 $
 */
public class AddLegendAction implements Action {
	
	private LegendService legendService;
	
	/*--------------------------------------------------------
	 * Input
	 *--------------------------------------------------------*/
	
	private String nameField;
	
	private String colorField;
	
	private double minField;
	
	private double maxField;	
	
	private boolean autoCreateMax;
	
	
	/*--------------------------------------------------------
	 * Getter & Setter
	 *--------------------------------------------------------*/
	
	
	

	public void setLegendService( LegendService legendService )
	{
		this.legendService = legendService;
	}
	

	public String getNameField()
	{
		return nameField;
	}

	public void setNameField( String nameField )
	{
		this.nameField = nameField;
	}

	public String getColorField()
	{
		return colorField;
	}

	public void setColorField( String colorField )
	{
		this.colorField = colorField;
	}

	public double getMinField()
	{
		return minField;
	}

	public void setMinField( double minField )
	{
		this.minField = minField;
	}

	public double getMaxField()
	{
		return maxField;
	}

	public void setMaxField( double maxField )
	{
		this.maxField = maxField;
	}
	
	public void setAutoCreateMax(boolean autoCreateMax) {
		this.autoCreateMax = autoCreateMax;
	}


	public String execute() throws Exception
	{	
		
		if(nameField==null){
			return INPUT;			
		}else{
			nameField = nameField.trim();
			if(nameField.length()==0){
				return INPUT;
			}
			
		}
		
		if(colorField==null){
			return INPUT;			
		}else{
			colorField = colorField.trim();
			if(colorField.length()==0){
				return INPUT;
			}
			
		}
		
		Legend legend = new Legend();
		legend.setName(nameField);
		legend.setColor(colorField);
		legend.setMax(maxField);
		legend.setMin(minField);
	
		
		if(autoCreateMax){
			legend.setAutoCreateMax(Legend.AUTO_CREATE_MAX);
		}else{
			legend.setAutoCreateMax(Legend.NO_AUTO_CREATE_MAX);
		}
		
		
		legendService.addLegend(legend);
		
		nameField=null;
		colorField=null;
		minField = 0.0;
		maxField = 0.0;
		
		return SUCCESS;
	}


	
	
}
