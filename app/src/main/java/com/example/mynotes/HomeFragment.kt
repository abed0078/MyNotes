package com.example.mynotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.adapter.NotesAdapter
import com.example.mynotes.database.NotesDatabase
import com.example.mynotes.entities.Notes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {
    var notesAdapter: NotesAdapter = NotesAdapter()
    var arrNotes = ArrayList<Notes>()
    // TODO: Rename and change types of parameter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        arguments?.let {
            var flo: FloatingActionButton

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        launch {
            context?.let {
                var notes = NotesDatabase.getInstance(it).noteDao().getAllNotes()
                notesAdapter!!.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                recycler_view.adapter = notesAdapter

            }
        }
            notesAdapter!!.setOnClickListener(onClicked)
        fabBtnCreateNote.setOnClickListener {
            replaceFragment(CreateNoteFragment.newInstance(), true)
        }
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr=ArrayList<Notes>()
                for (arr in arrNotes){
                    if(arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }
                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }

        })
    }

    private val onClicked=object :NotesAdapter.OnItemClickListener{
        override fun onClicked(notesId: Int) {
            var fragment:Fragment
            var bundle=Bundle()
            bundle.putInt("noteId",notesId)
            fragment=CreateNoteFragment.newInstance()
            fragment.arguments=bundle
            replaceFragment(fragment, false)

        }

    }

    fun replaceFragment(fragment: Fragment, istransition: Boolean) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        if (istransition) {
            fragmentTransaction.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransaction.replace(R.id.frameLayout, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commit()
    }


}
