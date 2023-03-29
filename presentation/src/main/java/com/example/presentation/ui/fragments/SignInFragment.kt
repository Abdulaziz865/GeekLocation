package com.example.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.presentation.R
import com.example.presentation.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

//    private lateinit var launcher: ActivityResultLauncher<Intent>
//    private lateinit var auth: FirebaseAuth
//    private val secondary = Firebase.app("[DEFAULT]")
//    private val secondaryDatabase = Firebase.auth(secondary)
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        initialize()
        setupListener()
    }

//    private fun initialize() {
//
//        Firebase.initialize(requireContext(), options, "GeekLocation2")
//
//        auth = Firebase.auth
//        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    firebaseAuthWithGoogle(account.idToken!!)
//                }
//            } catch (e: ApiException) {
//                Toast.makeText(
//                    requireContext(),
//                    "An error has occurred, please try again later",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
    private fun setupListener() = with(binding) {
        btnSignIn.setOnClickListener {

        }
    }

//    private fun getClient(): GoogleSignInClient {
//        val gso = GoogleSignInOptions
//            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        return GoogleSignIn.getClient(requireContext(), gso)
//    }

//    private fun signInWithGoogle() {
//        val signInClient = getClient()
//        launcher.launch(signInClient.signInIntent)
//    }

//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        secondaryDatabase.signInWithCredential(credential).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Toast.makeText(requireContext(), "Google signIn done", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "An error has occurred, please try again later",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
}