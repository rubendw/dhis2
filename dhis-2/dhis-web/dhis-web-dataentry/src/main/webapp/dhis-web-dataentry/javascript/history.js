
function createChart()
{
    var canvas = document.getElementById( 'canvas' );
    var width = parseInt( canvas.style.width ) - 1;
    var height = parseInt( canvas.style.height ) - 1;
    var verticalPadding = height * 0.02;
    var horzontalPadding = verticalPadding;
    var cellWidth = width / historyLength;
    var fontSizePixels = 10;
    var lineHeight = 4; // ?
    var barBase = Math.round( height - verticalPadding * 2 - fontSizePixels * 2 - lineHeight );
    var maxBarHeight = barBase - verticalPadding * 2 - fontSizePixels;
    var barWidth = cellWidth * 0.4;
    var barPaddingLeft = ( cellWidth - barWidth ) / 2;
    var barColor = '#0000ff';
    var barBaseColor = '#808080';
    var textColor = '#000000';
    var minLimitColor = '#008000';
    var maxLimitColor = '#ff0000';
    var averageColor = '#0080ff';

    var g = new jsGraphics( 'canvas' );
    g.setFont( 'sans-serif', fontSizePixels + 'px', Font.PLAIN );
    g.setStroke( 2 );

    g.setColor( barColor );
    var barHeight;

    for ( i = 0; i < historyLength; ++i )
    {
        if ( historyPoints[i][1] )
        {
    	    barHeight = Math.round( maxBarHeight * historyPoints[i][1] / maxValue );
            g.fillRect( cellWidth * i + barPaddingLeft, barBase - barHeight, barWidth, barHeight );
        }
    }

    g.setColor( averageColor );
    var previousPoint = barBase - Math.round( maxBarHeight * historyPoints[0][2] / maxValue );
    var nextPoint;

    for ( i = 1; i < historyLength; ++i )
    {
        nextPoint = barBase - Math.round( maxBarHeight * historyPoints[i][2] / maxValue );
    	g.drawLine( cellWidth * ( i - 1 ) + cellWidth / 2, previousPoint, cellWidth * i + cellWidth / 2, nextPoint );
    	previousPoint = nextPoint;
    }

    g.setColor( barBaseColor );
    g.drawLine( 0, barBase, width, barBase );

    if ( minLimit )
    {
        var minLimitPos = barBase - maxBarHeight * minLimit / maxValue;
        g.setColor( minLimitColor );
    	g.drawLine( 0, minLimitPos, width, minLimitPos );
    }

    if ( maxLimit )
    {
        var maxLimitPos = barBase - maxBarHeight * maxLimit / maxValue;
    	g.setColor( maxLimitColor );
    	g.drawLine( 0, maxLimitPos, width, maxLimitPos );
    }

    g.setColor( textColor );

    for ( i = 0; i < historyLength; ++i )
    {
        if ( historyPoints[i][1] )
        {
    	    barHeight = Math.round( maxBarHeight * historyPoints[i][1] / maxValue );
    		g.drawStringRect( historyPoints[i][1].toFixed(), cellWidth * ( i - 1 ) + cellWidth / 2, barBase - barHeight - verticalPadding - fontSizePixels, cellWidth * 2, 'center' );
        }

        g.drawStringRect( historyPoints[i][0], cellWidth * i, barBase + verticalPadding, cellWidth, 'center' );
    }

    g.paint();
}

function saveMinLimit( organisationUnitId, dataElementId )
{
    var minLimitField = document.getElementById( "minLimit" );
    var maxLimitField = document.getElementById( "maxLimit" );

    var request = new Request();
    request.setCallbackSuccess( refreshWindow );
    request.setCallbackError( refreshWindow );

    if ( minLimitField.value == '' )
    {
        request.send( 'removeMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId );
    }
    else
    {
        var minLimit = Number( minLimitField.value );
        var maxLimit = Number( maxLimitField.value );
        
        if ( minLimit )
        {
        	if ( minLimit < 0 )
        	{
        	    minLimit = 0;
        	}

            if ( !maxLimit || maxLimit <= minLimit )
            {
                maxLimit = minLimit + 1;
            }

            request.send( 'saveMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId + '&minLimit=' + minLimit + '&maxLimit=' + maxLimit );
        }
        else
        {
            refreshWindow();
        }
    }
}

function saveMaxLimit( organisationUnitId, dataElementId )
{
    var minLimitField = document.getElementById( "minLimit" );
    var maxLimitField = document.getElementById( "maxLimit" );

    var request = new Request();
    request.setCallbackSuccess( refreshWindow );
    request.setCallbackError( refreshWindow );

    if ( maxLimitField.value == '' )
    {
        request.send( 'removeMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId );
    }
    else
    {
        var minLimit = Number( minLimitField.value );
        var maxLimit = Number( maxLimitField.value );
        
        if ( maxLimit )
        {
            if ( maxLimit < 1 )
            {
                maxLimit = 1;
            }

            if ( !minLimit )
            {
                minLimit = 0;
            }
            else if ( minLimit >= maxLimit )
            {
                minLimit = maxLimit - 1;
            }

            request.send( 'saveMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId + '&minLimit=' + minLimit + '&maxLimit=' + maxLimit );
        }
        else
        {
            refreshWindow();
        }
    }
}

function refreshWindow()
{
    window.location.reload();
}
