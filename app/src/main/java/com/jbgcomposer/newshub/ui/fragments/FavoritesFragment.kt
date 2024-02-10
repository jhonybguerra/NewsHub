package com.jbgcomposer.newshub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jbgcomposer.newshub.R
import com.jbgcomposer.newshub.databinding.FragmentFavoritesBinding
import com.jbgcomposer.newshub.db.FavoriteArticle
import com.jbgcomposer.newshub.models.Article
import com.jbgcomposer.newshub.ui.NewsActivity
import com.jbgcomposer.newshub.ui.adapters.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { (activity as NewsActivity).viewModel }
    private val favoritesAdapter by lazy {
        FavoritesAdapter { articleEntity ->
            val article = viewModel.convertFavoriteArticleToArticle(articleEntity)
            navigateToArticleFragment(article)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeSavedArticles()
        setupSwipeToDelete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeSavedArticles() {
        viewModel.getSavedNews().observe(viewLifecycleOwner) { articles ->
            favoritesAdapter.submitList(articles)
        }
    }

    private fun navigateToArticleFragment(article: Article) {
        val bundle = Bundle().apply { putParcelable("article", article) }
        findNavController().navigate(R.id.action_favoritesFragment_to_articleFragment, bundle)
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(createItemTouchHelperCallback()).attachToRecyclerView(binding.rvFavorites)
    }

    private fun createItemTouchHelperCallback() = object : ItemTouchHelper.SimpleCallback(
        0, // Movimento de arrasto não necessário
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true // Movimento de arrasto não utilizado
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val articleEntity = favoritesAdapter.currentList[position]
            viewModel.deleteArticle(articleEntity)
            showUndoSnackbar(articleEntity)
        }
    }

    private fun showUndoSnackbar(articleEntity: FavoriteArticle) {
        Snackbar.make(binding.root,
            getString(R.string.successfully_deleted_article), Snackbar.LENGTH_LONG).apply {
            setAction(getString(R.string.undo)) {
                val article = viewModel.convertFavoriteArticleToArticle(articleEntity)
                viewModel.saveArticle(article)
            }
            show()
        }
    }

}