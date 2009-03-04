package org.hisp.dhis.dashboard.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;

public class GetSortedDataAction
implements Action
{
    double[][] data1;

    double[][] data2;

    String[] series1;

    String[] series2;

    String[] categories1;

    String[] categories2;

    private List<String> headingInfo;    

    public List<String> getHeadingInfo()
    {
        return headingInfo;
    }


    public String execute()
        throws Exception
    {

        headingInfo = new ArrayList<String>();
        
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest req = (HttpServletRequest) ctx.get( ServletActionContext.HTTP_REQUEST );
        String chartDisplayOption = req.getParameter( "chartDisplayOption" );

        HttpSession session = req.getSession();
        Double[][] objData1 = (Double[][]) session.getAttribute( "data1" );
        Double[][] objData2 = (Double[][]) session.getAttribute( "data2" );
                

        String[] series1S = (String[]) session.getAttribute( "series1" );
        String[] series2S = (String[]) session.getAttribute( "series2" );
        String[] categories1S = (String[]) session.getAttribute( "categories1" );
        String[] categories2S = (String[]) session.getAttribute( "categories2" );
                        

        initialzeAllLists(series1S, series2S, categories1S, categories2S);
        
        if(objData1 == null || objData2 == null || series1 == null || series2 == null || categories1 == null || categories2 == null )
                System.out.println("Session Objects are null");
        else
                System.out.println("Session Objects are not null");
        
        data1 = convertDoubleTodouble( objData1 );
        data2 = convertDoubleTodouble( objData2 );
        
        if(chartDisplayOption == null || chartDisplayOption.equalsIgnoreCase("none")) { }
        else if(chartDisplayOption.equalsIgnoreCase("ascend")) { sortByAscending(); }
        else if(chartDisplayOption.equalsIgnoreCase("desend")) { sortByDesscending(); }
        else if(chartDisplayOption.equalsIgnoreCase("alphabet")) { sortByAlphabet(); }          
        
        initializeDataLists();
        
        System.out.println(headingInfo);
        
        return SUCCESS;
    }// execute end

    
    public void initializeDataLists()
    {
        int i;
        headingInfo.add( "<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse; border-style: dotted\" bordercolor=\"#111111\" width=\"100%\"><tr><td class=\"TableHeadingCellStyles\" style=\"border-style: dotted; border-width: 1\">Service Name</td>" );
        
        for(i=0; i < categories1.length; i++)
        {
            headingInfo.add( "<td class=\"TableHeadingCellStyles\" align=\"center\" style=\"border-style: dotted; border-width: 1\">"+categories1[i]+"</td>" );
        }
        headingInfo.add( "</tr>" );
        
        for(i=0; i<data1.length; i++)
        {
            headingInfo.add( "<tr><td class=\"TableHeadingCellStyles\" style=\"border-style: dotted; border-width: 1\">"+series1[i]+"</td>" );
            for(int j=0; j<data1[i].length; j++)
            {
                headingInfo.add( "<td class=\"TableDataCellStyles\" align=\"center\" style=\"border-style: dotted; border-width: 1\">"+data1[i][j]+"</td>" );                                         
            }
            headingInfo.add( "</tr>" );
        }
        
        headingInfo.add( "</table>" );
    }
    
    public void initialzeAllLists(String[]series1S, String[] series2S, String[] categories1S, String[] categories2S)
    {
        int i;
        series1 = new String[series1S.length];
        series2 = new String[series2S.length];
        categories1 = new String[categories1S.length];
        categories2 = new String[categories2S.length];
        
        for(i = 0; i < series1S.length; i++)
        {
                series1[i] = series1S[i];
        }

        for(i = 0; i < series2S.length; i++)
        {
                series2[i] = series2S[i];
        }
        
        for(i = 0; i < categories1S.length; i++)
        {
                categories1[i] = categories1S[i];
        }
        
        for(i = 0; i < categories2S.length; i++)
        {
                categories2[i] = categories2S[i];
        }
        
    }
    
    public double[][] convertDoubleTodouble( Double[][] objData )
    {
        System.out.println("Before Sorting : ");
        double[][] data = new double[series1.length][categories1.length];
        for ( int i = 0; i < objData.length; i++ )
        {
            for ( int j = 0; j < objData[0].length; j++ )
            {
                data[i][j] = objData[i][j].doubleValue();
                System.out.print(categories1[j]+": "+data[i][j]+", ");                
            }
            System.out.println("");
        }

        return data;
    }// convertDoubleTodouble end

    public void sortByAscending()
    {
        for(int i=0; i < categories1.length-1 ; i++)
        {
                for(int j=0; j < categories1.length-1-i; j++)
                {
                        if(data1[0][j] > data1[0][j+1])
                        {
                                for(int k=0; k<series1.length; k++)
                                {
                                        double temp1 = data1[k][j];
                                        data1[k][j] = data1[k][j+1];
                                        data1[k][j+1] = temp1;                                          
                                }
                                
                                String temp2 = categories1[j];
                                categories1[j] = categories1[j+1];
                                categories1[j+1] = temp2;
                        }
                }
        }
        
        for(int i=0; i < categories2.length-1 ; i++)
        {
                for(int j=0; j < categories2.length-1-i; j++)
                {
                        if(data2[0][j] > data2[0][j+1])
                        {
                                for(int k=0; k<series2.length; k++)
                                {
                                        double temp1 = data2[k][j];
                                        data2[k][j] = data2[k][j+1];
                                        data2[k][j+1] = temp1;                                          
                                }
                                
                                String temp2 = categories2[j];
                                categories2[j] = categories2[j+1];
                                categories2[j+1] = temp2;
                        }
                }
        }
        
    }

    public void sortByDesscending()
    {
        for(int i=0; i < categories1.length-1 ; i++)
        {
                for(int j=0; j < categories1.length-1-i; j++)
                {
                        if(data1[0][j] < data1[0][j+1])
                        {
                                for(int k=0; k<series1.length; k++)
                                {
                                        double temp1 = data1[k][j];
                                        data1[k][j] = data1[k][j+1];
                                        data1[k][j+1] = temp1;                                          
                                }
                                
                                String temp2 = categories1[j];
                                categories1[j] = categories1[j+1];
                                categories1[j+1] = temp2;
                        }
                }
        }
        
        for(int i=0; i < categories2.length-1 ; i++)
        {
                for(int j=0; j < categories2.length-1-i; j++)
                {
                        if(data2[0][j] < data2[0][j+1])
                        {
                                for(int k=0; k<series2.length; k++)
                                {
                                        double temp1 = data2[k][j];
                                        data2[k][j] = data2[k][j+1];
                                        data2[k][j+1] = temp1;                                          
                                }
                                
                                String temp2 = categories2[j];
                                categories2[j] = categories2[j+1];
                                categories2[j+1] = temp2;
                        }
                }
        }

    }   
    
    public void sortByAlphabet()
        {
                for(int i=0; i < categories1.length-1 ; i++)
                {
                        for(int j=0; j < categories1.length-1-i; j++)
                        {
                                if(categories1[j].compareToIgnoreCase(categories1[j+1]) > 0)
                                {
                                        for(int k=0; k<series1.length; k++)
                                        {
                                        double temp1 = data1[k][j];
                                        data1[k][j] = data1[k][j+1];
                                        data1[k][j+1] = temp1;                                          
                                        }
                                        
                                        String temp2 = categories1[j];
                                        categories1[j] = categories1[j+1];
                                        categories1[j+1] = temp2;
                                }
                        }
                }
                
                for(int i=0; i < categories2.length-1 ; i++)
                {
                        for(int j=0; j < categories2.length-1-i; j++)
                        {
                                if(categories2[j].compareToIgnoreCase(categories2[j+1]) > 0)
                                {
                                        for(int k=0; k<series2.length; k++)
                                        {
                                        double temp1 = data2[k][j];
                                        data2[k][j] = data2[k][j+1];
                                        data2[k][j+1] = temp1;                                          
                                        }
                                        
                                        String temp2 = categories2[j];
                                        categories2[j] = categories2[j+1];
                                        categories2[j+1] = temp2;
                                }
                        }
                }
        
    }
    
}// class end
