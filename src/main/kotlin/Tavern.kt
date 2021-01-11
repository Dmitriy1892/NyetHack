import java.io.File
import kotlin.math.roundToInt

const val TAVERN_NAME = "Taernyl's Folly"

val patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt").readText().split("\n")
val patronGolg = mutableMapOf<String, Double>()


fun main(args: Array<String>) {
//    menuPrinted(menuList)
    if (patronList.contains("Eli")) {
        println("The tavern master says: Eli's in the back playing cards.")
    } else {
        println("The tavern master says: Eli isn't here")
    }

    if (patronList.containsAll(listOf("Sophie", "Mordoc"))) {
        println("The tavern master says: Yea, they're seated by the stew kettle.")
    } else {
        println("The tavern master says: Nay, they departed hours ago.")
    }

    (0..9).forEach {
        val first  =patronList.shuffled().first()
        val last = lastName.shuffled().first()
        val name = "$first $last"
        uniquePatrons += name
    }

    uniquePatrons.forEach {
        patronGolg[it] = 6.0
    }

    var orderCount = 0
    while (orderCount <= 9) {
        placeOrder(uniquePatrons.shuffled().first(), menuList.shuffled().first().replace("\r", ""))
        orderCount++
    }

    displayPatronBalances()
}

//fun menuPrinted(menuList: List<String>) {
//    println("*** Welcome to Taernyl's Folly ***")
//    var i = 0
//    var (_, name, price) = menuList[0].replace("\t", "").split(",")
//    var longestString = (name + price).count() + 10 //считает длину строки без типа + добавлет 10 точек
//    while (i <menuList.count()) {
//        var (_, name1, price1) = menuList[i].replace("\t", "").split(",")
//        var temp = (name1 + price1).count() + 10
//        if ( temp > longestString) longestString = temp
//        i++
//    }
//
//    i = 0
//    while (i < menuList.count()) {
//        var (_, name1, price1) = menuList[i].replace("\t", "").split(",")
//        var temp = (name1 + price1).count() + 10
//        var dotsCount = longestString - temp + 10
//        print(name1)
//        var j = 0
//        while (j < dotsCount) {
//            print(".")
//            j++
//        }
//        j = 0
//        print(price1)
//        println("")
//        i++
//    }
//}

private fun displayPatronBalances() {
    patronGolg.forEach { patron, balance ->
        println("$patron, balance: ${"%.2f".format(balance)}")
    }
}

fun performPurchase(price: Double, patronName: String) {
    val totalPurse = patronGolg.getValue(patronName)
    patronGolg[patronName] = totalPurse - price
}

private fun toDragonSpeak(phrase: String) =
        phrase.replace(Regex("[aeiou]")) {
            when (it.value) {
                "a" -> "4"
                "e" -> "3"
                "i" -> "1"
                "o" -> "0"
                "u" -> "|_|"
                else -> it.value
            }
        }

private fun placeOrder(patronName: String, menuData: String) {
    val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
    val tavernMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)
    println("$patronName speaks with $tavernMaster about their order.")

    val (type, name, price) = menuData.split(',')
    val message = "$patronName buys a $name ($type) for $price."
    println(message)

    performPurchase(price.toDouble(), patronName)

    val phrase = if (name == "Dragon's Breath") {
        "$patronName exclaims: ${toDragonSpeak("Ah, delicious $name!")}"
    } else {
        "$patronName says: Thanks for the $name."
    }
    println(phrase)
}