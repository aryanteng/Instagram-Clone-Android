package com.example.instagramclone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.databinding.ActivityCreateBinding
import com.example.instagramclone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "CreateActivity"
class CreateActivity : AppCompatActivity() {
    private var signedInUser: User? = null
    private lateinit var binding: ActivityCreateBinding
    private var photoURI: Uri? = null
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode == Activity.RESULT_OK){
                    photoURI = it.data?.data
                    Log.i("data", "$photoURI")
                    binding.ivImage.setImageURI(photoURI)
                }
                else{
                    Toast.makeText(this, "Image Picking Action Canceled", Toast.LENGTH_SHORT).show()
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val db = Firebase.firestore

        db.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid as String).get().addOnSuccessListener { userSnapshot ->
            signedInUser = userSnapshot.toObject(User::class.java)
            Log.i(TAG, "Signed In User: $signedInUser")
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        binding.btnImage.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
            getResult.launch(imagePickerIntent)
        }

        binding.btnPost.setOnClickListener {
            onSubmitHandler()
        }
    }

    private fun onSubmitHandler(){
        if(photoURI == null){
            Toast.makeText(this, "Select an Image first!", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.etCaption.text.isBlank()){
            Toast.makeText(this, "Enter a caption!", Toast.LENGTH_SHORT).show()
            return
        }
        if(signedInUser == null){
            Toast.makeText(this, "User must be signed in!", Toast.LENGTH_SHORT).show()
            return
        }

    }

}