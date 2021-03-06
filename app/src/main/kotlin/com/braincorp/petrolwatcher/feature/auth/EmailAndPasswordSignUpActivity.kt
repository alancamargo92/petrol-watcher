package com.braincorp.petrolwatcher.feature.auth

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.braincorp.petrolwatcher.DependencyInjection
import com.braincorp.petrolwatcher.R
import com.braincorp.petrolwatcher.feature.auth.contract.EmailAndPasswordSignUpContract
import com.braincorp.petrolwatcher.feature.auth.presenter.EmailAndPasswordSignUpActivityPresenter
import com.braincorp.petrolwatcher.utils.showFieldError
import com.braincorp.petrolwatcher.utils.startProfileActivity
import kotlinx.android.synthetic.main.activity_email_and_password_sign_up.*
import kotlinx.android.synthetic.main.content_email_and_password_sign_up.*

/**
 * The activity where e-mail and password based
 * accounts are created
 */
class EmailAndPasswordSignUpActivity : AppCompatActivity(), EmailAndPasswordSignUpContract.View {

    override lateinit var presenter: EmailAndPasswordSignUpContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_and_password_sign_up)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter = EmailAndPasswordSignUpActivityPresenter(view = this,
                authenticator = DependencyInjection.authenticator)
        setupNextButton()
    }

    /**
     * Shows an error when the password confirmation
     * field is empty
     */
    override fun showEmptyConfirmationError() {
        showCardView()
        showFieldError(edt_password_confirmation, getString(R.string.error_empty_confirmation))
    }

    /**
     * Shows an error when the password and confirmation
     * don't match
     */
    override fun showPasswordNotMatchingError() {
        showCardView()
        showFieldError(edt_password_confirmation, getString(R.string.error_password_and_confirmation_dont_match))
    }

    /**
     * Shows an e-mail format error
     */
    override fun showEmailFormatError() {
        showCardView()
        showFieldError(edt_email, getString(R.string.error_email_format))
    }

    /**
     * Shows a backend error dialogue
     */
    override fun showBackendError() {
        showCardView()
        AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setIcon(R.drawable.ic_error)
                .setMessage(R.string.error_creating_account)
                .setNeutralButton(R.string.ok, null)
                .show()
    }

    /**
     * Shows the profile
     */
    override fun showProfile() {
        showCardView()
        startProfileActivity(finishCurrent = true)
    }

    /**
     * Shows a password length warning
     */
    override fun showPasswordLengthWarning() {
        showCardView()
        AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.warning_password_length)
                .setIcon(R.drawable.ic_warning)
                .setNeutralButton(R.string.ok, null)
                .show()
    }

    private fun setupNextButton() {
        fab.setOnClickListener {
            card_view.visibility = GONE
            progress_bar.visibility = VISIBLE

            val email = edt_email.text.toString()
            val password = edt_password.text.toString()
            val confirmation = edt_password_confirmation.text.toString()
            presenter.createAccount(email, password, confirmation)
        }
    }

    private fun showCardView() {
        progress_bar.visibility = GONE
        card_view.visibility = VISIBLE
    }

}