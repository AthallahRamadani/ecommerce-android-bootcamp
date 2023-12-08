package com.athallah.ecommerce.fragment.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Review
import com.athallah.ecommerce.utils.compose.AppTheme
import com.athallah.ecommerce.utils.compose.poppinsFamily
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewFragmentCompose : Fragment() {

    private val viewModel: ReviewViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Membuat tampilan menggunakan ComposeView
        return ComposeView(requireContext()).apply {
            // Menentukan kontennya dengan menggunakan Compose (UI Framework)
            setContent {
                // Menerapkan tema ke UI
                AppTheme {
                    // Mendapatkan state terkini dari ulasan produk menggunakan viewModel
                    val resultState = viewModel.reviewProductState.collectAsState().value

                    // Menampilkan UI untuk menampilkan ulasan produk (mungkin di dalam ReviewFragmentScreen)
                    ReviewFragmentScreen(
                        resultState = resultState,
                        // Aksi ketika tombol kembali ditekan
                        onBackAction = {
                            // Melakukan navigasi ke atas (kembali)
                            findNavController().navigateUp()
                        },
                        // Aksi ketika terjadi kesalahan
                        onErrorAction = {
                            // Memanggil fungsi untuk mendapatkan ulasan produk
                            viewModel.getListReview()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val BUNDLE_PRODUCT_ID_KEY = "product_id"
    }
}

@Composable
fun ReviewFragmentScreen(
    resultState: ResultState<List<Review>>?,
    onBackAction: () -> Unit,
    onErrorAction: () -> Unit
) { // Scaffold adalah komponen yang menyediakan struktur dasar tata letak
    Scaffold(
        // Menambahkan top bar dengan tombol kembali
        topBar = { ReviewAppBar(onBackAction) }
    ) {
        // Column digunakan untuk mengatur tata letak vertikal
        Column(
            Modifier
                .padding(it) // Menambahkan padding sesuai dengan top bar
                .fillMaxSize() // Mengisi ruang maksimal yang tersedia
        ) {
            // Menangani berbagai hasil state
            when (resultState) {
                is ResultState.Loading -> Loading(Modifier.fillMaxSize())
                is ResultState.Success -> ReviewList(resultState.data)
                is ResultState.Error -> Error(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    onErrorAction
                )
                else -> {} // Kasus lainnya (mungkin tidak ada aksi)
            }
        }
    }
}

@Composable
fun ReviewList(listData: List<Review>) {
    // Menggunakan LazyColumn untuk efisiensi pemuatan item yang besar
    LazyColumn {
        // Menerapkan items() untuk membuat item sebanyak jumlah data yang ada
        items(listData) { data ->
            // Menampilkan elemen ulasan menggunakan ReviewItem
            ReviewItem(data)
        }
    }
}

@Composable
fun ReviewItem(data: Review) {
    // Menggunakan Column untuk tata letak vertikal
    Column {
        // Menggunakan Row untuk tata letak horizontal
        Row(Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)) {
            // AsyncImage digunakan untuk menampilkan gambar dengan pemuatan asinkron
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.userImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.product_image_placeholder),
                error = painterResource(id = R.drawable.product_image_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            // Column untuk menyusun teks nama pengguna dan peringkat ulasan
            Column(Modifier.padding(start = 8.dp)) {
                // Menampilkan nama pengguna
                Text(
                    text = data.userName,
                    fontSize = 12.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
                // Menampilkan peringkat ulasan dalam bentuk bintang
                Row {
                    for (i in 1..5) {
                        val color =
                            if (i <= data.userRating) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.outlineVariant
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        // Menampilkan teks ulasan pengguna
        Text(
            text = data.userReview,
            modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp),
            fontSize = 12.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        // Menambahkan pembatas antara item ulasan
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}


@Composable
fun Loading(modifier: Modifier = Modifier) {
    // Column digunakan untuk mengatur tata letak vertikal
    Column(
        modifier,
        verticalArrangement = Arrangement.Center, // Menengahkan vertikal
        horizontalAlignment = Alignment.CenterHorizontally // Menengahkan horizontal
    ) {
        // Menampilkan indikator loading (ProgressBar)
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAppBar(onBackAction: () -> Unit) {
    // Column digunakan untuk mengatur tata letak vertikal
    Column(Modifier.fillMaxWidth()) {
        // TopAppBar adalah komponen app bar dari Material Design
        TopAppBar(
            title = {
                // Menampilkan teks judul
                Text(
                    stringResource(id = R.string.buyer_review), // Mengambil string dari sumber daya
                    fontSize = 22.sp,
                    style = TextStyle(
                        fontFamily = poppinsFamily, // Mengatur jenis huruf
                        fontWeight = FontWeight.Normal, // Mengatur ketebalan huruf
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    )
                )
            },
            navigationIcon = {
                // Menampilkan tombol navigasi (back button)
                IconButton(onClick = { onBackAction() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24), // Mengambil ikon dari sumber daya
                        contentDescription = null
                    )
                }
            }
        )
        // Menambahkan pembatas (divider) setelah app bar
        Divider()
    }
}

@Composable
fun Error(modifier: Modifier = Modifier, onErrorAction: () -> Unit) {

    // Column digunakan untuk mengatur tata letak vertikal
    Column(
        modifier,
        verticalArrangement = Arrangement.Center, // Menengahkan vertikal
        horizontalAlignment = Alignment.CenterHorizontally // Menengahkan horizontal
    ) {
        // Menampilkan gambar untuk menunjukkan bahwa ada kesalahan
        Image(
            painter = painterResource(id = R.drawable.smartphone), // Mengambil gambar dari sumber daya
            contentDescription = null,
            modifier = Modifier.size(128.dp) // Mengatur ukuran gambar
        )
        // Spacer digunakan untuk memberikan ruang antar elemen
        Spacer(modifier = Modifier.height(8.dp))
        // Menampilkan judul kesalahan dengan ukuran dan gaya tertentu
        Text(
            text = stringResource(id = R.string.empty_title),
            fontSize = 32.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Menampilkan subjudul kesalahan dengan ukuran dan gaya tertentu
        Text(
            text = stringResource(R.string.empty_subtitle),
            fontSize = 16.sp,
            style = TextStyle(
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false,
                ),
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Button digunakan untuk menampilkan tombol
        Button(onClick = { onErrorAction() }) {
            // Menampilkan teks pada tombol dengan ukuran dan gaya tertentu
            Text(
                text = stringResource(R.string.reset),
                fontSize = 14.sp,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewFragmentPreview() {
    ReviewFragmentScreen(
        resultState = ResultState.Success(listOf(Review("","joko","wwww",3))),
        onBackAction = {},
        onErrorAction = {}
    )
}

