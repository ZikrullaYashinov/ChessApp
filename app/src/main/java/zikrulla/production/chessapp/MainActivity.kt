package zikrulla.production.chessapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import zikrulla.production.chessapp.databinding.ActivityMainBinding
import zikrulla.production.chessapp.model.ChessPiece
import zikrulla.production.chessapp.model.ChessDelegate
import zikrulla.production.chessapp.model.ChessGame

class MainActivity : AppCompatActivity(), ChessDelegate {

    private val TAG = "@@@@"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            chessView.chessDelegate = this@MainActivity
            reset.setOnClickListener {
                ChessGame.reset()
                chessView.invalidate()
            }
            listen.setOnClickListener {

            }
            connect.setOnClickListener {

            }
        }
        Log.d(TAG, ChessGame.toString())
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return ChessGame.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        ChessGame.movePiece(fromCol, fromRow, toCol, toRow)
        binding.chessView.invalidate()
    }
}