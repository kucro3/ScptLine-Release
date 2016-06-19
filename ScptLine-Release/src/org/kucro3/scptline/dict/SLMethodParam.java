package org.kucro3.scptline.dict;

import java.util.Map;
import java.util.HashMap;

public enum SLMethodParam {
	BOOL("boolean"),
	BYTE("byte"),
	CHAR("char"),
	
	N_SHORT("short"),
	N_INT("int"),
	N_LONG("long"),
	N_FLOAT("float"),
	N_DOUBLE("double"),
	
	L_OBJECT("object"),
	L_STRING("string"),
	
	V_ARGS("vargs");
	
	SLMethodParam(String name)
	{
		this.name = name;
		reg(name, this);
	}
	
	static void reg(String name, SLMethodParam p)
	{
		if(map == null)
			map = new HashMap<>();
		map.put(name, p);
	}
	
	public static SLMethodParam getParam(String t)
	{
		return map.get(t);
	}
	
	public static SLResolvedParam resolve(String t)
	{
		SLMethodParam param;
		if((param = getParam(t)) == null)
			return null;
		return new SLResolvedParam(param);
	}
	
	public static SLResolvedParam resolveWithVariable(String t)
	{
		String[] split;
		SLMethodParam param;
		
		split = t.split(":", 2);
		String // ->
			sparam = split[0],
			svariable = split.length == 2 ? split[1] : null;
		if((param = getParam(sparam)) == null)
			return null;
		return new SLResolvedParam(param, svariable);
	}
	
	public final String getName()
	{
		return name;
	}
	
	private final String name;

	private static Map<String, SLMethodParam> map;
	
	public static class SLResolvedParam
	{
		SLResolvedParam(SLMethodParam param)
		{
			this(param, null);
		}
		
		SLResolvedParam(SLMethodParam param, String variable)
		{
			this.param = param;
			this.variable = variable;
		}
		
		public final SLMethodParam getType()
		{
			return param;
		}
		
		public final String getName()
		{
			return param.getName();
		}
		
		public final String getVariable()
		{
			return variable;
		}
		
		private final String variable;
		
		private final SLMethodParam param;
	}
}
