package com.example.bookretriever.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookretriever.R
import com.example.bookretriever.adapters.BookAdapter
import com.example.bookretriever.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bookAdapter: BookAdapter

    private lateinit var binding: FragmentMainBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        val user: FirebaseUser? = auth.currentUser

        if (!user!!.isEmailVerified) {
            binding.verifyButton.visibility = View.VISIBLE
            binding.textEmailNotVerified.visibility = View.VISIBLE

            binding.verifyButton.setOnClickListener {
                user.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "The verification link has been resend",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Log.d("TAG", "onViewCreated:onFailure:email not send " + it.message)
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.google.android.material.R.id.container, LoginFragment())
    }

    private fun initRecyclerview() {
        binding.recyclerviewBooks.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
//            bookAdapter = BookAdapter()
            adapter = bookAdapter
        }
    }
}