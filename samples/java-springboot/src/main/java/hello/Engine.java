package hello;

import hello.Application.PlayerState;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Engine {

  private static String MY_NAME = "https://cloud-bowl-2yebdnw3bq-uc.a.run.app/";

  private final int width;
  private final int height;

  private Player myPlayer;

  private Set<Player> enemies;

  private boolean wasHit;

  private int[][] grid;

  private static Point center = new Point(4, 3);

  public Engine(Map<String, PlayerState> gameState, int width, int height) {
    this.myPlayer = new Player(gameState.get(MY_NAME));
    this.wasHit = gameState.get(MY_NAME).wasHit;
    this.width = width;
    this.height = height;

    this.enemies = new HashSet<>();
    for (Map.Entry<String, PlayerState> entry : gameState.entrySet()) {
      if (!entry.getKey().equals(MY_NAME)) {
        enemies.add(new Player(entry.getValue()));
      }
    }

    this.grid = new int[height][width];

    for (Player ene : enemies) {
      Point ep = ene.position;
      this.grid[ep.y][ep.x] = 99;

      for (Point p : killSet(ene)) {
        this.grid[p.y][p.x] += 1;
      }
    }

    System.out.println("Grid: ");
    for (int[] row : grid) {
      System.out.println(Arrays.toString(row));
    }
  }

  public String getMove() {
    if (myPlayer.position.x == center.x
        && myPlayer.position.y == center.y
        && myPlayer.dir == Direction.NORTH) {
      resetCenter();
    }

    Point currPos = myPlayer.position;
    System.out.println("My pos: " + currPos);

    if (grid[currPos.y][currPos.x] > 0) {
      Point forwardMove = getForward(myPlayer);
      if (grid[forwardMove.y][forwardMove.x] < grid[currPos.y][currPos.x]) {
        System.out.println("Forward to escape... " + forwardMove);
        return "F";
      } else if (wasHit && Math.random() < 0.35) {
        return "R";
      }
    }

    Set<Point> killSet = killSet(myPlayer);
    for (Player enemy : enemies) {
      if (killSet.contains(enemy.position)) {
        System.out.println("Throwing covers: " + killSet);
        return "T";
      }
    }

//    if (grid[currPos.y][currPos.x] == 1 && wasHit) {
//      Point forwardMove = getForward(myPlayer);
//      if (grid[forwardMove.y][forwardMove.x] == 1) {
//        System.out.println("Spin to escape... " + currPos);
//        return "R";
//      } else {
//        System.out.println("Forward to escape... " + forwardMove);
//        return "F";
//      }
//    }

    Point forward = getForward(myPlayer);

    if (grid[forward.y][forward.x] != 1
        && dist(forward, center) < dist(currPos, center)) {
      return "F";
    } else {
      return "R";
    }
  }

  private int dist(Point a, Point b) {
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
  }

  private Point getForward(Player player) {
    Point newPoint = new Point(
        player.position.x + player.dir.delta.x, player.position.y + player.dir.delta.y);

    if (inBounds(newPoint)) {
      return newPoint;
    } else {
      return player.position;
    }
  }

  private void resetCenter() {
    int cX = (int) (Math.random() * 3) - 1 + width / 2;
    int cY = (int) (Math.random() * 3) - 1 + height / 2;
    center = new Point(cX, cY);
  }

  private boolean inBounds(Point point) {
    return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
  }

  private HashSet<Point> killSet(Player player) {
    HashSet<Point> killSet = new HashSet<>();

    Point currPos = player.position;
    Point delta = player.dir.delta;

    for (int i = 0; i < 3; i++) {
      currPos = new Point(currPos.x + delta.x, currPos.y + delta.y);
      if (inBounds(currPos)) {
        killSet.add(currPos);
      }
    }

    return killSet;
  }
}
