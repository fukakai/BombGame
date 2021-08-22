package com.example.bombgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bombgame.data.dto.Player
import com.example.bombgame.models.Subscription
import com.example.bombgame.ui.main.RoomViewModel
import com.example.bombgame.ui.main.adapter.LobbyAdapter
import com.example.bombgame.utils.Constants.GREEN
import com.example.bombgame.utils.Constants.PLAYER_USERNAME_KEY
import com.example.bombgame.utils.Constants.READY_FALSE
import com.example.bombgame.utils.Constants.READY_TRUE
import com.example.bombgame.utils.Constants.RED
import com.example.bombgame.utils.Constants.ROOM_ID_KEY
import com.example.bombgame.utils.InjectorUtils
import com.example.bombgame.utils.PlayersUtils


class LobbyActivity : AppCompatActivity() {

    private lateinit var playerUsername: String
    private lateinit var roomId: String
    private lateinit var roomViewModel: RoomViewModel
    private var playerList: List<Player> = mutableListOf()
    private var ready = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val intentLobbyActivity = intent
        val bundle = intentLobbyActivity.extras

        var gameAlreadyStarted = false
        val playerListView = findViewById<RecyclerView>(R.id.players_list)
        val playerListAdapter = LobbyAdapter(playerList)
        val pseudo = findViewById<EditText>(R.id.pseudo_text)
        val readyButton = findViewById<Button>(R.id.ready_button)
        readyButton.setBackgroundColor(Color.parseColor(RED))
        val leaveButton = findViewById<Button>(R.id.leave_button)
        val startButton = findViewById<Button>(R.id.start_button)
        val roomIdText = findViewById<TextView>(R.id.room_id)

        playerListView.adapter = playerListAdapter
        playerListView.layoutManager = GridLayoutManager(this, 2)

        if (bundle?.getString(ROOM_ID_KEY) != null) {
            roomId = bundle.getString(ROOM_ID_KEY)!!
        }

        if (bundle?.getString(PLAYER_USERNAME_KEY) != null) {
            playerUsername = bundle.getString(PLAYER_USERNAME_KEY)!!
        }

        roomIdText.text = roomId

        val roomFactory = InjectorUtils.provideRoomViewModelFactory()
        roomViewModel = ViewModelProvider(this, roomFactory)
            .get(RoomViewModel::class.java)

        roomViewModel.getCurrentRoomObserver().observe(this, Observer {
            if (it.gameStarted && !gameAlreadyStarted) {
                gameAlreadyStarted = true
                startGameActivity(playerUsername, roomId)
            }
        })

        roomViewModel.getPlayerListObserver().observe(this, Observer { list ->
            playerList = list
            playerListAdapter.update(playerList)
            val player = playerList.find { it.username == playerUsername }
            if (player != null) {
                ready = player.ready
            }

            readyButton.setBackgroundColor(if (ready) Color.parseColor(GREEN) else Color.parseColor(
                RED))
            readyButton.text = if (ready) READY_TRUE else READY_FALSE

            startButton.isEnabled = playerList.size > 1 && PlayersUtils.areAllPlayersReady(playerList)
        })

        startButton.setOnClickListener {
            roomViewModel.updateStartedGame(roomId, true)
        }

        readyButton.setOnClickListener {
            roomViewModel.switchReady(roomId, playerUsername)
        }

        leaveButton.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        roomViewModel.deletePlayer(playerUsername, roomId)
        roomViewModel.listenToRoomList()
        roomViewModel.unsubscribe(listOf(Subscription.PLAYER_LIST, Subscription.ROOM))
    }

    private fun startGameActivity(playerId: String, gameId: String) {
        roomViewModel.switchReady(roomId, playerUsername)
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(PLAYER_USERNAME_KEY, playerId)
        intent.putExtra(ROOM_ID_KEY, gameId)
        startActivity(intent)
    }
}