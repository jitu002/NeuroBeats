package com.neurobeat.neurobeats.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


// All logic for login, signup, forgot password here.
class AuthenticationViewModel: ViewModel() {

    private val auth:FirebaseAuth=FirebaseAuth.getInstance()

    private val _authState=MutableLiveData<AuthenticationState>()
    val authState:LiveData<AuthenticationState> = _authState

    init {
        checkStatus()
    }


    fun checkStatus(){
        if(auth.currentUser==null){
            _authState.value=AuthenticationState.NotAuthenticated
        }
        else{
            _authState.value=AuthenticationState.Authenticated
        }
    }

    fun login(email: String,password: String){

        if (email.isEmpty() || password.isEmpty()){
            _authState.value=AuthenticationState.Error("Email or password can't be empty")
        }

        _authState.value=AuthenticationState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {result->
                if(result.isSuccessful){
                    _authState.value=AuthenticationState.Authenticated
                }
                else{
                    _authState.value=AuthenticationState.Error(result.exception?.message?:"Something's not right")
                }

            }
    }


    fun signUp (email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()){
            _authState.value=AuthenticationState.Error("Email or password can't be empty")
        }


        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {result->
                if(result.isSuccessful){
                    _authState.value=AuthenticationState.Authenticated
                }
                else{
                    _authState.value=AuthenticationState.Error(result.exception?.message?:"Something's not right")
                }

            }
    }

    fun signOut(){
        auth.signOut()
        _authState.value=AuthenticationState.NotAuthenticated

    }
}


sealed class AuthenticationState{
    object Authenticated:AuthenticationState()
    object NotAuthenticated:AuthenticationState()
    object Loading:AuthenticationState()
    data class Error(val errMessage: String):AuthenticationState()

}