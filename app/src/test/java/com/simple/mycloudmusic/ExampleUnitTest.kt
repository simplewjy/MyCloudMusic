package com.simple.mycloudmusic

import android.util.Log
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.max
import kotlin.math.min

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test10() {

    }


    private fun isMatch(s: String, p: String): Boolean {
        return true
    }

    @Test
    fun test11() {
        val testArray = intArrayOf(1, 1)
        val area = maxArea(testArray)
        println("test11: --------------$area")
    }

    private fun maxArea(height: IntArray): Int {
        var maxArea = 0
        var r = height.size - 1
        var l = 0
        while (l < r) {
            val minH = height[l].coerceAtMost(height[r])
            val area = ((r - l) * minH)
            maxArea = area.coerceAtLeast(maxArea)
            while (height[l] <= minH && l < r) {
                l++
            }
            while (height[r] <= minH && l < r) {
                r--
            }
        }
        return maxArea
    }

    @Test
    fun test12() {
        val romanNum = intToRoman(100)
        println("test12: --------------$romanNum")
    }

    fun intToRoman(num: Int): String {
        val strList = arrayListOf<String>()
        val reverseNum = num.toString().reversed()
        reverseNum.forEachIndexed { index, c ->
            when (index) {
                0 -> {
                    val individualNum = castIndividual(c.toString().toInt())
                    strList.add(individualNum)
                }
                1 -> {
                    val tenNum = castTen(c.toString().toInt())
                    strList.add(tenNum)
                }
                2 -> {
                    val hundredNum = castHundred(c.toString().toInt())
                    strList.add(hundredNum)
                }
                3 -> {
                    val castThousand = castThousand(c.toString().toInt())
                    strList.add(castThousand)
                }
            }
        }
        val romanNum = StringBuilder()
        strList.reverse()
        strList.forEach {
            romanNum.append(it)
        }
        return romanNum.toString()
    }

    private fun castIndividual(num: Int): String {
        return when (num) {
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            6 -> "VI"
            7 -> "VII"
            8 -> "VIII"
            9 -> "IX"
            else -> ""
        }
    }

    private fun castTen(num: Int): String {
        return when (num) {
            1 -> "X"
            2 -> "XX"
            3 -> "XXX"
            4 -> "XL"
            5 -> "L"
            6 -> "LX"
            7 -> "LXX"
            8 -> "LXXX"
            9 -> "XC"
            else -> ""
        }
    }

    private fun castHundred(num: Int): String {
        return when (num) {
            1 -> "C"
            2 -> "CC"
            3 -> "CCC"
            4 -> "CD"
            5 -> "D"
            6 -> "DC"
            7 -> "DCC"
            8 -> "DCCC"
            9 -> "CM"
            else -> ""
        }
    }

    private fun castThousand(num: Int): String {
        return when (num) {
            1 -> "M"
            2 -> "MM"
            3 -> "MMM"
            else -> ""
        }
    }


    @Test
    fun test13() {
        val romanNum = romanToInt("MCMXCIV")
        println("test13: --------------$romanNum")
    }

    fun romanToInt(s: String): Int {
        var newStr = s
        newStr = newStr.replace("IV", "a")
        newStr = newStr.replace("IX", "b")
        newStr = newStr.replace("XL", "c")
        newStr = newStr.replace("XC", "d")
        newStr = newStr.replace("CD", "e")
        newStr = newStr.replace("CM", "f")

        var num = 0
        newStr.forEach {
            num += castRomanNumToInt(it)
        }
        return num
    }

    private fun castRomanNumToInt(s: Char): Int {
        return when (s) {
            'I' -> 1
            'V' -> 5
            'X' -> 10
            'L' -> 50
            'C' -> 100
            'D' -> 500
            'M' -> 1000
            'a' -> 4
            'b' -> 9
            'c' -> 40
            'd' -> 90
            'e' -> 400
            'f' -> 900
            else -> 0
        }
    }

    @Test
    fun test14() {
        val aaa = arrayOf("flower", "flow", "flight")
        val longestCommonPrefix = longestCommonPrefix(aaa)
        println("test14: --------------$longestCommonPrefix")
    }

    /**
     * 编写一个函数来查找字符串数组中的最长公共前缀。如果不存在公共前缀，返回空字符串 ""。
     */
    fun longestCommonPrefix(strs: Array<String>): String {
        var s = ""
        if (strs.isEmpty()) return s
        s = strs[0]
        strs.forEach {
            while (!it.startsWith(s)) {
                if (s.isEmpty()) return ""
                s = s.substring(0, s.length - 1)
            }
        }
        return s
    }

    @Test
    fun test15() {
        val aaa = arrayOf("flower", "flow", "flight")
        val longestCommonPrefix = longestCommonPrefix(aaa)
        println("test15: --------------$longestCommonPrefix")
    }

    /**
     * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
     * 你返回所有和为 0 且不重复的三元组。注意：答案中不可以包含重复的三元组。
     */
    fun threeSum(nums: IntArray): List<List<Int>> {
        val resultList = arrayListOf<List<Int>>()
//        resultList.toList()
        return resultList
    }

    @Test
    fun test2032() {
        val nums1 = intArrayOf(1, 1, 3, 2)
        val nums2 = intArrayOf(2, 3)
        val nums3 = intArrayOf(3)
        val resultList = twoOutOfThree(nums1, nums2, nums3)
        println("test2032: --------------$resultList")
    }

    /**
     * 给你三个整数数组 nums1、nums2 和 nums3 ，请你构造并返回一个 元素各不相同的 数组，且由 至少 在 两个 数组中出现的所有值组成。数组中的元素可以按 任意 顺序排列。
     */
    fun twoOutOfThree(nums1: IntArray, nums2: IntArray, nums3: IntArray): List<Int> {
        val resultList = arrayListOf<Int>()
        val countMap = HashMap<Int, Int>()
        nums1.distinct().forEach {
            if (countMap.contains(it)) {
                countMap[it] = countMap[it]!! + 1
            } else {
                countMap[it] = 1
            }
        }
        nums2.distinct().forEach {
            if (countMap.contains(it)) {
                countMap[it] = countMap[it]!! + 1
            } else {
                countMap[it] = 1
            }
        }
        nums3.distinct().forEach {
            if (countMap.contains(it)) {
                countMap[it] = countMap[it]!! + 1
            } else {
                countMap[it] = 1
            }
        }
        countMap.forEach { t, u ->
            if (u > 1) {
                resultList.add(t)
            }
        }
        return resultList
    }
}