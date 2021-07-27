package com.example.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment.newInstance(), true)
    }

    fun replaceFragment(fragment: Fragment, istransition: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (istransition) {
            fragmentTransaction.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransaction.replace(R.id.frameLayout, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commit()

    }
}