package com.example.lib2

import com.sun.xml.internal.fastinfoset.util.StringArray
import org.jetbrains.annotations.TestOnly

/**
 *
 * @author Y&N
 * date: 2022-3-25
 * desc:
 */
class Test {
    fun fun1(num1: Int, num2: Int, block: (Int, Int) -> Int): Int {
        return block(num1, num2)
    }

    private fun add(num1: Int, num2: Int) = num1 + num2
    private fun reduce(num1: Int, num2: Int) = num1 - num2

    fun main() {
        val t = Test()
        println(
            "res----------" + t.fun1(10, 2, ::add)
        )
        println(
            "res-" + t.fun1(10, 2, ::reduce)
        )

    }
}