package au.com.dius.pact.consumer.dsl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import au.com.dius.pact.model.matchingrules.MatchingRule;

/**
 * Interface that defines the common behaviors on an Object type
 */
public interface PactDslBody<T extends PactDslBody> extends DslPart {
  T objectValue(String name, Object value);

  T stringValue(String name, String value);

  T numberValue(String name, Number value);

  T booleanValue(String name, Boolean value);

  T stringType(String name);

  default T stringType(String... names) {
    for (String name : names) {
      stringType(name);
    }
    return (T) this;
  }

  T stringType(String name, String example);

  T numberType(String name);

  default T numberType(String... names) {
    for (String name : names) {
      numberType(name);
    }
    return (T) this;
  }

  T numberType(String name, Number example);

  T integerType(String name);

  default T integerType(String... names) {
    for (String name : names) {
      integerType(name);
    }
    return (T) this;
  }

  T integerType(String name, Long example);

  T integerType(String name, Integer example);

  T decimalType(String name);

  default T decimalType(String... names) {
    for (String name : names) {
      decimalType(name);
    }
    return (T) this;
  }

  T decimalType(String name, Double example);

  T decimalType(String name, BigDecimal number);

  default T booleanType(String name) {
    return booleanType(name, true);
  }

  default T booleanType(String... names) {
    for (String name : names) {
      booleanType(name);
    }
    return (T) this;
  }

  T booleanType(String name, Boolean example);

  T stringMatcher(String name, String regex, String example);

  default T timestamp() {
    return timestamp("timestamp");
  }

  T timestamp(String name);

  T timestamp(String name, String format);

  T timestamp(String name, String format, Date example);

  default T date() {
    return date("date");
  }

  T date(String name);

  T date(String name, String format);

  T date(String name, String format, Date example);

  default T time() {
    return time("time");
  }

  T time(String name);

  T time(String name, String format);

  T time(String name, String format, Date example);

  T ipAddress(String name);

  default T id() {
    return id("id");
  }

  default T id(Long id) {
    return id("id", id);
  }

  T id(String name);

  T id(String name, Long id);

  T hexValue(String name);

  T hexValue(String name, String hexValue);

  T uuid(String name);

  T uuid(String name, UUID uuid);

  T uuid(String name, String uuid);

  T nullValue(String fieldName);

  T includesStr(String name, String value);

  T equalTo(String name, Object value);

  T and(String name, Object value, MatchingRule... rules);

  T or(String name, Object value, MatchingRule... rules);

  T matchUrl(String name, String basePath, Object... pathFragments);

  T valueFromProviderState(String name, String expression, Object example);

  T object(String name);

  T object(String name, DslPart value);
}
