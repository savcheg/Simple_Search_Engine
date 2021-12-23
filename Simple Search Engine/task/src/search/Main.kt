package search

import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    start(args[1])
}

object SearchEngine {
    var result = mutableListOf<String>()
    var database = mutableListOf<String>()
    var databaseIndex = mutableMapOf<String, ArrayList<Int>>()
}

class Index {
    private val wordIndex = SearchEngine.databaseIndex
    private val stringData = SearchEngine.database

    fun words() {
        for (line in stringData) {
            for (word in line.split(" ")) {
                if (wordIndex.keys.any { it.uppercase(Locale.getDefault()) == word.uppercase(Locale.getDefault()) }) {
                    wordIndex[word]?.add(stringData.indexOf(line))
                } else {
                    wordIndex[word] = ArrayList()
                    wordIndex[word]?.add(stringData.indexOf(line))
                }
            }
        }
    }
}

fun start(path: String) {
    val file = File(path)
    if (!file!!.exists()) exitProcess(2)
    file!!.forEachLine { SearchEngine.database.add(it) }
    Index().words()

    useMenu()
}

fun useMenu() {
    SearchEngine.result.add(
        """
        
        === Menu ===
        1. Find a person
        2. Print all people
        0. Exit
    """.trimIndent()
    )
    println(SearchEngine.result.joinToString("") + "\n")
    SearchEngine.result.clear()

    var input = readLine()!!.removePrefix("> ")

    when (input) {
        "1" -> chooseStrategy()
        "2" -> dataPrint()
        "0" -> {
            println("\nBye!")
            exitProcess(1)
        }
        else -> {
            println("\nIncorrect option! Try again.")
            useMenu()
        }
    }
    useMenu()
}

fun chooseStrategy() {
    println("\nSelect a matching strategy: ALL, ANY, NONE")
    var input = readLine()!!.removePrefix("> ")
    when (input) {
        "ANY" -> anyQuerySearch()
        "ALL" -> allQuerySearch()
        "NONE" -> noneQuerySearch()
        else -> println("No such strategy")
    }
}

fun findPerson() {
    println("\nEnter a name or email to search all suitable people.")
    var input = readLine()!!.removePrefix("> ")

    for (str in SearchEngine.database)
        if (str.contains(input, true)) SearchEngine.result.add(str + "\n")

    if (SearchEngine.result.isNotEmpty()) println(SearchEngine.result.joinToString(""))
    else println("\nNo matching people found.")

    SearchEngine.result.clear()
}

fun dataPrint() {
    println("\n=== List of people ===\n${SearchEngine.database.joinToString("\n")}")
}

fun anyQuerySearch() { // option 1
    var count = java.util.ArrayList<Int>()
    val target: String = inputTarget()
    val targetList = target.split(" ").toMutableList()
    var totalCount = 0
    var check = false

    // ********** single words in array **********
    for (target in targetList) {
        var checkInput: MutableList<String>
        for (i in SearchEngine.database) {
            checkInput = i.split(" ").map { it.uppercase() }.toMutableList()
            for (h in checkInput) {
                if (checkInput.contains(target.uppercase()) || h.indexOf(target.uppercase()) > -1)
                    check = true
            }
        }
        if (check) {
            SearchEngine.databaseIndex.forEach {
                if (it.key.equals(target, true)) count =
                    SearchEngine.databaseIndex[it.key]!!
            }
            for (i in count) {
                SearchEngine.result.add(SearchEngine.database[i])
            }
//         println( OpenSearchEngine.result.distinct().joinToString("\n"))
        }
        check = false
        totalCount += count.count()
    }
    if (totalCount == 1) SearchEngine.result.add(0, "${totalCount} Person found:")
    else SearchEngine.result.add(0, "${totalCount} Persons found:")
    println(SearchEngine.result.joinToString("\n"))
    SearchEngine.result.clear()
}

fun allQuerySearch() { // option 1
    var count = java.util.ArrayList<Int>()
    val target: String = inputTarget()
    val targetList = target.split(" ").toMutableList()
    var totalCount = 0
    var check = false

    // ********** single words in array **********
    for (target in targetList) {
        var checkInput: MutableList<String>
        for (i in SearchEngine.database) {
            checkInput = i.split(" ").map { it.uppercase() }.toMutableList()
            for (h in checkInput) {
                if (checkInput.contains(target.uppercase()) || h.indexOf(target.uppercase()) > -1)
                    check = true
            }
        }
        if (check) {
            SearchEngine.databaseIndex.forEach {
                if (it.key.equals(target, true)) count =
                    SearchEngine.databaseIndex[it.key]!!
            }
            for (i in count) {
                SearchEngine.result.add(SearchEngine.database[i])
            }
//         println( OpenSearchEngine.result.distinct().joinToString("\n"))
        }
        check = false
        totalCount += count.count()
    }
    if (totalCount == 1) SearchEngine.result.add(0, "${totalCount} Person found:")
    else SearchEngine.result.add(0, "${totalCount} Persons found:")
    println(SearchEngine.result.joinToString("\n"))
    SearchEngine.result.clear()
}

fun noneQuerySearch() { // option 1
    var count = java.util.ArrayList<Int>()
    val target: String = inputTarget()
    val targetList = target.split(" ").toMutableList()
    var totalCount = 0
    var check = false

    SearchEngine.result.addAll(SearchEngine.database)

    // ********** single words in array **********
    for (target in targetList) {
        var checkInput: MutableList<String>
        for (i in SearchEngine.database) {
            checkInput = i.split(" ").map { it.uppercase() }.toMutableList()
            for (h in checkInput) {
                if (checkInput.contains(target.uppercase()) || h.indexOf(target.uppercase()) > -1)
                    check = true
            }
        }
        if (check) {
            SearchEngine.databaseIndex.forEach {
                if (it.key.equals(target, true)) count =
                    SearchEngine.databaseIndex[it.key]!!
            }
            for (i in count) {
                SearchEngine.result.remove(SearchEngine.database[i])
            }
//         println( OpenSearchEngine.result.distinct().joinToString("\n"))
        }
        check = false
        totalCount += count.count()
    }
    if (totalCount == 1) SearchEngine.result.add(0, "${totalCount} Person found:")
    else SearchEngine.result.add(0, "${totalCount} Persons found:")
    println(SearchEngine.result.joinToString("\n"))
    SearchEngine.result.clear()
}

fun inputTarget(): String {
    println("\nEnter a name or email to search all suitable people.")
    return readLine()!!.removePrefix(">").removePrefix(" ")
}