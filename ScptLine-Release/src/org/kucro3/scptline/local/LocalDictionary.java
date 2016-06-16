package org.kucro3.scptline.local;

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
}
