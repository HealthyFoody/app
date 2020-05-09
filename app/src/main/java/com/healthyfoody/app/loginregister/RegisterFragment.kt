package com.healthyfoody.app.loginregister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.UserRecoverableException
import com.healthyfoody.app.R
import com.healthyfoody.app.models.UserRequest
import com.healthyfoody.app.models.UserResponse
import com.healthyfoody.app.services.UserService
import kotlinx.android.synthetic.main.fragment_register.view.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private lateinit var editTextFirstName :EditText
    private lateinit var editTextLastName :EditText
    private lateinit var editTextEmail :EditText
    private lateinit var editTextPassword :EditText
    private lateinit var editTextConfirmPassword :EditText
    private lateinit var viewGroup : ViewGroup
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewGroup = container!!
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextFirstName = view.edit_name_register
        editTextLastName = view.edit_lastname_register
        editTextPassword = view.edit_password_register
        editTextConfirmPassword = view.edit_confirmpassword_register
        editTextEmail = view.edit_email_register


        view.findViewById<Button>(R.id.btn_enrollregister).setOnClickListener {
            registerUser()
        }
        view.findViewById<TextView>(R.id.txt_have_account_register).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
    fun registerUser(){
        val firstName = editTextFirstName.text.toString()
        val lastName = editTextLastName.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val passwordConfirm = editTextConfirmPassword.text.toString()
        if (password.equals(passwordConfirm)){
            val userRequest = UserRequest(email,password,firstName,lastName)
            val retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-18-224-64-139.us-east-2.compute.amazonaws.com:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val userService: UserService
            userService = retrofit.create(UserService::class.java)
            val request = userService.registerUser("application/json",userRequest)
            request.enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(viewGroup.context,"Error",Toast.LENGTH_LONG).show()
                    Log.d("loginregisterActivity", t.toString())
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful){
                        Toast.makeText(viewGroup.context,"Registro satisfactorio",Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    }else{
                        Toast.makeText(viewGroup.context,"q fue",Toast.LENGTH_LONG).show()
                    }
                }
            })

        }else{
            Toast.makeText(viewGroup.context,"Las contraseñas ingresadas no coinciden",Toast.LENGTH_LONG).show()
        }


    }
}
