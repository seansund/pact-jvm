package au.com.dius.pact.consumer.dsl;

/**
 * Interface that provides the base definition for a Json implementation of PactDsl.
 * This interface was separated from <code>PactDslJson</code> because #PactDslJsonRootValue
 * and #PactDslRootValue defined static id() methods that conflict with the interface.
 * @param <This> the reference to the current class (for subclasses to provided)
 */
public abstract class PactDslJsonWithId<This extends PactDslJsonWithId> extends PactDslJson<PactDslJsonWithId> {
  public PactDslJsonWithId(PactDslJsonWithId parent, String rootPath, String rootName) {
    super(parent, rootPath, rootName);
  }

  public PactDslJsonWithId(String rootPath, String rootName) {
    super(rootPath, rootName);
  }

  /**
   * Attribute named 'id' that must be a numeric identifier
   *
   * @return A reference to <code>this</code>
   */
  public This id() {
    return id("id");
  }

  /**
   * Attribute named 'id' that has the value provided
   *
   * @param id the id value
   * @return A reference to <code>this</code>
   */
  public abstract This id(Long id);

  /**
   * Attribute that must be a numeric identifier
   *
   * @param name attribute name
   * @return A reference to <code>this</code>
   */
  public abstract This id(String name);

  /**
   * Attribute that must be a numeric identifier
   *
   * @param name attribute name
   * @param id   example id to use for generated bodies
   * @return A reference to <code>this</code>
   */
  public abstract This id(String name, Long id);
}
