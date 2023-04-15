package com.example.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.presentation.R
import com.example.presentation.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    account.idToken?.let { firebaseAuthWithGoogle(it) }
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fatal_exception_authorize),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private lateinit var auth: FirebaseAuth
    private val viewModel: SignInFragmentViewModel by viewModels()
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
        setupListener()
        checkAuthState()
    }

    private fun initialize() {
        auth = Firebase.auth
    }

    private fun setupListener() {
        binding.btnSignIn.setOnClickListener {
            val etName = binding.etSignForName.text.toString()
            if (etName.isNotEmpty()) {
                signInWithGoogle()
            } else {
                binding.etSignForName.error = getString(R.string.empty_edit_text)
            }
        }
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Google signIn done", Toast.LENGTH_SHORT).show()
                viewModel.saveDataAuthorize(getString(R.string.isAuthorize), true)
                findNavController().navigate(R.id.googleMapFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fatal_exception_authorize),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkAuthState() {
        if (auth.currentUser != null) {
            viewModel.saveDataAuthorize(getString(R.string.isAuthorize), true)
        }
    }
}