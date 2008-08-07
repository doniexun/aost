package example.tellurium

import org.tellurium.dsl.DslContext

/**
 * Ui Modules for Tellurium Wiki page at
 *
 *   http://code.google.com/p/aost/w/list
 *
 * @author Quan Bui (Quan.Bui@gmail.com)
 *
 * Date: Aug 6, 2008
 *
 */
class TelluriumWikiPage extends DslContext{

   public void defineUi() {

       //define UI module of a form include download type selector and download search
       ui.Form(uid: "wikiSearch", clocator: [action: "/p/aost/w/list", method: "get"], group: "true") {
           Selector(uid: "pageType", clocator: [name: "can", id: "can"])
           TextBox(uid: "searchLabel", clocator: [tag: "span"])
           InputBox(uid: "searchBox", clocator: [name: "q"])
           SubmitButton(uid: "searchButton", clocator: [value: "Search"])
       }

       ui.Table(uid: "wikiResult", clocator: [id: "resultstable", class: "results"], group: "true"){
           //define table header
           //for the border column
           TextBox(uid: "header: 1", clocator: [:])
           UrlLink(uid: "header: 2", clocator: [text: "%%PageName"])
           UrlLink(uid: "header: 3", clocator: [text: "%%Summary + Labels"])
           UrlLink(uid: "header: 4", clocator: [text: "%%Changed"])
           UrlLink(uid: "header: 5", clocator: [text: "%%ChangedBy"])           
           UrlLink(uid: "header: 6", clocator: [text: "%%..."])

           //define table elements
           //for the border column
           TextBox(uid: "row: *, column: 1", clocator: [:])
           //the summary + labels column consists of a list of UrlLinks
           List(uid: "row:*, column: 3", clocator: [:]){
               UrlLink(uid: "all", clocator: [:])
           }
           //For the rest, just UrlLink
           UrlLink(uid: "all", clocator: [:])
       }
   }

    public String[] getSearchOptions(){
        return  getSelectOptions("wikiSearch.pageType")
    }

    public String getSearchType(){
        return  getSelectedLabel("wikiSearch.pageType");
    }

    public void selectSearchType(String type){
        selectByLabel "wikiSearch.pageType", type
    }

    public void searchForKeyword(String keyword){
        type "wikiSearch.searchBox", keyword
        click "wikiSearch.searchButton"
        pause 5000        
    }

    public int getTableHeaderNum(){
        return getTableHeaderColumnNum("wikiResult")
    }

    public List<String> getHeaderNames(){
        List<String> headernames = new ArrayList<String>()
        int mcolumn = getTableHeaderColumnNum("wikiResult")
        for(int i=1; i<=mcolumn; i++){
            headernames.add(getText("wikiResult.header[${i}]"))
        }

        return headernames       
    }

    public List<String> getPageNames(){
        int mcolumn = getTableMaxRowNum("wikiResult")
        List<String> filenames = new ArrayList<String>()
        for(int i=1; i<=mcolumn; i++){
            filenames.add(getText("wikiResult[${i}][2]").trim())
        }

        return filenames
    }

    public void clickPageNameColumn(int row){
        click "wikiResult[${row}][2]"
        pause 1000        
    }

    //click on the summary + labels column
    public void clickSummaryLabelsColumn(int row, int index){
        //because the table element is a list of UrlLinks, just click on the first one
        click "wikiResult[${row}][3].[${index}]"
        waitForPageToLoad 30000
    }

    public void clickChangedColumn(int row){
        click "wikiResult[${row}][4]"    
        waitForPageToLoad 30000
    }

    public void clickChangedByColumn(int row){
        click "wikiResult[${row}][5]"
        waitForPageToLoad 30000
    } 

    public void clickOnTableHeader(int column){
        click "wikiResult.header[${column}]"
        pause 500
    }
}
