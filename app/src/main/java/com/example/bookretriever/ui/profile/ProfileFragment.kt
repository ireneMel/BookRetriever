package com.example.bookretriever.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookretriever.R
import com.example.bookretriever.databinding.FragmentProfileBinding
import com.example.bookretriever.repositories.UsersRepository
import com.example.bookretriever.utils.ExtensionFunctions.setActionBarText
import com.yalantis.ucrop.UCrop
import java.io.File

//TODO clear cache

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val repository = UsersRepository()
    private lateinit var outputUri: Uri

    private lateinit var profilePictureFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        activity?.setActionBarText(getString(R.string.your_profile))

        requireContext().cacheDir.mkdirs()
//        requireContext().cacheDir.deleteRecursively()

        with(binding) {
            profilePictureFile = File(requireContext().cacheDir, "avatar.png")
            binding.roundedImageView.setImageURI(profilePictureFile.toUri())

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
        getImageToCrop.launch("image/*")
    }

    private val uCropContract = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]
            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(800, 800)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }
    }

    @SuppressLint("NewApi")
    private val cropImage = registerForActivityResult(uCropContract) { uri ->
        binding.roundedImageView.setImageURI(uri)
        val avatar = File(requireContext().cacheDir, "avatar.png")
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(requireContext().contentResolver, uri)
        ).compress(Bitmap.CompressFormat.PNG, 100, avatar.outputStream())
        //delete previous file
        uri.toFile().delete()
    }

    private val getImageToCrop =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val file = File(requireContext().cacheDir, "IMG_${System.currentTimeMillis()}.jpg")
            outputUri = file.toUri()
            val listUri = listOf(uri!!, outputUri)
            cropImage.launch(listUri)
        }
}