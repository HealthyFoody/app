package com.healthyfoody.app.address

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.healthyfoody.app.R


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var myLocation : LatLng = LatLng(-12.077470,-77.081976)
    private var zoom : Float = 12.0F
    private lateinit var btnAddAddress : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        btnAddAddress = findViewById<Button>(R.id.btn_add_address)
        btnAddAddress?.setOnClickListener {
            var data =  Intent()
            var text = "Result to be returned...."
            //---set the data to pass back---
            data.setData(Uri.parse(text));
            setResult(RESULT_OK, data);
            //---close the activity---
            finish();
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
        if (latLong != null){
            myLocation = latLong
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(myLocation))
        }
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
