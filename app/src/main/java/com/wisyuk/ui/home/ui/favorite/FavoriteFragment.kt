package com.wisyuk.ui.home.ui.favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.databinding.FragmentFavoriteBinding
import com.wisyuk.ui.TourismAdapter
import com.wisyuk.ui.ViewModelFactory
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

        setupRecyclerView()
        observerViewModel()

        return root
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvPlan.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvPlan.addItemDecoration(itemDecoration)
    }

    private fun observerViewModel() {
        favoriteViewModel.listFavorite.observe(viewLifecycleOwner) {
            setData(it)
        }
        favoriteViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        favoriteViewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
        favoriteViewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                errorMessage = it
            }
        }
    }

    private fun setData(data: List<ListTourismItem>) {
        val adapter = TourismAdapter()
        adapter.submitList(data)
        binding.rvPlan.adapter = adapter
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