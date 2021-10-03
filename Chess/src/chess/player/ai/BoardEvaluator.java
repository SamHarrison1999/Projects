package chess.player.ai;

import chess.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);

}
