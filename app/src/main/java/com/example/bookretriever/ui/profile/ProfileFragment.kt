package com.example.bookretriever.ui.profile

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentProfileBinding
import com.example.bookretriever.repositories.UsersRepository
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val repository = UsersRepository()
    private var filePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.email.setText(repository.auth.currentUser?.email.toString())

        binding.logout.setOnClickListener {
            repository.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        binding.roundedImageView.setOnClickListener {
            changeProfilePicture()
        }
    }

    fun changeProfilePicture() {
        val intent = Intent().setType("image/*").setAction(ACTION_GET_CONTENT)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                filePath = result.data?.data
                try {
                    // Setting image on image view using Bitmap
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        filePath
                    )
                    binding.roundedImageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }

    // UploadImage method
    private fun uploadImage() {
        if (filePath == null) return

        // Code for showing progressDialog while uploading
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        // Defining the child of storageReference
        val ref: StorageReference =
            repository.imageStorage.child("images/" + UUID.randomUUID().toString())

        // adding listeners on upload
        // or failure of image
        ref.putFile(filePath!!).addOnSuccessListener { // Image uploaded successfully
            // Dismiss dialog
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e -> // Error, Image not uploaded
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
        }.addOnProgressListener { taskSnapshot ->
            // Progress Listener for loading
            // percentage on the dialog box
            val progress = ((100.0
                    * taskSnapshot.bytesTransferred
                    / taskSnapshot.totalByteCount))
            progressDialog.setMessage(
                ("Uploaded " + progress.toInt() + "%")
            )
        }
    }
}