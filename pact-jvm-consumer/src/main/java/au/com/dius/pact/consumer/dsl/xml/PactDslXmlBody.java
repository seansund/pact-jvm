package au.com.dius.pact.consumer.dsl.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslBody;
import org.apache.commons.lang3.StringUtils;

import org.w3c.dom.Node;

/**
 * Basic implementation of an XML DslPart used to generate an XML request or response
 * body for a pact test, as well as the associated matchers and value generators. This
 * implementation currently relies on the existing Json matchers to evaluate the results
 * so the matcher keys are generated as jsonPath.
 */
public class PactDslXmlBody extends PactDslXml<PactDslXmlBody> implements PactDslBody<PactDslXmlBody> {

  public PactDslXmlBody(String rootName) {
    super(rootName);
  }

  public PactDslXmlBody(String rootName, String namespace) {
    this(rootName);
    body.setAttribute("xmlns", namespace);
  }

  protected PactDslXmlBody(String rootPath, String rootName, PactDslXmlBody parent) {
    super(rootPath, rootName, parent);
  }

  protected PactDslXmlBody(String rootPath, String rootName, PactDslXml parent, PactDslXml body) {
    super(rootPath, rootName, parent, body);
  }

  protected String buildTextPath(String rootPath, String rootName, String nodeName) {
    return buildPath(rootPath, rootName) + nodeName + "['#text']";
  }

  protected void putObject(DslPart object) {
    PactDslXmlBody xmlPart = (PactDslXmlBody) object;

    for (String matcherName : xmlPart.getMatchers().getMatchingRules().keySet()) {
      getMatchers().setRules(matcherName, xmlPart.getMatchers().getMatchingRules().get(matcherName));
    }
    getGenerators().addGenerators(xmlPart.generators);

    String elementBase = StringUtils.difference(this.rootPath, xmlPart.rootPath);
    if (StringUtils.isNotEmpty(xmlPart.rootName)) {
      if (body != null) {
        body.appendChild(xmlPart.body);
      } else {
        document.appendChild(xmlPart.body);
      }
    } else {
      String name = StringUtils.strip(elementBase, ".");
      Pattern p = Pattern.compile("\\['(.+)'\\]");
      Matcher matcher = p.matcher(name);
      if (matcher.matches()) {
        // body.put(matcher.group(1), xmlPart.getBody());
        body.appendChild(xmlPart.body);
      } else {
        // body.put(name, object.getBody());
        body.appendChild(xmlPart.body);
      }
    }
  }

  @Override
  public PactDslXml closeChild() {
    if (parent instanceof PactDslXmlBody) {
      ((PactDslXmlBody) parent).putObject(this);
    } else {
      getMatchers().applyMatcherRootPrefix("$");
      getGenerators().applyRootPrefix("$");
    }
    closed = true;
    return parent;
  }

  @Override
  public PactDslXmlBody object(String name) {
    elementIndex++;

    String base = buildPath(rootPath, rootName);

    return new PactDslXmlBody(base, name, this);
  }

  @Override
  public PactDslXmlBody object(String name, DslPart value) {

    PactDslXmlBody object = new PactDslXmlBody(rootPath, name, this, (PactDslXmlBody) value);
    putObject(object);

    return this;
  }

  public PactDslXmlBodyAttribute attribute() {

    return new PactDslXmlBodyAttribute(rootPath, rootName, this);
  }

  public PactDslXmlBody stringValue(String value) {
    if (value != null) {
      body.setTextContent(value);
    }
    return this;
  }

  public PactDslXmlBody booleanValue(Boolean value) {
    if (value != null) {
      stringValue(String.valueOf(value));
    }
    return this;
  }

  public PactDslXmlBody numberValue(Number value) {
    if (value != null) {
      stringValue(String.valueOf(value));
    }
    return this;
  }

  @Override
  public PactDslXmlBody stringValue(String name, String value) {
    Node child = document.createElement(name);
    if (value != null) {
      child.setTextContent(value);
    }

    elementIndex++;
    System.out.println("stringValue(" + name + ", " + value + "), index is: " + elementIndex);

    if (body != null) {
      body.appendChild(child);
    } else {
      document.appendChild(child);
    }

    return this;
  }

  /**
   * Generates the key used to build the matcher rule
   * @param name the name of the node
   * @return The key used to build the matcher rule
   */
  @Override
  protected String matcherKey(String name) {

    return buildTextPath(getRootPath(), getRootName(), name);
  }
}
