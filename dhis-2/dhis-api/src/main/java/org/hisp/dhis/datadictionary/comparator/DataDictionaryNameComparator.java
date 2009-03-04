package org.hisp.dhis.datadictionary.comparator;

import java.util.Comparator;

import org.hisp.dhis.datadictionary.DataDictionary;

public class DataDictionaryNameComparator
implements Comparator<DataDictionary>
{
    public int compare( DataDictionary dataDictionary0, DataDictionary dataDictionary1 )
    {
        return dataDictionary0.getName().compareTo( dataDictionary1.getName() );
    }
}