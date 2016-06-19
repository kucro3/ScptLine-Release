package org.kucro3.scptline.local;

import java.util.regex.Matcher;

public interface Parser {
	public String[] parseArgs(String line, Matcher matcher, int last, int[] output);
	
	public Byte parseByte(String line, Matcher matcher, int last, int[] output);
	
	public Character parseChar(String line, Matcher matcher, int last, int[] output);
	
	public Double parseDouble(String line, Matcher matcher, int last, int[] output);
	
	public Float parseFloat(String line, Matcher matcher, int last, int[] output);
	
	public Integer parseInt(String line, Matcher matcher, int last, int[] output);
	
	public Long parseLong(String line, Matcher matcher, int last, int[] output);
	
	public Short parseShort(String line, Matcher matcher, int last, int[] output);
	
	public String parseString(String line, Matcher matcher, int last, int[] output);
	
	public Object parseObject(String line, Matcher matcher, int last, int[] output);

	public Boolean parseBoolean(String line, Matcher matcher, int last, int[] output);
}
