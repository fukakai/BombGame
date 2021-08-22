package com.example.bombgame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bombgame.data.dto.Room
import com.example.bombgame.data.dto.User
import com.example.bombgame.models.Subscription
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext


class MainActivity : AppCompatActivity() {

    private var roomList = listOf<Room>()
    private var currentRoom: Room? = null
    private var user: User = User()
    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )

    private lateinit var roomFactory: RoomViewModelFactory
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var userFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel
    private lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainHandler = Handler(Looper.getMainLooper())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )

        initializeViewModels()
        initializeButtons()
        initializeTexts()
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
                    mainHandler.post { username_text.setText(user.username) }
                }
                userViewModel.addUser(user)
            }
        }
    }

    /**
     * Starts the lobby activity. Subscribe to the room and the players list. Unsubscribe from the
     * rooms list.
     */
    private fun goToLobby(playerId: String, roomId: String) {
        val intent = Intent(this, LobbyActivity::class.java)
        intent.putExtra(PLAYER_USERNAME_KEY, playerId)
        intent.putExtra(ROOM_ID_KEY, roomId)
        roomViewModel.listenToRoom(roomId)
        roomViewModel.listenToPlayerList(roomId)
        roomViewModel.unsubscribe(Subscription.ROOM_LIST)
        startActivity(intent)
    }

    /**
     * Indicate if the user is logged in with google / facebook.
     */
    private fun isLoggedIn(): Boolean {
        return user.id != ""
    }

    /**
     * Initialize the buttons for the main activity.
     */
    private fun initializeButtons() {
        game_button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent);
        }

        create_room_button.setOnClickListener {
            userViewModel.updateUser(user, username_text.text.toString())
            val list = roomViewModel.addRoom(user)
            goToLobby(list[0], list[1])
        }

        delete_button.setOnClickListener {
            roomViewModel.deleteAllRooms()
        }

        game_button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent);
        }

        find_game_button.setOnClickListener {
            val gameId = game_id_text.text.toString()

            if (RoomUtils.isExistingRoom(gameId, roomList)) {
                if (isLoggedIn()) {
                    userViewModel.updateUser(user, username_text.text.toString())
                } else {
                    user.username = username_text.text.toString()
                }
                var usernameTaken: Boolean
                CoroutineScope(EmptyCoroutineContext).launch {
                    withContext(
                        Dispatchers.Default
                    ) { usernameTaken = roomViewModel.isUsernameTaken(gameId, user.username) }
                    if (!usernameTaken) {
                        roomViewModel.addPlayerToRoom(user, gameId)
                        goToLobby(user.username, gameId)
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

        log_in_button.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }

        log_out_button.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
            user = User()
        }

        find_game_button.isEnabled = false
        create_room_button.isEnabled = false
    }

    /**
     * Initialize the viewModels for the main activity.
     */
    private fun initializeViewModels() {
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
    }

    /**
     * Initialize the texts for the main activity.
     */
    private fun initializeTexts() {
        game_id_text.filters =
            arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(Constants.MAX_LENGTH_ROOM_ID))
        game_id_text.addTextChangedListener {
            find_game_button.isEnabled =
                it.toString().length == 6 && username_text.text.toString().isNotBlank()
        }

        username_text.filters = arrayOf(InputFilter.LengthFilter(Constants.MAX_LENGTH_USERNAME))
        username_text.addTextChangedListener {
            find_game_button.isEnabled = it.toString()
                .isNotBlank() && game_id_text.text.toString().length == Constants.MAX_LENGTH_ROOM_ID
            create_room_button.isEnabled = it.toString().isNotBlank()
        }
    }
}
