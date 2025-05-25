package project.hackmol.hackmolinstafix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import project.hackmol.hackmolinstafix.repository.AuthRepository
import project.hackmol.hackmolinstafix.util.Resource

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _loginResult = MutableLiveData<Resource<FirebaseUser>>()
    val loginResult: LiveData<Resource<FirebaseUser>> = _loginResult

    private val _registerResult = MutableLiveData<Resource<FirebaseUser>>()
    val registerResult: LiveData<Resource<FirebaseUser>> = _registerResult

    private val _resetPasswordResult = MutableLiveData<Resource<Unit>>()
    val resetPasswordResult: LiveData<Resource<Unit>> = _resetPasswordResult

    val isUserLoggedIn: Boolean
        get() = authRepository.isUserLoggedIn()

    val currentUser: FirebaseUser?
        get() = authRepository.getCurrentUser()

    fun loginUser(email: String, password: String) {
        _loginResult.value = Resource.Loading
        viewModelScope.launch {
            val result = authRepository.loginUser(email, password)
            _loginResult.value = result
        }
    }

    fun registerUser(name: String, email: String, password: String) {
        _registerResult.value = Resource.Loading
        viewModelScope.launch {
            val result = authRepository.registerUser(name, email, password)
            _registerResult.value = result
        }
    }

    fun resetPassword(email: String) {
        _resetPasswordResult.value = Resource.Loading
        viewModelScope.launch {
            val result = authRepository.resetPassword(email)
            _resetPasswordResult.value = result
        }
    }

    fun logout() {
        authRepository.logout()
    }
}
