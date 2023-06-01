package com.example.presentation.ui.fragments.signin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.UserModel
import com.example.presentation.R
import com.example.presentation.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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
                    account.idToken?.let {
                        firebaseAuthWithGoogle(it)
                    }
                }
            } catch (e: ApiException) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fatal_exception_authorize),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    private val viewModel: SignInFragmentViewModel by viewModels()
    private val db = Firebase.firestore
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
        setupListener()
        checkAuthState()
    }

    private fun initialize() {
        progressDialog = ProgressDialog(requireContext())
        auth = Firebase.auth
        textChangedListener()
    }

    private fun setupListener() {
        binding.btnSignIn.setOnClickListener {
            val etName = binding.etSignForName.text.toString().trim()
            if (etName.isNotEmpty()) {
                progressDialog.setCancelable(false)
                progressDialog.setMessage("Пожалуйста подождите.")
                progressDialog.show()
                signInWithGoogle()
            } else {
                Toast.makeText(requireContext(), "Empty!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun textChangedListener() {
        binding.etSignForName.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val imm: InputMethodManager? =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Google signIn done", Toast.LENGTH_SHORT).show()
                checkUserAvailable()
            } else {
                progressDialog.dismiss()
                viewModel.authorize = false
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fatal_exception_authorize),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkUserAvailable() {
        val userEmail = auth.currentUser?.email.toString()
        db.collection("users")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.size() == 0) {
                    addNewUserInFirebase()
                } else {
                    document.documents.forEach {
                        val doc = it.toObject(UserModel::class.java)
                        if (userEmail == doc?.email.toString()) {
                            Toast.makeText(
                                requireContext(), "вы вошли как: ${doc?.name}", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    viewModel.authorize = true
                    findNavController().navigate(R.id.googleMapFragment)
                    progressDialog.dismiss()
                }
            }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
//                viewModel.userAvailable.collect{
//                    when(it){
//                        is UIState.Error -> {
//                            Log.e("check", "error")
//                        }
//                        is UIState.Loading -> {
//                            Log.e("check", "loading")
//                        }
//                        is UIState.Success -> {
//                            if (!it.data){
//                                Log.e("check", "0")
//                                addNewUserInFirebase()
//                            }
//                            else {
//                                Log.e("check", "1")
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun addNewUserInFirebase() {
        val userName = binding.etSignForName.text.toString().trim()
        val userEmail = auth.currentUser?.email.toString()
        val model = UserModel(userName, userEmail)

        db.collection("users").add(model).addOnSuccessListener {
            Toast.makeText(requireContext(), "Вы зарегистрированы", Toast.LENGTH_SHORT).show()
            Log.e("addUser", "adding user: $userName , $userEmail")
            viewModel.authorize = true
            findNavController().navigate(R.id.googleMapFragment)
            progressDialog.dismiss()
        }.addOnFailureListener {
            Log.e("addUser", it.localizedMessage ?: "error")
        }
    }

    private fun checkAuthState() {
        viewModel.authorize = auth.currentUser != null
    }
}