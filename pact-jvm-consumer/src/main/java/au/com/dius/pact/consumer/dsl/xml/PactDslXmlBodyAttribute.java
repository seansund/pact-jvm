package au.com.dius.pact.consumer.dsl.xml;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslBody;

import org.w3c.dom.Element;

public class PactDslXmlBodyAttribute extends PactDslXml<PactDslXmlBodyAttribute> implements PactDslBody<PactDslXmlBodyAttribute> {

  protected PactDslXmlBodyAttribute(String rootPath, String rootName, PactDslXmlBody parent) {
    super(rootPath, rootName, parent, parent);
  }

  @Override
  public Object getBody() {
    throw new UnsupportedOperationException("Cannot getBody of an attribute set");
  }

  @Override
  public PactDslXmlBodyAttribute object(String name) {
    throw new UnsupportedOperationException("Cannot create new object on an attribute set");
  }

  @Override
  public PactDslXmlBodyAttribute object(String name, DslPart value) {
    throw new UnsupportedOperationException("Cannot create new object on an attribute set");
  }

  @Override
  public PactDslXmlBodyAttribute stringValue(String name, String value) {

    ((Element) body).setAttribute(name, value);

    return this;
  }

  @Override
  public PactDslXml closeChild() {
    closed = true;
    return parent;
  }

  protected String matcherKey(String name) {
    String key = buildTextPath(getRootPath(), getRootName(), name);

    return key;
  }

  protected String buildTextPath(String rootPath, String rootName, String attributeName) {
    // TODO revisit
    return buildPath(rootPath, rootName) + "@" + attributeName;
  }

}
