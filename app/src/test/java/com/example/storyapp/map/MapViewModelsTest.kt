package com.example.storyapp.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repo.Repository
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import com.example.storyapp.data.Repo.Result
import com.example.storyapp.ui.map.MapViewModels
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelsTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var mapsViewModel: MapViewModels
    private val dummyMarker = DataDummy.generateDummyUserLocation()

    @Before
    fun setUp() {
        mapsViewModel = MapViewModels(repository)
    }

    @Test
    fun `when Get Mark List Should Not Null and Return Success`() {
        val markExpect = MutableLiveData<Result<List<StoryResponseItem>>>()
        markExpect.value = Result.Success(dummyMarker)
        Mockito.`when`(repository.getListStoryByMaps(1, "Bearer")).thenReturn(markExpect)

        val actualMark = mapsViewModel.getStoryByMaps(1, "Bearer").getOrAwaitValue()
        Mockito.verify(repository).getListStoryByMaps(1, "Bearer")
        assertNotNull(actualMark)
        assertTrue(actualMark is Result.Success)
        assertEquals(dummyMarker.size, (actualMark as Result.Success).data.size)
    }

    @Test
    fun `when network error should return error`() {
        val markList = MutableLiveData<Result<List<StoryResponseItem>>>()
        markList.value = Result.Error("Error")
        Mockito.`when`(repository.getListStoryByMaps(1, "Bearer")).thenReturn(markList)

        val actualMark = mapsViewModel.getStoryByMaps(1, "Bearer").getOrAwaitValue()
        Mockito.verify(repository).getListStoryByMaps(1, "Bearer")
        assertNotNull(actualMark)
        assertTrue(actualMark is Result.Error)
    }
}