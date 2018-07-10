package com.braincorp.petrolwatcher.feature.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.braincorp.petrolwatcher.R
import com.braincorp.petrolwatcher.feature.auth.contract.EmailSignInContract
import com.braincorp.petrolwatcher.feature.auth.model.AuthErrorType
import com.braincorp.petrolwatcher.feature.auth.presenter.EmailSignInActivityPresenter
import com.braincorp.petrolwatcher.utils.dependencyInjection
import com.braincorp.petrolwatcher.utils.startAuthenticationErrorActivity
import com.braincorp.petrolwatcher.utils.startEmailAndPasswordSignUpActivity
import com.braincorp.petrolwatcher.utils.startMapActivity
import kotlinx.android.synthetic.main.activity_email_sign_in.*
import kotlinx.android.synthetic.main.content_email_sign_in.*

/**
 * The activity where e-mail and password based accounts
 * are signed in
 */
class EmailSignInActivity : AppCompatActivity(), View.OnClickListener, EmailSignInContract.View {

    override lateinit var presenter: EmailSignInContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_in)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupButtons()
        presenter = EmailSignInActivityPresenter(view = this,
                authenticator = dependencyInjection().getAuthenticator())
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_sign_in -> presenter.signIn(edt_email.text.toString(),
                    edt_password.text.toString())
            R.id.bt_sign_up -> startEmailAndPasswordSignUpActivity()
        }
    }

    /**
     * Shows an e-mail and password authentication
     * error screen
     */
    override fun showErrorScreen() {
        startAuthenticationErrorActivity(AuthErrorType.EMAIL_PASSWORD)
    }

    /**
     * Shows the map activity
     */
    override fun showMap() {
        startMapActivity(finishCurrent = true)
    }

    private fun setupButtons() {
        bt_sign_in.setOnClickListener(this)
        bt_sign_up.setOnClickListener(this)
    }

}