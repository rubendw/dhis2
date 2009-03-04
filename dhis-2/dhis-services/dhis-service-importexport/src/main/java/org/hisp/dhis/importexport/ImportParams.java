package org.hisp.dhis.importexport;

import java.util.Date;

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

/**
 * @author Lars Helge Overland
 * @version $Id: ImportParams.java 5506 2008-07-30 09:34:09Z larshelg $
 */
public class ImportParams
{
    private boolean preview;

    private boolean extendedMode;

    private ImportStrategy importStrategy;
    
    private boolean dataValues;
    
    private boolean skipCheckMatching;
    
    private Date lastUpdated;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ImportParams()
    {        
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public boolean isDataValues()
    {
        return dataValues;
    }

    public void setDataValues( boolean dataValues )
    {
        this.dataValues = dataValues;
    }

    public boolean isExtendedMode()
    {
        return extendedMode;
    }

    public void setExtendedMode( boolean extendedMode )
    {
        this.extendedMode = extendedMode;
    }

    public ImportStrategy getImportStrategy()
    {
        return importStrategy;
    }

    public void setImportStrategy( ImportStrategy importStrategy )
    {
        this.importStrategy = importStrategy;
    }

    public boolean isPreview()
    {
        return preview;
    }

    public void setPreview( boolean preview )
    {
        this.preview = preview;
    }

    public boolean isSkipCheckMatching()
    {
        return skipCheckMatching;
    }

    public void setSkipCheckMatching( boolean checkMatching )
    {
        this.skipCheckMatching = checkMatching;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }
}
