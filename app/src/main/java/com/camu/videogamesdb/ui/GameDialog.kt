package com.camu.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.camu.videogamesdb.R
import com.camu.videogamesdb.application.VideogamesDBApp
import com.camu.videogamesdb.data.GameRepository
import com.camu.videogamesdb.data.db.model.GameEntity
import com.camu.videogamesdb.databinding.GameDialogBinding
import com.camu.videogamesdb.util.GenreItem
import kotlinx.coroutines.launch
import java.io.IOException


class GameDialog(
    private val newGame: Boolean = true,
    private var game: GameEntity = GameEntity(
        title = "",
        genre = "",
        assessment = "",
        year = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {

    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: GameRepository

    private var spinnerItemSelected: String = "Comedia"

    var isSpinnerOpen = false


    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as VideogamesDBApp).repository

        builder = AlertDialog.Builder(requireContext())



        /*binding.tietTitle.setText(game.title)
        binding.tietGenre.setText(game.genre)
        binding.tietDeveloper.setText(game.developer)*/


        var SpinnerGenre: Spinner
        SpinnerGenre = binding.SpinnerGenre

        val genreItems = listOf(
            GenreItem("Comedia", R.drawable.comedia),
            GenreItem("Bélico", R.drawable.belico),
            GenreItem("Drama", R.drawable.drama),
            GenreItem("Fantasía", R.drawable.fantasia),
            GenreItem("Ficción", R.drawable.ficcion),
            GenreItem("Horror", R.drawable.horror),
            GenreItem("Detectives", R.drawable.mafia)
        )

        val adapter = GenreAdapter(requireContext(), genreItems)
        adapter.setDropDownViewResource(R.layout.spinner_item_layout)
        SpinnerGenre.adapter = adapter

        SpinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected = genreItems[position].text

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerItemSelected = "Comedia"

            }
        }
        binding.apply {
            tietTitle.setText(game.title)
            tietAssessment.setText(game.assessment)

            val indexToSelect = genreItems.indexOfFirst { it.text == game.genre }

            // Seleccion del elemento por su índice
            if (indexToSelect != -1) {
                SpinnerGenre.setSelection(indexToSelect)
            }
            tietYear.setText(game.year)

        }



        dialog = if (newGame) {
            buildDialog("Guardar", "Cancelar", {
                 //Create (Guardar)
                game.title = binding.tietTitle.text.toString()
                game.genre = spinnerItemSelected
                game.assessment = binding.tietAssessment.text.toString()
                game.year = binding.tietYear.text.toString()



                try {
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }

                    message(getString(R.string.juego_guardado))

                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al guardar el juego")
                }
            }, {
                //Cancelar
            })
        } else {
            buildDialog("Actualizar", "Borrar", {
                //Update
                game.title = binding.tietTitle.text.toString()
                game.genre = spinnerItemSelected
                game.assessment = binding.tietAssessment.text.toString()
                game.year = binding.tietYear.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.updateGame(game)
                    }

                    message("Juego actualizado exitosamente")

                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar el juego")
                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                    .setPositiveButton("Aceptar"){ _,_ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteGame(game)
                            }

                            message("Juego eliminado exitosamente")

                            //Actualizar la UI
                            updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            message("Error al eliminar el juego")
                        }
                    }
                    .setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()


            })
        }

        /*dialog = builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton("Guardar", DialogInterface.OnClickListener { _, _ ->
                //Guardar
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }

                    Toast.makeText(requireContext(), getString(R.string.juego_guardado), Toast.LENGTH_SHORT).show()
                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error al guardar el juego", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { _, _ ->

            })
            .create()*/

        return dialog
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog =
            dialog as AlertDialog //Lo usamos para poder emplear el método getButton (no lo tiene el dialog)
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })



        binding.SpinnerGenre.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                isSpinnerOpen = true
            }
            false
        }

        binding.SpinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSpinnerOpen) {
                    saveButton?.isEnabled = validateFields()
                }
                isSpinnerOpen = false // Restablece el estado del Spinner
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Puedes dejar esto vacío o manejarlo según sea necesario
            }
        }




        binding.tietAssessment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields() =
        (binding.tietTitle.text.toString().isNotEmpty() && binding.tietAssessment.text.toString()
            .isNotEmpty() && binding.tietYear.text.toString().isNotEmpty() && isSpinnerOpen )

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Acción para el botón positivo
                positiveButton()
            })
            .setNegativeButton(btn2Text) { _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()



}