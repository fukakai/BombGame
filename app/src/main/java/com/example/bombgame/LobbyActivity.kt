package com.example.bombgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bombgame.data.dto.Player
import com.example.bombgame.data.dto.User
import com.example.bombgame.models.Subscription
import com.example.bombgame.ui.main.RoomViewModel
import com.example.bombgame.ui.main.UserViewModel
import com.example.bombgame.ui.main.adapter.LobbyAdapter
import com.example.bombgame.utils.Constants.GREEN
import com.example.bombgame.utils.Constants.PLAYER_USERNAME_KEY
import com.example.bombgame.utils.Constants.READY_FALSE
import com.example.bombgame.utils.Constants.READY_TRUE
import com.example.bombgame.utils.Constants.RED
import com.example.bombgame.utils.Constants.ROOM_ID_KEY
import com.example.bombgame.utils.Constants.USER
import com.example.bombgame.utils.InjectorUtils
import com.example.bombgame.utils.PlayersUtils
import kotlinx.android.synthetic.main.activity_lobby.*


class LobbyActivity : AppCompatActivity() {

    private lateinit var playerUsername: String
    private lateinit var roomId: String
    private lateinit var user: User
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var userViewModel: UserViewModel
    private var playerList: List<Player> = mutableListOf()
    private var ready = false
    private var roomLoaded = false
    private var gameAlreadyStarted = false
    private val playerListAdapter = LobbyAdapter(playerList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        players_list_view.adapter = playerListAdapter
        players_list_view.layoutManager = GridLayoutManager(this, 2)

        initializeExtras()
        initializeButtons()
        initializeRoomViewModel()

        val roomText = "GAME ID : $roomId"
        room_id.text = roomText
    }

    override fun onDestroy() {
        super.onDestroy()
        roomViewModel.deletePlayer(playerUsername, roomId)
        roomViewModel.listenToRoomList()
        roomViewModel.unsubscribe(listOf(Subscription.PLAYER_LIST, Subscription.ROOM))
    }

    /**
     * Starts the game activity, set player ready to false.
     */
    private fun startGameActivity(playerId: String, gameId: String) {
        roomViewModel.switchReady(roomId, playerUsername)
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(PLAYER_USERNAME_KEY, playerId)
        intent.putExtra(ROOM_ID_KEY, gameId)
        startActivity(intent)
    }

    /**
     * Initialize the buttons for the lobby activity.
     */
    private fun initializeButtons() {
        ready_button.setBackgroundColor(Color.parseColor(RED))

        start_button.setOnClickListener {
            roomViewModel.updateStartedGame(roomId, true)
        }

        ready_button.setOnClickListener {
            roomViewModel.switchReady(roomId, playerUsername)
        }

        leave_button.setOnClickListener {
            finish()
        }

        ok_button_lobby.setOnClickListener {
            val newUsername = username_text_lobby.text.toString()
            if (user.id != "") {
                userViewModel.updateUser(user, newUsername)
            }
            roomViewModel.updatePlayerUsername(roomId, playerUsername, newUsername)
            playerUsername = newUsername
        }
    }

    /**
     * Get the game ID and the local player's username from the main activity.
     */
    private fun initializeExtras() {
        val intentLobbyActivity = intent
        val bundle = intentLobbyActivity.extras

        if (bundle?.getString(ROOM_ID_KEY) != null) {
            roomId = bundle.getString(ROOM_ID_KEY)!!
        }

        if (bundle?.getString(PLAYER_USERNAME_KEY) != null) {
            playerUsername = bundle.getString(PLAYER_USERNAME_KEY)!!
        }

        if (bundle?.getSerializable(USER) != null) {
            user = (bundle.getSerializable(USER) as User)
        }
    }

    /**
     * Initialize the RoomViewModel and listen to the current room and the players list.
     */
    private fun initializeRoomViewModel() {
        val roomFactory = InjectorUtils.provideRoomViewModelFactory()
        roomViewModel = ViewModelProvider(this, roomFactory)
            .get(RoomViewModel::class.java)
        val userFactory = InjectorUtils.provideUserViewModelFactory()
        userViewModel = ViewModelProvider(this, userFactory)
            .get(UserViewModel::class.java)

        roomViewModel.getCurrentRoomObserver().observe(this, {

            if (it != null && !roomLoaded) {
                roomLoaded = true
            } else if (it == null && roomLoaded) {
                Toast.makeText(this, "This room no longer exists !", Toast.LENGTH_SHORT).show()
                finish()
            } else if (roomLoaded && it.gameStarted && !gameAlreadyStarted) {
                gameAlreadyStarted = true
                startGameActivity(playerUsername, roomId)
            }
        })

        roomViewModel.getPlayerListObserver().observe(this, { list ->
            if (list != null) {
                playerList = list
                playerListAdapter.update(playerList)
                val player = playerList.find { it.username == playerUsername }
                if (player != null) {
                    ready = player.ready
                }
            }

            ready_button.setBackgroundColor(if (ready) Color.parseColor(GREEN) else Color.parseColor(
                RED))
            ready_button.text = if (ready) READY_TRUE else READY_FALSE

            start_button.isEnabled =
                playerList.size > 1 && PlayersUtils.areAllPlayersReady(playerList)
        })
    }
}