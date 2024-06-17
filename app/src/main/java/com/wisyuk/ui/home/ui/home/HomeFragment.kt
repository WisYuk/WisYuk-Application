package com.wisyuk.ui.home.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.Call.Details
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wisyuk.R
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.data.response.RecommendationsItem
import com.wisyuk.databinding.FragmentHomeBinding
import com.wisyuk.ui.RecommendationAdapter
import com.wisyuk.ui.TourismAdapter
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import com.wisyuk.ui.preference.PreferenceActivity
import com.wisyuk.ui.userdatemenu.DateActivity
import com.wisyuk.utils.Utils
import com.wisyuk.utils.Utils.dateFormatted
import com.wisyuk.utils.Utils.dateFormattedGoAt

class HomeFragment : Fragment() {
    companion object {
        const val EXTRA_GOAT = "go_at"
        const val EXTRA_USERID = "userid"
    }


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var errorMessage: String = ""
    private var goAt: String? = null
    private var userID: Int? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        goAt = activity?.intent?.getStringExtra(EXTRA_GOAT)
        userID = activity?.intent?.getIntExtra(EXTRA_USERID, -1)
        if (goAt != null && userID != null) {
            homeViewModel.getTourisms(goAt!!, userID!!)
        } else {
            homeViewModel.getTourism()
        }


        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                binding.loginButton.visibility = View.VISIBLE
                binding.logoutButton.visibility = View.GONE
                binding.choosePlanTitle.text = getString(R.string.welcome_title_guest)
            } else {
                homeViewModel.checkProfile(user.id)
                if (goAt != null && userID != null) {
                    binding.recommendationHeader.text = getString(R.string.recommendation_date,
                        Utils.convertDateFromYMDtoMDy(goAt!!)
                    )
                }
                binding.loginButton.visibility = View.GONE
                binding.logoutButton.visibility = View.VISIBLE
                binding.choosePlanTitle.text = getString(R.string.welcome_title, user.name)
            }
        }

        homeViewModel.noProfile.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireActivity(), PreferenceActivity::class.java)
                startActivity(intent)
            }
        }


        setupRecyclerView()
        observerViewModel()
        searchEngine()

        binding.loginButton.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener {
            homeViewModel.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

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
        homeViewModel.listRecs.observe(viewLifecycleOwner) {
            setDataRec(it)
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

    private fun setDataRec(data: List<RecommendationsItem>) {
        val adapter = goAt?.let { RecommendationAdapter(it) }
        adapter?.submitList(data)
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
            Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}