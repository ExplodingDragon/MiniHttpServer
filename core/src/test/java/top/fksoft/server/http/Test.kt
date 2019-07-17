package top.fksoft.server.http

import java.util.Arrays.asList

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object Test {
    @JvmStatic
    fun main(args:Array<String>){
        test()
    }
    fun test(){
        var asList = asList("s", "ss", "sss")
        var filter = asList.filter { it != "s" }.toHashSet()
        for (s in filter) {
            println(s)
        }
    }

}