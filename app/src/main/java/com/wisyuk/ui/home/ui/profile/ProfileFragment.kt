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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.wisyuk.R
import com.wisyuk.data.response.PreferencesItem
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
    private var prefItems = emptyList<String>()
    private var itemPos1 = -1
    private var itemPos2 = -1
    private var itemPos3 = -1

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
        profileViewModel.getPreferences()
        profileViewModel.preferencesResponse.observe(viewLifecycleOwner)  { pref ->
            prefItems = pref.map { it.name }
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, prefItems)
            binding.tvProfilePrefFirstEdit.adapter = adapter
            binding.tvProfilePrefSecondEdit.adapter = adapter
            binding.tvProfilePrefThirdEdit.adapter = adapter

            val selectedItem1 = binding.tvProfilePrefFirst.text.toString()
            val initPos1 = prefItems.indexOf(selectedItem1)
            if (initPos1 != -1) {
                binding.tvProfilePrefFirstEdit.setSelection(initPos1)
            }

            val selectedItem2 = binding.tvProfilePrefSecond.text.toString()
            val initPos2 = prefItems.indexOf(selectedItem2)
            if (initPos2 != -1) {
                binding.tvProfilePrefSecondEdit.setSelection(initPos2)
            }

            val selectedItem3 = binding.tvProfilePrefThird.text.toString()
            val initPos3 = prefItems.indexOf(selectedItem3)
            if (initPos3 != -1) {
                binding.tvProfilePrefThirdEdit.setSelection(initPos3)
            }

            binding.tvProfilePrefFirstEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    itemPos1 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemPos1 = -1
                }
            }

            binding.tvProfilePrefSecondEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    itemPos2 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemPos2 = -1
                }
            }

            binding.tvProfilePrefThirdEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    itemPos3 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    itemPos3 = -1
                }
            }
        }
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

            val preferences = listOf<Int>(
                itemPos1+1,
                itemPos2+1,
                itemPos3+1
            )

            if (currentImageUri != null) {
                currentImageUri?.let { uri ->
                    println("WEYNARD $uri")
                    val imageFile = uriToFile(uri, requireActivity()).reduceFile()
                    profileViewModel.updateProfile(userId, imageFile,
                        tvProfileNameEdit.text.toString(), preferences)
                }
            } else {
                profileViewModel.updateProfile(userId, null,
                    tvProfileNameEdit.text.toString(), preferences)
            }
//            Toast.makeText(requireActivity(), "Edit Profile Success!", Toast.LENGTH_SHORT).show()

        }

        profileViewModel.updateResponse.observe(viewLifecycleOwner) {
            profileViewModel.getProfile(userId)
            profileViewModel.toggleEditMode()
            Toast.makeText(requireActivity(), "Edit Profile Success!", Toast.LENGTH_SHORT).show()
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