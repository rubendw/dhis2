package org.hisp.dhis.gis;

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

import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Tran Thanh Tri
 * @version $Id: Feature.java 28-01-2008 16:06:00 $
 */
public class DefaultFeatureService
    implements FeatureService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private FeatureStore featureStore;

    public void setFeatureStore( FeatureStore featureStore )
    {
        this.featureStore = featureStore;
    }

    // -------------------------------------------------------------------------
    // FeatureService implementation
    // -------------------------------------------------------------------------

    public void add( Feature feature )
    {
        this.featureStore.add( feature );
    }

    public void delete( Feature feature )
    {
        this.featureStore.delete( feature );
    }

    public Feature get( int featureid )
    {
        return this.featureStore.get( featureid );
    }

    public Feature get( String featureCode )
    {
        return this.featureStore.get( featureCode );
    }

    public Feature get( OrganisationUnit organisationUnit )
    {
        return this.featureStore.get( organisationUnit );
    }

    public Collection<Feature> getAll()
    {
        return this.featureStore.getAll();
    }

    public void update( Feature feature )
    {
        this.featureStore.update( feature );
    }

    public void delete( String featureCode )
    {
        this.featureStore.delete( featureCode );
    }

    public void addMapFile( MapFile arg0 )
    {
        this.featureStore.addMapFile( arg0 );
    }

    public void deleteMapFile( MapFile arg0 )
    {
        this.featureStore.deleteMapFile( arg0 );
    }

    public Collection<MapFile> getAllMapFile()
    {
        return this.featureStore.getAllMapFile();
    }

    public MapFile getMapFile( int arg0 )
    {
        return this.featureStore.getMapFile( arg0 );
    }

    public MapFile getMapFile( OrganisationUnit arg0 )
    {
        return this.featureStore.getMapFile( arg0 );
    }

    public void updateMapFile( MapFile arg0 )
    {
        this.featureStore.updateMapFile( arg0 );

    }
}
