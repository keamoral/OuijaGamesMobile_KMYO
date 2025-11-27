package com.example.adoptapetmobile.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AuthViewModel : ViewModel(){
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registrar(usuario: String, rut: String, correo: String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val uid = auth.currentUser?.uid ?: ""
                    val data = hashMapOf(
                        "usuario" to usuario,
                        "rut" to rut,
                        "correo" to correo
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            withTimeout(5000) { // 5 segundos de timeout
                                db.collection("usuarios").document(uid).set(data).await()
                            }
                            onResult(true, null)
                        } catch (e: Exception) {
                            onResult(true, null)
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun iniciarSesion(correo: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    onResult(true, null)
                }else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun cerrarSesion(){
        auth.signOut()
    }
}