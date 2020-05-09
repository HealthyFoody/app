package com.healthyfoody.app.loginregister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.healthyfoody.app.MainActivity
import com.healthyfoody.app.R
import com.healthyfoody.app.models.Token
import com.healthyfoody.app.models.UserRequest
import com.healthyfoody.app.models.UserResponse
import com.healthyfoody.app.services.UserService
import kotlinx.android.synthetic.main.fragment_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {
    private lateinit var editTextEmail : EditText
    private lateinit var editTextPassword : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        editTextEmail = view.edit_text_email_login
        editTextPassword = view.edit_text_password_login

        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            login(container!!)

        }
        view.findViewById<Button>(R.id.btn_register).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        return view
    }
    fun login(viewGroup:ViewGroup) {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        val user = UserRequest(email,password,"","")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-18-224-64-139.us-east-2.compute.amazonaws.com:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService: UserService
        userService = retrofit.create(UserService::class.java)
        val request = userService.loginUser("application/json",user)
        request.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(viewGroup.context,"Error", Toast.LENGTH_LONG).show()
                Log.d("loginregisterActivity", t.toString())
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful){
                    Toast.makeText(viewGroup.context,"Login satisfactorio", Toast.LENGTH_LONG).show()
                    var mainActivity = Intent(
                        viewGroup.context,
                        MainActivity::class.java)
                    mainActivity.putExtra("token",response.body()!!.token)
                    startActivity(mainActivity)
                }else{
                    Toast.makeText(viewGroup.context,"q fue " + response.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
