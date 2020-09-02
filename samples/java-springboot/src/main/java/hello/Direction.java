package hello;

import java.awt.Point;

public enum Direction {
  NORTH(new Point(0, -1)),
  SOUTH(new Point(0, 1)),
  EAST(new Point(1, 0)),
  WEST(new Point(-1, 0));

  final Point delta;

  Direction(Point delta) {
    this.delta = delta;
  }

  public static Direction charToDir(String code) {
    if (code.equals("N")) {
      return NORTH;
    } else if (code.equals("S")) {
      return SOUTH;
    } else if (code.equals("E")) {
      return EAST;
    } else {
      return WEST;
    }
  }
}
