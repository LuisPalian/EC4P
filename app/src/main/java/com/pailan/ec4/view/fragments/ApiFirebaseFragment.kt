package com.pailan.ec4.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.pailan.ec4.R
import com.pailan.ec4.data.adapter.DataAdapter
import com.pailan.ec4.data.adapter.DataItems
import com.pailan.ec4.databinding.FragmentApiFirebaseBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ApiFirebaseFragment : Fragment() {

    private lateinit var binding: FragmentApiFirebaseBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val IMAGE_CAPTURE_REQUEST_CODE = 1
    private lateinit var currentPhotoPath: String
    private lateinit var imageRef: StorageReference
    private lateinit var dataAdapter: DataAdapter
    private val dataList: MutableList<DataItems> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiFirebaseBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        imageRef = Firebase.storage.reference.child("imagenes/${UUID.randomUUID()}.jpg")

        binding.btnTakePhoto.setOnClickListener {
            if (hasCameraPermission()) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
            }
        }

        binding.btnAddData.setOnClickListener {
            uploadDataToFirestore()
        }
        dataAdapter = DataAdapter(dataList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = dataAdapter

        firestore.collection("valorant")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val nombre = document.getString("nombre") ?: ""
                    val habilidad = document.getString("habilidad") ?: ""
                    val poder = document.getString("poder") ?: ""
                    val mana = document.getString("mana") ?: ""
                    val imagen = document.getString("imagen") ?: ""

                    val dataItem = DataItems(nombre, habilidad, poder, mana, imagen)
                    dataList.add(dataItem)

                       }
                dataAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
            }



        return binding.root
    }
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), IMAGE_CAPTURE_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara concedido, tomar la foto
                dispatchTakePictureIntent()
            } else {
                // Permiso de cámara denegado
                Toast.makeText(
                    requireContext(),
                    "Permiso de cámara requerido para tomar fotos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.pailan.ec4.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Mostrar la imagen en ImageView
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            binding.imgPhoto.setImageBitmap(imageBitmap)
        }
    }

    private fun uploadDataToFirestore() {
        val name = binding.txtName.editText?.text.toString()
        val character = binding.txtCharacter.editText?.text.toString()
        val gameSeries = binding.txtGameSeries.editText?.text.toString()
        val amiiboSeries = binding.txtAmiiboSeries.editText?.text.toString()

        val newData = hashMapOf(
            "nombre" to name,
            "habilidad" to character,
            "poder" to gameSeries,
            "mana" to amiiboSeries
        )

        firestore.collection("valorant")
            .add(newData)
            .addOnSuccessListener { documentReference ->
                imageRef.putFile(Uri.fromFile(File(currentPhotoPath)))
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUri = uri.toString()
                            documentReference.update("imagen", imageUri)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Datos agregados correctamente", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "Error al agregar los datos", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al agregar los datos", Toast.LENGTH_SHORT).show()
            }
    }


}