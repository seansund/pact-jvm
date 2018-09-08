package au.com.dius.pact.consumer.dsl

import spock.lang.Specification
import spock.lang.Unroll

class PactDslSpec extends Specification {

  @SuppressWarnings('MethodCount')
  private final PactDsl subject = new PactDsl('', '') {
    Object body = null

    @Override
    PactDsl closeChild() { null }

    @Override
    Object getBody() { body }
  }

  @Unroll
  def 'matcher methods generate the correct matcher definition - #matcherMethod'() {
    expect:
    subject."$matcherMethod"(param).toMap() == matcherDefinition

    where:

    matcherMethod     | param        | matcherDefinition
    'regexp'          | '[0-9]+'     | [match: 'regex', regex: '[0-9]+']
    'matchTimestamp'  | 'yyyy-mm-dd' | [match: 'timestamp', timestamp: 'yyyy-mm-dd']
    'matchDate'       | 'yyyy-mm-dd' | [match: 'date', date: 'yyyy-mm-dd']
    'matchTime'       | 'yyyy-mm-dd' | [match: 'time', time: 'yyyy-mm-dd']
    'matchMin'        | 1            | [match: 'type', min: 1]
    'matchMax'        | 1            | [match: 'type', max: 1]
    'includesMatcher' | 1            | [match: 'include', value: '1']

  }

}
