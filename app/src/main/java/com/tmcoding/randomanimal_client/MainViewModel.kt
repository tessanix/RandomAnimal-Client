package com.tmcoding.randomanimal_client

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmcoding.randomanimal_client.data.Animal
import com.tmcoding.randomanimal_client.data.AnimalsApi
import com.tmcoding.randomanimal_client.data.InputStreamRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: AnimalsApi
): ViewModel() {

    private val _state = mutableStateOf(AnimalState())
    val state: State<AnimalState> = _state

    data class AnimalState(
        val animal: Animal? = null,
        val isLoading: Boolean = false
    )

    fun getRandomAnimal(){
        viewModelScope.launch{
            try{
                _state.value = state.value.copy(isLoading = true)
                _state.value = state.value.copy(
                    animal = api.getRandomAnimal(),
                    isLoading = false
                )
            }catch(e: Exception){
                Log.e("MainViewModel", "getRandomAnimal: ", e)
                _state.value = state.value.copy(isLoading = true)
            }
        }
    }

    fun sendNewAnimal(
        contentUri: Uri,
        resolver: ContentResolver
    ) {
        viewModelScope.launch {
            val name = "nouvel animal".toRequestBody("text/plain".toMediaTypeOrNull())
            val description = "c'est un nouvel animal".toRequestBody("text/plain".toMediaTypeOrNull())
            val imageContentType = resolver.getType(contentUri)?.toMediaTypeOrNull()
            val contentPart = InputStreamRequestBody(imageContentType!!, resolver, contentUri)
            val image = MultipartBody.Part.createFormData(
                "image",
                "monNouveauFichierImage.jpeg",
                contentPart
            )

            try {
                val response = api.sendNewAnimal(name, description, image)
                Log.d("SERVER RESPONSE", response.toString())
            } catch (e: Exception) {
                Log.e("ERROR", e.message, e)
            }catch (e: HttpException) {
                e.printStackTrace()
            }
        }
    }


}