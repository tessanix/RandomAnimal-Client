package com.tmcoding.randomanimal_client

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.tmcoding.randomanimal_client.ui.theme.RandomAnimalClientTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

            val singlePhotoPikerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri -> selectedImageUri = uri}
            )

            val viewModel : MainViewModel = hiltViewModel()
            val animal = viewModel.state.value.animal
            val isLoading = viewModel.state.value.isLoading

            RandomAnimalClientTheme {
                // A surface container using the 'background' color from the theme
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = rememberAsyncImagePainter(
                            model = animal?.imageUrl,
                            fallback = painterResource(id = R.drawable.ic_launcher_background),
                            error = painterResource(id = R.drawable.ic_launcher_background)
                        ),
                        contentDescription = "Animal",
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (animal != null) {
                        Text(
                            text=animal.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text=animal.description)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = viewModel::getRandomAnimal,
                    ){
                        Text(text="Display a new animal!")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if(isLoading){
                        CircularProgressIndicator()
                    }

                    Button(onClick = {
                        singlePhotoPikerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        Log.d("URI",selectedImageUri.toString())
                    }) {
                        Text(text="Create a new animal")
                    }

                    AsyncImage(model = selectedImageUri, contentDescription = null )

                    selectedImageUri?.let{ uri ->
                        Button(onClick = { viewModel.sendNewAnimal(uri, contentResolver) }
                        ) {
                            Text(text="Send your new animal")
                        }
                    }
                }
            }
        }
    }
}
