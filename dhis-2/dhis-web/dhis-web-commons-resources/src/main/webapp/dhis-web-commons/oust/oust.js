
// -----------------------------------------------------------------------------
// Author:   Torgeir Lorange Ostby
// Version:  $Id: oust.js 5791 2008-10-02 11:04:41Z larshelg $
// -----------------------------------------------------------------------------

var selectionTreeSelection = new SelectionTreeSelection();
var selectionTree = new SelectionTree();
var selectionTreePath = '../dhis-web-commons/oust/';

// -----------------------------------------------------------------------------
// Selection
// -----------------------------------------------------------------------------

function SelectionTreeSelection()
{
    var onSelectFunction;
    
    var listenerFunction;
    
    var multipleSelectionAllowed = true;

    this.setOnSelectFunction = function( onSelectFunction_ )
    {
        onSelectFunction = onSelectFunction_;
    }

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
        if ( onSelectFunction )
        {
            onSelectFunction();
        }
        
        var request = new Request();
        request.setCallbackSuccess( responseReceived );
        request.setResponseTypeXML( 'selected' );
        
        var unitTag = document.getElementById( getTagId( unitId ));
        var linkTags = unitTag.getElementsByTagName( 'a' );

        if ( linkTags[0].className == 'selected' )
        {
            request.send( selectionTreePath + 'removeorgunit.action?id=' + unitId );
            linkTags[0].className = '';
        }
        else
        {
            if ( multipleSelectionAllowed )
            {
                request.send( selectionTreePath + 'addorgunit.action?id=' + unitId );
                linkTags[0].className = 'selected';
            }
            else
            {
                request.send( selectionTreePath + 'setorgunit.action?id=' + unitId );
                    
                // Remove all select marks
                var treeTag = document.getElementById( 'selectionTree' );
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
        return 'oustOrgUnit' + unitId;
    }
}

// -----------------------------------------------------------------------------
// Subtree
// -----------------------------------------------------------------------------

function SelectionTree()
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
            request.send( selectionTreePath + 'expandSubtree.action?parentId=' + unitId );
        }
        else
        {
            setVisible( children[0], false );
            setToggle( parentTag, false );
        }
    };

    this.buildSelectionTree = function()
    {
        var treeTag = document.getElementById( 'selectionTree' );
        
        setLoadingMessage( treeTag );
        
        var children = treeTag.getElementsByTagName( 'ul' );
        if ( children.length > 0 )
        {
            treeTag.removeChild( children[0] );
        }

        var request = new Request();
        request.setResponseTypeXML( 'units' );
        request.setCallbackSuccess( treeReceived );
        request.send( selectionTreePath + 'getExpandedTree.action' );
    };

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
        
        var treeTag = document.getElementById( 'selectionTree' );
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
        
        clearLoadingMessage( treeTag );
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
            toggleTag.onclick = new Function( 'selectionTree.toggle( ' + childId + ' )' );
            toggleTag.appendChild( getToggleExpand() );
        }
        else
        {
            toggleTag.appendChild( getToggleBlank() );
        }

        var linkTag = document.createElement( 'a' );
        linkTag.href = 'javascript:void selectionTreeSelection.select( ' + childId + ' )';
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
        return 'oustOrgUnit' + unitId;
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
    
    function setLoadingMessage( treeTag )
    {
        treeTag.style.backgroundImage = 'url( ../images/loading.png )';
        treeTag.style.backgroundRepeat = 'no-repeat';
    }
    
    function clearLoadingMessage( treeTag )
    {
        treeTag.style.backgroundImage = 'none';
    }
}
