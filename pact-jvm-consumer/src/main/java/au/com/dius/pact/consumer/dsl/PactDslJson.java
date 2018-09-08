package au.com.dius.pact.consumer.dsl;

/**
 * Interface that provides the base definition for a Json implementation of PactDsl
 * @param <Base> the reference to the base class that will be used for the Json implementation
 */
public abstract class PactDslJson<Base extends PactDslJson>
    extends PactDsl<Base> {

  public PactDslJson(Base parent, String rootPath, String rootName) {
    super(parent, rootPath, rootName);
  }

  public PactDslJson(String rootPath, String rootName) {
    super(rootPath, rootName);
  }

  protected abstract void putObject(DslPart object);

  protected abstract void putArray(DslPart object);

  /**
   * Field which is an array
   *
   * @param name field name
   */
  public abstract PactDslJsonArray array(String name);

  /**
   * Element as an array
   */
  public abstract PactDslJsonArray array();

  /**
   * Array field where each element must match the following object
   *
   * @param name field name
   * @deprecated Use eachLike instead
   */
  @Deprecated
  public abstract PactDslJsonBody arrayLike(String name);

  /**
   * Array element where each element of the array must match the following object
   *
   * @deprecated Use eachLike instead
   */
  @Deprecated
  public abstract PactDslJsonBody arrayLike();

  /**
   * Array field where each element must match the following object
   *
   * @param name field name
   */
  public abstract PactDslJsonBody eachLike(String name);

  /**
   * Array element where each element of the array must match the following object
   */
  public abstract PactDslJsonBody eachLike();

  /**
   * Array field where each element must match the following object
   *
   * @param name           field name
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody eachLike(String name, int numberExamples);

  /**
   * Array element where each element of the array must match the following object
   *
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody eachLike(int numberExamples);

  /**
   * Array field with a minumum size and each element must match the provided object
   *
   * @param name field name
   * @param size minimum size
   */
  public abstract PactDslJsonBody minArrayLike(String name, Integer size);

  /**
   * Array element with a minumum size and each element of the array must match the provided object
   *
   * @param size minimum size
   */
  public abstract PactDslJsonBody minArrayLike(Integer size);

  /**
   * Array field with a minumum size and each element must match the provided object
   *
   * @param name           field name
   * @param size           minimum size
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody minArrayLike(String name, Integer size, int numberExamples);

  /**
   * Array element with a minumum size and each element of the array must match the provided object
   *
   * @param size           minimum size
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody minArrayLike(Integer size, int numberExamples);

  /**
   * Array field with a maximum size and each element must match the provided object
   *
   * @param name field name
   * @param size maximum size
   */
  public abstract PactDslJsonBody maxArrayLike(String name, Integer size);

  /**
   * Array element with a maximum size and each element of the array must match the provided object
   *
   * @param size minimum size
   */
  public abstract PactDslJsonBody maxArrayLike(Integer size);

  /**
   * Array field with a maximum size and each element must match the provided object
   *
   * @param name           field name
   * @param size           maximum size
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody maxArrayLike(String name, Integer size, int numberExamples);

  /**
   * Array element with a maximum size and each element of the array must match the provided object
   *
   * @param size           minimum size
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody maxArrayLike(Integer size, int numberExamples);

  /**
   * Array field with a minimum and maximum size and each element must match the provided object
   *
   * @param name    field name
   * @param minSize minimum size
   * @param maxSize maximum size
   */
  public abstract PactDslJsonBody minMaxArrayLike(String name, Integer minSize, Integer maxSize);

  /**
   * Array element with a minimum and maximum size and each element of the array must match the provided object
   *
   * @param minSize minimum size
   * @param maxSize maximum size
   */
  public abstract PactDslJsonBody minMaxArrayLike(Integer minSize, Integer maxSize);

  /**
   * Array field with a minimum and maximum size and each element must match the provided object
   *
   * @param name           field name
   * @param minSize        minimum size
   * @param maxSize        maximum size
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody minMaxArrayLike(String name, Integer minSize, Integer maxSize, int numberExamples);

  /**
   * Array element with a minimum and maximum size and each element of the array must match the provided object
   *
   * @param minSize        minimum size
   * @param maxSize        maximum size
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonBody minMaxArrayLike(Integer minSize, Integer maxSize, int numberExamples);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name field name
   */
  public abstract PactDslJsonArray eachArrayLike(String name);

  /**
   * Array element where each element of the array is an array and must match the following object
   */
  public abstract PactDslJsonArray eachArrayLike();

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name           field name
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonArray eachArrayLike(String name, int numberExamples);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param numberExamples number of examples to generate
   */
  public abstract PactDslJsonArray eachArrayLike(int numberExamples);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name field name
   * @param size Maximum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMaxLike(String name, Integer size);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param size Maximum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMaxLike(Integer size);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name           field name
   * @param numberExamples number of examples to generate
   * @param size           Maximum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMaxLike(String name, int numberExamples, Integer size);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param numberExamples number of examples to generate
   * @param size           Maximum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMaxLike(int numberExamples, Integer size);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name field name
   * @param size Minimum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMinLike(String name, Integer size);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param size Minimum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMinLike(Integer size);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name           field name
   * @param numberExamples number of examples to generate
   * @param size           Minimum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMinLike(String name, int numberExamples, Integer size);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param numberExamples number of examples to generate
   * @param size           Minimum size of the outer array
   */
  public abstract PactDslJsonArray eachArrayWithMinLike(int numberExamples, Integer size);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name    field name
   * @param minSize minimum size
   * @param maxSize maximum size
   */
  public abstract PactDslJsonArray eachArrayWithMinMaxLike(String name, Integer minSize, Integer maxSize);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param minSize minimum size
   * @param maxSize maximum size
   */
  public abstract PactDslJsonArray eachArrayWithMinMaxLike(Integer minSize, Integer maxSize);

  /**
   * Array field where each element is an array and must match the following object
   *
   * @param name           field name
   * @param numberExamples number of examples to generate
   * @param minSize        minimum size
   * @param maxSize        maximum size
   */
  public abstract PactDslJsonArray eachArrayWithMinMaxLike(String name, int numberExamples, Integer minSize,
                                                           Integer maxSize);

  /**
   * Array element where each element of the array is an array and must match the following object
   *
   * @param numberExamples number of examples to generate
   * @param minSize        minimum size
   * @param maxSize        maximum size
   */
  public abstract PactDslJsonArray eachArrayWithMinMaxLike(int numberExamples, Integer minSize, Integer maxSize);

  /**
   * Object field
   *
   * @param name field name
   */
  public abstract PactDslJsonBody object(String name);

  /**
   * Object element
   */
  public abstract PactDslJsonBody object();

  public PactDslJsonBody asBody() {
    return (PactDslJsonBody) this;
  }

  public PactDslJsonArray asArray() {
    return (PactDslJsonArray) this;
  }

  /**
   * Close of the previous array element
   */
  public abstract Base closeArray();

  /**
   * Close off the previous object
   *
   * @return
   */
  public abstract Base closeObject();
}
