package au.com.dius.pact.consumer.dsl.xml;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import au.com.dius.pact.consumer.InvalidMatcherException;
import au.com.dius.pact.consumer.dsl.PactDsl;
import au.com.dius.pact.consumer.dsl.UrlMatcherSupport;
import au.com.dius.pact.model.generators.Category;
import au.com.dius.pact.model.generators.DateGenerator;
import au.com.dius.pact.model.generators.DateTimeGenerator;
import au.com.dius.pact.model.generators.ProviderStateGenerator;
import au.com.dius.pact.model.generators.RandomDecimalGenerator;
import au.com.dius.pact.model.generators.RandomHexadecimalGenerator;
import au.com.dius.pact.model.generators.RandomIntGenerator;
import au.com.dius.pact.model.generators.RandomStringGenerator;
import au.com.dius.pact.model.generators.TimeGenerator;
import au.com.dius.pact.model.generators.UuidGenerator;
import au.com.dius.pact.model.matchingrules.EqualsMatcher;
import au.com.dius.pact.model.matchingrules.MatchingRule;
import au.com.dius.pact.model.matchingrules.MatchingRuleGroup;
import au.com.dius.pact.model.matchingrules.RuleLogic;
import au.com.dius.pact.model.matchingrules.TypeMatcher;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Abstract class that serves as the common base for an XML implementation of DslPart. Unlike
 * the Json implementation of DslPart, PactDslXml uses a w3c Document and Element as the
 * underlying implementation of the model.
 * @param <This> the type of <code>This</code> class (child implementations provide the
 *              value so the correct type is returned from the builder methods)
 */
public abstract class PactDslXml<This extends PactDslXml> extends PactDsl<PactDslXml> {
  /**
   * The value used to separate the path elements, for the element tree. Ideally this would
   * be done using xpath but currently uses jsonPath to leverage the existing matchers.
   * @see #buildPath(String, String)
   */
  public static final String PATH_SEPARATOR = ".";

  private static final String INVALID_REGULAR_EXPRESSION = "Example \"%s\" does not match regular expression \"%s\"";
  private static final String INVALID_HEXVALUE = "Example \"%s\" is not a hexadecimal value";
  private static final String INVALID_UUID = "Example \"%s\" is not an UUID";

  protected final Document document;
  protected final Element body;
  /**
   * An index value used to keep track of where the element appears in the tree. This value
   * is used to correctly build the jsonPath value to the node.
   * @see #buildPath(String, String)
   */
  protected int elementIndex = -1;

  public PactDslXml() {
    super(PATH_SEPARATOR, "");
    document = buildDocument();
    body = null;
  }

  public PactDslXml(String rootName) {
    this(PATH_SEPARATOR, rootName);
  }

  public PactDslXml(String rootPath, String rootName) {
    super(rootPath, rootName);
    document = buildDocument();
    body = document.createElement(rootName);
    document.appendChild(body);
  }

  public PactDslXml(String rootPath, String rootName, PactDslXml parent) {
    super(parent, rootPath, rootName);
    document = parent.document;
    body = document.createElement(rootName);
  }

  protected PactDslXml(String rootPath, String rootName, PactDslXml parent, PactDslXml body) {
    super(parent, rootPath, rootName);
    this.document = parent.document;
    this.body = body.body;
    this.setMatchers(body.getMatchers().copyWithUpdatedMatcherRootPrefix(rootPath));
    this.setGenerators(body.getGenerators().copyWithUpdatedMatcherRootPrefix(rootPath));
  }

  protected static Document buildDocument() {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      return documentBuilder.newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("Unable to initialize XML framework");
    }
  }

  public String toString() {
    return getBody().toString();
  }

  @Override
  public Object getBody() {
    return new DocumentWrapper(document);
  }

  public abstract This stringValue(String name, String value);

  public This objectValue(String name, Object value) {
    if (value == null) {
      return nullValue(name);
    }

    return stringValue(name, String.valueOf(value));
  }

  public This numberValue(String name, Number value) {
    return stringValue(name, value != null ? valueOf(value) : null);
  }

  public This booleanValue(String name, Boolean value) {
    return stringValue(name, value != null ? valueOf(value) : null);
  }

  public This stringType(String name) {
    addGenerator(
        Category.BODY,
        matcherKey(name),
        new RandomStringGenerator(20));
    return stringType(name, "string");
  }

  /**
   * Builds the path to the element. Ideally this would use xpath but currently
   * uses jsonPath so that the existing matchers can be leveraged. Particularly
   * that means a <code>[{index}]</code> value is added to the end of each node
   * name.
   * @param rootPath the root path to the current element
   * @param rootName the name of the current element
   * @return The path to the element.
   */
  protected String buildPath(String rootPath, String rootName) {
    return rootPath + rootName + "[" + elementIndex + "]" + PATH_SEPARATOR;
  }

  public This stringType(String name, String example) {
    stringValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This numberType(String name) {
    addGenerator(
        Category.BODY,
        matcherKey(name),
        new RandomIntGenerator(1, 500));
    return numberType(name, 10);
  }

  public This numberType(String name, Number example) {
    numberValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This integerType(String name) {
    addGenerator(
        Category.BODY,
        matcherKey(name),
        new RandomIntGenerator(1, 500));
    return integerType(name, 10);
  }

  public This integerType(String name, Long example) {
    numberValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This integerType(String name, Integer example) {
    numberValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This decimalType(String name) {
    addGenerator(
        Category.BODY,
        matcherKey(name),
        new RandomDecimalGenerator(4));
    return decimalType(name, 10.0);
  }

  public This decimalType(String name, Double example) {
    numberValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This decimalType(String name, BigDecimal example) {
    numberValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This booleanType(String name, Boolean example) {
    booleanValue(name, example);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This stringMatcher(String name, String regex, String example) {
    if (!example.matches(regex)) {
      throw new InvalidMatcherException(format(
          INVALID_REGULAR_EXPRESSION,
          example,
          regex));
    }
    stringValue(name, example);
    addRule(matcherKey(name), regexp(regex));
    return (This) this;
  }

  public This timestamp(String name) {
    return timestamp(name, DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
  }

  public This timestamp(String name, String format) {
    addGenerator(Category.BODY, matcherKey(name), new DateTimeGenerator(format));
    return timestamp(name, format, new Date(DATE_2000));
  }

  public This timestamp(String name, String format, Date example) {
    FastDateFormat instance = FastDateFormat.getInstance(format);
    stringValue(name, instance.format(example));
    addRule(matcherKey(name), matchTimestamp(format));
    return (This) this;
  }

  public This date(String name) {
    return date(name, DateFormatUtils.ISO_DATE_FORMAT.getPattern());
  }

  public This date(String name, String format) {
    addGenerator(Category.BODY, matcherKey(name), new DateGenerator(format));
    return date(name, format, new Date(DATE_2000));
  }

  public This date(String name, String format, Date example) {
    FastDateFormat instance = FastDateFormat.getInstance(format);
    stringValue(name, instance.format(example));
    addRule(matcherKey(name), matchDate(format));
    return (This) this;
  }

  public This time(String name) {
    return time(name, DateFormatUtils.ISO_TIME_FORMAT.getPattern());
  }

  public This time(String name, String format) {
    addGenerator(Category.BODY, matcherKey(name), new TimeGenerator(format));
    return time(name, format, new Date(DATE_2000));
  }

  public This time(String name, String format, Date example) {
    FastDateFormat instance = FastDateFormat.getInstance(format);
    stringValue(name, instance.format(example));
    addRule(matcherKey(name), matchTime(format));
    return (This) this;
  }

  public This ipAddress(String name) {
    stringValue(name, "127.0.0.1");
    addRule(matcherKey(name), regexp("(\\d{1,3}\\.)+\\d{1,3}"));
    return (This) this;
  }

  public This id(String name) {
    addGenerator(Category.BODY, matcherKey(name), new RandomIntGenerator(0, Integer.MAX_VALUE));
    return id(name, 1234567890L);
  }

  public This id(String name, Long id) {
    numberValue(name, id);
    addRule(matcherKey(name), TypeMatcher.INSTANCE);
    return (This) this;
  }

  public This hexValue(String name) {
    addGenerator(Category.BODY, matcherKey(name), new RandomHexadecimalGenerator(10));
    return hexValue(name, "1234a");
  }

  public This hexValue(String name, String hexValue) {
    if (!hexValue.matches(HEXADECIMAL)) {
      throw new InvalidMatcherException(format(INVALID_HEXVALUE, hexValue));
    }
    stringValue(name, hexValue);
    addRule(matcherKey(name), regexp("[0-9a-fA-F]+"));
    return (This) this;
  }

  public This uuid(String name) {
    addGenerator(Category.BODY, matcherKey(name), UuidGenerator.INSTANCE);
    return uuid(name, "e2490de5-5bd3-43d5-b7c4-526e33f71304");
  }

  public This uuid(String name, UUID uuid) {
    return uuid(name, uuid.toString());
  }

  public This uuid(String name, String uuid) {
    if (!uuid.matches(UUID_REGEX)) {
      throw new InvalidMatcherException(format(INVALID_UUID, uuid));
    }
    stringValue(name, uuid);
    addRule(matcherKey(name), regexp(UUID_REGEX));
    return (This) this;
  }

  public This nullValue(String fieldName) {
    objectValue(fieldName, JSONObject.NULL);
    return (This) this;
  }

  public This includesStr(String name, String value) {
    stringValue(name, value);
    addRule(matcherKey(name), includesMatcher(value));
    return (This) this;
  }

  public This equalTo(String name, Object value) {
    objectValue(name, value);
    addRule(matcherKey(name), EqualsMatcher.INSTANCE);
    return (This) this;
  }

  public This and(String name, Object value, MatchingRule... rules) {
    objectValue(name, value);
    getMatchers().setRules(matcherKey(name), new MatchingRuleGroup(Arrays.asList(rules), RuleLogic.AND));
    return (This) this;
  }

  public This or(String name, Object value, MatchingRule... rules) {
    objectValue(name, value);
    getMatchers().setRules(matcherKey(name), new MatchingRuleGroup(Arrays.asList(rules), RuleLogic.OR));
    return (This) this;
  }

  public This matchUrl(String name, String basePath, Object... pathFragments) {
    UrlMatcherSupport urlMatcher = new UrlMatcherSupport(basePath, Arrays.asList(pathFragments));
    stringValue(name, urlMatcher.getExampleValue());
    addRule(matcherKey(name), regexp(urlMatcher.getRegexExpression()));
    return (This) this;
  }

  public This valueFromProviderState(String name, String expression, Object example) {
    addGenerator(Category.BODY, matcherKey(name), new ProviderStateGenerator(expression));
    objectValue(name, example);
    return (This) this;
  }

  /**
   * Generates the key used to build the matcher rule
   * @param name the name of the node
   * @return The key used to build the matcher rule
   */
  protected abstract String matcherKey(String name);

  public static class DocumentWrapper {
    private final Document document;

    public DocumentWrapper(Node node) {
      if (node instanceof Document) {
        document = (Document) node;
      } else {
        document = buildDocument();
        document.adoptNode(node);
      }
    }

    public String toString() {
      try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString();
      } catch (TransformerException e) {
        throw new XmlTransformError(e);
      }
    }
  }

  public static class XmlTransformError extends RuntimeException {
    public XmlTransformError(Throwable cause) {
      super(cause);
    }
  }
}
