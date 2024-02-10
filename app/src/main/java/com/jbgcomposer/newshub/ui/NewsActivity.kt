package com.jbgcomposer.newshub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jbgcomposer.newshub.R
import com.jbgcomposer.newshub.databinding.ActivityNewsBinding
import com.jbgcomposer.newshub.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

    val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.news_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.apply {
            newsBottomNav.setupWithNavController(navController)
            newsToolbar.apply {
                setupWithNavController(navController)
                inflateMenu(R.menu.toolbar_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.about -> {
                            showAboutDialog()
                            true
                        }

                        else -> false
                    }
                }
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.articleFragment -> binding.newsBottomNav.visibility = View.GONE
                else -> binding.newsBottomNav.visibility = View.VISIBLE
            }
        }
    }

    private fun showAboutDialog() {
        val aboutText = SpannableString(getString(R.string.about_text)).apply {
            Linkify.addLinks(
                this,
                Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES or Linkify.PHONE_NUMBERS
            )
        }

        AlertDialog.Builder(this, R.style.NewsThemeDialog)
            .setTitle(getString(R.string.about_the_developer))
            .setMessage(aboutText)
            .setPositiveButton(getString(R.string.ok), null)
            .create()
            .apply {
                show()
                findViewById<TextView>(android.R.id.message)?.movementMethod =
                    LinkMovementMethod.getInstance()
            }
    }
}