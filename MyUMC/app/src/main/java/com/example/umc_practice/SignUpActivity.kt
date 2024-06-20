package com.example.umc_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.umc_practice.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener{
            signUp()
            finish()
        }
    }

    private fun  getUser() : User {
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd : String = binding.signUpPasswordEt.text.toString()

        return User(email, pwd)
    }

    private fun signUp() {
        if(binding.signUpIdEt.text.toString().isEmpty() ||  binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpPasswordEt.text.toString() !=  binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.UserDao().insert(getUser())

        val user = userDB.UserDao().getUsers()
        Log.d("SIGNUPACT", user.toString())
    }
}