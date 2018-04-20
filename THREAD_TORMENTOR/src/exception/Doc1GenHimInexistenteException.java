package exception;

public class Doc1GenHimInexistenteException  extends Exception
{
	private static final long serialVersionUID = 3306167384729404517L;

    public Doc1GenHimInexistenteException() {}

    public Doc1GenHimInexistenteException(String message)
    {
       super(message);
    }
}
