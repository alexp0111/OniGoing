package ru.alexp0111.onigoing.ui.lists.page.utils

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.ui.lists.page.adapters.StarColors

class MarkPainter(
    private val activity: FragmentActivity,
) {

    fun paintStarIfNecessary(view: View, item: UserWatchingAnime, index: Int) {
        paintView(view, resolveColor(index, item))
    }

    private fun resolveColor(index: Int, item: UserWatchingAnime): StarColors {
        return if (index + 1 <= item.mark) {
            StarColors.MAIN_ACCENT
        } else {
            StarColors.WHITE
        }
    }

    private fun paintView(view: View, starColor: StarColors) {
        val typedValue = TypedValue().apply {
            activity.theme.resolveAttribute(
                R.attr.main_accent_color,
                this,
                true,
            )
        }
        val color = when (starColor) {
            StarColors.MAIN_ACCENT -> typedValue.data
            StarColors.WHITE -> activity.getColor(R.color.white)
        }
        (view as ImageView).setColorFilter(color)
    }

    fun getMarkAsResource(item: UserWatchingAnime): Int {
        return when (item.mark) {
            1 -> R.drawable.stars_h
            2 -> R.drawable.stars_1
            3 -> R.drawable.stars_1h
            4 -> R.drawable.stars_2
            5 -> R.drawable.stars_2h
            6 -> R.drawable.stars_3
            7 -> R.drawable.stars_3h
            8 -> R.drawable.stars_4
            9 -> R.drawable.stars_4h
            10 -> R.drawable.stars_5
            else -> R.drawable.stars_empty
        }
    }
}