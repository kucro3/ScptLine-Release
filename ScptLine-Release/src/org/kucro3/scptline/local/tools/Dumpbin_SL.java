package org.kucro3.scptline.local.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kucro3.scptline.anno.SLExport;
import org.kucro3.scptline.dict.SLMethodParam.SLResolvedParam;

public class Dumpbin_SL {
	private Dumpbin_SL()
	{
	}
	
	public static DumpbinResult dumpbinFully(Class<?> clz)
	{
		return dumpbinOptionally(clz, OPTION_METHODS, OPTION_FIELDS);
	}
	
	public static DumpbinResult dumpbinOptionally(Class<?> clz, String... options)
	{
		boolean optMethods = false,
				optFields = false,
				optCodeX = false;
		for(String opt : options)
			switch(opt)
			{
			case OPTION_METHODS:
				optMethods = true;
				break;
			
			case OPTION_FIELDS:
				optFields = true;
				break;
				
			case OPTION_CODEX:
				throw new UnsupportedOperationException("CodeX"); // TODO CodeX: to be continued
				
			default:
				throw new IllegalArgumentException(opt);
			}

		DumpbinResult result = new DumpbinResult();
		dumpbinMain(result, clz);
		if(!result.exported())
			return result;
		if(optMethods)
			dumpbinMethods(result, clz);
		if(optFields)
			dumpbinFields(result, clz);
		if(optCodeX)
			dumpbinCodeX(result, clz);
		return result;
	}
	
	static void log_info(DumpbinResult result, String msg)
	{
		result.debugInformation.add("INFO: " + msg);
	}
	
	static void log_warn(DumpbinResult result, String msg)
	{
		result.debugInformation.add("WARN: " + msg);
	}
	
	static void log_error(DumpbinResult result, String msg)
	{
		log_warn(result, msg);
		result.errorInformation.add(msg);
	}
	
	static void dumpbinMain(DumpbinResult result, Class<?> clz)
	{
		result.clazz = clz;
		result.clazzName = clz.getCanonicalName();
		result.debugInformation = new ArrayList<>();
		result.errorInformation = new ArrayList<>();
		result.annotation = dumpbinMain_Annotation(result, clz);
	}
	
	static SLExport dumpbinMain_Annotation(DumpbinResult result, Class<?> clz)
	{
		log_info(result, "Dumping exporting annotation.");
		SLExport annotation = clz.getAnnotation(SLExport.class);
		CODE_BLOCK: {
			if(!(result.exported = annotation != null))
				break CODE_BLOCK;
			
			log_info(result, "Exporting annotation found.");
			
			if((result.name = annotation.name()) == "")
				log_error(result, "Name undefined.");
			else
				log_info(result, "Exporting name: " + result.name);
			
			log_info(result, "Exporting version: " + (result.version = annotation.version()));
		}
		return annotation;
	}
	
	static void dumpbinFields(DumpbinResult result, Class<?> clz)
	{
		
	}
	
	static void dumpbinMethods(DumpbinResult result, Class<?> clz)
	{
		
	}
	
	static void dumpbinCodeX(DumpbinResult result, Class<?> clz)
	{
		// TODO CodeX: to be continued
	}

	public static class DumpbinResult
	{
		DumpbinResult()
		{
		}
		
		{
			clazz = null;
			clazzName = "";
			exported = false;
			exportedFieldCount = 0;
			exportedMethodCount = 0;
			exportedMethods = new ArrayList<>();
			exportedFields = new ArrayList<>();
			version = "";
			name = "";
			errorInformation = null;
			debugInformation = null;
			annotation = null;
		}
		
		public boolean exported()
		{
			return exported;
		}
		
		public Class<?> getMaster()
		{
			return clazz;
		}
		
		public String getMasterName()
		{
			return clazzName;
		}
		
		public String getDictionaryName()
		{
			return dictionaryName;
		}
		
		public int getExportedFieldCount()
		{
			return exportedFieldCount;
		}
		
		public int getExportedMethodCount()
		{
			return exportedMethodCount;
		}
		
		public Collection<ExportedMethod> getExportedMethods()
		{
			return exportedMethods;
		}
		
		public Collection<ExportedField> getExportedFields()
		{
			return exportedFields;
		}
		
		public Collection<String> getErrorInformation()
		{
			return errorInformation;
		}
		
		public Collection<String> getDebugInformation()
		{
			return debugInformation;
		}
		
		public String getVersion()
		{
			return version;
		}
		
		Class<?> clazz;
		
		String clazzName;
		
		String dictionaryName;
		
		boolean exported;
		
		int exportedFieldCount;
		
		int exportedMethodCount;
		
		List<ExportedMethod> exportedMethods;
		
		List<ExportedField> exportedFields;
		
		String version;
		
		String name;
		
		Collection<String> errorInformation;
		
		Collection<String> debugInformation;
		
		SLExport annotation;
	}
	
	public static class ExportedMethod
	{
		ExportedMethod()
		{
		}
		
		public DumpbinResult getParent()
		{
			return parent;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getEssentialTypeID()
		{
			return essence;
		}
		
		public Method getInvocation()
		{
			return invocation;
		}
		
		public int getParamCount()
		{
			return paramCount;
		}
		
		public Class<?>[] getParamTypes()
		{
			return paramTypes;
		}
		
		public String getRealName()
		{
			return realName;
		}
		
		public SLResolvedParam[] getResolvedParams()
		{
			return resolvedParams;
		}
		
		public Class<?> getReturnType()
		{
			return returnType;
		}
 		
		DumpbinResult parent;
		
		String name;
		
		int essence;
		
		Method invocation;
		
		int paramCount;
		
		Class<?>[] paramTypes;
		
		String realName;
		
		SLResolvedParam[] resolvedParams;
		
		Class<?> returnType;
	}
	
	public static class ExportedField
	{
		ExportedField()
		{
		}
		
		public DumpbinResult getParent()
		{
			return parent;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getEssentialTypeID()
		{
			return essence;
		}
		
		public Field getFieldInvocation()
		{
			return invocation0;
		}
		
		public Method getMethodInvocation()
		{
			return invocation1;
		}
		
		public boolean delegate()
		{
			return delegate;
		}
		
		public Class<?> getType()
		{
			return type;
		}
		
		DumpbinResult parent;
		
		String name;
		
		int essence;
		
		Field invocation0;
		
		Method invocation1;
		
		boolean delegate;
		
		Class<?> type;
	}
	
	public static final String OPTION_METHODS = "m";
	
	public static final String OPTION_FIELDS = "f";
	
	public static final String OPTION_CODEX = "b";
}