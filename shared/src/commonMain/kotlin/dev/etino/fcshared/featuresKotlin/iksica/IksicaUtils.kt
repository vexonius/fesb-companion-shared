package dev.etino.fcshared.featuresKotlin.iksica

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.featuresKotlin.iksica.models.Receipt
import dev.etino.fcshared.featuresKotlin.iksica.models.ReceiptItem
import dev.etino.fcshared.featuresKotlin.iksica.models.StudentData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

fun parseStudentInfo(body: String): StudentData {
    val doc = Ksoup.parse(body)

    val image = doc.selectFirst(".slikastud")?.attr("src")
    val user = doc.selectFirst(".card-title")?.text()
    val number = doc.selectFirst("td:contains(Izdana)")?.parent()?.selectFirst("td")?.text()
    val oib = doc.selectFirst("span:contains(OIB:)")?.nextSibling()?.toString()?.trim()
    val jmbag = doc.selectFirst("span:contains(JMBAG:)")?.nextSibling()?.toString()?.trim()
    doc.selectFirst("span:contains(Nadležna ustanova:)")?.nextSibling().toString()
    val rightsLevel = doc.selectFirst("p:contains(RAZINA PRAVA)")?.parent()?.selectFirst("u")?.text().toString()
    val rightsFrom = doc.selectFirst("span:contains(Prava od datuma:)")?.nextSibling().toString()
    val rightsTo = doc.selectFirst("span:contains(Prava do datuma:)")?.nextSibling().toString()
    val balance =
        doc.selectFirst("p:contains(RASPOLOŽIVI SALDO)")
            ?.parent()?.lastElementChild()?.text()
    val spentToday =
        doc.selectFirst("p:contains(POTROŠENO DANAS)")?.parent()?.lastElementChild()?.text()
    val dailySupport =
        doc.selectFirst("p:contains(DNEVNA POTPORA)")
            ?.parent()?.lastElementChild()?.text()

    val studentData = StudentData(
        imageUrl = image,
        nameSurname = user ?: "",
        rightsLevel = rightsLevel,
        dailySupport = dailySupport.toCentsOrNull() ?: 0,
        oib = oib ?: "",
        jmbag = jmbag ?: "",
        cardNumber = number ?: "",
        rightsFrom = rightsFrom,
        rightsTo = rightsTo,
        balanceInCents = balance.toCentsOrNull() ?: 0,
        spentTodayInCents = spentToday.toCentsOrNull() ?: 0,
    )
    return studentData
}

fun String?.toCentsOrNull(): Int? {
    if (this == null) return null
    var temp = this.substringBefore(" €")
    if (temp.substringAfter(",").length == 1)
        temp += '0'
    return temp.filter { it.isDigit() }.toIntOrNull()
}

fun parseRacuni(doc: String): List<Receipt> {
    val racuni = mutableListOf<Receipt>()
    val table = Ksoup.parse(doc).selectFirst("table")
    val rows = table?.select("tr")
    rows?.forEach { row ->
        val cols = row.select("td")
        val formatter = LocalDate.Format {
            day()
            char('.')
            monthNumber()
            char('.')
            year()
        }

        val date = try {
            formatter.parse(cols[1].text())
        } catch (e: Exception) {
            LocalDate(1, 1, 1) // replacement for MIN
        }
        if (cols.size >= 6) {
            racuni.add(
                Receipt(
                    cols[0].text(),
                    date,
                    cols[1].text(),
                    cols[2].text(),
                    cols[3].text().dropLast(2).toCentsOrNull() ?: 0,
                    cols[4].text().dropLast(2).toCentsOrNull() ?: 0,
                    ((cols[3].text().dropLast(2).toCentsOrNull() ?: 0) - (cols[4].text().dropLast(2).toCentsOrNull()
                        ?: 0)),
                    cols[5].text(),
                    cols[6].selectFirst("a")?.attr("href") ?: ""
                )
            )
        }

    }
    val timeFormatter = LocalTime.Format {
        hour()
        char(':')
        minute()
    }

    return racuni
        .sortedByDescending {
            runCatching {
                timeFormatter.parse(it.time)
            }.getOrElse {
                LocalTime(0, 0)
            }
        }
        .sortedByDescending { it.date }
}

fun parseDetaljeRacuna(doc: String): MutableList<ReceiptItem> {
    val detaljiRacuna = mutableListOf<ReceiptItem>()
    val table = Ksoup.parse(doc).selectFirst(".table-responsive")
    val rows = table?.selectFirst("tbody")?.select("tr")
    rows?.forEach { row ->
        val cols = row.select("td")
        val item = ReceiptItem(
            cols[0].text(),
            cols[1].text().toIntOrNull() ?: 0,
            cols[2].text().toCentsOrNull() ?: 0,
            cols[3].text().toCentsOrNull() ?: 0,
            cols[4].text().toCentsOrNull() ?: 0
        )
        if (!detaljiRacuna.any { it.articleName == item.articleName && it.priceInCents == item.priceInCents }) {
            detaljiRacuna.add(item)
        } else {
            val index = detaljiRacuna.indexOf(
                detaljiRacuna.first {
                    it.articleName == item.articleName && it.priceInCents == item.priceInCents
                })
            detaljiRacuna[index].amount = (detaljiRacuna[index].amount + item.amount)
        }
    }
    return detaljiRacuna
}