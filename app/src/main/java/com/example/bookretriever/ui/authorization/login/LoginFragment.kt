package com.example.bookretriever.ui.authorization.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentLoginBinding
import com.example.bookretriever.utils.GeneralFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class LoginFragment : GeneralFragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    init {
        lifecycleScope.launchWhenStarted {
            viewModel.state.onEach {
                when (it) {
                    LoginUserState.Verified -> {
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                    LoginUserState.Loading -> {
                        binding.progressbarLogin.visibility = View.VISIBLE
                    }
                    LoginUserState.Unknown -> {
                        binding.progressbarLogin.visibility = View.GONE
                    }
                    LoginUserState.Error -> {}
                }
            }.collect()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        with(binding) {
            register.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            loginButton.setOnClickListener { loginUser() }
            forgotPassword.setOnClickListener { resetPassword() }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorEventFlow.collect { event ->
                when (event) {
                    is LoginErrorEvent.ErrorMessage -> {
                        makeToast(event.message)
                    }
                }
                binding.progressbarLogin.visibility = View.GONE
            }
        }
    }

    private fun loginUser() {
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString().trim()

        if (isDataValid(email, password)) {
            viewModel.login(email, password)
            Log.d("LoginFragment", "loginUser: state = ${viewModel.state.value}")
        }
    }

    private fun isDataValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailLogin.error = "Email is required"
            binding.emailLogin.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLogin.error = "Provide valid email"
            binding.emailLogin.requestFocus()
            return false
        } else if (password.length < 0) {
            binding.passwordLogin.error = "Password must contain at least 6 characters"
            binding.passwordLogin.requestFocus()
            return false
        }
        return true
    }

    private fun resetPassword() {
        val resetMail = EditText(requireContext())
        val passwordResetDialog = AlertDialog.Builder(requireContext())

        passwordResetDialog.setTitle("Reset password")
            .setMessage("Enter your email to receive reset link")
            .setView(resetMail)
            .setPositiveButton("Send") { _, _ ->
                val isSuccessful = viewModel.resetPassword(resetMail.text.toString())
                if (isSuccessful) makeToast("Reset link was sent to your email.")
                else makeToast("Error occurred. Could not send reset link.")
            }.setNegativeButton("Close") { _, _ -> }.create().show()
    }
}