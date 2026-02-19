package dev.etino.fcshared.screens.menza.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.menza.models.MenzaLocation
import dev.etino.fcshared.menza.models.MealTime
import dev.etino.fcshared.menza.models.MeniSpecial
import dev.etino.fcshared.menza.models.Menu
import dev.etino.fcshared.menza.models.Menza
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.beverage
import fesb_companion_shared.composeapp.generated.resources.dessert
import fesb_companion_shared.composeapp.generated.resources.dinner_title
import fesb_companion_shared.composeapp.generated.resources.lunch_title
import fesb_companion_shared.composeapp.generated.resources.main_course
import fesb_companion_shared.composeapp.generated.resources.meals_by_choice
import fesb_companion_shared.composeapp.generated.resources.meni_price
import fesb_companion_shared.composeapp.generated.resources.menza_no_data
import fesb_companion_shared.composeapp.generated.resources.no_data_icon
import fesb_companion_shared.composeapp.generated.resources.page_not_found
import fesb_companion_shared.composeapp.generated.resources.salad
import fesb_companion_shared.composeapp.generated.resources.side_dish
import fesb_companion_shared.composeapp.generated.resources.soup
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeniComposeIksica(meni: Pair<MenzaLocation, Menza?>?) {
    val menzaLocation = meni?.first
    val menies = meni?.second
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp)
            .heightIn(min = screenHeight.times(0.7f))
            .fillMaxWidth()
    ) {
        Text(
            text = menzaLocation?.name ?: "",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 0.dp)
        )
        Text(
            text = menzaLocation?.address ?: "",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 24.dp)
        )
        if (meni?.second?.dateFetched == meni?.second?.datePosted) {
            menies?.let { MealTimeContent(it, MealTime.LUNCH) }
            menies?.let { MealTimeContent(it, MealTime.DINNER) }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.no_data_icon),
                    contentDescription = stringResource(Res.string.page_not_found),
                    modifier = Modifier
                        .padding(12.dp, 80.dp, 12.dp, 12.dp)
                        .size(80.dp)
                )
                Text(stringResource(Res.string.menza_no_data))
            }
        }
    }
}

@Composable
fun MealTimeContent(menza: Menza, mealTime: MealTime) {

    val menies = if (mealTime == MealTime.LUNCH) menza.meniesLunch else menza.meniesDinner
    val meniesSpecial = if (mealTime == MealTime.LUNCH) menza.meniesSpecialLunch else menza.meniesSpecialDinner

    if (menies.isEmpty() && meniesSpecial.isEmpty()) return

    val cornerRadius = 15.dp
    val mealModifier = Modifier
        .padding(bottom = 16.dp)
        .clip(RoundedCornerShape(cornerRadius))
        .background(MaterialTheme.colorScheme.background)
        .padding(24.dp, 8.dp)
        .fillMaxWidth()
    Row(
        Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (mealTime == MealTime.LUNCH) stringResource(Res.string.lunch_title)
            else stringResource(Res.string.dinner_title),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
    menies.forEach { MeniItem(it, mealModifier) }
    MeniSpecialIksica(meniesSpecial, mealModifier)
}

@Composable
fun MeniItem(meni: Menu, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = modifier
    ) {
        Text(
            text = meni.name,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        val soupOrTea = if (meni.mealTime == MealTime.LUNCH) stringResource(Res.string.soup)
        else stringResource(Res.string.beverage)

        MeniTextIksica(meni.soupOrTea, soupOrTea)
        MeniTextIksica(meni.mainCourse, stringResource(Res.string.main_course))
        MeniTextIksica(meni.sideDish, stringResource(Res.string.side_dish))
        MeniTextIksica(meni.salad, stringResource(Res.string.salad))
        MeniTextIksica(meni.dessert, stringResource(Res.string.dessert), false)
        if (meni.price != "") {
            Text(
                text = stringResource(Res.string.meni_price, meni.price),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
        } else {
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
fun MeniSpecialIksica(meni: List<MeniSpecial>, modifier: Modifier) {
    if (meni.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = modifier
        ) {
            Text(
                text = stringResource(Res.string.meals_by_choice),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp),
            )
            meni.forEachIndexed { index, it ->
                if (index != 0) HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = it.meal,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(0.8f),
                    )
                    Text(
                        text = stringResource(Res.string.meni_price, it.price),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(0.2f),
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun MeniTextIksica(text: String, type: String, divider: Boolean = true) {
    if (text.isNotEmpty()) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = type,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.padding(vertical = 5.dp)
            )
            Text(
                text = text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 5.dp)
            )
        }
        if (divider) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        }
    }
}