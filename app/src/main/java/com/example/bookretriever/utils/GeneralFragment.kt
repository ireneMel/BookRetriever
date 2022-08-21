package com.example.bookretriever.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.bookretriever.R

open class GeneralFragment : Fragment() {
    fun makeToast(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun goToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
        }
    }
}