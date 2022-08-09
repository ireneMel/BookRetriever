package com.example.bookretriever.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

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
        auth = Firebase.auth

        binding.register.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment()).commit()
        }

        binding.loginButton.setOnClickListener {
            View.OnClickListener { loginUser() }
        }

        binding.forgotPassword.setOnClickListener {
            val resetMail = EditText(requireContext())
            val passwordResetDialog = AlertDialog.Builder(requireContext())
            passwordResetDialog.setTitle("Reset password")
                .setMessage("Enter your email to receive reset link")
                .setView(resetMail)
                .setPositiveButton("Yes") { _, _ ->
                    auth.sendPasswordResetEmail(resetMail.text.toString()).addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Reset link was sent to your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Error occured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .setNegativeButton("No") { _, _ -> }.create().show()
        }
    }

    private fun loginUser() {
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailLogin.error = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLogin.error = "Provide valid email"
            binding.emailLogin.requestFocus()
        } else if (password.length < 0) {
            binding.passwordLogin.error = "Password must contain at least 6 characters"
        }

        binding.progressbarLogin.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MainFragment())
            } else {
                Toast.makeText(
                    requireContext(),
                    "There is no account with such email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}