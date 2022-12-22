package com.example.storyapp.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Repo.Repository
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.data.Repo.Result
import com.example.storyapp.ui.register.RegisterViewModels
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelsTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var registerViewModel: RegisterViewModels
    private var dummyRegisterResponse = DataDummy.generateDummyRegisterReponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModels(repository)
    }

    @Test
    fun `when register success Should Not Null and Return Success`() {
        val responseExpect = MutableLiveData<Result<RegisterResponse>>()
        responseExpect.value = Result.Success(dummyRegisterResponse)
        Mockito.`when`(repository.register("yuan", "yuan@apalah.com", "123"))
            .thenReturn(responseExpect)

        val actualLoginResponse = registerViewModel.registerUser("yuan", "yuan@apalah.com", "123").getOrAwaitValue()
        Mockito.verify(repository).register("yuan", "yuan@apalah.com", "123")
        assertNotNull(actualLoginResponse)
        assertTrue(actualLoginResponse is Result.Success)
        assertEquals(dummyRegisterResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `when register failed or error should return error`() {
        val regisResponse = MutableLiveData<Result<RegisterResponse>>()
        regisResponse.value = Result.Error("Error")
        Mockito.`when`(repository.register("yuan", "yuan@apalah.com", "123")).thenReturn(regisResponse)

        val actualResponse = registerViewModel.registerUser("yuan", "yuan@apalah.com", "123").getOrAwaitValue()
        Mockito.verify(repository).register("yuan", "yuan@apalah.com", "123")
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

}