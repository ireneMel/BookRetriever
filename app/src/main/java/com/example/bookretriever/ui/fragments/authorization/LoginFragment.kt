package com.example.bookretriever.ui.fragments.authorization

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentLoginBinding
import com.example.bookretriever.ui.fragments.MainFragment
import com.example.bookretriever.ui.viewmodels.authorization.LoginErrorEvent
import com.example.bookretriever.ui.viewmodels.authorization.LoginUserState
import com.example.bookretriever.ui.viewmodels.authorization.LoginViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class LoginFragment : Fragment() {

    private val TAG = "Login Fragment"

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

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
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RegisterFragment()).commit()
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

        lifecycleScope.launchWhenStarted {
            viewModel.state.onEach {
                when (it) {
                    LoginUserState.Verified -> {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, MainFragment()).commit()
                    }
                    LoginUserState.Unknown -> {

                    }
                    LoginUserState.Loading -> {
                        binding.progressbarLogin.visibility = View.VISIBLE
                    }

                    LoginUserState.Error -> {

                    }
                }
            }.collect()
        }
    }

    private fun loginUser() {
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString().trim()

        if (isDataValid(email, password)) {
            Log.d(TAG, "loginUser: data valid")
            viewModel.login(email, password)

            Log.d(TAG, "loginUser: logged")

            Log.d(TAG, "loginUser: state = ${viewModel.state.value}")

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

                if (isSuccessful) {
                    makeToast("Reset link was sent to your email")
                } else
                    makeToast("Error occurred")
            }.setNegativeButton("Close") { _, _ -> }.create().show()
    }

    private fun makeToast(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}