package com.wisyuk.ui.home.ui.profile

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.wisyuk.databinding.FragmentProfileBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var errorMessage: String = ""

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
                profileViewModel.getProfile(user.id)
            }
        }


        observerViewModel()

        return root
    }

    private fun observerViewModel() {


        profileViewModel.profileResponse.observe(viewLifecycleOwner) { profile ->
            Glide
                .with(requireActivity())
                .load(profile.data.image)
                .into(binding.ivProfilePhoto)

            binding.tvProfileEmailBox.text = profile.data.email
            binding.tvProfilePrefFirst.text = profile.data.preferences[0] ?: "Not found"
            binding.tvProfilePrefSecond.text = profile.data.preferences[1] ?: "Not found"
            binding.tvProfilePrefThird.text = profile.data.preferences[2] ?: "Not found"

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