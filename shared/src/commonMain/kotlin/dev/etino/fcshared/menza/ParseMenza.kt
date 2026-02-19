package dev.etino.fcshared.menza

import dev.etino.fcshared.menza.models.MealTime
import dev.etino.fcshared.menza.models.MeniSpecial
import dev.etino.fcshared.menza.models.Menu
import dev.etino.fcshared.menza.models.Menza
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


fun parseMenza(jsonString: String): Menza? {
    try {
        val values = Json.parseToJsonElement(jsonString).jsonObject["values"]?.jsonArray?.map { listItems ->
            listItems.jsonArray.map { string ->
                string.jsonPrimitive.content
            }
        }

        val menza = Menza(
            name = values?.get(0)?.get(2) ?: "",
            datePosted = values?.get(0)?.get(3) ?: "",
            dateFetched = values?.get(0)?.get(4) ?: "",
            meniesLunch = mutableListOf(),
            meniesSpecialLunch = mutableListOf(),
            meniesDinner = mutableListOf(),
            meniesSpecialDinner = mutableListOf(),
        )
        values?.forEach {
            if (it.isEmpty()) return@forEach
            val type = it.getOrNull(0) ?: return@forEach
            val mealTime = mealTimeTest(type)

            if (type.contains("MENI")) {
                val meni = Menu(
                    type = type,
                    mealTime = mealTime,
                    name = it.getOrNull(1) ?: "",
                    soupOrTea = it.getOrNull(2) ?: "",
                    mainCourse = it.getOrNull(3) ?: "",
                    sideDish = it.getOrNull(4) ?: "",
                    salad = it.getOrNull(5) ?: "",
                    dessert = it.getOrNull(6) ?: "",
                    price = checkAndFixPrice(it.getOrNull(7) ?: "")
                )
                if (!meni.isNotEmpty()) return@forEach

                if (mealTime == MealTime.LUNCH) menza.meniesLunch.add(meni)
                else if (mealTime == MealTime.DINNER) menza.meniesDinner.add(meni)
            } else if (type.contains("JELO PO IZBORU") && it.size >= 2) {
                for (i in 1 until it.size) {
                    val item = it.getOrNull(i) ?: ""
                    val name = item.split(Regex(" (?=\\d)")).firstOrNull() ?: ""
                    if (!name.isNotEmpty()) return@forEach

                    val price = checkAndFixPrice(item.split(" ").lastOrNull() ?: "")
                    val meniSpecial = MeniSpecial(type = type, mealTime = mealTime, meal = name, price = price)

                    if (mealTime == MealTime.LUNCH) menza.meniesSpecialLunch.add(meniSpecial)
                    else if (mealTime == MealTime.DINNER) menza.meniesSpecialDinner.add(meniSpecial)
                }
            }
        }
        return menza
    } catch (e: Exception) {
        //Log.d("MenzaParse", e.toString())
    }
    return null
}

fun checkAndFixPrice(pricee: String): String {
    var price = pricee
    if (!Regex("^[0-9,]+$").matches(price)) {
        price = ""
    }
    when (price.substringAfter(",", "xxxx").length) {
        1 -> price += "0"
        0 -> price += "00"
        else -> {}
    }
    return price
}

fun mealTimeTest(title: String) : MealTime {
    val firstLetter = title.getOrNull(0) ?: return MealTime.LUNCH
    return if (firstLetter == 'R') MealTime.LUNCH else if (firstLetter == 'V') MealTime.DINNER else MealTime.LUNCH
}
