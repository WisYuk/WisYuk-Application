package com.wisyuk.ui.home.ui.plans

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
import com.wisyuk.data.response.PlanTourismItem
import com.wisyuk.databinding.FragmentPlanBinding
import com.wisyuk.ui.PlanAdapter
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.login.LoginActivity
import kotlin.properties.Delegates

class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val planViewModel by viewModels<PlanViewModel> {
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
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        planViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(
                    Intent(
                        requireActivity(), LoginActivity::class.java)
                )
                activity?.finish()
            } else {
                userId = user.id
                planViewModel.getTourism(userId)
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
        planViewModel.listPaid.observe(viewLifecycleOwner) {
            setData(it)
        }
        planViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        planViewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
        planViewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                errorMessage = it
            }
        }
    }

    private fun setData(data: List<PlanTourismItem>) {
        val adapter = PlanAdapter()
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