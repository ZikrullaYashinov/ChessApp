package zikrulla.production.chessapp.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import zikrulla.production.chessapp.R
import zikrulla.production.chessapp.model.ChessDelegate
import zikrulla.production.chessapp.model.ChessPiece
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val TAG = "@@@@"
    private var originX = 0f
    private var originY = 0f
    private var cellSide = 0f
    private var scaleFactor = 1f
    private val lightColor = Color.parseColor("#779854")
    private val darkColor = Color.parseColor("#E8ECCB")
    private val paint = Paint()
    private val imgResourceIds = setOf(
        R.drawable.chess_klt,
        R.drawable.chess_kdt,
        R.drawable.chess_qlt,
        R.drawable.chess_qdt,
        R.drawable.chess_rlt,
        R.drawable.chess_rdt,
        R.drawable.chess_nlt,
        R.drawable.chess_ndt,
        R.drawable.chess_blt,
        R.drawable.chess_bdt,
        R.drawable.chess_plt,
        R.drawable.chess_pdt,
    )
    private var drawables = mutableMapOf<Int, Drawable>()
    var chessDelegate: ChessDelegate? = null
    private var fromCol = -1
    private var fromRow = -1
    private var movingPieceX = -1f
    private var movingPieceY = -1f
    private var movingPieceDrawable: Drawable? = null
    private var movingPiece: ChessPiece? = null

    init {
        loadDrawable()
    }

    override fun onDraw(canvas: Canvas) {
        setSize()
        drawChessBoard(canvas)
        drawPieces(canvas)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(min, min)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.pieceAt(fromCol, fromRow)?.let {
                    movingPiece = it
                    movingPieceDrawable = drawables[it.resId]
                }
            }

            ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }

            ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.movePiece(fromCol, fromRow, col, row)
                movingPieceDrawable = null
                movingPiece = null
                invalidate()
            }
        }
        return true
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0..7) {
            for (col in 0..7) {
                chessDelegate?.pieceAt(col, row)?.let {
                    if (movingPiece != it)
                        drawPieceAt(canvas, col, row, it.resId)
                }
            }
        }

        movingPieceDrawable?.let {
            it.setBounds(
                (movingPieceX - cellSide / 2).toInt(),
                (movingPieceY - cellSide / 2).toInt(),
                (movingPieceX + cellSide / 2).toInt(),
                (movingPieceY + cellSide / 2).toInt()
            )
            it.draw(canvas)
        }
    }

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resId: Int) {
        val drawable = drawables[resId]!!
        drawable.setBounds(
            (originX + col * cellSide).toInt(),
            (originY + (7 - row) * cellSide).toInt(),
            (originX + (col + 1) * cellSide).toInt(),
            (originY + (7 - row + 1) * cellSide).toInt()
        )
        drawable.draw(canvas)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadDrawable() {
        imgResourceIds.forEach {
            drawables[it] = resources.getDrawable(it, null)
        }
    }

    private fun setSize() {
        val min = min(width, height)
        cellSide = ((min - originX) / 8) * scaleFactor
        originX = (width - 8 * cellSide) / 2
        originY = (height - 8 * cellSide) / 2
    }

    private fun drawChessBoard(canvas: Canvas) {
        for (row in 0..7) {
            for (col in 0..7) {
                drawSquareAt(canvas, row, col, (row + col) % 2 == 1)
            }
        }
    }

    private fun drawSquareAt(canvas: Canvas, col: Int, row: Int, isDark: Boolean) {
        paint.color = if (isDark) darkColor else lightColor
        canvas.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY + (row + 1) * cellSide,
            paint
        )
    }

}