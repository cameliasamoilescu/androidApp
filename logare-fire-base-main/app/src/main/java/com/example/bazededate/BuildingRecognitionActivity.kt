package com.example.bazededate

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bazededate.ml.ModelLastV2Withmetadata
import com.example.bazededate.ml.ModelLastV4
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import java.nio.ByteBuffer

class BuildingRecognitionActivity: AppCompatActivity() {
        lateinit var bitmap: Bitmap
        lateinit var imgview: ImageView
        private lateinit var label: String
        private lateinit var database: DatabaseReference
        private lateinit var addressId: String



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_building_recognition)

            var select: Button = findViewById(R.id.choose_pic)
            var homeButton: FloatingActionButton = findViewById(R.id.home)
            var predictionHolder: TextView = findViewById(R.id.prediction)
            var predict: Button = findViewById(R.id.predict)
            var more_info: Button = findViewById(R.id.discover_more)
            imgview = findViewById(R.id.imageToBeClassified)


            database = FirebaseDatabase.getInstance().getReference("recogntion_labels")
            label = ""
            select.setOnClickListener(View.OnClickListener {
                openGalleryForImage();
            })

            homeButton.setOnClickListener({
                val intent = Intent(applicationContext, HelloActivity::class.java)
                startActivity(intent)
                finish()
            })



            predict.setOnClickListener(View.OnClickListener {
                var resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)


                val model = ModelLastV4.newInstance(this)


                val inputFeature0 = TensorImage.fromBitmap(resized)


                var outputs = model.process(inputFeature0)


                predictionHolder.setText(getMax(outputs.probabilityAsCategoryList))

                model.close()

                more_info.visibility = View.VISIBLE


            })

            more_info.setOnClickListener {
                addPostEventListener(database)

            }
        }

    private fun addPostEventListener(postReference: DatabaseReference) {
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                var buildings: Iterable<DataSnapshot> = dataSnapshot.children
                for (building in buildings) {
                    if (building.key != null)
                        if (building.key.equals(label)){
                            Log.w("Location recognition", building.child("id_locatie").value.toString())

                            addressId = building.child("id_locatie").value.toString()
                            val database1: DatabaseReference = FirebaseDatabase.getInstance().getReference("locations")
                            addPostEventListenerLocation(database1, addressId)

                        }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Recognised building address", "When getting the address of the recognised building", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
        // [END post_value_event_listener]
    }

    private fun addPostEventListenerLocation(postReference: DatabaseReference, idd: String) {
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI



                    if (dataSnapshot.child(idd).exists()) {
                        Log.w("Location found in database", dataSnapshot.child("id").value.toString())

                        var location: Location = dataSnapshot.child(idd).getValue(Location::class.java) as Location
                        Log.w("checky", location.story)
                        val user = FirebaseAuth.getInstance().currentUser
                        val intent = Intent(applicationContext, ShowInfoActivity::class.java)
                        intent.putExtra("Location", location)
                        intent.putExtra("UID", user!!.uid)
                        startActivity(intent)
                    }



            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Recognised building address", "When getting the address of the recognised building", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)
        // [END post_value_event_listener]
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            super.onActivityResult(requestCode, resultCode, data)
            imgview.setImageURI(data?.data)

            var uri: Uri? = data?.data

            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }


    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }


    fun getMax(arr: MutableList<Category>): String {
            var ind: String
            ind = ""
            var min = 0.0f

            for (i in 0..3) {
                println(arr[i])
                if (arr[i].score > min) {
                    min = arr[i].score
                    ind = arr[i].label
                }

            }
            label = ind
            return ind
        }
    }
