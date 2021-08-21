package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment: BaseFragment(R.layout.fragment_article) {

    val args : ArticleFragmentArgs by navArgs()
    val TAG: String = "ArticleFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article Saved",Snackbar.LENGTH_SHORT).show()
            Log.i(TAG,"Entry on DB")
        }
    }
}