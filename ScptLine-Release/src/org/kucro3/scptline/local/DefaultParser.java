package org.kucro3.scptline.local;

import java.util.regex.Matcher;

import org.kucro3.scptline.exception.SLExternalException;

public class DefaultParser implements Parser {
	@Override
	public Boolean parseBoolean(String line, Matcher matcher, int last, int[] output)
	{
		switch(line.toLowerCase())
		{
		case TRUE:
			return true;
		case FALSE:
			return false;
		default:
			throw new SLExternalException("not a boolean");
		}
	}
	
	@Override
	public Byte parseByte(String line, Matcher matcher, int last, int[] output)
	{
		try {
			return Byte.parseByte(sub(line, matcher, last));
		} catch (NumberFormatException e) {
			throw new SLExternalException(e);
		}
	}
	
	@Override
	public Character parseChar(String line, Matcher matcher, int last, int[] output)
	{
		line = sub(line, matcher, last);
		if(line.length() != 1)
			throw new SLExternalException("not a character");
		return line.charAt(0);
	}
	
	@Override
	public Double parseDouble(String line, Matcher matcher, int last, int[] output)
	{
		try {
			return Double.parseDouble(sub(line, matcher, last));
		} catch (NumberFormatException e) {
			throw new SLExternalException(e);
		}
	}
	
	@Override
	public Float parseFloat(String line, Matcher matcher, int last, int[] output)
	{
		try {
			return Float.parseFloat(sub(line, matcher, last));
		} catch (NumberFormatException e) {
			throw new SLExternalException(e);
		}
	}
	
	@Override
	public Short parseShort(String line, Matcher matcher, int last, int[] output)
	{
		try {
			return Short.parseShort(sub(line, matcher, last));
		} catch (NumberFormatException e) {
			throw new SLExternalException(e);
		}
	}
	
	@Override
	public Long parseLong(String line, Matcher matcher, int last, int[] output)
	{
		try {
			return Long.parseLong(sub(line, matcher, last));
		} catch (NumberFormatException e) {
			throw new SLExternalException(e);
		}
	}
	
	@Override
	public Integer parseInt(String line, Matcher matcher, int last, int[] output)
	{
		try {
			return Integer.parseInt(sub(line, matcher, last));
		} catch (NumberFormatException e) {
			throw new SLExternalException(e);
		}
	}
	
	@Override
	public String[] parseArgs(String line, Matcher matcher, int last, int[] output)
	{
		return CMDLine.SPACE.split(line.substring(last));
	}
	
	@Override
	public Object parseObject(String line, Matcher matcher, int last, int[] output)
	{
		throw new SLExternalException(new UnsupportedOperationException());
	}
	
	@Override
	public String parseString(String line, Matcher matcher, int last, int[] output)
	{
		return sub(line, matcher, last);
	}
	
	public static String sub(String line, Matcher matcher, int last)
	{
		if(!matcher.find())
			return line.substring(last);
		else
			return line.substring(last, matcher.start());
	}
	
	public static final String TRUE = "true";
	
	public static final String FALSE = "false";
}
