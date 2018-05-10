package com.ThreadTormentor.exception;

public class CorruptConfigurationFileException  extends Exception
{
	private static final long serialVersionUID = -2611796233975409402L;

	public CorruptConfigurationFileException() {}

    public CorruptConfigurationFileException(String message)
    {
       super(message);
    }
}
