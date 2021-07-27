package com.example.mynotes.util

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mynotes.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_notes_bottom_sheet.*

class NoteBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedColor = "#171c26"

    companion object {
        var noteId=-1
        fun newInstance(id:Int): NoteBottomSheetFragment {
            val args = Bundle()
            val fragment = NoteBottomSheetFragment()
            fragment.arguments = args
            noteId=id
            return fragment
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notes_bottom_sheet, null)
        dialog.setContentView(view)
        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = param.behavior
        if (behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
        ///            TODO("Not yet implemented")
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                }


            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if( noteId!=-1){
            deleteLayout.visibility=View.VISIBLE
        }else{
            deleteLayout.visibility=View.GONE
        }
        setListener()
    }

    private fun setListener() {


        frameLayout1.setOnClickListener {
            imageView1.setImageResource(R.drawable.ic_check)
            imageView2.setImageResource(0)
            imageView33.setImageResource(0)
            imageView44.setImageResource(0)
            imageView55.setImageResource(0)
            imageView6.setImageResource(0)
            imageView7.setImageResource(0)
            selectedColor = "#4e33ff"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Blue")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        frameLayout2.setOnClickListener {
            imageView1.setImageResource(0)
            imageView2.setImageResource(R.drawable.ic_check)
            imageView33.setImageResource(0)
            imageView44.setImageResource(0)
            imageView55.setImageResource(0)
            imageView6.setImageResource(0)
            imageView7.setImageResource(0)
            selectedColor = "#ffd633"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Yellow")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        frameLayout3.setOnClickListener {
            imageView1.setImageResource(0)
            imageView2.setImageResource(0)
            imageView33.setImageResource(R.drawable.ic_check)
            imageView44.setImageResource(0)
            imageView55.setImageResource(0)
            imageView6.setImageResource(0)
            imageView7.setImageResource(0)
            selectedColor = "#ae3b76"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "White")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        frameLayout4.setOnClickListener {
            imageView1.setImageResource(0)
            imageView2.setImageResource(0)
            imageView33.setImageResource(0)
            imageView44.setImageResource(R.drawable.ic_check)
            imageView55.setImageResource(0)
            imageView6.setImageResource(0)
            imageView7.setImageResource(0)
            selectedColor = "#0aebaf"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Purple")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        frameLayout5.setOnClickListener {
            imageView1.setImageResource(0)
            imageView2.setImageResource(0)
            imageView33.setImageResource(0)
            imageView44.setImageResource(0)
            imageView55.setImageResource(R.drawable.ic_check)
            imageView6.setImageResource(0)
            imageView7.setImageResource(0)
            selectedColor = "#ff7746"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Green")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        frameLayout6.setOnClickListener {
            imageView1.setImageResource(0)
            imageView2.setImageResource(0)
            imageView33.setImageResource(0)
            imageView44.setImageResource(0)
            imageView55.setImageResource(0)
            imageView6.setImageResource(R.drawable.ic_check)
            imageView7.setImageResource(0)
            selectedColor = "#ff7746"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Orange")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        frameLayout7.setOnClickListener {
            imageView1.setImageResource(0)
            imageView2.setImageResource(0)
            imageView33.setImageResource(0)
            imageView44.setImageResource(0)
            imageView55.setImageResource(0)
            imageView6.setImageResource(0)
            imageView7.setImageResource(R.drawable.ic_check)
            selectedColor = "#4e33ff"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Gray")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        layoutImage.setOnClickListener {

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Image")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        layoutWebUrl.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "WebUrl")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }
        deleteLayout.setOnClickListener {   val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "DeleteNote")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent) }
    }


}