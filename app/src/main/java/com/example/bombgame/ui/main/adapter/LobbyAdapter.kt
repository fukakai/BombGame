package com.example.bombgame.ui.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bombgame.R
import com.example.bombgame.data.dto.Player
import com.example.bombgame.utils.Constants.GREEN
import com.example.bombgame.utils.Constants.RED
import kotlinx.android.synthetic.main.lobby_grid_layout.view.*

/**
 * Adapter class to display the players in a RecyclerView in the lobby.
 */
class LobbyAdapter(private var playerList: List<Player>): RecyclerView.Adapter<LobbyAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val playerView = LayoutInflater.from(parent.context).inflate(R.layout.lobby_grid_layout,
        parent, false)
        return PlayerViewHolder(playerView)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val currentPlayer = playerList[position]
        holder.usernameText.text = currentPlayer.username
        holder.readyText.text = if (currentPlayer.ready) "Ready !" else "Not ready"

        if (currentPlayer.ready) {
            holder.readyText.setTextColor(Color.parseColor(GREEN))
        } else {
            holder.readyText.setTextColor(Color.parseColor(RED))
        }
    }

    override fun getItemCount() = playerList.size

    /**
     * Update the players list to display.
     * @param newPlayerList The players list that will replace the old one.
     */
    fun update(newPlayerList: List<Player>) {
        playerList = newPlayerList
        this.notifyDataSetChanged()
    }

    /**
     * Class that represents one player in the RecyclerView.
     */
    class PlayerViewHolder(playerView: View): RecyclerView.ViewHolder(playerView) {
        val usernameText: TextView = playerView.username_grid
        val readyText: TextView = playerView.ready_grid
    }
}