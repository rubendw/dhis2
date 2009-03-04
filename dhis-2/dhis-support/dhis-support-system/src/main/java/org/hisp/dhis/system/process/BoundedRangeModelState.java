package org.hisp.dhis.system.process;

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

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeListener;

/**
 * {@link InternalProcess.State} class for internal processes which wish to
 * report progress. A {@link DefaultBoundedRangeModel} is used internally, which
 * thus provides the default values. See the respective interfaces and classes
 * for documentation.
 * 
 * @see BoundedRangeModel
 * @see DefaultBoundedRangeModel
 * @see InternalProcess.State
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: BoundedRangeModelState.java 4369 2007-12-22 21:40:08Z torgeilo $
 */
public class BoundedRangeModelState
    extends InternalProcess.State
    implements BoundedRangeModel
{
    private DefaultBoundedRangeModel model = new DefaultBoundedRangeModel();

    // -------------------------------------------------------------------------
    // BoundedRangeModel redirects
    // -------------------------------------------------------------------------

    public final void addChangeListener( ChangeListener x )
    {
        model.addChangeListener( x );
    }

    public final int getExtent()
    {
        return model.getExtent();
    }

    public final int getMaximum()
    {
        return model.getMaximum();
    }

    public final int getMinimum()
    {
        return model.getMinimum();
    }

    public final int getValue()
    {
        return model.getValue();
    }

    public final boolean getValueIsAdjusting()
    {
        return model.getValueIsAdjusting();
    }

    public final void removeChangeListener( ChangeListener x )
    {
        model.removeChangeListener( x );
    }

    public final void setExtent( int newExtent )
    {
        model.setExtent( newExtent );
    }

    public final void setMaximum( int newMaximum )
    {
        model.setMaximum( newMaximum );
    }

    public final void setMinimum( int newMinimum )
    {
        model.setMinimum( newMinimum );
    }

    public final void setRangeProperties( int value, int extent, int min, int max, boolean adjusting )
    {
        model.setRangeProperties( value, extent, min, max, adjusting );
    }

    public final void setValue( int newValue )
    {
        model.setValue( newValue );
    }

    public final void setValueIsAdjusting( boolean b )
    {
        model.setValueIsAdjusting( b );
    }
}
