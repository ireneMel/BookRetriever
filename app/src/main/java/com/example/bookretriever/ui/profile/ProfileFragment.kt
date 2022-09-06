package com.example.bookretriever.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentProfileBinding
import com.example.bookretriever.repositories.UsersRepository
import java.io.File
import java.io.IOException


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val repository = UsersRepository()
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    //ucrop

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        with(binding) {
//            repository.databaseReference.addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    if (snapshot.value == null) {
//                        println("value is null")
//
//                    } else {
//                        for (snap in snapshot.children) {
//                            for (s in snap.children) {
//                                val photoUrl = s.value as String
//                                val uri = Uri.parse(photoUrl)
////                                roundedImageView.setImageURI(uri)
//
//                                // Setting image on image view using Bitmap
//                                val bitmap = MediaStore.Images.Media.getBitmap(
//                                    requireContext().contentResolver,
//                                    uri
//                                )
//                                binding.roundedImageView.setImageBitmap(bitmap)
//
//                            }
//                        }
//
//                        val a = 12
////                        val photoUrl = snapshot.getValue() //as Uri
////                    roundedImageView.setImageURI(photoUrl)
//                    }
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//                override fun onChildRemoved(snapshot: DataSnapshot) {}
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//                override fun onCancelled(error: DatabaseError) {}
//            })
//                .child(repository.auth.currentUser!!.uid)
//                .child("photoUrl").get().addOnSuccessListener {
//                    val a = it.value
//                    roundedImageView.setImageURI(it.value as Uri?)
//                }

            val avatar = File(requireContext().dataDir, "avatar.png")
            Glide.with(requireContext())
                .load(avatar).diskCacheStrategy(DiskCacheStrategy.NONE).into(roundedImageView)

            email.setText(repository.auth.currentUser?.email.toString())
            logout.setOnClickListener {
                repository.signOut()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
            roundedImageView.setOnClickListener {
                changeProfilePicture()
            }
        }
    }


    private fun changeProfilePicture() {
        val intent = Intent().setType("image/*").setAction(ACTION_GET_CONTENT)
        resultLauncher.launch(intent)
    }

    //todo crop image

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                photoUri = result.data?.data ?: return@registerForActivityResult
                try {
//                     Setting image on image view using Bitmap
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        photoUri
                    )

                    //save cropped image here
                    binding.roundedImageView.setImageBitmap(bitmap)

                    val avatar = File(requireContext().dataDir, "avatar.png")
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, avatar.outputStream())

//                    uploadImageToDatabase()
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }

    // UploadImage method
    private fun uploadImageToDatabase() {
        if (photoUri == null) return

        repository.uploadPhoto(photoUri!!)

        // Defining the child of storageReference
//        val ref: StorageReference =
//            repository.storageReference.child("images/" + UUID.randomUUID().toString())
//
//        // adding listeners on upload
//        // or failure of image
//        ref.putFile(filePath!!).addOnSuccessListener { // Image uploaded successfully
//            // Dismiss dialog
//            progressDialog.dismiss()
//            Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener { e -> // Error, Image not uploaded
//            progressDialog.dismiss()
//            Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
//        }.addOnProgressListener { taskSnapshot ->
//            // Progress Listener for loading
//            // percentage on the dialog box
//            val progress = ((100.0
//                    * taskSnapshot.bytesTransferred
//                    / taskSnapshot.totalByteCount))
//            progressDialog.setMessage(
//                ("Uploaded " + progress.toInt() + "%")
//            )
//        }
    }
}