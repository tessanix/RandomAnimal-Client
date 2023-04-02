package com.tmcoding.randomanimal_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.tmcoding.randomanimal_client.ui.theme.RandomAnimalClientTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val animal = viewModel.state.value.animal
            val isLoading = viewModel.state.value.isLoading

            RandomAnimalClientTheme {
                // A surface container using the 'background' color from the theme
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ){

                    animal?.let {
                        Image(
                            painter = rememberAsyncImagePainter(animal.imageUrl),
                            contentDescription = "Animal"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (animal != null) {
                        Text(
                            text=animal.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (animal != null) {
                        Text(text=animal.description)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = viewModel::getRandomAnimal,
                        modifier = Modifier.align(Alignment.End)
                    ){
                        Text(text="New Animal!")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if(isLoading){
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

