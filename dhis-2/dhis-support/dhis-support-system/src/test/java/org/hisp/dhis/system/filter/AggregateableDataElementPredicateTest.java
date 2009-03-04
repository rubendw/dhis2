package org.hisp.dhis.system.filter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.dataelement.DataElement;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class AggregateableDataElementPredicateTest
    extends DhisConvenienceTest
{
    public void testPredicate()
    {
        DataElement elementA = createDataElement( 'A' );
        DataElement elementB = createDataElement( 'B' );
        DataElement elementC = createDataElement( 'C' );
        DataElement elementD = createDataElement( 'D' );
        DataElement elementE = createDataElement( 'E' );
        DataElement elementF = createDataElement( 'F' );
        
        elementA.setType( DataElement.TYPE_BOOL );
        elementB.setType( DataElement.TYPE_INT );
        elementC.setType( DataElement.TYPE_STRING );
        elementD.setType( DataElement.TYPE_BOOL );
        elementE.setType( DataElement.TYPE_INT );
        elementF.setType( DataElement.TYPE_STRING );        
        
        Set<DataElement> set = new HashSet<DataElement>();
        
        set.add( elementA );
        set.add( elementB );
        set.add( elementC );
        set.add( elementD );
        set.add( elementE );
        set.add( elementF );
        
        Set<DataElement> reference = new HashSet<DataElement>();
        
        reference.add( elementA );
        reference.add( elementB );
        reference.add( elementD );
        reference.add( elementE );
        
        CollectionUtils.filter( set, new AggregateableDataElementPredicate() );
        
        assertEquals( reference.size(), set.size() );
        assertEquals( reference, set );
    }
}
