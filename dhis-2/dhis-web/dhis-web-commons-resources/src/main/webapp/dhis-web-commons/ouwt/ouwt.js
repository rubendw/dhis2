
// -----------------------------------------------------------------------------
// Author:   Torgeir Lorange Ostby
// Version:  $Id: ouwt.js 3457 2007-07-11 12:34:24Z torgeilo $
// -----------------------------------------------------------------------------

/*
 * Usage:
 *
 * Use the selection.setListenerFunction function to register a callback
 * function to be called when an organisation unit is selected. The callback
 * function must have one argument, an array with the ids of the selected
 * organisation units.
 *
 * Multiple selection is by default turned off. Use the
 * selection.setMultipleSelectionAllowed function to change this.
 */

var selection = new Selection();
var subtree = new Subtree();
var organisationUnitTreePath = '../dhis-web-commons/ouwt/';

// -----------------------------------------------------------------------------
// Selection
// -----------------------------------------------------------------------------

function Selection()
{
    var listenerFunction;
    var multipleSelectionAllowed = false;

    this.setListenerFunction = function( listenerFunction_ )
    {
        listenerFunction = listenerFunction_;
    };

    this.setMultipleSelectionAllowed = function( allowed )
    {
        multipleSelectionAllowed = allowed;
    };

    this.select = function( unitId )
    {
        var request = new Request();
        request.setCallbackSuccess( responseReceived );
        request.setResponseTypeXML( 'selected' );
        
        var unitTag = document.getElementById( getTagId( unitId ));
        var linkTags = unitTag.getElementsByTagName( 'a' );

        if ( linkTags[0].className == 'selected' )
        {
            request.send( organisationUnitTreePath + 'removeorgunit.action?id=' + unitId );
            linkTags[0].className = '';
        }
        else
        {
            if ( multipleSelectionAllowed )
            {
                request.send( organisationUnitTreePath + 'addorgunit.action?id=' + unitId );
                linkTags[0].className = 'selected';
            }
            else
            {
                request.send( organisationUnitTreePath + 'setorgunit.action?id=' + unitId );

                // Remove all select marks
                var treeTag = document.getElementById( 'orgUnitTree' );
                var linkTags = treeTag.getElementsByTagName( 'a' );

                for ( var i = 0, linkTag; ( linkTag = linkTags[i] ); ++i )
                {
                    linkTag.className = '';
                }

                // Set new select mark
                var unitTag = document.getElementById( getTagId( unitId ));
                linkTags = unitTag.getElementsByTagName( 'a' );
                linkTags[0].className = 'selected';
            }
        }
    };

    function responseReceived( rootElement )
    {
        if ( !listenerFunction )
        {
            return;
        }

        var unitIds = new Array();
        
        var unitIdElements = rootElement.getElementsByTagName( 'unitId' );
        for ( var i = 0, unitIdElement; ( unitIdElement = unitIdElements[i] ); ++i )
        {
            unitIds[i] = unitIdElement.firstChild.nodeValue;
        }
        
        listenerFunction( unitIds );
    }

    function getTagId( unitId )
    {
        return 'orgUnit' + unitId;
    }
}

// -----------------------------------------------------------------------------
// Subtree
// -----------------------------------------------------------------------------

function Subtree()
{
    this.toggle = function( unitId )
    {
        var parentTag = document.getElementById( getTagId( unitId ));
        var children = parentTag.getElementsByTagName( 'ul' );

        var request = new Request();
        request.setResponseTypeXML( 'units' );

        if ( children.length < 1 || !isVisible( children[0] ))
        {
            request.setCallbackSuccess( processExpand );
            request.send( organisationUnitTreePath + 'expandSubtree.action?parentId=' + unitId );
        }
        else
        {
            request.setCallbackSuccess( processCollapse );
            request.send( organisationUnitTreePath + 'collapseSubtree.action?parentId=' + unitId );
        }
    };

    this.refreshTree = function()
    {
        var treeTag = document.getElementById( 'orgUnitTree' );

        var children = treeTag.getElementsByTagName( 'ul' );
        treeTag.removeChild( children[0] );

        var request = new Request();
        request.setResponseTypeXML( 'units' );
        request.setCallbackSuccess( treeReceived );
        request.send( organisationUnitTreePath + 'getExpandedTree.action' );
    };

    function processCollapse( rootElement )
    {
        var unitElements = rootElement.getElementsByTagName( 'unit' );
        
        for ( var i = 0, unitElement; ( unitElement = unitElements[i] ); ++i )
        {
            var parentId = unitElement.firstChild.nodeValue;
            var parentTag = document.getElementById( getTagId( parentId ));
            var children = parentTag.getElementsByTagName( 'ul' );
            
            setVisible( children[0], false );
            setToggle( parentTag, false );
        }
    }
    
    function processExpand( rootElement )
    {
        var parentElements = rootElement.getElementsByTagName( 'parent' );

        for ( var i = 0, parentElement; ( parentElement = parentElements[i] ); ++i )
        {
            var parentId = parentElement.getAttribute( 'parentId' );
            var parentTag = document.getElementById( getTagId( parentId ));
            var children = parentTag.getElementsByTagName( 'ul' );

            if ( children.length < 1 )
            {
                createChildren( parentTag, parentElement );
            }
            else
            {
                setVisible( children[0], true );
                setToggle( parentTag, true );
            }
        }
    }

    function treeReceived( rootElement )
    {
        var rootsElement = rootElement.getElementsByTagName( 'roots' )[0];
        var unitElements = rootsElement.getElementsByTagName( 'unit' );
        
        var treeTag = document.getElementById( 'orgUnitTree' );
        var rootsTag = document.createElement( 'ul' );

        for ( var i = 0; i < unitElements.length; ++i )
        {
            var unitTag = createTreeElementTag( unitElements[i] );
            
            rootsTag.appendChild( unitTag );
        }

        treeTag.appendChild( rootsTag );

        var childrenElement = rootElement.getElementsByTagName( 'children' )[0];
        var parentElements = childrenElement.getElementsByTagName( 'parent' );

        for ( var i = 0, parentElement; ( parentElement = parentElements[i] ); ++i )
        {
            var parentId = parentElement.getAttribute( 'parentId' );
            var parentTag = document.getElementById( getTagId( parentId ));

            createChildren( parentTag, parentElement );
        }
    }

    function createChildren( parentTag, parentElement )
    {
        var children = parentElement.getElementsByTagName( 'child' );
        var childrenTag = document.createElement( 'ul' );

        for ( var i = 0, child; ( child = children[i] ); ++i )
        {
            var childTag = createTreeElementTag( child );

            childrenTag.appendChild( childTag );
        }

        setVisible( childrenTag, true );
        setToggle( parentTag, true );

        parentTag.appendChild( childrenTag );
    }

    function createTreeElementTag( child )
    {
        var childId = child.getAttribute( 'id' );
        var hasChildren = child.getAttribute( 'hasChildren' ) != '0';

        var toggleTag = document.createElement( 'span' );
        toggleTag.className = 'toggle';

        if ( hasChildren )
        {
            toggleTag.onclick = new Function( 'subtree.toggle( ' + childId + ' )' );
            toggleTag.appendChild( getToggleExpand() );
        }
        else
        {
            toggleTag.appendChild( getToggleBlank() );
        }

        var linkTag = document.createElement( 'a' );
        linkTag.href = 'javascript:void selection.select( ' + childId + ' )';
        linkTag.appendChild( document.createTextNode( child.firstChild.nodeValue ));

        if ( child.getAttribute( 'selected' ) == 'true' )
        {
            linkTag.className = 'selected';
        }

        var childTag = document.createElement( 'li' );
        childTag.id = getTagId( childId );
        childTag.appendChild( document.createTextNode( ' ' ));
        childTag.appendChild( toggleTag );
        childTag.appendChild( document.createTextNode( ' ' ));
        childTag.appendChild( linkTag );
        
        return childTag;
    }

    function setToggle( unitTag, expanded )
    {
        var spans = unitTag.getElementsByTagName( 'span' );
        var toggleTag = spans[0];
        var toggleImg = expanded ? getToggleCollapse() : getToggleExpand();

        if ( toggleTag.firstChild )
        {
        	toggleTag.replaceChild( toggleImg, toggleTag.firstChild );
		}
		else
		{
			toggleTag.appendChild( toggleImg );
		}
    }

    function setVisible( tag, visible )
    {
        tag.style.display = visible ? 'block' : 'none';
    }

    function isVisible( tag )
    {
        return tag.style.display != 'none';
    }

    function getTagId( unitId )
    {
        return 'orgUnit' + unitId;
    }
    
    function getToggleExpand()
    {
        var imgTag = getToggleImage();
        imgTag.src = '../images/plus.png';
        imgTag.alt = '[+]';
        return imgTag;
    }
    
    function getToggleCollapse()
    {
        var imgTag = getToggleImage();
        imgTag.src = '../images/minus.png';
        imgTag.alt = '[-]';
        return imgTag;
    }

	function getToggleBlank()
	{
		var imgTag = getToggleImage();
		imgTag.src = '../images/transparent.gif';
		imgTag.alt = '';
		return imgTag;
	}
    
    function getToggleImage()
    {
        var imgTag = document.createElement( 'img' );
        imgTag.width = '9';
        imgTag.height = '9';
        return imgTag;
    }
}
