package au.com.dius.pact.consumer.dsl.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import au.com.dius.pact.consumer.dsl.DslPart;
import org.junit.Test;

public class PactDslXmlBodyTest {
  protected String buildRuleName(String... paths) {
    StringBuffer ruleName = new StringBuffer();

    for (Iterator<String> i = Arrays.asList(paths).iterator(); i.hasNext(); ) {
      String path = i.next();

      ruleName.append(PactDslXmlBody.PATH_SEPARATOR);
      ruleName.append(path);
    }
    return ruleName.toString() + "['#text']";
  }

  @Test
  public void testBuildEmptyObject() {

    assertDslPartEquals(
        "<test/>",
        new PactDslXmlBody("test"));
  }

  @Test
  public void testNestedObject() {

    assertDslPartEquals(
        "<test><child/></test>",
        new PactDslXmlBody("test")
            .object("child").closeChild());
  }

  @Test
  public void testWithStringValue() {

    assertDslPartEquals(
        "<test><child>value</child></test>",
        new PactDslXmlBody("test")
            .stringValue("child", "value"));
  }

  @Test
  public void testWithStringType() {

    assertDslPartEquals(
        "<test><child>string</child></test>",
        new PactDslXmlBody("test")
            .stringType("child"),
        buildRuleName("test[0]", "child"));
  }

  @Test
  public void testWithMultipleStringTypes() {

    assertDslPartEquals(
        "<test><child><key1>string</key1><key2>string</key2></child></test>",
        new PactDslXmlBody("test")
            .object("child")
            .stringType("key1")
            .stringType("key2")
            .closeChild(),
        buildRuleName("test[0]", "child[0]", "key1"),
        buildRuleName("test[0]", "child[1]", "key2")
    );
  }

  @Test
  public void testWithStringMatcher() {

    assertDslPartEquals(
        "<test><refCode>1234</refCode><child>01-01-01</child></test>",
        new PactDslXmlBody("test")
            .numberValue("refCode", 1234)
            .stringMatcher("child", "\\d{2}-\\d{2}-\\d{2}", "01-01-01"),
        buildRuleName("test[1]", "child")
    );
  }

  @Test
  public void testWithNamespace() {

    assertDslPartEquals(
        "<test xmlns=\"urn:mynamespace\"><child>value</child></test>",
        new PactDslXmlBody("test", "urn:mynamespace")
            .stringValue("child", "value")
    );
  }

  protected void assertDslPartEquals(String expectedBody, DslPart body, String... ruleNames) {

    assertEquals(expectedBody, body.toString());

    if (ruleNames.length > 0) {
      assertFalse(body.getMatchers().isEmpty());

      for (String ruleName : ruleNames) {
        assertTrue("Matchers should contain rule: " + ruleName, body.getMatchers().getMatchingRules().containsKey(ruleName));
      }
    }
  }
}
