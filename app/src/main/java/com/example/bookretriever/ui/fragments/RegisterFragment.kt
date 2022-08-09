package com.example.bookretriever.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentRegisterBinding
import com.example.bookretriever.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    private val databaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://bookretriever-80955-default-rtdb.firebaseio.com/users")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        auth = Firebase.auth

        if (auth.currentUser != null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    MainFragment.newInstance("data about the user?", "2")
                )
        }

        binding.registerButton.setOnClickListener {
            registerUser()
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
//        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        if (currentUser != null) reload()
    }

    private fun registerUser() {
        val name = binding.nameRegister.text.toString().trim()
        val email = binding.emailRegister.text.toString().trim()
        val password = binding.passwordRegister.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameRegister.error = "Name is required"
            binding.nameRegister.requestFocus()
        } else if (email.isEmpty()) {
            binding.emailRegister.error = "Email is required"
            binding.emailRegister.requestFocus()
        } else if (password.length < 6) {
            binding.emailRegister.error = "Password must contain at least 6 characters"
            binding.passwordRegister.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailRegister.error = "Provide valid email"
            binding.emailRegister.requestFocus()
        } else {
            loginUser()
        }
    }

    private fun loginUser() {
        val name = binding.nameRegister.text.toString().trim()
        val email = binding.emailRegister.text.toString().trim()
        val password = binding.passwordRegister.text.toString().trim()

        Log.d("TAG", "loginUser: $email $password")

        binding.progressbarLogin.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->  // <<< CHANGE WAS MADE HERE !
                binding.progressbarLogin.visibility = View.GONE
                if (task.isSuccessful) {
                    //send verification link
                    val firebaseUser: FirebaseUser? = auth.currentUser
                    firebaseUser?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Verification link has been sent",
                            Toast.LENGTH_SHORT
                        ).show()
                    }?.addOnFailureListener {
                        Log.d("TAG", "loginUser:onFailure:email not send " + it.message)
                    }

                    val user = User(name, email, password)
                    databaseReference
//                        .child(email)
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("user")
                        .setValue(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, MainFragment()).commit()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Error saving to database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    // Sign in success, update UI with the signed-in user's information
//                    val user = auth.currentUser

                    Toast.makeText(
                        requireContext(),
                        "Registered successfully",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI()
                } else {
                    binding.progressbarLogin.visibility = View.GONE
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure = ${task.exception!!.message!!}")

                    Toast.makeText(
                        requireContext(),
                        "Error registering user",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI()
                }
            }
    }
}