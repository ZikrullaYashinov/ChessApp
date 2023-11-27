package zikrulla.production.chessapp

import zikrulla.production.chessapp.model.ChessPiece
import zikrulla.production.chessapp.model.Chessman
import zikrulla.production.chessapp.model.Move
import zikrulla.production.chessapp.model.Player
import zikrulla.production.chessapp.model.Square
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object ChessGame : ChessDelegate {

    private var piecesBox = mutableSetOf<ChessPiece>()
    private var moves = mutableListOf<Move>()
    private var backwardPosition = 0

    init {
        reset()
    }

    fun addPiece(piece: ChessPiece) {
        piecesBox.add(piece)
    }

    fun resetGame() {
        reset()
        moves.clear()
        backwardPosition = 0
    }

    private fun reset() {
        clear()
        for (col in 0..7) {
            addPiece(
                ChessPiece(col, 1, Player.WHITE, Chessman.PAWN, R.drawable.chess_plt)
            )
            addPiece(
                ChessPiece(col, 6, Player.BLACK, Chessman.PAWN, R.drawable.chess_pdt)
            )
        }
        for (i in 0..1) {
            addPiece(ChessPiece(i * 7, 0, Player.WHITE, Chessman.ROOK, R.drawable.chess_rlt))
            addPiece(ChessPiece(i * 7, 7, Player.BLACK, Chessman.ROOK, R.drawable.chess_rdt))
            addPiece(ChessPiece(1 + i * 5, 0, Player.WHITE, Chessman.KNIGHT, R.drawable.chess_nlt))
            addPiece(ChessPiece(1 + i * 5, 7, Player.BLACK, Chessman.KNIGHT, R.drawable.chess_ndt))
            addPiece(ChessPiece(2 + i * 3, 0, Player.WHITE, Chessman.BISHOP, R.drawable.chess_blt))
            addPiece(ChessPiece(2 + i * 3, 7, Player.BLACK, Chessman.BISHOP, R.drawable.chess_bdt))
        }
        addPiece(ChessPiece(3, 0, Player.WHITE, Chessman.QUEEN, R.drawable.chess_qlt))
        addPiece(ChessPiece(3, 7, Player.BLACK, Chessman.QUEEN, R.drawable.chess_qdt))
        addPiece(ChessPiece(4, 0, Player.WHITE, Chessman.KING, R.drawable.chess_klt))
        addPiece(ChessPiece(4, 7, Player.BLACK, Chessman.KING, R.drawable.chess_kdt))
    }

    fun clear() {
        piecesBox.clear()
    }

    override fun movePiece(from: Square, to: Square): Boolean {
        val isMove = if (moves.size % 2 == 0) Player.WHITE else Player.BLACK
        val checkKing = isCheckKing(isMove)
        val r = canMove(from, to, isMove, checkKing)
        if (r) {
            val moveSuccess = movePiecePrivate(from, to)
            if (isCheckKing(isMove) && moveSuccess) {
                backward()
                return false
            }
            return true
        }
        return false
    }

    private fun movePiecePrivate(from: Square, to: Square, isShow: Boolean? = null): Boolean {
        if (from.row == to.row && from.col == to.col) return false
        if (to.col > 7 || from.row > 7 || to.col < 0 || from.row < 0) return false
        val piece = pieceAt(from) ?: return false

        pieceAt(to)?.let {
            if (it.player == piece.player) return false
            piecesBox.remove(it)
        }

        if (isShow == null || isShow == false)
            moves.add(Move(piece, to))

        piecesBox.remove(piece)
        addPiece(piece.copy(col = to.col, row = to.row))
        return true
    }

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            pieceAt(Square(nextCol, from.row))?.let {
                return false
            }
        }
        return true
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            pieceAt(Square(from.col, nextRow))?.let {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonallyBetween(from: Square, to: Square): Boolean {
        val gapCol = abs(from.col - to.col) - 1
        val gapRow = abs(from.row - to.row) - 1
        if (gapCol == 0 && gapRow == 0) return true
        for (i in 1..gapRow) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            pieceAt(Square(nextCol, nextRow))?.let {
                return false
            }
        }
        return true
    }

    private fun canKnightMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canRookMove(from: Square, to: Square): Boolean {
        return from.col == to.col && isClearVerticallyBetween(from, to)
                || from.row == to.row && isClearHorizontallyBetween(from, to)
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == abs(from.row - to.row)
                && isClearDiagonallyBetween(from, to)
    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canBishopMove(from, to) || canRookMove(from, to)
    }

    private fun canKingMove(
        from: Square,
        to: Square,
        isOppositeSide: Boolean? = null,
        player: Player? = null,
        orientation: Boolean? = null
    ): Boolean {
        val isKingMove = 1 >= abs(from.col - to.col) && 1 >= abs(from.row - to.row)
        if (abs(from.col - to.col) == 2 && player != null) {
            var fromRook: Square? = null
            var toRook: Square? = null
            var haveGot = true
            var isClear = true
            var isOppositeSideBoards = true
            var isNotMove = true
            if (orientation == null || orientation == true) {
                if (player == Player.WHITE) {
//                    have got
                    haveGot = from.col == 4 && from.row == 0
                    var rookSquare = Square(7, 0)
                    if (to.col == 6 && to.row == 0) {
                        fromRook = rookSquare
                        toRook = Square(5,0)
                        rookSquare = Square(7, 0)
                        haveGot = haveGot && pieceAt(rookSquare)?.let {
                            it.chessman == Chessman.ROOK && it.player == player
                        } ?: false
                    } else if (to.col == 2 && to.row == 0) {
                        rookSquare = Square(0, 0)
                        toRook = Square(3,0)
                        fromRook = rookSquare
                        haveGot = haveGot && pieceAt(rookSquare)?.let {
                            it.chessman == Chessman.ROOK && it.player == player
                        } ?: false
                    }


//                    is clear
                    isClear = isClearHorizontallyBetween(from, rookSquare)

//                    is not opposite side
                    val start = min(from.col, to.col)
                    val end = max(from.col, to.col)
                    for (col in start..end) {
                        isOppositeSideBoards = isOppositeSideBoards &&
                                isOppositeSide(player, Square(col, 0), from)
                    }

//                    is not move
                    moves.forEach {
                        if (it.piece.player == player) {
                            isNotMove =
                                isNotMove && it.piece.chessman != Chessman.KING && it.piece.chessman != Chessman.ROOK
                        }
                    }
                } else {
//                    have got
                    haveGot = from.col == 4 && from.row == 7
                    var rookSquare = Square(7, 7)
                    if (to.col == 6 && to.row == 7) {
                        rookSquare = Square(7, 7)
                        fromRook = rookSquare
                        toRook = Square(5,7)
                        haveGot = haveGot && pieceAt(rookSquare)?.let {
                            it.chessman == Chessman.ROOK && it.player == player
                        } ?: false
                    } else if (to.col == 2 && to.row == 7) {
                        rookSquare = Square(0, 7)
                        toRook = Square(3,7)
                        fromRook = rookSquare
                        haveGot = haveGot && pieceAt(rookSquare)?.let {
                            it.chessman == Chessman.ROOK && it.player == player
                        } ?: false
                    }


//                    is clear
                    isClear = isClearHorizontallyBetween(from, rookSquare)

//                    is not opposite side
                    val start = min(from.col, to.col)
                    val end = max(from.col, to.col)
                    for (col in start..end) {
                        isOppositeSideBoards = isOppositeSideBoards &&
                                isOppositeSide(player, Square(col, 7), from)
                    }

//                    is not move
                    moves.forEach {
                        if (it.piece.player == player) {
                            isNotMove =
                                isNotMove && it.piece.chessman != Chessman.KING && it.piece.chessman != Chessman.ROOK
                        }
                    }
                }
            } else {
                TODO("orientation false")
            }
            if (haveGot && isClear && isNotMove && isOppositeSideBoards) {

                movePiecePrivate(fromRook!!, toRook!!)
                moves.removeAt(moves.size - 1)
                return true
            }
        }
        if (isOppositeSide != null)
            return isKingMove && !isOppositeSide
        return isKingMove
    }

    private fun canPawnMove(from: Square, to: Square, player: Player, isAttack: Boolean): Boolean {
        return when (player) {
            Player.WHITE -> {
                val deltaRow = to.row - from.row
                if (deltaRow <= 0) return false
                if (isAttack) {
                    if (deltaRow == 1 && abs(to.col - from.col) == 1)
                        return true
                } else {
                    val piece = pieceAt(to)
                    if ((deltaRow) <= 2 && from.col == to.col && piece == null)
                        return if (from.row != 1) deltaRow == 1 else true
                    else if (piece != null && canPawnMove(from, to, player, true)) {
                        return true
                    }
                }
                false
            }

            Player.BLACK -> {
                val deltaRow = from.row - to.row
                if (deltaRow <= 0) return false
                if (isAttack) {
                    if (deltaRow == 1 && abs(to.col - from.col) == 1)
                        return true
                } else {
                    val piece = pieceAt(to)
                    if ((deltaRow) <= 2 && from.col == to.col && piece == null)
                        return if (from.row != 6) deltaRow == 1 else true
                    else if (piece != null && canPawnMove(from, to, player, true)) {
                        return true
                    }
                }
                false
            }
        }
    }

    fun canMove(
        from: Square,
        to: Square,
        isMove: Player? = null,
        checkKing: Boolean? = null
    ): Boolean {
        if (from.row == to.row && from.col == to.col) return false
        val piece = pieceAt(from) ?: return false
        isMove?.let { if (piece.player != it) return false }

        val playerOpposite = if (piece.player == Player.WHITE) Player.BLACK else Player.WHITE

        when (piece.chessman) {
            Chessman.KING -> {
                if (isMove == null) {
                    val r = canKingMove(from, to)
                    return r
                } else {
                    val square = findKing(playerOpposite)?.let { findKing ->
                        Square(findKing.col, findKing.row)
                    }
                    val oppositeSide =
                        isOppositeSide(oppositePlayer = playerOpposite, to, myKingSquare = square)
                    val r = canKingMove(from, to, oppositeSide, piece.player)

                    return r
                }
            }

            Chessman.QUEEN -> {
                val r = canQueenMove(from, to)
                return r
            }

            Chessman.ROOK -> {
                val r = canRookMove(from, to)
                return r
            }

            Chessman.BISHOP -> {
                val r = canBishopMove(from, to)
                return r
            }

            Chessman.KNIGHT -> {
                val r = canKnightMove(from, to)
                return r
            }

            Chessman.PAWN -> {
                val r = canPawnMove(from, to, piece.player, false)
                return r
            }
        }
    }

    override fun pieceAt(square: Square): ChessPiece? {
        piecesBox.forEach {
            if (square.col == it.col && square.row == it.row) {
                return it
            }
        }
        return null
    }

    private fun findKing(player: Player): ChessPiece? {
        piecesBox.forEach {
            if (it.chessman == Chessman.KING && it.player == player) {
                return it
            }
        }
        return null
    }

    private fun isOppositeSide(oppositePlayer: Player, to: Square, myKingSquare: Square?): Boolean {
        piecesBox.forEach { piece ->
            if (piece.player == oppositePlayer) {
                val fromPiece = Square(piece.col, piece.row)
                val findKing = findKing(oppositePlayer)
                if (piece.chessman == Chessman.KING) {
                    if (findKing?.let {
                            abs(it.col - to.col) <= 1 && abs(it.row - to.row) <= 1
                        } == true) {
                        return true
                    }
                } else if (piece.chessman == Chessman.PAWN) {
                    if (canPawnMove(fromPiece, to, piece.player, true))
                        return true
                } else if (piece.chessman == Chessman.QUEEN) {
                    if (canQueenMove(fromPiece, to))
                        return true
                } else if (piece.chessman == Chessman.BISHOP) {
                    if (canBishopMove(fromPiece, to))
                        return true
                } else if (piece.chessman == Chessman.ROOK) {
                    if (canRookMove(fromPiece, to))
                        return true
                } else {
                    if (canMove(fromPiece, to))
                        return true
                }
            }
        }
        return false
    }

    private fun isCheckKing(player: Player): Boolean {
        when (player) {
            Player.WHITE -> {
                val findKing = findKing(Player.WHITE)
                val kingSquare = findKing?.let {
                    Square(findKing.col, findKing.row)
                }

                piecesBox.forEach { piece ->
                    val pieceSquare = Square(piece.col, piece.row)
                    if (piece.player == Player.BLACK) {
                        kingSquare?.let {
                            if (canMove(pieceSquare, it)) {
                                val a = canMove(pieceSquare, it)
                                return true
                            }
                        }
                    }
                }
            }

            Player.BLACK -> {
                val findKing = findKing(Player.BLACK)
                val kingSquare = findKing?.let {
                    Square(findKing.col, findKing.row)
                }

                piecesBox.forEach { piece ->
                    val pieceSquare = Square(piece.col, piece.row)
                    if (piece.player == Player.WHITE) {
                        kingSquare?.let {
                            if (canMove(pieceSquare, kingSquare))
                                return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun showMove(isForward: Boolean = true, exactlyPosition: Int? = null) {
        reset()
        var position = 0
        position = if (exactlyPosition != null) {
            exactlyPosition
        } else {
            if (isForward) {
                if (backwardPosition < 0)
                    backwardPosition++
            } else {
                if (moves.size + backwardPosition > 0)
                    backwardPosition--
            }
            moves.size + backwardPosition - 1
        }

        if (position >= 0) {
            for (i in 0..position) {
                val move = moves[i]
                val from = Square(move.piece.col, move.piece.row)
                movePiecePrivate(from, move.toSquare, true)
            }
        }
    }

    private fun backward() {
        if (moves.size >= 1) {
            moves.removeAt(moves.size - 1)
            showMove(exactlyPosition = moves.size - 1)
        }
    }


    fun pgnBoard(): String {
        var desc = ""
        desc += "  a b c d e f g h\n"
        for (row in 7 downTo 0) {
            desc += "${row + 1}"
            desc += boardRow(row)
            desc += " ${row + 1}\n"
        }
        desc += "  a b c d e f g h"
        return desc
    }

    override fun toString(): String {
        var desc = ""
        for (row in 7 downTo 0) {
            desc += "$row"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }

    private fun boardRow(row: Int): String {
        var desc = ""
        for (col in 0..7) {
            val piece = pieceAt(Square(col, row))
            desc += " "
            desc += piece?.let {
                val white = piece.player == Player.WHITE
                when (piece.chessman) {
                    Chessman.KING -> if (white) "k" else "K"
                    Chessman.QUEEN -> if (white) "q" else "Q"
                    Chessman.ROOK -> if (white) "r" else "R"
                    Chessman.BISHOP -> if (white) "b" else "B"
                    Chessman.KNIGHT -> if (white) "n" else "N"
                    Chessman.PAWN -> if (white) "p" else "P"
                }
            } ?: "."
        }
        return desc
    }

}