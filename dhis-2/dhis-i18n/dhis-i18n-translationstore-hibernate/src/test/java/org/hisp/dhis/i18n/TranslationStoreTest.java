package org.hisp.dhis.i18n;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.transaction.TransactionManager;

import java.util.Locale;
import java.util.Collection;

/**
 * @author Oyvind Brucker
 */
public class TranslationStoreTest extends DhisSpringTest
{
    private TransactionManager transactionManager;

    private TranslationStore translationStore;

    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        transactionManager = (TransactionManager) getBean( TransactionManager.ID );

        translationStore = (TranslationStore) getBean( TranslationStore.ID );
    }

    // -------------------------------------------------------------------------
    // Testdata
    // -------------------------------------------------------------------------

    private int id1 = 0;
    private int id2 = 1;

    String UK = Locale.UK.toString();
    String US = Locale.US.toString();
    String FR = Locale.FRANCE.toString();

    private String className1 = OrganisationUnit.class.getName();
    private String className2 = DataElement.class.getName();


    private Translation translation1a = new Translation( className1, id1, UK, "name", "orgunitss" );
    private Translation translation1b = new Translation( className1, id1, UK, "shortName", "orgs" );
    private Translation translation2a = new Translation( className1, id1, US, "name", "orgunitzz" );
    private Translation translation2b = new Translation( className2, id1, US, "name", "dataelement1" );
    private Translation translation2c = new Translation( className2, id2, US, "name", "dataelement2" );
    private Translation translation3 = new Translation( className1, id1, FR, "name", "orgunit" );

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testAddGetUpdateDelete()
        throws Exception
    {
        // Add
        transactionManager.enter();

        translationStore.addTranslation( translation1a );
        translationStore.addTranslation( translation1b );
        translationStore.addTranslation( translation2a );
        translationStore.addTranslation( translation2b );
        translationStore.addTranslation( translation2c );
        translationStore.addTranslation( translation3 );

        transactionManager.leave();
        transactionManager.enter();

        // Get
        Translation savedTranslation1a = translationStore.getTranslation( className1, id1, Locale.UK, "name" );
        Translation savedTranslation1b = translationStore.getTranslation( className1, id1, Locale.UK, "shortName" );
        Translation savedTranslation2b = translationStore.getTranslation( className2, id1, Locale.US, "name" );
        Translation savedTranslation2c = translationStore.getTranslation( className2, id2, Locale.US, "name" );

        assertEquals( "orgunitss", savedTranslation1a.getValue() );
        assertEquals( "orgs", savedTranslation1b.getValue() );
        assertEquals( "dataelement1", savedTranslation2b.getValue() );
        assertEquals( "dataelement2", savedTranslation2c.getValue() );

        Collection<Translation> col = translationStore.getTranslations( className1, id1, Locale.UK );

        assertEquals( "Unexpected amount of translations received", 2, col.size() );

        transactionManager.leave();
        transactionManager.enter();

        // Update
        translation1a.setValue( "org-unitssz" );
        translation2c.setValue( "dataelement-2" );

        translationStore.updateTranslation( translation1a );
        translationStore.updateTranslation( translation2c );

        Translation savedTranslationAfterUpdate1 =
            translationStore.getTranslation( className1, id1, Locale.UK, "name" );
        Translation savedTranslationAfterUpdate2 =
            translationStore.getTranslation( className2, id2, Locale.US, "name" );

        assertEquals( "org-unitssz", savedTranslationAfterUpdate1.getValue() );
        assertEquals( "dataelement-2", savedTranslationAfterUpdate2.getValue() );

        transactionManager.leave();
        transactionManager.enter();

        // Delete
        translationStore.deleteTranslation( translation3 );

        transactionManager.leave();
        transactionManager.enter();

        Translation deletedTranslation = translationStore.getTranslation(className1, id1, Locale.FRANCE ,"name" );

        assertNull(deletedTranslation);

        translationStore.deleteTranslations(className1, id1);

        transactionManager.leave();
        transactionManager.enter();

        col = translationStore.getAllTranslations();

        transactionManager.leave();

        assertEquals("Unexpected amount of translations received after delete", 2, col.size() );
    }

    public void testGetAvailableLocales()
        throws Exception
    {
        transactionManager.enter();

        translationStore.addTranslation( translation1a );
        translationStore.addTranslation( translation2a );
        translationStore.addTranslation( translation3 );

        transactionManager.leave();
        transactionManager.enter();

        Collection<Locale> locales = translationStore.getAvailableLocales();

        transactionManager.leave();

        assertEquals( "Unexpected size of available locales", 3, locales.size() );
    }
}
