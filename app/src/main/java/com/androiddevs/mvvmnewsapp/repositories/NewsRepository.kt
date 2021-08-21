package com.androiddevs.mvvmnewsapp.repositories

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article

class NewsRepository(
    val database : ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String,pageNumber :Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article : Article) = database.getArticleDao().upsert(article)

    suspend fun delete(article: Article) = database.getArticleDao().deleteArticle(article)

    fun getSavedNews() = database.getArticleDao().getAllArticles()

}