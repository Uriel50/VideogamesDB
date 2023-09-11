package com.camu.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camu.videogamesdb.R
import com.camu.videogamesdb.data.db.model.GameEntity
import com.camu.videogamesdb.databinding.GameElementBinding
import com.camu.videogamesdb.util.GenreItem


class GameAdapter(private val onGameClick: (GameEntity) -> Unit): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var games: List<GameEntity> = emptyList()



    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivIcon = binding.ivIcon

        val genreItems = listOf(
            GenreItem("Comedia", R.drawable.comedia),
            GenreItem("Bélico", R.drawable.belico),
            GenreItem("Drama", R.drawable.drama),
            GenreItem("Fantasía", R.drawable.fantasia),
            GenreItem("Ficción", R.drawable.ficcion),
            GenreItem("Horror", R.drawable.horror),
            GenreItem("Detectives", R.drawable.mafia)
        )

        fun bind(game: GameEntity){
            /*binding.tvTitle.text = game.title
            binding.tvGenre.text = game.genre
            binding.tvDeveloper.text = game.developer*/

            binding.apply {
                tvTitle.text = game.title
                tvGenre.text = game.genre
                tvAssessment.text = game.assessment
                tvYear.text = game.year
                val foundGenreItem = genreItems.find { it.text == game.genre }

                if (foundGenreItem != null) {

                    val iconResourceId = foundGenreItem.imageResId


                    ivIcon.setImageResource(iconResourceId)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])

        holder.itemView.setOnClickListener {
            //Aquí va el click del elemento
            onGameClick(games[position])
        }

        holder.ivIcon.setOnClickListener {
            //Click para la vista del imageview con el ícono
        }

    }

    fun updateList(list: List<GameEntity>){
        games = list
        notifyDataSetChanged()
    }

}