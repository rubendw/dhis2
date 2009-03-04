package org.hisp.dhis.gis;


/*
 * Copyright (c) 2004-2007, University of Oslo All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the HISP project nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public class Legend implements Comparable<Legend>
{
	public static final int AUTO_CREATE_MAX = 1;
	
	public static final int NO_AUTO_CREATE_MAX = 0;
	
    private int id;

    private String name;

    private String color;

    private double min;

    private double max;
    
    private int autoCreateMax;

    public Legend( String color, double min, double max )
    {
        this.color = color;
        this.min = min;
        this.max = max;
    }

    public Legend( String name, String color, double min, double max )
    {
        this.name = name;
        this.color = color;
        this.min = min;
        this.max = max;
    }

    public Legend( int id, String name, String color, double min, double max )
    {
        this.id = id;
        this.name = name;
        this.color = color;
        this.min = min;
        this.max = max;
    }
    
    

 
	public int getAutoCreateMax() {
		return autoCreateMax;
	}

	public void setAutoCreateMax(int autoCreateMax) {
		this.autoCreateMax = autoCreateMax;
	}

	public boolean in( double value )
    {
        if ( value >= min && value <= max )
        {
            return true;
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public double getAverage()
    {
        return (max - min) / 2 ;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public Legend()
    {
        super();
    }

    public String getColor()
    {
        return color;
    }

    public void setColor( String color )
    {
        this.color = color;
    }

    public double getMin()
    {
        return min;
    }

    public void setMin( double min )
    {
        this.min = min;
    }

    public double getMax()
    {
        return max;
    }

    public void setMax( double max )
    {
        this.max = max;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        
        final Legend other = (Legend) obj;
                
        if ( name == null )
        {
            if ( other.name != null )
                return false;
        }
        else if ( !name.equals( other.name ) )
            return false;
        
        return true;
    }


	public int compareTo ( Legend legend ) {
		if(this.getMin() > legend.getMin()){
			return 1;
		}else if(this.getMin() < legend.getMin()){
			return -1;
		}else{
			return 0;
		}
	
	}
}
