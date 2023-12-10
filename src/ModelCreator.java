import model.HexReversiModel;
import model.ModelStatus;
import model.MutableReversiModel;
import model.ReadOnlyReversiModel;
import model.SquareReversiModel;

/**
 * The {@code ModelCreator} class provides static methods for creating instances
 * of {@code MutableReversiModel}. It supports the creation of different types of
 * Reversi models, such as SQUARE and HEX, with specified size and status.
 */
public class ModelCreator {

  /**
   * Creates a new MutableReversiModel based on the specified model type, size, and status.
   *
   * @param type   The type of the Reversi model (SQUARE or HEX).
   * @param size   The size of the Reversi model board.
   * @param status The initial status of the Reversi model.
   * @return A new MutableReversiModel instance based on the specified parameters.
   * @throws IllegalArgumentException If the size is invalid or if an unsupported model type
   *                                  is provided.
   */
  public static MutableReversiModel create(ReadOnlyReversiModel.ModelType type, int size,
                                           ModelStatus status) {
    switch (type) {
      case SQUARE: {
        if (size > 0 && size % 2 == 0) {
          return new SquareReversiModel.ModelBuilder().setSize(size).setStatus(status).build();
        } else {
          throw new IllegalArgumentException("Invalid size");
        }
      }
      case HEX: {
        if (size > 1) {
          return new HexReversiModel.ModelBuilder().setSize(size).setStatus(status).build();
        } else {
          throw new IllegalArgumentException("Invalid size");
        }
      }
      default: {
        throw new IllegalArgumentException("Can not play on mock model");
      }
    }
  }

  /**
   * Creates a new MutableReversiModel based on the specified model type and status.
   *
   * @param type   The type of the Reversi model (SQUARE or HEX).
   * @param status The initial status of the Reversi model.
   * @return A new MutableReversiModel instance based on the specified parameters.
   * @throws IllegalArgumentException If an unsupported model type is provided.
   */
  public static MutableReversiModel create(ReadOnlyReversiModel.ModelType type,
                                           ModelStatus status) {
    switch (type) {
      case SQUARE: {
        return new SquareReversiModel.ModelBuilder().setStatus(status).build();
      }
      case HEX: {
        return new HexReversiModel.ModelBuilder().setStatus(status).build();
      }
      default: {
        throw new IllegalArgumentException("Can not play on mock model");
      }
    }
  }
}
