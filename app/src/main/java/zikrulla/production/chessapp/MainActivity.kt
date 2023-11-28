package zikrulla.production.chessapp

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import zikrulla.production.chessapp.databinding.ActivityMainBinding
import zikrulla.production.chessapp.model.ChessPiece
import zikrulla.production.chessapp.model.Square

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
                ChessGame.resetGame()
                chessView.invalidate()
            }
            backward.setOnClickListener {
                ChessGame.showMove(false)
                chessView.invalidate()
            }
            forward.setOnClickListener {
                ChessGame.showMove(true)
                chessView.invalidate()
            }
        }
        Log.d(TAG, ChessGame.toString())
    }

    override fun pieceAt(square: Square): ChessPiece? {
        return ChessGame.pieceAt(square)
    }

    override fun movePiece(from: Square, to: Square, isShow: Boolean?): Boolean {
        val result = ChessGame.movePiece(from, to)
        binding.chessView.invalidate()
        return result
    }

    override fun drawSelectedFigures(canvas: Canvas, square: Square) {
        TODO("Not yet implemented")
    }


}