package au.com.dius.pact.consumer.dsl;

import java.math.BigDecimal;
import java.util.Date;

import au.com.dius.pact.model.matchingrules.MatchingRule;

/**
 * Interface that defines the common behaviors on an Array type
 * @param <T> <code>This</code> Array type implementation
 * @param <O> the Object type used by this implementation
 */
public interface PactDslArray<T extends PactDslArray, O extends PactDslBody> extends DslPart {

  O object();

  T array();

  T stringValue(String value);

  T string(String value);

  T numberValue(Number value);

  T number(Number value);

  T booleanValue(Boolean value);

  T stringType();

  T stringType(String example);

  T numberType();

  T numberType(Number example);

  T integerType();

  T integerType(Long example);

  T decimalType();

  T decimalType(BigDecimal example);

  T decimalType(Double example);

  T booleanType();

  T booleanType(Boolean example);

  T stringMatcher(String regex);

  T stringMatcher(String regex, String value);

  T timestamp();

  T timestamp(String format);

  T timestamp(String format, Date example);

  T date();

  T date(String format);

  T date(String format, Date example);

  T time();

  T time(String format);

  T time(String format, Date example);

  T ipAddress();

  T id();

  T id(Long id);

  T hexValue();

  T hexValue(String hexValue);

  T uuid();

  T uuid(String uuid);

  T nullValue();

  T matchUrl(String basePath, Object... pathFragments);

  T includesStr(String value);

  T equalsTo(Object value);

  T and(Object value, MatchingRule... rules);

  T or(Object value, MatchingRule... rules);

  T valueFromProviderState(String expression, Object example);
}