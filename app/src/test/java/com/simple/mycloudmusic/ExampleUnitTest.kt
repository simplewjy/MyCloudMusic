package com.simple.mycloudmusic

import org.junit.Assert.*
import org.junit.Test
import java.util.*


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
        val aaa = intArrayOf(1, 2, -2, -1)
        val longestCommonPrefix = threeSum(aaa)
        println("test15: --------------$longestCommonPrefix")
    }

    /**
     * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
     * 你返回所有和为 0 且不重复的三元组。注意：答案中不可以包含重复的三元组。
     */
    fun threeSum(nums: IntArray): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        if (nums.size < 3) {
            return result
        }
        //先从小到大排序
        nums.sort()
        for (i in 0 until nums.size - 2) {
            //如果该数大于0，则从后面再找2个数，3个数的和必然大于0
            if (nums[i] > 0) {
                break
            }
            if (i > 0 && nums[i - 1] == nums[i]) continue
            var j = i + 1
            var k = nums.size - 1
            while (j < k) {
                val sum = nums[i] + nums[j] + nums[k]
                when {
                    sum > 0 -> {
                        k--
                    }
                    sum == 0 -> {
                        result.add(listOf(nums[i], nums[j], nums[k]))
                        while (j < k && nums[j] == nums[++j]) {
                        }
                        while (j < k && nums[k] == nums[--k]) {
                        }
                    }
                    else -> {
                        j++
                    }
                }
            }
        }
        return result
    }

    @Test
    fun test16() {
        val aaa = intArrayOf(-1, 2, 1, -4)
        val longestCommonPrefix = threeSumClosest(aaa, 1)
        println("test16: --------------$longestCommonPrefix")
    }

    /**
     * 给你一个长度为 n 的整数数组 nums 和 一个目标值 target。请你从 nums 中选出三个整数，使它们的和与 target 最接近。
     * 返回这三个数的和。
     */
    fun threeSumClosest(nums: IntArray, target: Int): Int {
        var result = Int.MAX_VALUE
        if (nums.size < 3) return 0
        if (nums.size == 3) return nums.sum()
        //将数组从小到大排序
        nums.sort()
        for (i in 0 until nums.size - 2) {
            //去重处理
            if (i > 0 && nums[i] == nums[i - 1]) continue
            var j = i + 1
            var k = nums.size - 1
            while (j < k) {
                val tempValue = nums[i] + nums[j] + nums[k]
                val differenceValue = tempValue - target
                when {
                    differenceValue == 0 -> {
                        return tempValue
                    }
                    differenceValue < 0 -> {
                        if (Math.abs(differenceValue) < Math.abs(result - target)) {
                            result = tempValue
                        }
                        j++
                    }
                    else -> {
                        if (Math.abs(differenceValue) < Math.abs(result - target)) {
                            result = tempValue
                        }
                        k--
                    }
                }
            }
        }
        return result
    }

    @Test
    fun test17() {
        val aaa = "23"
        val result = letterCombinations(aaa)
        println("test17: --------------$result")
    }

    private val digistMap = HashMap<Char, CharArray>()

    fun letterCombinations(digits: String): List<String> {
        //声明map对应数字与字母
        digistMap['2'] = charArrayOf('a', 'b', 'c')
        digistMap['3'] = charArrayOf('d', 'e', 'f')
        digistMap['4'] = charArrayOf('g', 'h', 'i')
        digistMap['5'] = charArrayOf('j', 'k', 'l')
        digistMap['6'] = charArrayOf('m', 'n', 'o')
        digistMap['7'] = charArrayOf('p', 'q', 'r', 's')
        digistMap['8'] = charArrayOf('t', 'u', 'v')
        digistMap['9'] = charArrayOf('w', 'x', 'y', 'z')

        val resultList = arrayListOf<String>()
        if (digits.isEmpty()) return resultList

        val builder = StringBuilder()

        fun backtrack(index: Int) {
            // 当索引与数组长度相同时，跳出递归
            if (index == digits.length) {
                resultList.add(builder.toString())
                return
            }
            for (c in digistMap[digits[index]]!!) {
                builder.append(c)
                // 递归
                backtrack(index + 1)
                // 回溯
                builder.deleteLastChar()
            }
        }
        backtrack(0)
        return resultList
    }

    private fun StringBuilder.deleteLastChar() {
        if (this.isNotEmpty()) {
            this.deleteCharAt(this.length - 1)
        }
    }

    @Test
    fun test18() {
        val aaa = intArrayOf(1000000000, 1000000000, 1000000000, 1000000000)
        val longestCommonPrefix = fourSum(aaa, -294967296)
        println("test18: --------------$longestCommonPrefix")
    }

    /**
     *  给你一个由 n 个整数组成的数组 nums ，和一个目标值 target 。请你找出并返回满足下述全部条件且不重复的四元组 [nums[a], nums[b], nums[c], nums[d]]
     * （若两个四元组元素一一对应，则认为两个四元组重复）：
     *  0 <= a, b, c, d < n
     *  a、b、c 和 d 互不相同
     *  nums[a] + nums[b] + nums[c] + nums[d] == target
     *  你可以按 任意顺序 返回答案 。
     */
    fun fourSum(nums: IntArray, target: Int): List<List<Int>> {
        val resultList = arrayListOf<List<Int>>()
        if (nums.size < 4) return resultList
        nums.sort()
        //四个指针i,j,k,l
        for (i in 0 until nums.size - 3) {
            if (i > 0 && nums[i] == nums[i - 1]) continue
            for (j in i + 1 until nums.size - 2) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue
                var k = j + 1
                var l = nums.size - 1

                while (k < l) {
                    val sum = nums[i].toLong() + nums[j] + nums[k] + nums[l]
                    when {
                        sum > target -> {
                            while (k < l && nums[l] == nums[--l]) {
                            }
                        }
                        sum < target -> {
                            while (k < l && nums[k] == nums[++k]) {
                            }
                        }
                        else -> {
                            resultList.add(listOf(nums[i], nums[j], nums[k], nums[l]))
                            while (k < l && nums[k] == nums[++k]) {
                            }
                            while (k < l && nums[l] == nums[--l]) {
                            }
                        }
                    }
                }
            }
        }
        return resultList
    }


    @Test
    fun test19() {
        var li = ListNode(0)
        var li1 = ListNode(1)
        var li2 = ListNode(2)
        var li3 = ListNode(3)
        var li4 = ListNode(4)
        li.next = li1
        li1.next = li2
        li2.next = li3
        li3.next = li4
        var test = removeNthFromEnd(li, 2)
        val sb = StringBuilder()
        while (test != null) {
            sb.append("${test.`val`},")
            test = test.next
        }
        println("test19: --------------$sb")
    }

    fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {
        val pre = ListNode(0).apply {
            this.next = head
        }
        var fastNode: ListNode? = pre
        var slowNode: ListNode? = pre
        for (i in 0..n) {
            fastNode = fastNode?.next
        }
        while (fastNode != null) {
            slowNode = slowNode?.next
            fastNode = fastNode.next
        }
        slowNode?.next = slowNode?.next?.next
        return pre.next
    }

    @Test
    fun test20() {
        val test = isValid("()")
        println("test20: --------------$test")
    }

    /**
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
     * 有效字符串需满足：
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * 每个右括号都有一个对应的相同类型的左括号。
     */
    fun isValid(s: String): Boolean {
        val stack = LinkedList<Char>()
        for (char in s) {
            when {
                char == '(' -> {
                    stack.push(')')
                }
                char == '{' -> {
                    stack.push('}')
                }
                char == '[' -> {
                    stack.push(']')
                }
                stack.isEmpty() || stack.pop() != char -> {
                    return false
                }
            }
        }
        return stack.isEmpty()
    }

    @Test
    fun test21() {
        var li = ListNode(0)
        var li1 = ListNode(1)
        var li2 = ListNode(2)
        var li3 = ListNode(3)
        var li4 = ListNode(4)
        li.next = li1
        li1.next = li2
        li2.next = li3
        li3.next = li4


        var li5 = ListNode(5)
        var li6 = ListNode(6)
        var li7 = ListNode(7)
        var li8 = ListNode(8)
        var li9 = ListNode(9)
        li5.next = li6
        li6.next = li7
        li7.next = li8
        li8.next = li9
        val sb = StringBuilder()
        var test = mergeTwoLists(li, li5)
        while (test != null) {
            sb.append("${test.`val`},")
            test = test.next
        }

        println("test21: --------------$sb")
    }

    /**
     * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
     */
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        return when {
            list1 == null -> list2
            list2 == null -> list1
            list1.`val` < list2.`val` -> {
                list1.next = mergeTwoLists(list1.next, list2)
                list1
            }
            else -> {
                list2.next = mergeTwoLists(list1, list2.next)
                list2
            }
        }
    }

    @Test
    fun test855() {
        val nums1 = intArrayOf(1, 1, 3, 2)
        val nums2 = intArrayOf(2, 3)
        val nums3 = intArrayOf(3)
        val resultList = twoOutOfThree(nums1, nums2, nums3)
        println("test855: --------------$resultList")
    }

//    fun seat(): Int {
//
//    }
//
//    fun leave(p: Int) {
//
//    }

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