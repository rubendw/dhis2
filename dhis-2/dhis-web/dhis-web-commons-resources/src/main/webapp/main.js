
var leftBar = new LeftBar();

function LeftBar()
{
    var visible = true;

    this.toggleVisible = function()
    {
        if ( visible )
        {
            this.hide();
        }
        else
        {
            this.show();
        }
    };

    this.show = function()
    {
        setMainPageLeftMargin( '300px' );
        setLeftBarWidth( '250px' );
        setLeftBarContentsVisible( true );
        setToggleShow( false );

        var request = new Request();
        request.send( '../dhis-web-commons/menu/setMenuVisible.action' );

        visible = true;
    };
    
    this.hide = function()
    {
        setLeftBarContentsVisible( false );
        setLeftBarWidth( '16px' );
        setMainPageLeftMargin( '65px' );
        setToggleShow( true );

        var request = new Request();
        request.send( '../dhis-web-commons/menu/setMenuHidden.action' );

        visible = false;
    };

    function setLeftBarWidth( width )
    {
        document.getElementById( 'leftBar' ).style.width = width;
        document.getElementById( 'mainMenu' ).style.width = width;
    }
    
    function setLeftBarContentsVisible( visible )
    {
        document.getElementById( 'leftBarContents' ).style.display = ( visible ? 'block' : 'none' );
        document.getElementById( 'mainMenuLink' ).style.display = ( visible ? 'block' : 'none' );
    }
    
    function setMainPageLeftMargin( width )
    {
        document.getElementById( 'mainPage' ).style.marginLeft = width;
    }
    
    function setToggleShow( show )
    {
        var image = document.getElementById( 'leftbarToggleImage' );
        var link = document.getElementById( 'leftbarToggleLink' );
        
        if ( show ) 
        {
	        image.src = '../images/show_menu.png';
	        image.alt = i18n_show_menu;
	        link.title = i18n_show_menu;
        }
        else
        {
	        image.src = '../images/hide_menu.png';
	        image.alt = i18n_hide_menu;
	        link.title = i18n_hide_menu;
        }
    }
}
