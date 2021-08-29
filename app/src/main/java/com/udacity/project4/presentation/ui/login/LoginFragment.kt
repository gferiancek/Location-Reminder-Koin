package com.udacity.project4.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentLoginBinding

class LoginFragment : androidx.fragment.app.Fragment() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> this.onSignInResult(res) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_login,
            container,
            false
        )

        // If user is signed in, got to RemindersList or launch the sign in flow.
        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToRemindersListFragment())
        } else {
            launchSignInFlow()
        }
        return binding.root
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToRemindersListFragment())
        } else {
            // Used to close app if user presses the back button on the sign in flow and returns to the Login Fragment.
            requireActivity().finish()
        }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_location_reminder)
            .setTheme(R.style.ReminderFireBaseUI)
            .build()
        signInLauncher.launch(signInIntent)
    }
}