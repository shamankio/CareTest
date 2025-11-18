package com.rustan.domain

import com.rustan.data.StoryRepository
import com.rustan.data.model.PhotoDTO
import com.rustan.domain.entities.Story
import com.rustan.domain.maper.MapEntities
import com.rustan.domain.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetStoriesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: StoryRepository
    private lateinit var mapEntities: MapEntities
    private lateinit var getStoriesUseCase: GetStoriesUseCaseImpl

    @Before
    fun setUp() {
        repository = mockk()
        mapEntities = mockk()
        getStoriesUseCase = GetStoriesUseCaseImpl(
            repository = repository,
            coroutineDispatcherIO = mainCoroutineRule.testDispatcher,
            mapEntities = mapEntities
        )
    }

    @Test
    fun `invoke WHEN repository returns success THEN emit Success`() = runTest {
        // GIVEN
        val city = "london"
        val count = 1
        val photoDTO =PhotoDTO(urls = PhotoDTO.Urls(regular = "ligula"))
        val story = Story(imageURL = "url")
        coEvery { repository.getPhotos(city, count) } returns listOf(photoDTO)
        coEvery { mapEntities.mapPhotoDTOToStory(photoDTO) } returns story

        // WHEN
        val result = getStoriesUseCase(city, count).first()

        // THEN
        assertTrue(result is NetworkResult.Success)
        assertEquals(listOf(story), (result as NetworkResult.Success).data)
    }

    @Test
    fun `invoke WHEN repository throws HttpException THEN emit ApiError`() = runTest {
        // GIVEN
        val city = "london"
        val count = 1
        val httpException = HttpException(Response.error<List<PhotoDTO>>(404, mockk(relaxed = true)))
        coEvery { repository.getPhotos(city, count) } throws httpException

        // WHEN
        val result = getStoriesUseCase(city, count).first()

        // THEN
        assertTrue(result is NetworkResult.Error.ApiError)
        assertEquals(404, (result as NetworkResult.Error.ApiError).code)
    }

    @Test
    fun `invoke WHEN repository throws Exception THEN emit Exception`() = runTest {
        // GIVEN
        val city = "london"
        val count = 1
        val exception = Exception("Network error")
        coEvery { repository.getPhotos(city, count) } throws exception

        // WHEN
        val result = getStoriesUseCase(city, count).first()

        // THEN
        assertTrue(result is NetworkResult.Error.Exception)
        assertEquals("Network error", (result as NetworkResult.Error.Exception).message)
    }
}
