package com.athallah.ecommerce

import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.request.FulfillmentRequest
import com.athallah.ecommerce.data.datasource.api.request.RatingRequest
import com.athallah.ecommerce.data.datasource.api.request.RefreshRequest
import com.athallah.ecommerce.data.datasource.api.response.Data
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponse
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponseData
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductVariantItem
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponseData
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponseData
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponseItem
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.response.RatingResponse
import com.athallah.ecommerce.data.datasource.api.response.RefreshResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponseItem
import com.athallah.ecommerce.data.datasource.api.response.SearchResponse
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponse
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponseData
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponseItem
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.HttpURLConnection

class ApiServiceTest {
    private var mockWebServer = MockWebServer()
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun register() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("register.json"))
        mockWebServer.enqueue(response)
        val authRequest = AuthRequest("", "", "")
        val actualData = apiService.register(authRequest)
        val expectedData = dummyRegisterResponse

        assertEquals(expectedData, actualData)
    }

    private val dummyRegisterResponse = RegisterResponse(
        code = 200,
        message = "OK",
        data = RegisterDataResponse(
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDE1MjB9.ldL_6Qoo-MfMmwHrhxXUv670Uz6j0CCF9t9I8uOmW_LuAUTzCWhjMcQelP8MjfnVDqKSZj2LaqHv3TY08AB7TQ",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDQ1MjB9.HeeNuQww-w2tb3pffNC43BCmMCcE3rOj-yL7-pTGOEcIcoFCv2n9IEWS0gqxNnDaNf3sXBm7JHCxFexB5FGRgQ",
            expiresAt = 600
        )
    )

    @Test
    fun login() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("login.json"))
        mockWebServer.enqueue(response)
        val authRequest = AuthRequest("", "", "")
        val actualData = apiService.login(authRequest)
        val expectedData = dummyLoginResponse

        assertEquals(expectedData, actualData)
    }

    private val dummyLoginResponse = LoginResponse(
        code = 200,
        message = "OK",
        data = LoginDataResponse(
            userName = "",
            userImage = "",
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDE4OTV9.AceVKZlMeFFvwNPAC5Opc6mSxhAXWz1CSf4E2FipZsJkPfaFt021Yi3TpG08ENUashUwJX-YLCuIolqnb7EulA",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoicmVmcmVzaFRva2VuIiwiZXhwIjoxNjg1MzQ0ODk1fQ.tB4EeMvkfJAV_kSwakcEujsJEqNtlvKaBbz6ga58lMw6R1NKNOSi6iy3Qn-dFtHGMkzwqpokY3uOdQYcVtahCA",
            expiresAt = 600
        )
    )

    @Test
    fun profile() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("profile.json"))
        mockWebServer.enqueue(response)
        val userNamePart = MultipartBody.Part.createFormData("userName", "")
        val token = ""
        val actualData = apiService.profile(userNamePart, null, token)
        val expectedData = dummyProfileResponse

        assertEquals(expectedData, actualData)
    }

    private val dummyProfileResponse = ProfileResponse(
        code = 200,
        message = "OK",
        data = ProfileDataResponse(
            userName = "Test",
            userImage = "1d32ba79-e879-4425-a011-2da4281f1c1b-test.png"
        )
    )

    @Test
    fun products() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("products.json"))
        mockWebServer.enqueue(response)
        val query = mapOf("" to "")
        val actualData = apiService.products(query, "")
        val expectedData = dummyProductsResponse
        assertEquals(expectedData, actualData)
    }

    private val dummyProductsResponse = ProductsResponse(
        code = 200,
        message = "OK",
        data = ProductsResponseData(
            itemsPerPage = 10,
            currentItemCount = 10,
            pageIndex = 1,
            totalPages = 3,
            items = listOf(
                ProductsResponseItem(
                    productId = "601bb59a-4170-4b0a-bd96-f34538922c7c",
                    productName = "Lenovo Legion 3",
                    productPrice = 10000000,
                    image = "image1",
                    brand = "Lenovo",
                    store = "LenovoStore",
                    sale = 2,
                    productRating = 4.0f
                ),
                ProductsResponseItem(
                    productId = "3134a179-dff6-464f-b76e-d7507b06887b",
                    productName = "Lenovo Legion 5",
                    productPrice = 15000000,
                    image = "image1",
                    brand = "Lenovo",
                    store = "LenovoStore",
                    sale = 4,
                    productRating = 4.0f
                )
            )
        )
    )

    @Test
    fun refresh() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("refresh.json"))
        mockWebServer.enqueue(response)
        val token = RefreshRequest("")
        val actualData = apiService.refresh(token)
        val expectedData = dummyRefreshResponse
        assertEquals(expectedData, actualData)
    }

    private val dummyRefreshResponse = RefreshResponse(
        code = 200,
        message = "OK",
        data = Data(
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDIwMjN9.g4y-WkXHsk6gTxb72-L2Kk2Wv7dZ438zWZIfJ1Z9bER2Ob3ULnuo2ExBzq5S5l6eJ85PUYOeuiCUCeBRZ94RQQ",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDUwMjN9.U3FQQCGsyBCWE5qUOkWjneI_igtUj9bDKvJI-25o-8a6NMekmvvdlzjJVvK2Yyed9IpAaGTMXNgeQsl9M04uDA",
            expiresAt = 600
        )
    )

    @Test
    fun search() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("search.json"))
        mockWebServer.enqueue(response)
        val query = ""
        val actualData = apiService.search(query)
        val expectedData = dummySearchResponse
        assertEquals(expectedData, actualData)
    }

    private val dummySearchResponse = SearchResponse(
        code = 200,
        message = "OK",
        data = listOf(
            "Lenovo Legion 3",
            "Lenovo Legion 5",
            "Lenovo Legion 7",
            "Lenovo Ideapad 3",
            "Lenovo Ideapad 5",
            "Lenovo Ideapad 7"
        )
    )

    @Test
    fun detailProducts() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("detail.json"))
        mockWebServer.enqueue(response)
        val id = ""
        val actualData = apiService.detailProducts(id)
        val expectedData = dummyDetailResponse
        assertEquals(expectedData, actualData)
    }

    private val dummyDetailResponse = ProductsDetailResponse(
        code = 200,
        message = "OK",
        data = ProductsDetailResponseData(
            productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
            productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
            productPrice = 24499000,
            image = listOf(
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/0cc3d06c-b09d-4294-8c3f-1c37e60631a6.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/33a06657-9f88-4108-8676-7adafaa94921.jpg"
            ),
            brand = "Asus",
            description = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\n\nCPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\nGPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\nGraphics Memory : 6GB GDDR6\nDiscrete/Optimus : MUX Switch + Optimus\nTGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\nPanel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
            store = "AsusStore",
            sale = 12,
            stock = 2,
            totalRating = 7,
            totalReview = 5,
            totalSatisfaction = 100,
            productRating = 5.0f,
            productVariant = listOf(
                ProductVariantItem(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                ),
                ProductVariantItem(
                    variantName = "RAM 32GB",
                    variantPrice = 1000000
                )
            )
        )
    )

    @Test
    fun reviewProducts() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("reviewProduct.json"))
        mockWebServer.enqueue(response)
        val id = ""
        val actualData = apiService.reviewProducts(id)
        val expectedData = dummyReviewProductsResponse

        assertEquals(expectedData, actualData)
    }

    private val dummyReviewProductsResponse = ReviewResponse(
        code = 200,
        message = "OK",
        data = listOf(
            ReviewResponseItem(
                userName = "John",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
                userRating = 4,
                userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            ReviewResponseItem(
                userName = "Doe",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
                userRating = 5,
                userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        )
    )

    @Test
    fun fulfillment() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("fulfillment.json"))
        mockWebServer.enqueue(response)
        val body = FulfillmentRequest("", listOf())
        val actualData = apiService.fulfillment(body)
        val expectedData = dummyFulfillmentResponse
        assertEquals(expectedData, actualData)
    }

    private val dummyFulfillmentResponse = FulfillmentResponse(
        code = 200,
        message = "OK",
        data = FulfillmentResponseData(
            invoiceId = "ba47402c-d263-49d3-a1f8-759ae59fa4a1",
            status = true,
            date = "09 Jun 2023",
            time = "08:53",
            payment = "Bank BCA",
            total = 48998000
        )
    )

    @Test
    fun rating() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("rating.json"))
        mockWebServer.enqueue(response)
        val body = RatingRequest("", null, "")
        val actualData = apiService.rating(body)
        val expectedData = dummyRatingResponse
        assertEquals(expectedData, actualData)
    }

    private val dummyRatingResponse = RatingResponse(
        code = 200,
        message = "Fulfillment rating and review success"
    )

    @Test
    fun transaction() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("transaction.json"))
        mockWebServer.enqueue(response)
        val actualData = apiService.transaction()
        val expectedData = dummyTransactionResponse
        assertEquals(expectedData, actualData)
    }

    private val dummyTransactionResponse = TransactionResponse(
        code = 200,
        message = "OK",
        data = listOf(
            TransactionResponseData(
                invoiceId = "8cad85b1-a28f-42d8-9479-72ce4b7f3c7d",
                status = true,
                date = "09 Jun 2023",
                time = "09:05",
                payment = "Bank BCA",
                total = 48998000,
                rating = 4,
                review = "LGTM",
                image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                name = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
                items = listOf(
                    TransactionResponseItem(
                        productId = "bee98108-660c-4ac0-97d3-63cdc1492f53",
                        variantName = "RAM 16GB",
                        quantity = 2
                    )
                )
            )
        )
    )


    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path ?: "")
        return String(file.readBytes())
    }


}
