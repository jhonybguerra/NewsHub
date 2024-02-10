package com.jbgcomposer.newshub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jbgcomposer.newshub.R
import com.jbgcomposer.newshub.databinding.FragmentArticleBinding
import com.jbgcomposer.newshub.ui.NewsActivity
import com.jbgcomposer.newshub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { (activity as NewsActivity).viewModel }
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? NewsActivity)?.binding?.newsToolbar?.title = args.article.source?.name
        setupWebView()
        setupSaveArticle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupWebView() {
        args.article.url?.let { url ->
            binding.webView.apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                loadUrl(url)
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                }
            }
        } ?: run {
            showToast(requireContext(), getString(R.string.error_loading_article))
        }
    }

    private fun setupSaveArticle() {
        binding.fab.setOnClickListener {
            viewModel.saveArticle(args.article)
            showToast(requireContext(), getString(R.string.article_saved_successfully))
        }
    }

}