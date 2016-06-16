package org.kucro3.scptline.local;

import java.util.EnumMap;
import java.util.regex.Matcher;

import org.kucro3.scptline.dict.SLMethodParam;

import static org.kucro3.scptline.dict.SLMethodParam.*;

public class ParserCaller {
	public static Object call(Parser parser,
			SLMethodParam param, String line, Matcher matcher, int last, int[] output)
	{
		return caller(param).call(parser, line, matcher, last, output);
	}
	
	public static ParserFunction caller(SLMethodParam param)
	{
		return map.get(param);
	}
	
	private static final EnumMap<SLMethodParam, ParserFunction> map =
			new EnumMap<SLMethodParam, ParserFunction>(SLMethodParam.class) {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2011138155861660655L;

		{
			put(BYTE, (p, l, m, i, o) -> p.parseByte(l, m, i, o));
			put(CHAR, (p, l, m, i, o) -> p.parseChar(l, m, i, o));
			
			put(N_DOUBLE, (p, l, m, i, o) -> p.parseDouble(l, m, i, o));
			put(N_FLOAT, (p, l, m, i, o) -> p.parseFloat(l, m, i, o));
			put(N_INT, (p, l, m, i, o) -> p.parseInt(l, m, i, o));
			put(N_LONG, (p, l, m, i, o) -> p.parseLong(l, m, i, o));
			put(N_SHORT, (p, l, m, i, o) -> p.parseShort(l, m, i, o));
			
			put(V_ARGS, (p, l, m, i, o) -> p.parseArgs(l, m, i, o));
			
			put(L_STRING, (p, l, m, i, o) -> p.parseString(l, m, i, o));
			put(L_OBJECT, (p, l, m, i, o) -> p.parseObject(l, m, i, o));
		}
	};

	public static interface ParserFunction
	{
		Object call(Parser parser, String line, Matcher matcher, int last, int[] output);
	}
}
