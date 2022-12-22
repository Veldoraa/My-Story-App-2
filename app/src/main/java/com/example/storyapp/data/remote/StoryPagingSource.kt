package com.example.storyapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.utils.StoryApp
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.local.SharedPreferences

class StoryPagingSource(private val apiService: ApiService):
    PagingSource<Int, StoryResponseItem>(){
    private lateinit var preferences: SharedPreferences

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseItem> {
        preferences = StoryApp.appContext?.let { SharedPreferences(it) }!!

        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val dataResponse = apiService.getListStory("Bearer ${preferences.getToken()}",
                position,
                params.loadSize).storyResponseItems

            LoadResult.Page(
                data = dataResponse,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (dataResponse.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}