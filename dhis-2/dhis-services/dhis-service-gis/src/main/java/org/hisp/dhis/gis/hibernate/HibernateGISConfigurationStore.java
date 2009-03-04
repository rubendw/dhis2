package org.hisp.dhis.gis.hibernate;

import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.gis.GISConfiguration;
import org.hisp.dhis.gis.GISConfigurationStore;
import org.hisp.dhis.hibernate.HibernateSessionManager;

public class HibernateGISConfigurationStore
    implements GISConfigurationStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateSessionManager sessionManager;

    public void setSessionManager( HibernateSessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }

    public void add( String arg0, String arg1 )
    {

        Session session = sessionManager.getCurrentSession();

        session.save( new GISConfiguration( arg0, arg1 ) );

    }

    public void delete( String key )
    {
        Session session = sessionManager.getCurrentSession();

        session.createQuery( "delete GISConfiguration as f where f.key = ?" ).setEntity( 0, key );
    }

    public GISConfiguration get( String key )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( GISConfiguration.class );
        criteria.add( Restrictions.eq( "key", key ) );

        return (GISConfiguration) criteria.uniqueResult();
    }

    public Collection<GISConfiguration> getALL()
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( GISConfiguration.class );

        return criteria.list();
    }

    public String getValue( String key )
    {
        Session session = sessionManager.getCurrentSession();

        Criteria criteria = session.createCriteria( GISConfiguration.class );
        criteria.add( Restrictions.eq( "key", key ) );
        
        GISConfiguration gisConfiguration = (GISConfiguration) criteria.uniqueResult();
        if(gisConfiguration==null) return null;
        return gisConfiguration.getValue();
    }

    public void update( String key, String value )
    {
        GISConfiguration gisConfiguration = get( key );

        gisConfiguration.setValue( value );

        Session session = sessionManager.getCurrentSession();

        session.update( gisConfiguration );

    }

}
