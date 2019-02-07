package functions;

public abstract interface IFunction<T>
{
  public abstract String getDataType();
  
  public abstract T evaluate();
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/functions/IFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */