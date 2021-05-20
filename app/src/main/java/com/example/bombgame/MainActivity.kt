package com.example.bombgame

import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bombgame.data.dto.Room
import com.example.bombgame.ui.main.MainViewModel
import com.example.bombgame.utils.InjectorUtils
import com.example.bombgame.utils.RoomUtils


class MainActivity : AppCompatActivity() {

    private var roomList = listOf<Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createButton = findViewById<Button>(R.id.create_room_button)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val findButton = findViewById<Button>(R.id.find_game_button)
        findButton.isEnabled = false

        val idText = findViewById<EditText>(R.id.game_id_text)

        val factory = InjectorUtils.provideMainViewModelFactory()
        val viewModel = ViewModelProvider(this, factory)
            .get(MainViewModel::class.java)
        viewModel.getRoomListObserver().observe(this, Observer {
            roomList = it
        })

        idText.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(6))
        idText.addTextChangedListener {
            findButton.isEnabled = it.toString().length == 6
        }

        createButton.setOnClickListener {
            viewModel.addRoom(Room())
        }

        deleteButton.setOnClickListener {
            viewModel.deleteAllRooms()
        }

        findButton.setOnClickListener {
            val gameId = idText.text.toString()
            if (RoomUtils.isExistingRoom(gameId, roomList)) {
                Toast.makeText(this, "Found game $gameId !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Game $gameId not found !", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


