package com.healthyfoody.app.address

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.healthyfoody.app.R
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences
import com.healthyfoody.app.models.Address
import com.healthyfoody.app.models.GoogleMapsResponse
import com.healthyfoody.app.models.MainUserValues
import com.healthyfoody.app.services.AddressService
import com.healthyfoody.app.services.GoogleMapsApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var myLocation : LatLng = LatLng(-12.077470,-77.081976)
    private var zoom : Float = 13.0F
    private lateinit var btnAddAddress : Button
    private lateinit var addressService : AddressService
    private var gson = Gson()
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var mainInfo : MainUserValues
    private lateinit var txtName : EditText
    private lateinit var geocoder : Geocoder
    private var fullAddress = "Dirección autodefinida"
    private lateinit var gmapsService: GoogleMapsApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)
        sharedPreferences = SharedPreferences(this)
        mainInfo = gson.fromJson(sharedPreferences.getValue(CONSTANTS.MAIN_INFO), MainUserValues::class.java)
        btnAddAddress = findViewById(R.id.btn_add_address)
        txtName = findViewById(R.id.txt_name_address)
        geocoder = Geocoder(this,Locale.getDefault())

        val retrofitMaps = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        gmapsService = retrofitMaps.create(GoogleMapsApiService::class.java)



        btnAddAddress.setOnClickListener {
            val textName = txtName.text.toString()
            if(textName == ""){
                Toast.makeText(this,"Por favor llene el campo de apodo",Toast.LENGTH_SHORT).show()
            }else{

                val address = Address(myLocation.latitude.toFloat(),myLocation.longitude.toFloat(),textName,fullAddress,false,"",mainInfo.customerId!!)

                Log.d("DIRECCION ENVIADA",gson.toJson(address))

                val retrofit = Retrofit.Builder()
                    .baseUrl(CONSTANTS.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                addressService = retrofit.create(AddressService::class.java)

                addressService.addAddress("application/json",mainInfo.token!!,address).enqueue(object :
                    Callback<Address> {
                    override fun onFailure(call: Call<Address>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<Address>,
                        response: Response<Address>
                    ) {
                        if (response.isSuccessful) {
                            val data =  Intent()
                            val text = "Result to be returned...."
                            //---set the data to pass back---
                            data.setData(Uri.parse(text));
                            setResult(RESULT_OK, data);
                            //---close the activity---
                            finish();
                        }else{
                            Log.d("ADDRESS NOT SUCCESSFUL",gson.toJson(response.code()))
                        }
                    }
                })


            }

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            changeLocation(it)
        }
        // Add a marker in Sydney and move the camera
        mMap.addMarker(MarkerOptions().position(myLocation).title("Mi ubicación").icon(bitmapDescriptorFromVector(this,R.drawable.ic_store_black_24dp)))
        mMap.setMaxZoomPreference(20.0F)
        //drawCircle(myLocation)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,zoom))

    }
    private fun showStores(latLong:LatLng){

    }
    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    fun changeLocation(latLong : LatLng){
        myLocation = latLong
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(myLocation))
        val strLatLng = latLong.latitude.toString() + ","+latLong.longitude.toString()

        gmapsService.getAddress(strLatLng,getString(R.string.google_maps_key)).enqueue(object :
            Callback<GoogleMapsResponse> {
            override fun onFailure(call: Call<GoogleMapsResponse>, t: Throwable) {
                Log.e("ADDRESS NOT FOUND",t.toString())
            }

            override fun onResponse(
                call: Call<GoogleMapsResponse>,
                response: Response<GoogleMapsResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    if(result.response.isNotEmpty()){
                        fullAddress = result.response[0].formatter_address
                        Log.d("UBICACION GOOGLE MAPS",result.response[0].formatter_address)
                    }



                }else{
                    Log.d("ADDRESS NOT FOUND",gson.toJson(response.code()))
                }
            }
        })
        Log.d("LOCATION",latLong.toString())
    }

    private fun drawCircle(point: LatLng) {

        // Instantiating CircleOptions to draw a circle around the marker
        val circleOptions = CircleOptions()

        // Specifying the center of the circle
        circleOptions.center(point)

        // Radius of the circle
        circleOptions.radius(5000.0)

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK)

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000)

        // Border width of the circle
        circleOptions.strokeWidth(2f)

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions)
    }
    override fun onStart() {

        super.onStart()
    }
}
