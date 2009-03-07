package org.tellurium.test

import org.tellurium.test.IncludeUiModule

public class IncludeUiModule_UT extends GroovyTestCase {

  public void testIncludeGoolgeSearch(){
    IncludeUiModule module = new IncludeUiModule()
    module.defineUi()
    
    String result = module.getLocator("SearchModule.Input")
    assertEquals("/descendant-or-self::td[descendant::input[@title=\"Google Search\"] and descendant::input[@name=\"btnG\" and @value=\"Google Search\" and @type=\"submit\"] and descendant::input[@value=\"I'm Feeling Lucky\" and @type=\"submit\"]]/descendant-or-self::input[@title=\"Google Search\"]", result)

    result = module.getLocator("Google.SearchModule.Input")
    assertEquals("/descendant-or-self::table/descendant-or-self::td[descendant::input[@title=\"Google Search\"] and descendant::input[@name=\"btnG\" and @value=\"Google Search\" and @type=\"submit\"] and descendant::input[@value=\"I'm Feeling Lucky\" and @type=\"submit\"]]/descendant-or-self::input[@title=\"Google Search\"]", result)

    result = module.getLocator("GoogleBooksList.category")
    assertEquals("/descendant-or-self::table[descendant::div[@class=\"sub_cat_title\"] and descendant::div[@class=\"sub_cat_section\"] and @id=\"hp_table\"]/descendant-or-self::div[@class=\"sub_cat_title\"]", result)

    result = module.getLocator("Test.category")
    assertEquals("/descendant-or-self::div/descendant-or-self::div[@class=\"sub_cat_title\"]", result)

    result = module.getLocator("Test.subcategory")
    assertEquals("/descendant-or-self::div/descendant-or-self::div[@class=\"sub_cat_section\"]", result)
  }

}