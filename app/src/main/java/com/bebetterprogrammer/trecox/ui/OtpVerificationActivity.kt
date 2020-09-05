package com.bebetterprogrammer.trecox.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bebetterprogrammer.trecox.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_otp_verification.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationInProgress = false
    private var mobileNumber: String = ""
    private var sec by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        auth = Firebase.auth

        mobileNumber = intent.getStringExtra("mobileNumber")!!

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                verificationInProgress = false
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                Toast.makeText(applicationContext, "Something went wrong! \nPlease check your Mobile number", Toast.LENGTH_LONG).show()

                verificationInProgress = false
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                Toast.makeText(applicationContext, "Message Sent Successfully!", Toast.LENGTH_LONG)
                    .show()

                storedVerificationId = verificationId
                resendToken = token
            }
        }
        // [END phone_auth_callbacks]

        verifyPhoneNumber()

        num_txt.setOnClickListener {
            if (num_txt.text == sec.toString()) {
                val changeIntent = Intent(this, LoginActivity::class.java)
                startActivity(changeIntent)
                finish()
            } else if (num_txt.text == "Resend") {
                verifyPhoneNumber()
                Toast.makeText(this, "Resend", Toast.LENGTH_LONG).show()
            }
        }

        btn_verify.setOnClickListener {
            val code = et_otp.text.toString()
            if (et_otp.text.toString() == "") {
                btn_verify.isEnabled = false
            } else {
                btn_verify.isEnabled = true
                verifyPhoneNumberWithCode(storedVerificationId, code)
            }
        }


    }

    private fun verifyPhoneNumber() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobileNumber,           // Phone number to verify
            30,                     // Timeout duration
            TimeUnit.SECONDS,       // Unit of timeout
            this,                   // Activity (for callback binding)
            callbacks
        )              // OnVerificationStateChangedCallbacks

        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() {
                tv_remaining_seconds.visibility = View.GONE
                num_txt.text = "Resend"
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_remaining_seconds.visibility = View.VISIBLE
                sec = (millisUntilFinished / 1000).toInt()
                num_txt.text = sec.toString()
            }

        }
        countDownTimer.start()
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
//        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
//        private const val STATE_INITIALIZED = 1
//        private const val STATE_VERIFY_FAILED = 3
//        private const val STATE_VERIFY_SUCCESS = 4
//        private const val STATE_CODE_SENT = 2
//        private const val STATE_SIGNIN_FAILED = 5
//        private const val STATE_SIGNIN_SUCCESS = 6
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")

                    val i = Intent(this, HomePageActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong \n Please try again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }
}