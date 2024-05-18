package ru.alexp0111.onigoing.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.PieData
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentProfileBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import javax.inject.Inject


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        viewModel.loadUsersStatistics()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        setUpPieChart()
        binding.apply {
            cvSettings.setOnClickListener {
                viewModel.goToSettingsFragment()
            }
        }
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state: UiState ->
                        handleState(state)
                    }
                }
            }
        }
    }

    private fun handleState(state: UiState) {
        binding.apply {
            txtTotalEpisodes.text = (state.totalEpisodes ?: "0").toString()
            txtTotalTitles.text = (state.totalTitles ?: "0").toString()
            state.pieData?.let {
                pieChartStatistics.data = it
                setUpPieChartStatistics(it)
            }
        }
    }

    private fun setUpPieChartStatistics(it: PieData) {
        val statisticsViews = getPieStatisticsViews()
        val viewsSources = getPieStatisticsResources()
        for (indexOfView in statisticsViews.indices) {
            statisticsViews[indexOfView].text = resources.getString(
                viewsSources[indexOfView],
                getValueFromPie(it, indexOfView)
            )
        }
    }

    private fun getPieStatisticsResources(): Array<Int> {
        return arrayOf(
            R.string.status_watched,
            R.string.status_actual,
            R.string.status_planned,
            R.string.status_stopped,
        )
    }

    private fun getPieStatisticsViews(): Array<TextView> {
        binding.apply {
            return arrayOf(
                txtWatchedStatus, txtCurWatchingStatus, txtPlannedStatus, txtStoppedStatus,
            )
        }
    }

    private fun getValueFromPie(data: PieData, index: Int): String {
        return data.dataSet.getEntryForIndex(index).value.toInt().toString()
    }

    private fun setUpPieChart() {
        binding.pieChartStatistics.apply {
            description.isEnabled = false
            holeRadius = 48f
            transparentCircleRadius = 0f
            legend.isEnabled = false
            setEntryLabelTextSize(32f)
            setHoleColor(R.color.dark)
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}