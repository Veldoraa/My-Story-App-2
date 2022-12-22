package com.example.storyapp.mainstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.Repo.Repository
import com.example.storyapp.data.local.Entity
import com.example.storyapp.ui.mainstory.MainViewModel
import com.example.storyapp.ui.mainstory.StoryListAdapter
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRules
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRules()

    @Mock
    private lateinit var repository: Repository

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Entity> = StoryPagingSource.snapshot(dummyStory)
        val storyExpect = MutableLiveData<PagingData<Entity>>()
        storyExpect.value = data
        Mockito.`when`(repository.getStoryList()).thenReturn(storyExpect)

        val storyViewModel = MainViewModel(repository)
        val actualStory: PagingData<Entity> = storyViewModel.storyList.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)

    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Entity>>>() {

    companion object {
        fun snapshot(items: List<Entity>): PagingData<Entity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Entity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Entity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}