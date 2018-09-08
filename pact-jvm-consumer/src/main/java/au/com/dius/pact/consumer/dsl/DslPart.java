package au.com.dius.pact.consumer.dsl;

import au.com.dius.pact.model.generators.Generators;
import au.com.dius.pact.model.matchingrules.Category;

/**
 * Interface to support building data structures for pact tests including
 * matcher rules and value generators.
 */
public interface DslPart {

  /**
   * Returns the string representation of the underlying data
   * model in the appropriate format (e.g. json). This should
   * return the same thing as #getBody().toString().
   *
   * @return The string representation of the underlying data
   * model
   */
  String toString();

  /**
   * Returns an object that represents the data that makes
   * up the <code>body</code> of the request or the response.
   * This will ultimately be used to call #toString() to turn
   * the object into the string representation of the model.
   *
   * @return An object that represents the data of the DslPart
   */
  Object getBody();

  /**
   * Returns the list of rule matchers that have been defined
   * for the model.
   *
   * @return The list of rule matchers that have been defined
   * for the model.
   */
  Category getMatchers();

  /**
   * Returns the list of value generators that have been
   * defined for the model.
   *
   * @return The list of value generators that have been
   * defined for the model.
   */
  Generators getGenerators();

  String getRootPath();

  String getRootName();

  /**
   * Marks the object as completed for defining the model
   * (i.e. closed) and returns the root DslPart for the
   * object tree.
   *
   * @return The root DslPart for the object tree.
   */
  DslPart close();
}
