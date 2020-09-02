package hello;

import hello.Application.PlayerState;
import java.awt.Point;

public class Player {

  public final Point position;
  public final Direction dir;

  public Player(PlayerState state) {
    this.position = new Point(state.x, state.y);
    this.dir = Direction.charToDir(state.direction);
  }

  @Override
  public String toString() {
    return "Player{" +
        "position=" + position +
        ", dir=" + dir +
        '}';
  }
}
