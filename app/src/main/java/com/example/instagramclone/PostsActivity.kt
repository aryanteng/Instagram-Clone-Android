package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.instagramclone.models.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        val db = Firebase.firestore
        val postsReference = db.collection("Posts").limit(20).orderBy("createdAt", Query.Direction.DESCENDING)
        postsReference.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying posts", error)
            }
            if (snapshot != null) {
                val postList = snapshot.toObjects(Post::class.java)
                for (post in postList){
                    Log.i(TAG, "Post $post")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId === R.id.menu_profile){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}