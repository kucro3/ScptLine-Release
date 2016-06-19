package org.kucro3.scptline;

import java.io.File;
import java.util.Scanner;

import org.kucro3.decl.DeclObject;
import org.kucro3.kargs.KArgsContext;
import org.kucro3.kargs.KArgsContext.Property;
import org.kucro3.kargs.KArgsService;
import org.kucro3.scptline.dict.SLDictionaryFactory;
import org.kucro3.scptline.dict.SLDictionaryLoaded;
import org.kucro3.scptline.local.CMDLine;
import org.kucro3.scptline.local.LocalDictionary;
import org.kucro3.scptline.opstack.SLHandlerStack;

public class Main {
	public static void main(String[] args)
	{
		System.out.println("Launching...");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Console input scanner constructed.");
		try {
			KArgsContext ctx;
			KArgsService.parse(ctx = KARGS_CONTEXT, args, true);
		
			//
			System.out.println("Constructing environment object.");
			long a = System.currentTimeMillis();
			DeclObject obj = new DeclObject();
			Property propertyD = ctx.getProperty("D");
			
			boolean intpoint = DEFAULT_INTPOINT;
			int opstackDepth = DEFAULT_OPSTACK_DEPTH;
			boolean unspecified0;
			String temp;
			if(unspecified0 = (propertyD == null))
				printUnspecifiedSection("D");
			if(unspecified0 || (temp = propertyD.get("intpoint")) == null)
				printUnspecifiedProperty("D", "intpoint", DEFAULT_INTPOINT);
			else
				intpoint = parseBoolean(temp, DEFAULT_INTPOINT);
			if(unspecified0 || (temp = propertyD.get("opstack.depth")) == null)
				printUnspecifiedProperty("D", "opstack.depth", DEFAULT_OPSTACK_DEPTH);
			else
				opstackDepth = parseInteger(temp, DEFAULT_OPSTACK_DEPTH);
			obj.putBoolean("IntPointEnabled", intpoint);
			obj.putInt("SizeOfOpStack", opstackDepth);
			
			SLEnvironment env = new SLEnvironment(obj);
			env.getHandlerStack().add(new CMDLine());
			printDone(a);
			System.out.println();
			//
			
			//
			a = System.currentTimeMillis();
			System.out.println("Loading dictionaries...");
			if(ctx.hasOption("localDict"))
				bindLocalDictionary(env);
			if(ctx.hasOption("dicts"))
			{
				System.out.println("Option 'dicts' found, loading specified dictionaries.");
				String optDicts = ctx.getOption("dicts");
				String[] files = optDicts.split(";");
				for(String file : files)
					loadDictionary(env, new File(file));
			}
			System.out.println(env.getDictionaries().size() + " dictionary(s) loaded in total.");
			printDone(a);
			System.out.println();
			//
			
			printVersion();
			System.out.println();
			while(running)
			{
				if(env.getHandlerStack().isEmpty())
				{
					System.out.println();
					System.out.println("-> No handler in stack(" +
							SLHandlerStack.SLHandlerStackException.MESSAGE_STACK_UNDERFLOW + ").");
					break;
				}
					
				System.out.print("> ");
				String line = scanner.nextLine();
				try {
					env.execute(line);
				} catch (SLException e) {
					env.internalException(e);
				} catch (SLExternalException e) {
					env.externalException(e);
				}
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Ouch, that hurt! :(");
			System.out.println("Crashed, caused by:");
			e.printStackTrace();
		}
		System.out.println("-> Terminated. Press enter to exit programme.");
		scanner.nextLine();
		scanner.close();
	}
	
	public static void printDictionaryLoaded(SLDictionaryLoaded dict)
	{
		System.out.println("Dictionary loaded: " + dict.getName()
				+ " (" + dict.getMethods().size() + " method(s) and " + dict.getFields().size() + " field(s) exported)");
	}
	
	static void bindLocalDictionary(SLEnvironment env)
	{
		System.out.println("Loading local dictonary.");
		try {
			SLDictionaryLoaded dict = env.load(LocalDictionary.class);
			if(dict != null)
			{
				SLDictionaryFactory.bind(env.getDictionaries(), dict);
				printDictionaryLoaded(dict);
			}
			else
				printFailedToLoadLocalDictionary(LocalDictionary.class.getCanonicalName(), UNSPECIFIED_ERROR);
		} catch (SLException e) {
			printFailedToLoadLocalDictionary(LocalDictionary.class.getCanonicalName(), e.getMessage());
		}
	}
	
	public static void printFailedToLoadLocalDictionary(String name, String e)
	{
		System.out.println("Failed to load local dictionary: " + name
				+ "(" + e + ")");
	}
	
	public static void printFailedToLoadDictionary(String name, String e)
	{
		System.out.println("Failed to load dictionary: " + name
		+ "(" + e + ")");
	}
	
	public static void loadDictionary(SLEnvironment env, File file)
	{
		File[] files;
		if(file.isDirectory())
			files = file.listFiles((f, name) -> name.endsWith(".jar"));
		else
			files = new File[] {file};
		for(File f : files)
			try {
				SLDictionaryLoaded dict = env.load(f);
				if(dict != null)
				{
					SLDictionaryFactory.bind(env.getDictionaries(), dict);
					printDictionaryLoaded(dict);
				}
				else
					printFailedToLoadDictionary(f.getName(), UNSPECIFIED_ERROR);
			} catch (SLException e) {
				printFailedToLoadDictionary(f.getName(), e.getMessage());
			}
	}
	
	public static boolean parseBoolean(String value, boolean v)
	{
		value = value.toLowerCase();
		if("true".equals(value))
			v = true;
		else if("false".equals(value))
			v = false;
		else
			printIllegalArgument(value, "true&false");
		return v;
	}
	
	public static int parseInteger(String value, int v)
	{
		try {
			v = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			printIllegalArgument(value, "integer");
		}
		return v;
	}
	
	public static void printIllegalArgument(String value, String type)
	{
		System.out.println("Illegal argument: " + value + ", " + type + " required.");
	}
	
	public static void printUnspecifiedProperty(String section, String name, Object defaultValue)
	{
		System.out.println("Unspecified property: (" + section + ")" + name + ", using default value (" +
				defaultValue.toString() + ").");
	}
	
	public static void printUnspecifiedSection(String section)
	{
		System.out.println("Unspecified property section: " + section + ".");
	}
	
	public static void printDone(long time)
	{
		System.out.println("Done! (" + (System.currentTimeMillis() - time) + "ms).");
	}
	
	public static void printVersion()
	{
		System.out.println("ScptLine Executing Framework (Environment Module 2)");
		System.out.println("Version: Skeleton Beta 0.2.1");
		System.out.println("KuCrO3 Studio all rights reserved");
	}
	
	public static final String UNSPECIFIED_ERROR = "Unspecified error";
	
	public static final boolean DEFAULT_INTPOINT = false;
	
	public static final int DEFAULT_OPSTACK_DEPTH = 1024;
	
	public static boolean running = true;
	
	private static final KArgsContext KARGS_CONTEXT;
	
	static {
		KArgsContext ctx = new KArgsContext();
		ctx.addOption("localDict", false)
			.addOption("dicts", true)
			.addProperty("D");
		KARGS_CONTEXT = ctx;
	}
}
