import java.io.*;

/**
 * Abstract base class for graphs. A graph is a set of vertices and a set of
 * edges. Vertices are represented by integers from 0 to n - 1. Edges are
 * ordered pairs of vertices.
 * 
 * @author Koffman and Wolfgang
 */

public abstract class GeneralizedAbstractGraph implements GeneralizedGraph {

  // Data Fields
  /** The number of vertices */
  private int numV;

  /** Flag to indicate whether this is a directed graph */
  private boolean directed;

  protected int weightNum;

  // Constructor
  /**
   * Construct a graph with the specified number of vertices and the directed
   * flag. If the directed flag is true, this is a directed graph.
   * 
   * @param numV     The number of vertices
   * @param directed The directed flag
   */
  public GeneralizedAbstractGraph(int numV, boolean directed, int weightCount) {
    this.numV = numV;
    this.directed = directed;
    weightNum = weightCount;
  }

  // Accessor Methods
  /**
   * Return the number of vertices.
   * 
   * @return The number of vertices
   */
  public int getNumV() {
    return numV;
  }

  /**
   * Return whether this is a directed graph.
   * 
   * @return true if this is a directed graph
   */
  public boolean isDirected() {
    return directed;
  }
}

