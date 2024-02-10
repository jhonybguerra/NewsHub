package com.jbgcomposer.newshub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbgcomposer.newshub.R
import com.jbgcomposer.newshub.databinding.FragmentSearchBinding
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.ui.NewsActivity
import com.jbgcomposer.newshub.ui.adapters.NewsAdapter
import com.jbgcomposer.newshub.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.jbgcomposer.newshub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { (activity as NewsActivity).viewModel }
    private val searchAdapter by lazy {
        NewsAdapter { article ->
            navigateToArticleFragment(article)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchListener()
        submitDataAdapter()
        setupProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToArticleFragment(article: Article) {
        val bundle = Bundle().apply { putParcelable("article", article) }
        findNavController().navigate(R.id.action_searchFragment_to_articleFragment, bundle)
    }

    private fun setupRecyclerView() {
        binding.rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchListener() {
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.setSearchQuery(editable.toString())
                    }
                }
            }
        }
    }

    private fun submitDataAdapter() {
        lifecycleScope.launch {
            viewModel.searchPagingDataFlow.collectLatest { pagingData ->
                searchAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupProgressBar() {
        searchAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                setProgressBarVisibility(true)
            } else {
                setProgressBarVisibility(false)

                val error = when {
                    loadState.source.prepend is LoadState.Error -> loadState.source.prepend as LoadState.Error
                    loadState.source.append is LoadState.Error -> loadState.source.append as LoadState.Error
                    loadState.source.refresh is LoadState.Error -> loadState.source.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    showToast(requireContext(), it.error.localizedMessage ?: getString(R.string.unknown_error))
                }
            }
        }
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

}