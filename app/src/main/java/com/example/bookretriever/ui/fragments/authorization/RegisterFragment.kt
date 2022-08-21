package com.example.bookretriever.ui.fragments.authorization

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentRegisterBinding
import com.example.bookretriever.ui.fragments.MainFragment
import com.example.bookretriever.ui.viewmodels.authorization.RegisterErrorEvent
import com.example.bookretriever.ui.viewmodels.authorization.RegisterViewModel
import com.example.bookretriever.utils.GeneralFragment
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class RegisterFragment : GeneralFragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.goToLogin.setOnClickListener {
            goToFragment(LoginFragment())
        }

        binding.registerButton.setOnClickListener {
            registerUser()

            lifecycleScope.launchWhenStarted {
                viewModel.isComplete.filter { it }.first()
                goToFragment(LoginFragment())
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorEventFlow.collect { event ->
                when (event) {
                    is RegisterErrorEvent.ExistingUserMessage -> {
                        makeToast("User with this email already exists.\nLog in")
                        goToFragment(LoginFragment())
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        // if the current user is logged in show MainFragment
        if (viewModel.isComplete.value)
            goToFragment(MainFragment())
    }

    private fun registerUser() {
        val name = binding.nameRegister.text.toString().trim()
        val email = binding.emailRegister.text.toString().trim()
        val password = binding.passwordRegister.text.toString().trim()

        if (dataValid(name, email, password)) register()
    }

    private fun register() {
        val name = binding.nameRegister.text.toString().trim()
        val email = binding.emailRegister.text.toString().trim()
        val password = binding.passwordRegister.text.toString().trim()

        binding.progressbarLogin.visibility = View.VISIBLE
        viewModel.register(name, email, password)

        if (viewModel.isComplete.value) {
            makeToast("You have been successfully registered")
        }
    }

    private fun dataValid(name: String, email: String, password: String): Boolean {
        with(binding) {
            if (name.isEmpty()) {
                nameRegister.error = "Name is required"
                nameRegister.requestFocus()
                return false
            }
            if (email.isEmpty()) {
                emailRegister.error = "Email is required"
                emailRegister.requestFocus()
                return false
            }
            if (password.length < 6) {
                emailRegister.error = "Password must contain at least 6 characters"
                passwordRegister.requestFocus()
                return false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailRegister.error = "Provide valid email"
                emailRegister.requestFocus()
                return false
            }
        }
        return true
    }
}