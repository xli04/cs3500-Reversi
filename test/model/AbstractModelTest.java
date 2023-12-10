package model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractModelTest {
  //Factory methods to create concrete models

  /**
   * Factory method to create a rigged empty model via the package-private constructor
   *
   * @return a concrete MutableReversiModel impl. whose tiles all have color empty
   */
  public abstract MutableReversiModel createRiggedEmptyModel(int size);

  /**
   * Factory method to create a rigged full model via the package-private constructor
   *
   * @return a concrete MutableReversiModel impl. whose tiles all have a color black or white
   */
  public abstract MutableReversiModel createRiggedFullModel(int size);

  /**
   * Factory method to create a rigged full model via the public direction (builder constructor)
   *
   * @return a concrete MutableReversiModel impl. whose tiles are set to the default starting
   * position
   */
  public abstract MutableReversiModel createDefaultModel();

  //Tests to abstract together
  //getter tests (getSize()), getDeepCopy()
  //check tests for empty boards / full board
  //game over false / true
  // can move false,
  // make move throws ISE,
  // start game cannot be called twice

  @Test
  public void gameOverIsFalseForEmptyBoard() {
    MutableReversiModel model = createRiggedEmptyModel(6);
    assertTrue(model.hasToPass());

  }

}
