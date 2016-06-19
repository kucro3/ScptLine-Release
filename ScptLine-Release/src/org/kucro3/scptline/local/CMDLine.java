package org.kucro3.scptline.local;

import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kucro3.exception.Untraced;
import org.kucro3.scptline.SLEnvironment;
import org.kucro3.scptline.SLException;
import org.kucro3.scptline.SLExternalException;
import org.kucro3.scptline.dict.SLDictionaryCollection;
import org.kucro3.scptline.dict.SLDictionaryLoader;
import org.kucro3.scptline.dict.SLMethodLoaded;
import org.kucro3.scptline.dict.SLMethodParam;
import org.kucro3.scptline.dict.SLMethodParam.SLResolvedParam;
import org.kucro3.scptline.opstack.SLHandler;

public class CMDLine extends SLHandler {
	@Override
	public boolean process(SLEnvironment env, String line)
	{
		String[] args = SPACE.split(line, 2);
		line = args.length == 2 ? args[1] : "";
		
		String insn = args[0], dict = null;
		
		SLDictionaryCollection c = env.getDictionaries();
		Matcher matcher = SPLIT.matcher(insn);
		if(matcher.find())
		{
			dict = insn.substring(0, matcher.start());
			insn = insn.substring(matcher.end());
		}
		SLMethodLoaded method = c.requireMethod(dict, insn);
		boolean envRequired = 
				method.getEssentialTypeID() == SLDictionaryLoader.TYPE_FIELD ? (
						method.getParameterCount() > 0 ? (
								method.getParameterTypes()[0].equals(SLEnvironment.class)) : false) : true;
		
		Object[] objs;
		assert method != null;
		int[] input = new int[1];
		if(!"".equals(line))
		{
			matcher = SPACE.matcher(line);
			
			List<SLResolvedParam> paramList = method.getResolvedParamList();
			ListIterator<SLResolvedParam> paramIter = paramList.listIterator();
			
			int i;
			objs = new Object[paramList.size() + (i = envRequired ? 1 : 0)];
			
			SLResolvedParam resolved;
			SLMethodParam param;
			int count = 0;
			
			do {
				resolved = paramIter.next();
				param = resolved.getType();
				objs[i++] = ParserCaller.call(parser, param, line, matcher, count, input);
				if(matcher.matches())
					count = matcher.end();
				count += input[0];
				input[0] = 0;
			} while(paramIter.hasNext());
			
			if(envRequired)
				objs[0] = env;
		}
		else if(envRequired)
			objs = new Object[] {env};
		else
			objs = new Object[0];
		try {
			method.invoke(objs);
		} catch (SLException e) {
			Untraced.untraceAll(e);
			throw e;
		}
		
		return true;
	}
	
	@Override
	public void internalException(SLEnvironment env, SLException e)
	{
		System.out.println("Failed to execute command: ");
		e.printStackTrace();
	}
	
	@Override
	public void externalException(SLEnvironment env, SLExternalException e)
	{
		System.out.println("Failed to execute command: ");
		e.printStackTrace();
	}
	
	public static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
	
	static final Pattern SPACE = Pattern.compile(" ");
	
	static final Pattern SPLIT = Pattern.compile(":");
	
	public Parser parser = new DefaultParser();
}