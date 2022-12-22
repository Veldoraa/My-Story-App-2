package com.example.storyapp.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repo.Repository
import com.example.storyapp.data.response.UploadDataResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.data.Repo.Result
import com.example.storyapp.story.addstory.AddStoryViewModels
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelsTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var addStoryViewModel: AddStoryViewModels
    private var dummyAddStoryResponse = DataDummy.generateUploadDataResponse()

    @Mock
    private lateinit var dummyMockFile : File

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModels(repository)
    }

    @Test
    fun `when add story success should not null and return success`() {
        val responseExpect = MutableLiveData<Result<UploadDataResponse>>()
        responseExpect.value = Result.Success(dummyAddStoryResponse)
        Mockito.`when`(repository.uploadLiveStory("bearer", "foto", dummyMockFile)).thenReturn(responseExpect)

        val actualUploadResponse = addStoryViewModel.uploadStory("bearer", "foto", dummyMockFile).getOrAwaitValue()
        Mockito.verify(repository).uploadLiveStory("bearer", "foto", dummyMockFile)
        assertNotNull(actualUploadResponse)
        assertTrue(actualUploadResponse is Result.Success)
        assertEquals(dummyAddStoryResponse, (actualUploadResponse as Result.Success).data)
    }

    @Test
    fun `when add Story failed or error should return error`() {
        val uploadResponse = MutableLiveData<Result<UploadDataResponse>>()
        uploadResponse.value = Result.Error("Error")
        Mockito.`when`(repository.uploadLiveStory("bearer", "foto", dummyMockFile)).thenReturn(uploadResponse)

        val actualResponse = addStoryViewModel.uploadStory("bearer", "foto", dummyMockFile).getOrAwaitValue()
        Mockito.verify(repository).uploadLiveStory("bearer", "foto", dummyMockFile)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}