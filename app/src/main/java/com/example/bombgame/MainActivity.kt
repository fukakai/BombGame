package com.example.bombgame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bombgame.data.dto.Room
import com.example.bombgame.data.dto.User
import com.example.bombgame.game.GameActivity
import com.example.bombgame.ui.main.RoomViewModel
import com.example.bombgame.ui.main.RoomViewModelFactory
import com.example.bombgame.ui.main.UserViewModel
import com.example.bombgame.ui.main.UserViewModelFactory
import com.example.bombgame.utils.Constants
import com.example.bombgame.utils.Constants.PLAYER_USERNAME_KEY
import com.example.bombgame.utils.Constants.RC_SIGN_IN
import com.example.bombgame.utils.Constants.ROOM_ID_KEY
import com.example.bombgame.utils.InjectorUtils
import com.example.bombgame.utils.RoomUtils
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext


class MainActivity : AppCompatActivity() {

    private var roomList = listOf<Room>()
    private var currentRoom: Room? = null
    private var user: User = User()
    private lateinit var roomFactory: RoomViewModelFactory
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var userFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel
    private lateinit var mainHandler: Handler
    private lateinit var usernameText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainHandler = Handler(Looper.getMainLooper())

        val createButton = findViewById<Button>(R.id.create_room_button)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val findButton = findViewById<Button>(R.id.find_game_button)
        val gameButton = findViewById<Button>(R.id.game_button)
        val logInButton = findViewById<Button>(R.id.log_in_button)
        val logOutButton = findViewById<Button>(R.id.log_out_button)
        findButton.isEnabled = false
        createButton.isEnabled = false

        val idText = findViewById<EditText>(R.id.game_id_text)
        usernameText = findViewById(R.id.username_text)

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )

        roomFactory = InjectorUtils.provideRoomViewModelFactory()
        roomViewModel = ViewModelProvider(this, roomFactory)
            .get(RoomViewModel::class.java)
        roomViewModel.getRoomListObserver().observe(this, Observer {
            roomList = it
        })

        roomViewModel.getCurrentRoomObserver().observe(this, Observer {
            currentRoom = it
        })

        userFactory = InjectorUtils.provideUserViewModelFactory()
        userViewModel = ViewModelProvider(this, userFactory)
            .get(UserViewModel::class.java)
        userViewModel.getUserObserver().observe(this, Observer {
            if (it != null) {
                user = it
            }
        })

        gameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent);
        }

        idText.filters =
            arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(Constants.MAX_LENGTH_ROOM_ID))
        idText.addTextChangedListener {
            findButton.isEnabled =
                it.toString().length == 6 && usernameText.text.toString().isNotBlank()
        }

        usernameText.filters = arrayOf(InputFilter.LengthFilter(Constants.MAX_LENGTH_USERNAME))
        usernameText.addTextChangedListener {
            findButton.isEnabled = it.toString()
                .isNotBlank() && idText.text.toString().length == Constants.MAX_LENGTH_ROOM_ID
            createButton.isEnabled = it.toString().isNotBlank()
        }

        createButton.setOnClickListener {
            userViewModel.updateUser(user, usernameText.text.toString())
            val list = roomViewModel.addRoom(user)
            goToLobby(list[0], list[1])
        }

        deleteButton.setOnClickListener {
            roomViewModel.deleteAllRooms()
        }

        gameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent);
        }

        findButton.setOnClickListener {
            val gameId = idText.text.toString()
            var room: Room?

            if (RoomUtils.isExistingRoom(gameId, roomList)) {
                userViewModel.updateUser(user, usernameText.text.toString())
                CoroutineScope(EmptyCoroutineContext).launch {
                    withContext(
                        Dispatchers.Default
                    ) { room = roomViewModel.getRoom(gameId) }
                    if (roomViewModel.addPlayerToRoom(user, room!!)) {
                        goToLobby(user.username, room?.gameId!!)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "You username is already taken !",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Game $gameId not found !", Toast.LENGTH_SHORT).show()
            }
        }

        logInButton.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }

        logOutButton.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
            user = User()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                user.id = FirebaseAuth.getInstance().currentUser?.uid.toString()
                CoroutineScope(EmptyCoroutineContext).launch {
                    user.username =
                        userViewModel.getUser(user.id)?.username.toString()
                    mainHandler.post { usernameText.setText(user.username) }
                }
                userViewModel.addUser(user)
            }
        }
    }

    private fun goToLobby(playerId: String, roomId: String) {
        val intent = Intent(this, LobbyActivity::class.java)
        intent.putExtra(PLAYER_USERNAME_KEY, playerId)
        intent.putExtra(ROOM_ID_KEY, roomId)
        roomViewModel.listenToPlayersList(roomId)
        startActivity(intent)
    }
}
