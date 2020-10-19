package com.danielqueiroz.netflixremake.kotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielqueiroz.netflixremake.R
import com.danielqueiroz.netflixremake.model.Category
import com.danielqueiroz.netflixremake.util.CategoryTask
import kotlinx.android.synthetic.main.activity_movie.view.*
import kotlinx.android.synthetic.main.activity_movie.view.text_view_title
import kotlinx.android.synthetic.main.category_item.*
import kotlinx.android.synthetic.main.category_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        recycler_view_movie.adapter = MainAdapter(categories)
        recycler_view_movie.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader { categories ->
            categories.size
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(
            val categories: List<Category>
    ) : RecyclerView.Adapter<CategoryHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(
                    layoutInflater
                            .inflate(R.layout.category_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int = categories.size

    }

    private class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category){
            itemView.text_view_title.text = category.name
        }
    }

}