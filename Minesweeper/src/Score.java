import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class Score {
    private Game game; // game passed through to allow for game manipulation

    public Score(Game game) {
        this.game = game;
    }

    public void score() {

        /* if user wins this adds their score to 2 lists orders the lists (one for the top 5 scores & one for all scores) then adds them to two separate files  */
        if (game.totalBoxesRevealed() >= 100 - game.totalMines() && (!game.win && !game.lose)) {
            game.win = true;
            game.endDate = new Date();
            game.allScores.add(game.time);
            game.scoreList.add(game.time);
            Collections.sort(game.scoreList);
            Collections.sort(game.allScores);

            try {
                FileWriter fileWriter = new FileWriter("highscores.txt");
                fileWriter.write("Top 5 Leaderboard" + "\n");
                for (int i1 = 0; i1 < game.scoreList.size(); i1++) {
                    if (i1 < 5) {
                        Integer score = game.scoreList.get(i1);
                        fileWriter.write(score.toString() + "\n");
                    }
                }
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException exception) {
                System.out.println("An error occurred.");
                exception.printStackTrace();
            }


            try {
                FileWriter fileWriter = new FileWriter("allscores.txt");
                fileWriter.write("All Scores" + "\n");
                for (int i1 = 0; i1 < game.allScores.size(); i1++) {
                    Integer score = game.scoreList.get(i1);
                    fileWriter.write(score.toString() + "\n");
                }
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException exception) {
                System.out.println("An error occurred.");
                exception.printStackTrace();
            }
        }
    }
}