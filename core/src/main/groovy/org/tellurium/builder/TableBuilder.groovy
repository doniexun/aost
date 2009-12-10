package org.telluriumsource.builder

import org.telluriumsource.object.Table
import org.telluriumsource.object.UiObject

/**
 * Table builder
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 */

class TableBuilder extends UiObjectBuilder{

  protected static final String TBODY = "tbody"

    def build(Map map, Closure closure) {
        //add default parameters so that the builder can use them if not specified
        def df = [:]
        df.put(TAG, Table.TAG)

        Map clocator = map.get(CLOCATOR)

        Map tbody = null
        if(clocator != null && clocator.size() > 0){
            tbody = clocator.remove(TBODY)
        }
        Table table = this.internBuild(new Table(), map, df)
        table.setBodyAttributes(tbody)
      
        if (closure)
            closure(table)

        return table
    }

    def build(Table table, UiObject[] objects) {

        if (table == null || objects == null || objects.length < 1)
            return table

        objects.each {UiObject obj ->
          table.add(obj)
        }

        return table
    }

    def build(Table table, UiObject object) {

        if (table == null || object == null)
            return table

        table.add(object)

        return table
    }
}
