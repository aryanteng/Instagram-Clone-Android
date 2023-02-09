package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.databinding.ActivityLoginBinding
import com.example.instagramclone.databinding.ActivityPostsBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
open class PostsActivity : AppCompatActivity() {

    private var signedInUser: User? = null
    private lateinit var binding: ActivityPostsBinding
    private var posts: MutableList<Post> = mutableListOf()
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        postsAdapter = PostsAdapter(this, posts)

        binding.rvPosts.adapter = postsAdapter
        binding.rvPosts.layoutManager = LinearLayoutManager(this)

        val db = Firebase.firestore

        db.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid as String).get().addOnSuccessListener { userSnapshot ->
            signedInUser = userSnapshot.toObject(User::class.java)
            Log.i(TAG, "Signed In User: $signedInUser")
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        var postsReference = db.collection("Posts").limit(20).orderBy("createdAt", Query.Direction.DESCENDING)
        val username = intent.getStringExtra("username")
        if(username != null){
            supportActionBar?.title = username
            postsReference = postsReference.whereEqualTo("user.username", username)
        }

        postsReference.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying posts", error)
            }
            if (snapshot != null) {
                val postList = snapshot.toObjects(Post::class.java)
                posts.clear()
                posts.addAll(postList)
                postsAdapter.notifyDataSetChanged()
                for (post in postList){
                    Log.i(TAG, "Post $post")
                }
            }
        }

        binding.fabCreate.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_profile){
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("username", signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}