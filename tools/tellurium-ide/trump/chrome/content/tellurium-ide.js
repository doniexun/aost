

var trumpWindow;
/*
//opens or update the xpatherBowser window
function openUIModelWindow(clickedNode){
    //window opens asynchronously... so you cannot call methods immediately
    trumpWindow = window.open("chrome://trump/content/telluriumIdeBrowser.xul","uimodel_results","chrome,centerscreen,alwaysRaised=true,resizable");

}
*/

//opens Trump IDE Window
function openTelluriumIDEWindow(){
    //window opens asynchronously... so you cannot call methods immediately
    trumpWindow = window.open("chrome://trump/content/tellurium-ide.xul","uiEditor","chrome,centerscreen,alwaysRaised=true,resizable");
}


