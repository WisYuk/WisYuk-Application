package com.wisyuk.ui.home.ui.favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.wisyuk.databinding.FragmentFavoriteBinding
import com.wisyuk.databinding.FragmentProfileBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.home.ui.profile.ProfileViewModel
import com.wisyuk.ui.login.LoginActivity
import kotlin.properties.Delegates

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
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
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favoriteViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(
                    Intent(
                        requireActivity(), LoginActivity::class.java)
                )
                activity?.finish()
            } else {
                userId = user.id
                favoriteViewModel.getTourism(userId)
            }
        }
        return root
    }

    // TODO setup view

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}