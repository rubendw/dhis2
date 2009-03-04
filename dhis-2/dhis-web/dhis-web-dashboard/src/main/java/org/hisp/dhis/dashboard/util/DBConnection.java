package org.hisp.dhis.dashboard.util;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DBConnection
{

    Connection con = null;

    String dbConnectionXMLFileName = System.getProperty( "user.home" ) + File.separator + "dhis" + File.separator
        + "db" + File.separator + "DBConnections.xml";

    /*
     * To retrieve the db details from xml file
     */
    public List getDBDeatilsFromXML()
    {
        List<String> li = null;
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse( new File( dbConnectionXMLFileName ) );

            NodeList listOfDBConnections = doc.getElementsByTagName( "db-connection" );

            Node dbConnectionsNode = listOfDBConnections.item( 0 );
            li = new ArrayList<String>();
            if ( dbConnectionsNode.getNodeType() == Node.ELEMENT_NODE )
            {
                Element dbConnectionElement = (Element) dbConnectionsNode;

                NodeList dbUserNameList = dbConnectionElement.getElementsByTagName( "uname" );
                Element dbUserNameElement = (Element) dbUserNameList.item( 0 );
                NodeList textDBUNList = dbUserNameElement.getChildNodes();
                li.add( 0, ( textDBUNList.item( 0 )).getNodeValue().trim() );

                NodeList dbUserPwdList = dbConnectionElement.getElementsByTagName( "upwd" );
                Element dbUserPwdElement = (Element) dbUserPwdList.item( 0 );
                NodeList textDUPwdList = dbUserPwdElement.getChildNodes();
                li.add( 1, ( textDUPwdList.item( 0 )).getNodeValue().trim() );

                NodeList dbURLList = dbConnectionElement.getElementsByTagName( "dburl" );
                Element dbURLElement = (Element) dbURLList.item( 0 );
                NodeList textDBURLList = dbURLElement.getChildNodes();
                li.add( 2, ( textDBURLList.item( 0 )).getNodeValue().trim() );

                NodeList dbStateNameList = dbConnectionElement.getElementsByTagName( "state-name" );
                Element dbStateNameElement = (Element) dbStateNameList.item( 0 );
                NodeList textDBSNameList = dbStateNameElement.getChildNodes();
                li.add( 3, ( textDBSNameList.item( 0 )).getNodeValue().trim() );
            }// end of if clause
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
        return li;
    }

    public List getDBDeatilsFromHibernate()
    {
        String path = System.getProperty( "user.home" ) + File.separator + "dhis" + File.separator
            + "hibernate.properties";
        FileReader fr = null;
        BufferedReader input = null;

        List<String> li = new ArrayList<String>();
        try
        {
            fr = new FileReader( path );
            input = new BufferedReader( fr );

            String s = input.readLine();
            while ( s instanceof String )
            {
                if ( s.contains( "jdbc:mysql:" ) )
                {
                    /*
                     * String tempS2[] = s.split("/"); String dbName =
                     * "jdbc:mysql://localhost/"+tempS2[tempS2.length-1].substring(0,
                     * tempS2[tempS2.length-1].indexOf('?'));
                     * System.out.println("DBName : "+dbName); li.add(0,dbName);
                     */
                    String tempS2[] = s.split( "=" );
                    String dbName = tempS2[1].substring( 0, tempS2[1].indexOf( '?' ) ).trim();
                    //System.out.println( "DBName : " + dbName );
                    li.add( 0, dbName );
                }
                if ( s.contains( "hibernate.connection.username" ) )
                {
                    String tempS2[] = s.split( "=" );
                    //System.out.println( "UserName : " + tempS2[tempS2.length - 1].trim() );
                    li.add( 1, tempS2[tempS2.length - 1].trim() );
                }
                if ( s.contains( "hibernate.connection.password" ) )
                {
                    String tempS2[] = s.split( "=" );
                    //System.out.println( "PassWord : " + tempS2[tempS2.length - 1].trim() );
                    li.add( 2, tempS2[tempS2.length - 1].trim() );
                }
                // System.out.println(s);
                s = input.readLine();
            }// while loop end
        }
        catch ( FileNotFoundException e )
        {
            System.out.println( e.getMessage() );
        }
        catch ( IOException e )
        {
            System.out.println( e.getMessage() );
        }
        finally
        {
            try
            {
                if ( fr != null )
                    fr.close();
                if ( input != null )
                    input.close();
            }
            catch ( Exception e )
            {
                System.out.println( e.getMessage() );
            }
        }

        return li;
    }// getDBDeatilsFromHibernate end

    public Connection openConnection()
    {

        try
        {

            // To get From XML File
            // List li = (ArrayList)getDBDeatilsFromXML();
            // String userName = (String) li.get(0);
            // String userPass = (String) li.get(1);
            // String urlForConnection = (String) li.get(2);

            // To get From Hibernate.Properties File
            List li = (ArrayList) getDBDeatilsFromHibernate();
            String urlForConnection = (String) li.get( 0 );
            String userName = (String) li.get( 1 );
            String userPass = (String) li.get( 2 );

            // Direct DBConnection
            // String userName = "dhis";
            // String userPass = "";
            // String urlForConnection = "jdbc:mysql://localhost/jh_dhis2";

            Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
            con = DriverManager.getConnection( urlForConnection, userName, userPass );
        }
        catch ( Exception e )
        {
            System.out.println( "Exception while opening connection : " + e.getMessage() );
            return null;
        }
        return con;
    } // openConnection end

    public void closeConnection()
    {
        try
        {
        }
        finally
        {
            try
            {
                if ( con != null )
                    con.close();
            }
            catch ( Exception e )
            {
                System.out.println( e.getMessage() );
            }
        }
    } // closeConnection end

}
