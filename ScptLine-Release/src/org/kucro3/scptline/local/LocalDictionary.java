package org.kucro3.scptline.local;

import java.io.PrintStream;

import org.kucro3.scptline.Main;
import org.kucro3.scptline.SLEnvironment;
import org.kucro3.scptline.anno.SLExport;
import org.kucro3.scptline.dict.SLDictionary;
import org.kucro3.scptline.dict.SLMain;

@SLExport(name = "local")
public class LocalDictionary implements SLDictionary, SLMain {
	@Override
	public SLDictionary onLoad(SLEnvironment env, Object reserved)
	{
		return this;
	}
	
	@SLExport(type = "method", name = "version")
	public void version(SLEnvironment env)
	{
		Main.printVersion();
	}
	
	@SLExport(type = "method", name = "dicts", targs = {"vargs"})
	public void dicts(SLEnvironment env, String[] args)
	{
	}
	
	// println
	@SLExport(type = "method", name = "println", targs = {"string"}, 
			delegate = "println", delegateParams = {String.class})
	@SLExport(type = "method", name = "bprintln", targs = {"boolean"},
			delegate = "println", delegateParams = {boolean.class})
	@SLExport(type = "method", name = "cprintln", targs = {"char"},
			delegate = "println", delegateParams = {char.class})
	@SLExport(type = "method", name = "dprintln", targs = {"double"},
			delegate = "println", delegateParams = {double.class})
	@SLExport(type = "method", name = "fprintln", targs = {"float"},
			delegate = "println", delegateParams = {float.class})
	@SLExport(type = "method", name = "iprintln", targs = {"int"},
			delegate = "println", delegateParams = {int.class})
	@SLExport(type = "method", name = "lprintln", targs = {"long"},
			delegate = "println", delegateParams = {long.class})
	// print
	@SLExport(type = "method", name = "print", targs = {"string"},
			delegate = "print", delegateParams = {String.class})
	public static PrintStream out = System.out;
}
