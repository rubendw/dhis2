package org.hisp.dhis.dashboard.ds.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hisp.dhis.dashboard.util.PeriodStartDateComparator;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.opensymphony.xwork.Action;

public class GenerateDataStatusFormAction
    implements Action
{

    /* Dependencies */
    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private DataSetStore dataSetStore;

    public void setDataSetStore( DataSetStore dataSetStore )
    {
        this.dataSetStore = dataSetStore;
    }

    /* Output Parameters */
    private List<DataSet> dataSetList;

    public List<DataSet> getDataSetList()
    {
        return dataSetList;
    }

    private List<Period> monthlyPeriods;

    public List<Period> getMonthlyPeriods()
    {
        return monthlyPeriods;
    }

    private SimpleDateFormat simpleDateFormat;

    public SimpleDateFormat getSimpleDateFormat()
    {
        return simpleDateFormat;
    }

    public String execute()
        throws Exception
    {
        /* DataSet List */
        dataSetList = new ArrayList<DataSet>(getDataSetListFromXML());

        /* Monthly Periods */
        monthlyPeriods = new ArrayList<Period>( periodStore.getPeriodsByPeriodType( new MonthlyPeriodType() ) );
        Collections.sort( monthlyPeriods, new PeriodStartDateComparator() );
        simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        return SUCCESS;
    }
    
    private List<DataSet> getDataSetListFromXML()
    {
        List<DataSet> dsList = new ArrayList<DataSet>();
        
        String path = System.getProperty( "user.home" ) + File.separator + "dhis" + File.separator + "db"
            + File.separator + "dataSetList.xml";
        try
        {
            String newpath = System.getenv( "USER_HOME" );
            if ( newpath != null )
            {
                path = newpath + File.separator + "dhis" + File.separator + "db" + File.separator
                    + "dataSetList.xml";
            }
        }
        catch ( NullPointerException npe )
        {
            // do nothing, but we might be using this somewhere without
            // USER_HOME set, which will throw a NPE
        }

        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse( new File( path ) );
            if ( doc == null )
            {
                // System.out.println( "There is no DECodes related XML file in
                // the user home" );
                return null;
            }

            NodeList listOfDataSets = doc.getElementsByTagName( "dataset" );
            int totalDataSets = listOfDataSets.getLength();

            for ( int s = 0; s < totalDataSets; s++ )
            {
                Element dsElement = (Element) listOfDataSets.item( s );
                NodeList textDSList = dsElement.getChildNodes();
                int dsId = Integer.parseInt( ((Node) textDSList.item( 0 )).getNodeValue().trim() );
                DataSet ds = dataSetStore.getDataSet( dsId );
                dsList.add( ds );
            }// end of for loop with s var
        }// try block end
        catch ( SAXParseException err )
        {
            System.out.println( "** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId() );
            System.out.println( " " + err.getMessage() );
        }
        catch ( SAXException e )
        {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
        }
        return dsList;
    }


}// class end
