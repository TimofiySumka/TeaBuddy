package com.example.teabuddy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.teabuddy.BottomNav.HomePage.HomePageFragment
import com.example.teabuddy.BottomNav.Map.MapFragment
import com.example.teabuddy.BottomNav.Profile.ProfileFragment
import com.example.teabuddy.BottomNav.Teas.ShelfFragment
import com.example.teabuddy.BottomNav.ToolsPage.ToolsFragment
import com.example.teabuddy.Teas.TeaModel
import com.example.teabuddy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var ArrayList:ArrayList<TeaModel>

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomePageFragment())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()


        //Перекинути на реєстрацію
        val UserID = currentUser?.uid
        if (UserID == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()}
        //Завантажити ім'я
        else{
            firestore.collection("Users").document(UserID).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        editor.putString("UserName", document.getString("name").toString())
                        editor.putString("NormalizedUID","@"+document.getString("normalizedUID").toString())
                        editor.apply()
                    }
                }
        }

        //Нижня навігація
        binding.BottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.Home -> {
                    HomePageFragment()
                }
                R.id.Tools ->{
                    ToolsFragment()
                }
                R.id.Profile -> {
                    ProfileFragment()
                }
                R.id.Shelf->{
                    ShelfFragment()
                }
                R.id.Map->{
                    MapFragment()
                }
                else -> {
                    HomePageFragment()
                }
            }
            replaceFragment(fragment)
            true
        }


}

    //Зміна фрагментів
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FragmentContainer, fragment)

        fragmentTransaction.commit()


    }

}
