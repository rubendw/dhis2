package org.hisp.dhis.reporting.dataset.utils;

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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class CollectionMapDataSource
    implements JRDataSource
{
    private SortedMap<String, Collection<String>> tabularReportElements = null;

    private SortedMap<String, Collection<String>> copyOfTabularReportElements = null;

    private Collection<String> availableRows = null;

    private Set<String> keySets = null;

    private int totalItems = 0;

    private int index = -1;

    public CollectionMapDataSource( SortedMap<String, Collection<String>> tabularReportElements )
    {
        this.tabularReportElements = tabularReportElements;
        this.copyOfTabularReportElements = tabularReportElements;

        keySets = copyOfTabularReportElements.keySet();

        Iterator<String> iterator = keySets.iterator();

        while ( iterator.hasNext() )
        {
            Collection<String> values = copyOfTabularReportElements.get( iterator.next() );

            totalItems += values.size();
        }
    }

    public CollectionMapDataSource()
    {
    }

    public boolean next()
        throws JRException
    {
        index++;
        return (index < totalItems);
    }

    public Object getFieldValue( JRField jrField )
        throws JRException
    {
        String key = jrField.getName();

        Object value = null;

        if ( tabularReportElements.containsKey( key ) )
        {
            availableRows = tabularReportElements.get( key );
        }

        Iterator<String> iterator = availableRows.iterator();

        if ( iterator != null )
        {
            if ( iterator.hasNext() )
            {
                value = iterator.next();
                iterator.remove();
                tabularReportElements.put( key, availableRows );
            }
        }

        if ( value == null )
        {
            index = totalItems;
        }

        return value;
    }
}
