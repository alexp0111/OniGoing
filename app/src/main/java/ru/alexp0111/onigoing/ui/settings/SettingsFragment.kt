package ru.alexp0111.onigoing.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.databinding.FragmentSettingsBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.base.BackPressable
import javax.inject.Inject

class SettingsFragment : Fragment(), BackPressable {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cvBack.setOnClickListener {
                settingsViewModel.onBackPressed()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        settingsViewModel.onBackPressed()
        return true
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}