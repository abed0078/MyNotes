package com.example.mynotes

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mynotes.database.NotesDatabase
import com.example.mynotes.entities.Notes
import com.example.mynotes.util.NoteBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.fragment_create_note.tvDateTime
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notes_bottom_sheet.*
import kotlinx.android.synthetic.main.item_rv_notes.*
import kotlinx.android.synthetic.main.item_rv_notes.view.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class CreateNoteFragment : BaseFragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    //    val imageNotes = view!!.findViewById<ImageView>(R.id.imgNote)
    var selectedColor = "#171c26"
    var currentDate: String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var noteId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = requireArguments().getInt("noteId", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        if (noteId != -1) {
            launch {
                context?.let {
                    var notes = NotesDatabase.getInstance(it).noteDao().getSpecificNotes(noteId)
                    colorView.setBackgroundColor(Color.parseColor(notes.color))
                    etNoteTitle.setText(notes.title)
                    etNoteSubTitle.setText(notes.subtitle)
                    etNoteDesc.setText(notes.noteText)
                    if (notes.imagePath != "") {
                        selectedImagePath = notes.imagePath!!
                        imgNote.setImageBitmap(BitmapFactory.decodeFile(notes.imagePath))
                        ///////////////////////same layout
                        imgNote.visibility = View.VISIBLE
                        deleteImage.visibility = View.VISIBLE
                        ///////////////////////////////////

                    } else {

                        imgNote.visibility = View.GONE
                        deleteImage.visibility = View.GONE

                    }
                    if (notes.webLink != "") {
                        imgDeleteUrl.visibility=View.VISIBLE
                        webLink = notes.webLink!!
                        tvWebLink.text = notes.webLink
                        llayoutWebUrl.visibility = View.VISIBLE
                        etWebLink.setText(notes.webLink)

                    } else {

                        llayoutWebUrl.visibility = View.GONE
                        imgDeleteUrl.visibility=View.GONE

                    }

                }
            }
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )
        currentDate = sdf.format(Date())
        colorView.setBackgroundColor(Color.parseColor(selectedColor))
        tvDateTime.text = currentDate

        imgDone.setOnClickListener {
            if (noteId != -1) {
                updateNote()

            } else {
                saveNote()

            }

        }


        imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        imageMore.setOnClickListener {
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(noteId)
            noteBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Note Bottom Sheet Fragment"
            )
        }

        deleteImage.setOnClickListener {
            selectedImagePath = ""
            imgNote.visibility = View.GONE
            deleteImage.visibility = View.GONE

        }
        btnOk.setOnClickListener {
            if (etWebLink.text.toString().trim().isNotEmpty()) {
                checkWebUrl()
            } else {
                Toast.makeText(requireContext(), "Url is required", Toast.LENGTH_SHORT).show()
            }
        }
        imgDeleteUrl.setOnClickListener {
            webLink=""
            tvWebLink.visibility=View.GONE
           llayoutWebUrl.visibility=View.GONE
            imgDeleteUrl.visibility=View.GONE
        }
        btnCancel.setOnClickListener {
            if (noteId != -1) {
                tvWebLink.visibility = View.VISIBLE
                llayoutWebUrl.visibility = View.GONE
            } else {

                llayoutWebUrl.visibility = View.GONE
            }

        }

    }

    private fun updateNote() {
        launch {

            context?.let {
                var notes = NotesDatabase.getInstance(it).noteDao().getSpecificNotes(noteId)

                notes.title = etNoteTitle.text.toString()
                notes.subtitle = etNoteSubTitle.text.toString()
                notes.dateTime = currentDate
                notes.noteText = etNoteDesc.text.toString()
                notes.color = selectedColor
                notes.imagePath = selectedImagePath
                notes.webLink = webLink

                NotesDatabase.getInstance(it).noteDao().updateNote(notes)
                etNoteTitle.setText("")
                etNoteSubTitle.setText("")
                etNoteDesc.setText("")
                imgNote.visibility = View.GONE
                deleteImage.visibility = View.GONE
                tvWebLink.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    private fun saveNote() {
        if (etNoteTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Title is Required", Toast.LENGTH_SHORT).show()
        } else if (etNoteDesc.text.isNullOrEmpty()) {
            Toast.makeText(context, "Note Description is Required", Toast.LENGTH_SHORT).show()
        } else if (etNoteSubTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Note Sub Title Required", Toast.LENGTH_SHORT).show()
        } else {
            launch {
                var notes = Notes()
                notes.title = etNoteTitle.text.toString()
                notes.subtitle = etNoteSubTitle.text.toString()
                notes.dateTime = currentDate
                notes.noteText = etNoteDesc.text.toString()
                notes.color = selectedColor
                notes.imagePath = selectedImagePath
                notes.webLink = webLink
                context?.let {
                    NotesDatabase.getInstance(it).noteDao().insertNotes(notes)
                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")
                    imgNote.visibility = View.GONE
                    deleteImage.visibility = View.GONE
                    tvWebLink.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

    }
private fun deleteNote(){
    launch {
        context.let {
            if (it != null) {
                NotesDatabase.getInstance(it).noteDao().deleteSpecificNotes(noteId)
                requireActivity().supportFragmentManager.popBackStack()

            }

        }
    }
}
    private fun checkWebUrl() {
        if (Patterns.WEB_URL.matcher(etWebLink.text.toString()).matches()) {
            llayoutWebUrl.visibility = View.GONE
            etWebLink.isEnabled = false
            webLink = etWebLink.text.toString()
            tvWebLink.visibility = View.VISIBLE
            tvWebLink.text = etWebLink.text.toString()
        } else {
            Toast.makeText(requireContext(), "Web link is not valid", Toast.LENGTH_SHORT).show()
        }
    }

    private val BroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionColor = p1!!.getStringExtra("action")
            when (actionColor!!) {
                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "White" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }
                "Image" -> {
                    readStorageTask()
                    llayoutWebUrl.visibility = View.GONE
                }
                "WebUrl" -> {
                    llayoutWebUrl.visibility = View.VISIBLE

                }
                "DeleteNote" -> {
                 deleteNote()

                }
                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Gray" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                else -> {
                    deleteImage.visibility = View.GONE
                    imgNote.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }

    private fun hasReadStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


    private fun readStorageTask() {
        if (hasReadStoragePerm()) {
            pickImageFromGallery()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

        }

    }

    private fun pickImageFromGallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath: String? = null
        var cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                var selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    try {
                        var inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        imgNote.setImageBitmap(bitmap)
                        imgNote.visibility = View.VISIBLE
                        deleteImage.visibility = View.VISIBLE
                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            requireActivity()
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

}



