package com.example.bombgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bombgame.data.dto.Player
import com.example.bombgame.ui.main.RoomViewModel
import com.example.bombgame.utils.Constants.PLAYER_USERNAME_KEY
import com.example.bombgame.utils.Constants.ROOM_ID_KEY
import com.example.bombgame.utils.InjectorUtils
import com.example.bombgame.utils.PlayersUtils
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStreamReader


class LobbyActivity : AppCompatActivity() {

    private var playerList = listOf<Player>()
    private lateinit var playerUsername: String
    private lateinit var roomId: String
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val intentLobbyActivity = intent
        val bundle = intentLobbyActivity.extras

        val gridView = findViewById<GridView>(R.id.players_list)
        val pseudo = findViewById<EditText>(R.id.pseudo_text)
        val readyButton = findViewById<Button>(R.id.ready_button)
        val leaveButton = findViewById<Button>(R.id.leave_button)
        val startButton = findViewById<Button>(R.id.start_button)
        val roomIdText = findViewById<TextView>(R.id.room_id)
        val intentStartGame = Intent(this, GameActivity::class.java)

        if (bundle?.getString(ROOM_ID_KEY) != null) {
            roomId = bundle.getString(ROOM_ID_KEY)!!
        }

        if (bundle?.getString(PLAYER_USERNAME_KEY) != null) {
            playerUsername = bundle.getString(PLAYER_USERNAME_KEY)!!
        }

//        if (readPseudo().isNullOrBlank()) {
//            pseudo.hint = readPseudo()
//        }

        roomIdText.text = roomId

        val roomFactory = InjectorUtils.provideRoomViewModelFactory()
        roomViewModel = ViewModelProvider(this, roomFactory)
            .get(RoomViewModel::class.java)

        roomViewModel.getPlayerListObserver().observe(this, Observer {
            playerList = it
            startButton.isEnabled = playerList.isNotEmpty() && PlayersUtils.areAllPlayersReady(playerList)
        })

        startButton.setOnClickListener {
            startActivity(intentStartGame)
        }

        //TODO gridView
//        var adapter: PlayersListAdapter? = null
//        adapter = PlayersListAdapter(playersList, this)
//
        readyButton.setOnClickListener {
            roomViewModel.switchReady(roomId, playerUsername)
        }

        leaveButton.setOnClickListener {
            finish()
        }
    }

    fun savePseudo(pseudo: String) {
        val file = "UserPseudo"
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(pseudo.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readPseudo(): String {
        var text = ""
        try {
            val inputStreamReader = InputStreamReader(openFileInput("UserPseudo"))
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()

            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            return text
        } catch (exception: FileNotFoundException) {
            return text
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        roomViewModel.deletePlayer(playerUsername, roomId)
    }
}