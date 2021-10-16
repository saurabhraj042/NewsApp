package com.androiddevs.mvvmnewsapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repositories.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    init {
        getBreakingNews("in")
    }

    private fun getBreakingNews(countryCode : String) = viewModelScope.launch {

        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue( handleBreakingNewsResponse(response) )
    }

    fun searchAllNews(searchQuery : String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let{
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse = it
                }else{
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = it.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse?:it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{

        if(response.isSuccessful){

            response.body()?.let{
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse = it
                }else{
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = it.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse?:it)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveArticle(article : Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedArticle() = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }
}