 test("test for ui module parser", function() {
    var json = [{"obj":{"uid":"Google","locator":{"tag":"table"},"generated":"\/\/descendant-or-self::table","uiType":"Container"},"key":"Google"},{"obj":{"uid":"Input","locator":{"title":"Google Search","tag":"input","name":"q"},"generated":"\/\/descendant-or-self::table\/descendant-or-self::input[@title=\"Google Search\" and @name=\"q\"]","uiType":"InputBox"},"key":"Google.Input"},{"obj":{"uid":"Search","locator":{"tag":"input","name":"btnG","value":"Google Search","type":"submit"},"generated":"\/\/descendant-or-self::table\/descendant-or-self::input[@type=\"submit\" and @value=\"Google Search\" and @name=\"btnG\"]","uiType":"SubmitButton"},"key":"Google.Search"},{"obj":{"uid":"ImFeelingLucky","locator":{"tag":"input","name":"btnI","value":"I'm Feeling Lucky","type":"submit"},"generated":"\/\/descendant-or-self::table\/descendant-or-self::input[@type=\"submit\" and @value=\"I'm Feeling Lucky\" and @name=\"btnI\"]","uiType":"SubmitButton"},"key":"Google.ImFeelingLucky"}];
    var uim = new UiModule();
    uim.parseUiModule(json);
 });    
