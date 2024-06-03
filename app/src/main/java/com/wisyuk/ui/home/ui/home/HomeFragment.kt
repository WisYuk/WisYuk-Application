package com.wisyuk.ui.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wisyuk.R
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.databinding.FragmentHomeBinding
import com.wisyuk.ui.TourismAdapter
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.ui.userdatemenu.DateActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var errorMessage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(
                    requireActivity(), LoginActivity::class.java))
                activity?.finish()
            }
        }


        setupRecyclerView()
        observerViewModel()
        searchEngine()
        return root
    }



    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvTour.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvTour.addItemDecoration(itemDecoration)
    }

    private fun observerViewModel() {
        homeViewModel.listTourism.observe(viewLifecycleOwner) {
            setData(it)
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        homeViewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
        homeViewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                errorMessage = it
            }
        }
    }

    private fun setData(data: List<ListTourismItem>) {
        val adapter = TourismAdapter()
        adapter.submitList(data)
        binding.rvTour.adapter = adapter
    }

    private fun searchEngine() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()

                    val query: String = searchView.text.toString()

                    homeViewModel.getTourism(query)
                    false
                }
            searchBar.inflateMenu(R.menu.filter_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_date_filter -> {
                        val intent = Intent(requireActivity(), DateActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            //
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}