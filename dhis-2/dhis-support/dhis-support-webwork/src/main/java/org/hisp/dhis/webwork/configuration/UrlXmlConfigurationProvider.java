package org.hisp.dhis.webwork.configuration;

/*
 * This is a modified com.opensymphony.xwork.config.XmlConfigurationProvider.
 * The modification makes it possible to load xwork.xml files from inside JAR
 * files through URLs.
 * 
 * What kind of license should be put here?
 */

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.opensymphony.util.ClassLoaderUtil;
import com.opensymphony.util.FileManager;
import com.opensymphony.util.TextUtils;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.ObjectFactory;
import com.opensymphony.xwork.XworkException;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationException;
import com.opensymphony.xwork.config.ConfigurationProvider;
import com.opensymphony.xwork.config.ConfigurationUtil;
import com.opensymphony.xwork.config.ExternalReferenceResolver;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork.config.entities.ExternalReference;
import com.opensymphony.xwork.config.entities.InterceptorConfig;
import com.opensymphony.xwork.config.entities.InterceptorStackConfig;
import com.opensymphony.xwork.config.entities.PackageConfig;
import com.opensymphony.xwork.config.entities.ResultConfig;
import com.opensymphony.xwork.config.entities.ResultTypeConfig;
import com.opensymphony.xwork.config.providers.InterceptorBuilder;
import com.opensymphony.xwork.config.providers.XmlHelper;
import com.opensymphony.xwork.util.DomHelper;
import com.opensymphony.xwork.util.location.Location;

/**
 * @version $Id: UrlXmlConfigurationProvider.java 2743 2007-01-26 12:14:06Z torgeilo $
 */
@SuppressWarnings( "unchecked" )
public class UrlXmlConfigurationProvider
    implements ConfigurationProvider
{
    private static final Log LOG = LogFactory.getLog( UrlXmlConfigurationProvider.class );

    private Configuration configuration;

    private Set includedFileNames = new TreeSet();

    private String configFileName = "xwork.xml";

    public UrlXmlConfigurationProvider( String filename )
    {
        this.configFileName = filename;
    }

    public UrlXmlConfigurationProvider( String filename, ClassLoader loader )
    {
        this.configFileName = filename;
    }

    public void destroy()
    {
    }

    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( !(o instanceof UrlXmlConfigurationProvider) )
        {
            return false;
        }

        final UrlXmlConfigurationProvider UrlXmlConfigurationProvider = (UrlXmlConfigurationProvider) o;

        if ( (configFileName != null) ? (!configFileName.equals( UrlXmlConfigurationProvider.configFileName ))
            : (UrlXmlConfigurationProvider.configFileName != null) )
        {
            return false;
        }

        return true;
    }

    public int hashCode()
    {
        return ((configFileName != null) ? configFileName.hashCode() : 0);
    }

    public void init( Configuration configuration )
    {
        this.configuration = configuration;
        includedFileNames.clear();

        try
        {
            loadConfigurationFile( configFileName, null );
        }
        catch ( ConfigurationException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            LOG.fatal( "Could not load XWork configuration file, failing", e );
            throw new ConfigurationException( "Error loading configuration file " + configFileName, e );
        }
    }

    /**
     * Tells whether the ConfigurationProvider should reload its configuration.
     * This method should only be called if
     * ConfigurationManager.isReloadingConfigs() is true.
     * 
     * @return true if the file has been changed since the last time we read it
     */
    public boolean needsReload()
    {
        boolean needsReload = FileManager.fileNeedsReloading( configFileName );
        Iterator fileNameIterator = includedFileNames.iterator();

        while ( !needsReload && (fileNameIterator.hasNext()) )
        {
            String fileName = (String) fileNameIterator.next();
            needsReload = FileManager.fileNeedsReloading( fileName );
        }

        return needsReload;
    }

    protected InputStream getInputStream( String fileName )
        throws ConfigurationException
    {
        try
        {
            URL url = new URL( fileName );
            return url.openStream();
        }
        catch ( MalformedURLException e )
        {
            return FileManager.loadFile( fileName, this.getClass() );
        }
        catch ( IOException e )
        {
            throw new ConfigurationException( "The source of " + fileName + " is not accsesable.", e );
        }
    }

    protected void addAction( Element actionElement, PackageConfig packageContext )
        throws ConfigurationException
    {
        String name = actionElement.getAttribute( "name" );
        String className = actionElement.getAttribute( "class" );
        String methodName = actionElement.getAttribute( "method" );
        Location location = DomHelper.getLocationObject( actionElement );

        // methodName should be null if it's not set
        methodName = (methodName.trim().length() > 0) ? methodName.trim() : null;

        // if you don't specify a class on <action/>, it defaults to
        // ActionSupport
        if ( !TextUtils.stringSet( className ) )
        {
            className = ActionSupport.class.getName();
        }

        if ( !verifyAction( className, name, location ) )
        {
            return;
        }

        Map actionParams = XmlHelper.getParams( actionElement );

        Map results;

        try
        {
            results = buildResults( actionElement, packageContext );
        }
        catch ( ConfigurationException e )
        {
            throw new ConfigurationException( "Error building results for action " + name + " in namespace "
                + packageContext.getNamespace(), e, actionElement );
        }

        List interceptorList = buildInterceptorList( actionElement, packageContext );

        List externalrefs = buildExternalRefs( actionElement, packageContext );

        List exceptionMappings = buildExceptionMappings( actionElement, packageContext );

        ActionConfig actionConfig = new ActionConfig( methodName, className, actionParams, results, interceptorList,
            externalrefs, exceptionMappings, packageContext.getName() );
        actionConfig.setLocation( location );
        packageContext.addActionConfig( name, actionConfig );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Loaded "
                + (TextUtils.stringSet( packageContext.getNamespace() ) ? (packageContext.getNamespace() + "/") : "")
                + name + " in '" + packageContext.getName() + "' package:" + actionConfig );
        }
    }

    protected boolean verifyAction( String className, String name, Location loc )
    {
        try
        {
            Class clazz = ObjectFactory.getObjectFactory().getClassInstance( className );
            if ( ObjectFactory.getObjectFactory().isNoArgConstructorRequired() )
            {
                if ( !Modifier.isPublic( clazz.getModifiers() ) )
                {
                    LOG.error( "Action class [" + className + "] is not public, skipping action [" + name + "]" );
                    return false;
                }
                clazz.getConstructor( new Class[] {} );
            }
            return true;
        }
        catch ( ClassNotFoundException e )
        {
            LOG.error( "Action class [" + className + "] not found, skipping action [" + name + "] at " + loc, e );
            return false;
        }
        catch ( NoSuchMethodException e )
        {
            LOG.error( "Action class [" + className + "] does not have a public no-arg constructor,"
                + " skipping action [" + name + "] at " + loc, e );
            return false;
        }
        catch ( Exception ex )
        {
            throw new ConfigurationException( ex, loc );
        }
    }

    /**
     * Create a PackageConfig from an XML element representing it.
     */
    protected void addPackage( Element packageElement )
        throws ConfigurationException
    {
        PackageConfig newPackage = buildPackageContext( packageElement );

        // Check for allready existing configurations ..
        PackageConfig check = configuration.getPackageConfig( newPackage.getName() );
        if ( check != null )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Package is allready loaded! " + newPackage.getName() );
            }
            return;
        }

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Loaded " + newPackage );
        }

        // add result types (and default result) to this package
        addResultTypes( newPackage, packageElement );

        // load the interceptors and interceptor stacks for this package
        loadInterceptors( newPackage, packageElement );

        // load the default interceptor reference for this package
        loadDefaultInterceptorRef( newPackage, packageElement );

        // load the global result list for this package
        loadGlobalResults( newPackage, packageElement );

        // load the global exception handler list for this package
        loadGobalExceptionMappings( newPackage, packageElement );

        // get actions
        NodeList actionList = packageElement.getElementsByTagName( "action" );

        for ( int i = 0; i < actionList.getLength(); i++ )
        {
            Element actionElement = (Element) actionList.item( i );
            addAction( actionElement, newPackage );
        }

        // load the default action reference for this package
        loadDefaultActionRef( newPackage, packageElement );
        configuration.addPackageConfig( newPackage.getName(), newPackage );

    }

    protected void addResultTypes( PackageConfig packageContext, Element element )
    {
        NodeList resultTypeList = element.getElementsByTagName( "result-type" );

        for ( int i = 0; i < resultTypeList.getLength(); i++ )
        {
            Element resultTypeElement = (Element) resultTypeList.item( i );
            String name = resultTypeElement.getAttribute( "name" );
            String className = resultTypeElement.getAttribute( "class" );
            String def = resultTypeElement.getAttribute( "default" );

            Location loc = DomHelper.getLocationObject( resultTypeElement );

            Class clazz = verifyResultType( className, loc );
            if ( clazz != null )
            {
                String paramName = null;
                try
                {
                    paramName = (String) clazz.getField( "DEFAULT_PARAM" ).get( null );
                }
                catch ( Throwable t )
                {
                    // if we get here, the result type doesn't have a default
                    // param defined.
                }
                ResultTypeConfig resultType = new ResultTypeConfig( name, className, paramName );
                resultType.setLocation( DomHelper.getLocationObject( resultTypeElement ) );

                Map params = XmlHelper.getParams( resultTypeElement );

                if ( !params.isEmpty() )
                {
                    resultType.setParams( params );
                }
                packageContext.addResultTypeConfig( resultType );

                // set the default result type
                if ( "true".equals( def ) )
                {
                    packageContext.setDefaultResultType( name );
                }
            }
        }
    }

    protected Class verifyResultType( String className, Location loc )
    {
        try
        {
            return ObjectFactory.getObjectFactory().getClassInstance( className );
        }
        catch ( ClassNotFoundException e )
        {
            LOG.warn( "Result class [" + className + "] doesn't exist (ClassNotFoundException) at " + loc.toString()
                + ", ignoring", e );
        }
        catch ( NoClassDefFoundError e )
        {
            LOG.warn( "Result class [" + className + "] doesn't exist (NoClassDefFoundError) at " + loc.toString()
                + ", ignoring", e );
        }

        return null;
    }

    protected List buildExternalRefs( Element element, PackageConfig context )
        throws ConfigurationException
    {
        List refs = new ArrayList();
        NodeList externalRefList = element.getElementsByTagName( "external-ref" );

        String refName;
        String refValue = null;
        String requiredTemp;
        boolean required;

        for ( int i = 0; i < externalRefList.getLength(); i++ )
        {
            Element refElement = (Element) externalRefList.item( i );

            if ( refElement.getParentNode().equals( element ) )
            {
                refName = refElement.getAttribute( "name" );

                // If the external ref is not declared explicitly, we can
                // introspect the
                // reference type using it's name and try resolving the
                // reference using it's class type
                if ( refElement.getChildNodes().getLength() > 0 )
                {
                    refValue = refElement.getChildNodes().item( 0 ).getNodeValue();
                }

                requiredTemp = refElement.getAttribute( "required" );

                if ( (requiredTemp == null) || "".equals( requiredTemp ) )
                {
                    required = true;
                }
                else
                {
                    required = Boolean.valueOf( requiredTemp ).booleanValue();
                }

                refs.add( new ExternalReference( refName, refValue, required ) );
            }
        }

        return refs;
    }

    protected List buildInterceptorList( Element element, PackageConfig context )
        throws ConfigurationException
    {
        List interceptorList = new ArrayList();
        NodeList interceptorRefList = element.getElementsByTagName( "interceptor-ref" );

        for ( int i = 0; i < interceptorRefList.getLength(); i++ )
        {
            Element interceptorRefElement = (Element) interceptorRefList.item( i );

            if ( interceptorRefElement.getParentNode().equals( element ) )
            {
                List interceptors = lookupInterceptorReference( context, interceptorRefElement );
                interceptorList.addAll( interceptors );
            }
        }

        return interceptorList;
    }

    /**
     * This method builds a package context by looking for the parents of this
     * new package. <p/> If no parents are found, it will return a root package.
     */
    protected PackageConfig buildPackageContext( Element packageElement )
    {
        String parent = packageElement.getAttribute( "extends" );
        String abstractVal = packageElement.getAttribute( "abstract" );
        boolean isAbstract = Boolean.valueOf( abstractVal ).booleanValue();
        String name = TextUtils.noNull( packageElement.getAttribute( "name" ) );
        String namespace = TextUtils.noNull( packageElement.getAttribute( "namespace" ) );

        // RM* Load the ExternalReferenceResolver if one has been set
        ExternalReferenceResolver erResolver = null;

        String externalReferenceResolver = TextUtils
            .noNull( packageElement.getAttribute( "externalReferenceResolver" ) );

        PackageConfig cfg = null;
        if ( !("".equals( externalReferenceResolver )) )
        {
            try
            {
                erResolver = (ExternalReferenceResolver) ObjectFactory.getObjectFactory().buildBean(
                    externalReferenceResolver, null );
            }
            catch ( ClassNotFoundException e )
            {
                // TODO this should be localized
                String msg = "Could not find External Reference Resolver: " + externalReferenceResolver + ". "
                    + e.getMessage();
                LOG.error( msg );
                throw new ConfigurationException( msg, e, packageElement );
            }
            catch ( Exception e )
            {
                // TODO this should be localized
                String msg = "Could not create External Reference Resolver: " + externalReferenceResolver + ". "
                    + e.getMessage();
                LOG.error( msg );
                throw new ConfigurationException( msg, e, packageElement );
            }
        }

        if ( !TextUtils.stringSet( TextUtils.noNull( parent ) ) )
        { // no parents

            cfg = new PackageConfig( name, namespace, isAbstract, erResolver );
        }
        else
        { // has parents, let's look it up

            List parents = ConfigurationUtil.buildParentsFromString( configuration, parent );

            if ( parents.size() <= 0 )
            {
                LOG.error( "Unable to find parent packages " + parent );

                cfg = new PackageConfig( name, namespace, isAbstract, erResolver );
            }
            else
            {
                cfg = new PackageConfig( name, namespace, isAbstract, erResolver, parents );
            }
        }

        if ( cfg != null )
        {
            cfg.setLocation( DomHelper.getLocationObject( packageElement ) );
        }
        return cfg;
    }

    /**
     * Build a map of ResultConfig objects from below a given XML element.
     */
    protected Map buildResults( Element element, PackageConfig packageContext )
    {
        NodeList resultEls = element.getElementsByTagName( "result" );

        Map results = new LinkedHashMap();

        for ( int i = 0; i < resultEls.getLength(); i++ )
        {
            Element resultElement = (Element) resultEls.item( i );

            if ( resultElement.getParentNode().equals( element ) )
            {
                String resultName = resultElement.getAttribute( "name" );
                String resultType = resultElement.getAttribute( "type" );

                // if you don't specify a name on <result/>, it defaults to
                // "success"
                if ( !TextUtils.stringSet( resultName ) )
                {
                    resultName = Action.SUCCESS;
                }

                // there is no result type, so let's inherit from the parent
                // package
                if ( !TextUtils.stringSet( resultType ) )
                {
                    resultType = packageContext.getFullDefaultResultType();

                    // now check if there is a result type now
                    if ( !TextUtils.stringSet( resultType ) )
                    {
                        // uh-oh, we have a problem
                        LOG.error( "No result type specified for result named '" + resultName
                            + "', perhaps the parent package does not specify the result type?" );
                    }
                }

                ResultTypeConfig config = (ResultTypeConfig) packageContext.getAllResultTypeConfigs().get( resultType );

                if ( config == null )
                {
                    throw new ConfigurationException( "There is no result type defined for type '" + resultType
                        + "' mapped with name '" + resultName + "'", resultElement );
                }

                String resultClass = config.getClazz();

                // invalid result type specified in result definition
                if ( resultClass == null )
                {
                    LOG.error( "Result type '" + resultType + "' is invalid. Modify your xwork.xml file." );
                }

                Map resultParams = XmlHelper.getParams( resultElement );

                if ( resultParams.size() == 0 ) // maybe we just have a body -
                // therefore a default parameter
                {
                    // if <result ...>something</result> then we add a parameter
                    // of 'something' as this is the most used result param
                    if ( (resultElement.getChildNodes().getLength() == 1)
                        && (resultElement.getChildNodes().item( 0 ).getNodeType() == Node.TEXT_NODE) )
                    {
                        resultParams = new LinkedHashMap();

                        String paramName = config.getDefaultResultParam();
                        if ( paramName != null )
                        {
                            String paramValue = resultElement.getChildNodes().item( 0 ).getNodeValue();
                            if ( paramValue != null )
                            {
                                paramValue = paramValue.trim();
                            }
                            resultParams.put( paramName, paramValue );
                        }
                        else
                        {
                            LOG.warn( "no default parameter defined for result of type " + config.getName() );
                        }
                    }
                }

                // create new param map, so that the result param can override
                // the config param
                Map params = new LinkedHashMap();
                Map configParams = config.getParams();
                if ( configParams != null )
                {
                    params.putAll( configParams );
                }
                params.putAll( resultParams );

                ResultConfig resultConfig = new ResultConfig( resultName, resultClass, params );
                resultConfig.setLocation( DomHelper.getLocationObject( element ) );
                results.put( resultConfig.getName(), resultConfig );
            }
        }

        return results;
    }

    /**
     * Build a map of ResultConfig objects from below a given XML element.
     */
    protected List buildExceptionMappings( Element element, PackageConfig packageContext )
    {
        NodeList exceptionMappingEls = element.getElementsByTagName( "exception-mapping" );

        List exceptionMappings = new ArrayList();

        for ( int i = 0; i < exceptionMappingEls.getLength(); i++ )
        {
            Element ehElement = (Element) exceptionMappingEls.item( i );

            if ( ehElement.getParentNode().equals( element ) )
            {
                String emName = ehElement.getAttribute( "name" );
                String exceptionClassName = ehElement.getAttribute( "exception" );
                String exceptionResult = ehElement.getAttribute( "result" );

                Map params = XmlHelper.getParams( ehElement );

                if ( !TextUtils.stringSet( emName ) )
                {
                    emName = exceptionResult;
                }

                ExceptionMappingConfig ehConfig = new ExceptionMappingConfig( emName, exceptionClassName,
                    exceptionResult, params );
                ehConfig.setLocation( DomHelper.getLocationObject( ehElement ) );
                exceptionMappings.add( ehConfig );
            }
        }

        return exceptionMappings;
    }

    protected void loadDefaultInterceptorRef( PackageConfig packageContext, Element element )
    {
        NodeList resultTypeList = element.getElementsByTagName( "default-interceptor-ref" );

        if ( resultTypeList.getLength() > 0 )
        {
            Element defaultRefElement = (Element) resultTypeList.item( 0 );
            packageContext.setDefaultInterceptorRef( defaultRefElement.getAttribute( "name" ) );
        }
    }

    protected void loadDefaultActionRef( PackageConfig packageContext, Element element )
    {
        NodeList resultTypeList = element.getElementsByTagName( "default-action-ref" );

        if ( resultTypeList.getLength() > 0 )
        {
            Element defaultRefElement = (Element) resultTypeList.item( 0 );
            packageContext.setDefaultActionRef( defaultRefElement.getAttribute( "name" ) );
        }
    }

    /**
     * Load all of the global results for this package from the XML element.
     */
    protected void loadGlobalResults( PackageConfig packageContext, Element packageElement )
    {
        NodeList globalResultList = packageElement.getElementsByTagName( "global-results" );

        if ( globalResultList.getLength() > 0 )
        {
            Element globalResultElement = (Element) globalResultList.item( 0 );
            Map results = buildResults( globalResultElement, packageContext );
            packageContext.addGlobalResultConfigs( results );
        }
    }

    /**
     * Load all of the global results for this package from the XML element.
     */
    protected void loadGobalExceptionMappings( PackageConfig packageContext, Element packageElement )
    {
        NodeList globalExceptionMappingList = packageElement.getElementsByTagName( "global-exception-mappings" );

        if ( globalExceptionMappingList.getLength() > 0 )
        {
            Element globalExceptionMappingElement = (Element) globalExceptionMappingList.item( 0 );
            List exceptionMappings = buildExceptionMappings( globalExceptionMappingElement, packageContext );
            packageContext.addGlobalExceptionMappingConfigs( exceptionMappings );
        }
    }

    // protected void loadIncludes(Element rootElement, DocumentBuilder db)
    // throws Exception {
    // NodeList includeList = rootElement.getElementsByTagName("include");
    //
    // for (int i = 0; i < includeList.getLength(); i++) {
    // Element includeElement = (Element) includeList.item(i);
    // String fileName = includeElement.getAttribute("file");
    // includedFileNames.add(fileName);
    // loadConfigurationFile(fileName, db);
    // }
    // }
    protected InterceptorStackConfig loadInterceptorStack( Element element, PackageConfig context )
        throws ConfigurationException
    {
        String name = element.getAttribute( "name" );

        InterceptorStackConfig config = new InterceptorStackConfig( name );
        config.setLocation( DomHelper.getLocationObject( element ) );
        NodeList interceptorRefList = element.getElementsByTagName( "interceptor-ref" );

        for ( int j = 0; j < interceptorRefList.getLength(); j++ )
        {
            Element interceptorRefElement = (Element) interceptorRefList.item( j );
            List interceptors = lookupInterceptorReference( context, interceptorRefElement );
            config.addInterceptors( interceptors );
        }

        return config;
    }

    protected void loadInterceptorStacks( Element element, PackageConfig context )
        throws ConfigurationException
    {
        NodeList interceptorStackList = element.getElementsByTagName( "interceptor-stack" );

        for ( int i = 0; i < interceptorStackList.getLength(); i++ )
        {
            Element interceptorStackElement = (Element) interceptorStackList.item( i );

            InterceptorStackConfig config = loadInterceptorStack( interceptorStackElement, context );

            context.addInterceptorStackConfig( config );
        }
    }

    protected void loadInterceptors( PackageConfig context, Element element )
        throws ConfigurationException
    {
        NodeList interceptorList = element.getElementsByTagName( "interceptor" );

        for ( int i = 0; i < interceptorList.getLength(); i++ )
        {
            Element interceptorElement = (Element) interceptorList.item( i );
            String name = interceptorElement.getAttribute( "name" );
            String className = interceptorElement.getAttribute( "class" );

            Map params = XmlHelper.getParams( interceptorElement );
            InterceptorConfig config = new InterceptorConfig( name, className, params );
            config.setLocation( DomHelper.getLocationObject( interceptorElement ) );

            context.addInterceptorConfig( config );
        }

        loadInterceptorStacks( element, context );
    }

    // protected void loadPackages(Element rootElement) throws
    // ConfigurationException {
    // NodeList packageList = rootElement.getElementsByTagName("package");
    //
    // for (int i = 0; i < packageList.getLength(); i++) {
    // Element packageElement = (Element) packageList.item(i);
    // addPackage(packageElement);
    // }
    // }
    private void loadConfigurationFile( String fileName, Element includeElement )
    {
        if ( !includedFileNames.contains( fileName ) )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Loading xwork configuration from: " + fileName );
            }

            includedFileNames.add( fileName );

            Document doc = null;
            InputStream is = null;

            try
            {
                is = getInputStream( fileName );

                if ( is == null )
                {
                    throw new Exception( "Could not open file " + fileName );
                }

                InputSource in = new InputSource( is );

                // FIXME: we shouldn't be doing this lookup twice
                try
                {
                    in.setSystemId( ClassLoaderUtil.getResource( fileName, getClass() ).toString() );
                }
                catch ( Exception e )
                {
                    in.setSystemId( fileName );
                }

                Map dtdMappings = new HashMap();
                dtdMappings.put( "-//OpenSymphony Group//XWork 1.1.1//EN", "xwork-1.1.1.dtd" );
                dtdMappings.put( "-//OpenSymphony Group//XWork 1.1//EN", "xwork-1.1.dtd" );
                dtdMappings.put( "-//OpenSymphony Group//XWork 1.0//EN", "xwork-1.0.dtd" );

                doc = DomHelper.parse( in, dtdMappings );
            }
            catch ( XworkException e )
            {
                // WTF!!??
                if ( includeElement != null )
                {
                    System.out.println( e );
                    throw new ConfigurationException( e, includeElement );
                }
                else
                {
                    throw new ConfigurationException( e );
                }
            }
            catch ( Exception e )
            {
                final String s = "Caught exception while loading file " + fileName;
                LOG.error( s, e );
                // throw new ConfigurationException(s, e, includeElement);
                throw new ConfigurationException( s, e );
            }
            finally
            {
                if ( is != null )
                {
                    try
                    {
                        is.close();
                    }
                    catch ( IOException e )
                    {
                        LOG.error( "Unable to close input stream", e );
                    }
                }
            }

            Element rootElement = doc.getDocumentElement();
            NodeList children = rootElement.getChildNodes();
            int childSize = children.getLength();

            for ( int i = 0; i < childSize; i++ )
            {
                Node childNode = children.item( i );

                if ( childNode instanceof Element )
                {
                    Element child = (Element) childNode;

                    final String nodeName = child.getNodeName();

                    if ( nodeName.equals( "package" ) )
                    {
                        addPackage( child );
                    }
                    else if ( nodeName.equals( "include" ) )
                    {
                        String includeFileName = child.getAttribute( "file" );
                        loadConfigurationFile( includeFileName, child );
                    }
                }
            }

            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Loaded xwork configuration from: " + fileName );
            }
        }
    }

    /**
     * Looks up the Interceptor Class from the interceptor-ref name and creates
     * an instance, which is added to the provided List, or, if this is a ref to
     * a stack, it adds the Interceptor instances from the List to this stack.
     * 
     * @param interceptorRefElement Element to pull interceptor ref data from
     * @param context The PackageConfig to lookup the interceptor from
     * @return A list of Interceptor objects
     */
    private List lookupInterceptorReference( PackageConfig context, Element interceptorRefElement )
        throws ConfigurationException
    {
        String refName = interceptorRefElement.getAttribute( "name" );
        Map refParams = XmlHelper.getParams( interceptorRefElement );

        return InterceptorBuilder.constructInterceptorReference( context, refName, refParams );
    }
}
