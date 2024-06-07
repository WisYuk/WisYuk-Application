package com.wisyuk.ui.home.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.wisyuk.R
import com.wisyuk.databinding.FragmentProfileBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.utils.Utils.reduceFile
import com.wisyuk.utils.Utils.uriToFile
import kotlin.properties.Delegates

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var errorMessage: String = ""
    private var userId by Delegates.notNull<Int>()
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(
                    Intent(
                        requireActivity(), LoginActivity::class.java)
                )
                activity?.finish()
            } else {
                userId = user.id
                profileViewModel.getProfile(userId)
            }
        }


        observerViewModel()

        profileViewModel.editMode.observe(viewLifecycleOwner) {
            if (it) {
                editView()
            }
        }

        binding.ivProfilePhoto.setOnClickListener {
            if (profileViewModel.editMode.value == true)
                startGallery()
        }

        binding.saveButton.setOnClickListener {
            if (profileViewModel.editMode.value == true) save()
        }

        binding.editButton.setOnClickListener {
            profileViewModel.toggleEditMode()
            binding.editButton.visibility = View.GONE
            binding.saveButton.visibility = View.VISIBLE
            binding.editIcon.visibility = View.VISIBLE
            binding.tvProfileName.visibility = View.GONE
            binding.tvProfileNameEdit.visibility = View.VISIBLE
            binding.preferenceContainer.visibility = View.GONE
            binding.preferenceContainerEdit.visibility = View.VISIBLE
        }

        return root
    }

    private fun observerViewModel() {


        profileViewModel.profileResponse.observe(viewLifecycleOwner) { profile ->

            if (profile.image != null) {
                Glide
                    .with(requireActivity())
                    .load(profile.image)
                    .into(binding.ivProfilePhoto)
                }

            binding.tvProfileName.text = profile.name
            binding.tvProfileEmailBox.text = profile.email
            binding.tvProfilePrefFirst.text = profile.preferences[0] ?: "Not found"
            binding.tvProfilePrefSecond.text = profile.preferences[1] ?: "Not found"
            binding.tvProfilePrefThird.text = profile.preferences[2] ?: "Not found"

        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        profileViewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
        profileViewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                errorMessage = it
            }
        }
    }

    private fun editView() {
        binding.tvProfileNameEdit.setText(binding.tvProfileName.text, TextView.BufferType.EDITABLE)
        binding.tvProfilePrefFirstEdit.setText(binding.tvProfilePrefFirst.text, TextView.BufferType.EDITABLE)
        binding.tvProfilePrefSecondEdit.setText(binding.tvProfilePrefSecond.text, TextView.BufferType.EDITABLE)
        binding.tvProfilePrefThirdEdit.setText(binding.tvProfilePrefThird.text, TextView.BufferType.EDITABLE)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivProfilePhoto.setImageURI(it)
        }
    }

    private fun save() {
        with(binding) {
            val preferences = listOf<String>(
                tvProfilePrefFirstEdit.text.toString(),
                tvProfilePrefSecondEdit.text.toString(),
                tvProfilePrefThirdEdit.text.toString())

            if (currentImageUri != null) {
                currentImageUri?.let { uri ->
                    val imageFile = uriToFile(uri, requireActivity()).reduceFile()
                    profileViewModel.updateProfile(userId, imageFile,
                        tvProfileNameEdit.text.toString(), preferences)
                }
            } else {
                profileViewModel.updateProfile(userId, null,
                    tvProfileNameEdit.text.toString(), preferences)
            }
        }


        profileViewModel.updateResponse.observe(viewLifecycleOwner) {
            profileViewModel.toggleEditMode()
            binding.editButton.visibility = View.VISIBLE
            binding.saveButton.visibility = View.GONE
            binding.editIcon.visibility = View.GONE
            binding.tvProfileName.visibility = View.VISIBLE
            binding.tvProfileNameEdit.visibility = View.GONE
            binding.preferenceContainer.visibility = View.VISIBLE
            binding.preferenceContainerEdit.visibility = View.GONE
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}