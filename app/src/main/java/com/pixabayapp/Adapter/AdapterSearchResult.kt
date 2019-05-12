package com.pixabayapp.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pixabayapp.Model.SearchResult
import com.pixabayapp.R
import kotlinx.android.synthetic.main.row_movie_list.view.*
import java.util.*


/**
 * Create adapter view to display movie list.
 * @context Holds activity context
 * @movieListItem Holds movie list
 * @clickListener Holds listener to handle click event on recycler view
 */
class AdapterSearchResult(
    val context: Context,
    var searchResultHitList: ArrayList<SearchResult.Hit>,
    val clickListener: (SearchResult.Hit) -> Unit
) : RecyclerView.Adapter<AdapterSearchResult.ViewHolder>() {

    // Tag value to show AdapterSearchResult name in log
    companion object {
        private val TAG = "AdapterSearchResult"
    }

    //This method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSearchResult.ViewHolder {
        Log.v(TAG, String.format("onCreateViewHolder() :: parent, viewType "))
        var v = LayoutInflater.from(parent.context).inflate(R.layout.row_movie_list, parent, false)
        return ViewHolder(v)
    }

    //This method is binding the data on the list
    override fun onBindViewHolder(holder: AdapterSearchResult.ViewHolder, position: Int) {
        Log.v(TAG, String.format("onBindViewHolder() :: holder, position = %d", position))
        (holder as ViewHolder).bindItems(searchResultHitList[position], clickListener)
    }

    // Get total item count
    override fun getItemCount() = searchResultHitList.size

    //The class is holding the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(searchResultHit: SearchResult.Hit, clickListener: (SearchResult.Hit) -> Unit) {

            var url: String =  searchResultHit.previewURL.toString()
            itemView.imgvPoster.setImageURI(url)

            Log.v(
                TAG,
                String.format(
                    "ViewHolder :: bindItems() :: searchResultHitListItem  title = %s, vote count = %s, url = %s",
                    "" + searchResultHit.pageURL,
                    searchResultHit.largeImageURL,
                    url
                )
            )

            itemView.setOnClickListener { clickListener(searchResultHit) }
        }
    }
}