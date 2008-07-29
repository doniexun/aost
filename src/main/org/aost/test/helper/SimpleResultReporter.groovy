package org.aost.test.helper
/**
 * Simply print out 
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jul 28, 2008
 *
 */
class SimpleResultReporter implements ResultReporter{

    public void report(List<TestResult> results) {
        int total = 0
        int succeeded = 0
        int failed = 0
        if(results != null && (!results.isEmpty())){
            total = results.size()
            results.each{ val ->
                if(val.getProperty("passed") == true){
                    succeeded++
                }else{
                    failed++
                }
            }
        }

        println "Test Results: \n\n"
        println "Total tests: ${total}\n"
        println "Tests succeeded: ${succeeded}\n"
        println "Tests failed: ${failed}\n\n"
        println "------------------------------------------------------------------------\n"
        if(results != null && (!results.isEmpty())){
            for(TestResult result : results){
                println "{"
                println result.toString()
                println "}\n"
            }
        }
        println "------------------------------------------------------------------------\n"
    }

}