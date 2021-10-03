package chess.pgn;

import chess.board.Board;
import chess.board.Move;
import chess.player.Player;

public interface PGNPersistence {

    void persistGame(Game game);

    Move getNextBestMove(Board board, Player player, String gameText);

}
