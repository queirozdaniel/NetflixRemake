package com.danielqueiroz.netflixremake.kotlin

import android.media.Image
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielqueiroz.netflixremake.R
import com.danielqueiroz.netflixremake.model.Movie
import com.danielqueiroz.netflixremake.util.ImageDownloaderTask
import com.danielqueiroz.netflixremake.util.MovieDetailTask
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_movie.image_view_cover
import kotlinx.android.synthetic.main.activity_movie.text_view_title
import kotlinx.android.synthetic.main.activity_movie.view.*
import kotlinx.android.synthetic.main.category_item.*
import kotlinx.android.synthetic.main.movie_item_similar.*

class MovieActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        setSupportActionBar(toolbar)

        intent.extras?.let {
            val id = it.getInt("id")
            val task = MovieDetailTask(this)
            task.setMovieDetailLoader {movieDetail ->
                text_view_title.text = movieDetail.movie.title
                text_view_desc.text =  movieDetail.movie.desc
                text_view_cast.text = getString(R.string.cast, movieDetail.movie.cast)

                ImageDownloaderTask(image_view_cover).apply {
                    setShadowEnabled(true)
                    execute(movieDetail.movie.coverUrl)
                }

                movieAdapter.movies.clear()
                movieAdapter.movies.addAll(movieDetail.moviesSimilar)
                movieAdapter.notifyDataSetChanged()
            }

            task.execute("https://tiagoaguiar.co/api/netflix/$id")
        }

        supportActionBar?.let { toolbar ->
            toolbar.setDisplayHomeAsUpEnabled(true)
            toolbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            toolbar.title = null
        }

        val movies = arrayListOf<Movie>()
        movieAdapter = MovieAdapter(movies)
        recycler_view_similar.adapter = movieAdapter
        recycler_view_similar.layoutManager = GridLayoutManager(this, 3)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MovieAdapter(val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
                MovieHolder(
                        layoutInflater.inflate(R.layout.movie_item_similar, parent, false)
                )

        override fun onBindViewHolder(holder: MovieHolder, position: Int)
                = holder.bind(movies[position])

        override fun getItemCount(): Int = movies.size

    }


    private inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                ImageDownloaderTask(image_view_cover).execute(movie.coverUrl)
            }
        }
    }
}