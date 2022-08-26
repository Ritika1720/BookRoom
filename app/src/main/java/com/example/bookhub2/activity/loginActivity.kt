package com.example.bookhub2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.bookhub2.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class loginActivity : AppCompatActivity() {
    lateinit var sendButton:Button
    lateinit var phoneNo:EditText
    lateinit var verifyButton:Button
    lateinit var authCode:String
    lateinit var phonetxt: TextView
    lateinit var login:androidx.constraintlayout.widget.ConstraintLayout
    lateinit var verification:androidx.constraintlayout.widget.ConstraintLayout
    lateinit var progressLayout: RelativeLayout
    val TAG="loginActivitylog"
    var mAuth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        verifyButton=findViewById(R.id.button2)
        phonetxt=findViewById(R.id.phonetxt)
        var code1:EditText=findViewById(R.id.code1)
        var code2:EditText=findViewById(R.id.code2)
        var code3:EditText=findViewById(R.id.code3)
        var code4:EditText=findViewById(R.id.code4)
        var code5:EditText=findViewById(R.id.code5)
        var code6:EditText=findViewById(R.id.code6)
        progressLayout=findViewById(R.id.progressLayout)
        login=findViewById(R.id.login)
        verification=findViewById(R.id.verification)
        phoneNo = findViewById(R.id.phoneNo)
        sendButton = findViewById(R.id.button1)

        mAuth = FirebaseAuth.getInstance()
        sendButton.setOnClickListener {
            if(!phoneNo.text.isNullOrBlank()){
                if(phoneNo.text.length==10) {
                    val phonenumber = "+91" +phoneNo.text.toString()
                    sendVerificationCode(phonenumber)
                }else{
                    Toast.makeText(this, "Please Enter 10 Digits Phone Number", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@loginActivity, "Fill the details", Toast.LENGTH_SHORT).show()
            }

        }
        verifyButton.setOnClickListener{

            if (code1.text.isNotEmpty()
                && code2.text.isNotEmpty()
                && code3.text.isNotEmpty()
                && code4.text.isNotEmpty()
                && code5.text.isNotEmpty()
                && code6.text.isNotEmpty()
            ){
                val enteredCode:String="${code1.text}${code2.text}${code3.text}${code4.text}${code5.text}${code6.text}"
                Log.d(TAG,"USER CODE $enteredCode")
                Log.d(TAG,"authcode "+authCode)
                    var credential=PhoneAuthProvider.getCredential( authCode,enteredCode)
                    signInWithCredentials(credential)
            }
            else{
                Toast.makeText(this, "fill the required block", Toast.LENGTH_SHORT).show()
            }
        }
        val resend:TextView=findViewById(R.id.resend)
        resend.setOnClickListener{
            againSendVerificationCode("+91"+phoneNo.text.toString())
        }
        testCursorMove(code1,code2,code3,code4,code5,code6)

    }
    private fun againSendVerificationCode(number:String?){

        val options = PhoneAuthOptions.newBuilder(mAuth!!).setPhoneNumber(number!!)
            .setActivity(this).setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallBack).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            authCode=p0
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(
                this@loginActivity,
                "UNable to send code!!" + p0.message,
                Toast.LENGTH_SHORT).show()
        }

    }
    private fun testCursorMove(code1: EditText, code2: EditText, code3: EditText, code4: EditText, code5: EditText, code6: EditText) {
        code1.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().isEmpty()){
                    code2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        code2.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().isEmpty()){
                    code3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        code3.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().isEmpty()){
                    code4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        code4.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().isEmpty()){
                    code5.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        code5.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().isEmpty()){
                    code6.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun signInWithCredentials(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){

                verifyButton.visibility= View.GONE
                progressLayout.visibility= View.VISIBLE
                //send user to Dashboard
                Handler().postDelayed(
                    { val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()},1300)

            }
            else{
                //show Error
                Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!).setPhoneNumber(number)
            .setActivity(this).setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallBacks).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
              authCode=p0
            Log.d(TAG,"first $authCode real $p0")
            phonetxt.text="code sent to +91"+phoneNo.text.toString()
            login.visibility=View.GONE
            verification.visibility=View.VISIBLE

        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(
                this@loginActivity,
                "UNable to send code!!" + p0.message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onStart() {
        if (mAuth?.currentUser!=null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }

    }