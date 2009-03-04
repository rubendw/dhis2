window.onload = function () {
  var container = byId('selectedDataElements');
  var buttons =  container.getElementsByTagName('button');
  for ( var i = 0, button; button = buttons[i]; i++ )
  {
    button.onclick = removeCDEDataElement;
  }
}