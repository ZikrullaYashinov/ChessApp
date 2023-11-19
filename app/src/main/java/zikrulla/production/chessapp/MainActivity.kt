package zikrulla.production.chessapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import zikrulla.production.chessapp.model.ChessModel

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity @"
    private var chessModel = ChessModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, chessModel.toString())
    }
}