package ru.alexp0111.onigoing.ui.profile.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.databinding.FragmentAccountBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.MenuConfigurator
import ru.alexp0111.onigoing.utils.account.ProfileManager
import ru.alexp0111.onigoing.utils.snack
import javax.inject.Inject

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    @Inject
    lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cvLogIn.setOnClickListener {
                if (!inputIsCorrect()) {
                    snack("Input is incorrect")
                    return@setOnClickListener
                }
                val isLoggedInSuccessfully = viewModel.tryToLogIn(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                )
                (requireActivity() as MenuConfigurator).updateMenuStateIfNeeded()
                if (!isLoggedInSuccessfully) {
                    snack("Email or password is incorrect")
                }
            }
            cvRegistration.setOnClickListener {
                snack("In dev..")
            }
            ivHide.setOnClickListener { hidePasswordIfNeeded(false) }
            ivShow.setOnClickListener { hidePasswordIfNeeded(true) }
        }
    }

    private fun hidePasswordIfNeeded(shouldHide: Boolean) {
        binding.apply {
            ivHide.isVisible = shouldHide
            ivShow.isVisible = !shouldHide
            etPassword.transformationMethod = if (shouldHide) {
                PasswordTransformationMethod()
            } else null
            etPassword.setSelection(etPassword.length())
        }
    }

    private fun inputIsCorrect(): Boolean {
        binding.apply {
            return !etEmail.text.isNullOrEmpty()
                    && !etPassword.text.isNullOrEmpty()
                    && etPassword.text.length >= ProfileManager.MIN_PASSWORD_SIZE
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}