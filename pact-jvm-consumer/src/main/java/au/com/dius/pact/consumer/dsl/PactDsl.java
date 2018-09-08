package au.com.dius.pact.consumer.dsl;

import au.com.dius.pact.model.generators.Generator;
import au.com.dius.pact.model.generators.Generators;
import au.com.dius.pact.model.matchingrules.Category;
import au.com.dius.pact.model.matchingrules.DateMatcher;
import au.com.dius.pact.model.matchingrules.IncludeMatcher;
import au.com.dius.pact.model.matchingrules.MatchingRule;
import au.com.dius.pact.model.matchingrules.MaxTypeMatcher;
import au.com.dius.pact.model.matchingrules.MinMaxTypeMatcher;
import au.com.dius.pact.model.matchingrules.MinTypeMatcher;
import au.com.dius.pact.model.matchingrules.RegexMatcher;
import au.com.dius.pact.model.matchingrules.TimeMatcher;
import au.com.dius.pact.model.matchingrules.TimestampMatcher;

/**
 * Abstract base class to support building data structures for pact tests
 * including matcher rules and value generators.
 *
 * @param <Base> describes the base class of the elements in the PactDsl
 *              tree that will be used to reference the parent type
 */
public abstract class PactDsl<Base extends PactDsl> implements DslPart {
  public static final String HEXADECIMAL = "[0-9a-fA-F]+";
  public static final String IP_ADDRESS = "(\\d{1,3}\\.)+\\d{1,3}";
  public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
  public static final long DATE_2000 = 949323600000L;

  protected final Base parent;
  protected final String rootPath;
  protected final String rootName;
  protected Category matchers = new Category("body");
  protected Generators generators = new Generators();
  protected boolean closed = false;

  public PactDsl(Base parent, String rootPath, String rootName) {
    this.parent = parent;
    this.rootPath = rootPath;
    this.rootName = rootName;
  }

  public PactDsl(String rootPath, String rootName) {
    this.parent = null;
    this.rootPath = rootPath;
    this.rootName = rootName;
  }

  @Override
  public Category getMatchers() {
    return matchers;
  }

  public void setMatchers(Category matchers) {
    this.matchers = matchers;
  }

  @Override
  public Generators getGenerators() {
    return generators;
  }

  public void setGenerators(Generators generators) {
    this.generators = generators;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public String getRootName() {
    return rootName;
  }

  protected RegexMatcher regexp(String regex) {
    return new RegexMatcher(regex);
  }

  protected TimestampMatcher matchTimestamp(String format) {
    return new TimestampMatcher(format);
  }

  protected DateMatcher matchDate(String format) {
    return new DateMatcher(format);
  }

  protected TimeMatcher matchTime(String format) {
    return new TimeMatcher(format);
  }

  protected MinTypeMatcher matchMin(Integer min) {
    return new MinTypeMatcher(min);
  }

  protected MaxTypeMatcher matchMax(Integer max) {
    return new MaxTypeMatcher(max);
  }

  protected MinMaxTypeMatcher matchMinMax(Integer minSize, Integer maxSize) {
    return new MinMaxTypeMatcher(minSize, maxSize);
  }

  protected IncludeMatcher includesMatcher(Object value) {
    return new IncludeMatcher(String.valueOf(value));
  }

  protected Category addRule(String item, MatchingRule matchingRule) {
    return matchers.addRule(item, matchingRule);
  }

  protected Category addRule(MatchingRule matchingRule) {
    return matchers.addRule(matchingRule);
  }

  protected Generators addGenerator(au.com.dius.pact.model.generators.Category category, String key, Generator generator) {
    return generators.addGenerator(category, key, generator);
  }

  protected Generators addGenerator(au.com.dius.pact.model.generators.Category category, Generator generator) {
    return generators.addGenerator(category, generator);
  }

  /**
   * This closes off the object graph build from the DSL in case any close[Object|Array] methods have not been called.
   *
   * @return The root object of the object graph
   */
  @Override
  public DslPart close() {
    PactDsl parentToReturn = this;

    if (!closed) {
      for (PactDsl parent = closeChild(); parent != null; parent = parent.closeChild()) {
        parentToReturn = parent;
      }
    }

    return parentToReturn;
  }

  /**
   * Closes the child element (object or array) and returns the parent
   * @return The parent
   */
  public abstract Base closeChild();

  /**
   * Closes the child element (object or array) and returns the parent,
   * cast to the <code>parentType</code>
   * @param parentType the specific, expected class type of the parent
   * @param <T> the specific, expected type of the parent
   * @return The parent of the child element, cast to the <code>parentType</code>
   */
  public <T extends Base> T closeChild(Class<T> parentType) {
    return (T) closeChild();
  }

  /**
   * Returns the parent of this part (object or array)
   *
   * @return parent, or null if it is the root
   */
  public Base getParent() {
    return parent;
  }
}
