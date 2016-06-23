package org.kucro3.scptline.local;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.kucro3.scptline.Main;
import org.kucro3.scptline.SLEnvironment;
import org.kucro3.scptline.exception.SLMessage;
import org.kucro3.scptline.anno.SLExport;
import org.kucro3.scptline.dict.SLDictionary;
import org.kucro3.scptline.dict.SLDictionaryCollection;
import org.kucro3.scptline.dict.SLDictionaryFactory;
import org.kucro3.scptline.dict.SLDictionaryLoaded;
import org.kucro3.scptline.dict.SLDictionaryLoader;
import org.kucro3.scptline.dict.SLFieldLoaded;
import org.kucro3.scptline.dict.SLMain;
import org.kucro3.scptline.dict.SLMethodLoaded;

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
	public Object dicts(SLEnvironment env, String[] args)
	{
		SLDictionaryLoaded loaded;
		String dictName;
		String insn = args[0];
		switch(insn.toLowerCase())
		{
		case "list":
			if(!env.underConsole())
				return SLEnvironment.VOID;
			
			SLMessage.checkArgument(args, 0, 1);
			SLDictionaryCollection collection = env.getDictionaries();
			env.println("Listing dictionaries: " + collection.size() + " dictionary(s) loaded");
			env.println("Dictionary information:");
			Collection<SLDictionaryLoaded> dicts = collection.dictionaries();
			
			int i = 0;
			Collection<SLMethodLoaded> methods;
			Collection<SLFieldLoaded> fields;
			for(SLDictionaryLoaded dict : dicts)
			{
				env.println("[" + (++i) + "] Dictionary Name: " + dict.getName());
				methods = dict.getMethods();
				fields = dict.getFields();
				
				if(methods.isEmpty())
					env.println("\tNo method exported.");
				else
				{
					env.println("\tExported methods(" + methods.size() + " in total):");
					int j = 0;
					for(SLMethodLoaded method : methods)
					{
						Method mthd = method.getMethod();
						boolean delegate = method.getEssentialTypeID() == SLDictionaryLoader.TYPE_FIELD;
						env.println("\t\t[" + (++j) + "] " + method.getName() + " (" 
								+ (delegate ? "delegating " : "") + mthd.getDeclaringClass().getCanonicalName()
								+ "." + mthd.getName() + "()" + ") with "
								+ method.getResolvedParams().length + " argument(s)");
					}
				}
				
				if(fields.isEmpty())
					env.println("\tNo field exported.");
				else
				{
					env.println("\tExported fields(" + fields.size() + " in total):");
					int j = 0;
					for(SLFieldLoaded field : fields)
					{
						Method mthd = field.getMethod();
						Field fd = field.getField();
						boolean delegate = field.getEssentialTypeID() == SLDictionaryLoader.TYPE_METHOD;
						env.println("\t\t[" + (++j) + "]" + field.getName() + " ("
								+ (delegate ? 
										("delegating " + mthd.getDeclaringClass().getCanonicalName() + "." + mthd.getName() + "()") :
										(fd.getDeclaringClass().getCanonicalName() + "." + fd.getName()) 
									)
								+ ")" + (delegate ? (" with " + mthd.getParameterCount() + " argument(s)") : ""));
					}
				}
			}
			return SLEnvironment.VOID;
			
		case "load":
			SLMessage.checkArgument(args, 2, 0);
			String loader = args[1];
			switch(loader)
			{
			case "current":
				SLMessage.checkArgument(args, 2);
				if(current_dict == null)
				{
					env.printlnConsole("Not looking at any dictionary");
					return null;
				}
				if(env.getDictionaries().contiansDictionary((dictName = current_dict.getName())))
						env.printlnConsole("Dictionary already loaded " + dictName);
				else
				{
					SLDictionaryFactory.bind(env.getDictionaries(), current_dict);
					env.printlnConsole("Loaded dictionary: " + dictName);
				}
				return current_dict;
				
			case "jar":
				SLMessage.checkArgument(args, 3);
				loaded = env.load(new File(args[2]));
				if(loaded == null)
					env.printlnConsole("Failed to load dictionary.");
				else
					env.println("Dictionary loaded: " + loaded.getName());
				return loaded;
				
			case "class":
				SLMessage.checkArgument(args, 3);
				try {
					return env.load(Class.forName(args[2]));
				} catch (ClassNotFoundException e) {
					env.printlnConsole("Class not found: " + args[2]);
					return null;
				}
				
			default:
				env.printlnConsole("Unknown loader: " + loader);
			}
			return null;
			
		case "unload":
			if(args.length == 1)
			{
				if(current_dict == null)
				{
					env.printlnConsole("Not looking at any dictionary");
					return null;
				}
				dictName = current_dict.getName();
				if(!env.getDictionaries().contiansDictionary(dictName))
				{
					env.printlnConsole("Dictionary not found: " + dictName);
					return null;
				}
			}
			else
			{
				SLMessage.checkArgument(args, 2);
				dictName = args[1];
				if(!env.getDictionaries().contiansDictionary(dictName))
				{
						env.printlnConsole("Dictionary not found: " + dictName);
					return null;
				}
			}
			env.printlnConsole("Removed dictionary: " + dictName);
			return env.getDictionaries().removeDictionary(dictName);
			
		case "info":
			// TODO TBC
			break;
			
		case "link":
			// TODO TBC
			break;
			
		case "unlink":
			// TODO TBC
			break;
			
		case "relink":
			// TODO TBC
			break;
			
		case "enter":
			SLMessage.checkArgument(args, 2);
			dictName = args[1];
			loaded = env.getDictionaries().getDictionary(dictName);
			if(loaded == null)
				env.printlnConsole("Dictionary not found: " + dictName);
			else
			{
				current_dict = loaded;
				env.printlnConsole("Entered");
			}
			return loaded;
			
		case "escape":
			SLMessage.checkArgument(args, 1);
			loaded = current_dict;
			if(current_dict == null)
				env.printlnConsole("Not looking at any dictionary");
			else
				env.printlnConsole("Escaped");
			current_dict = null;
			return loaded;
			
		case "current":
			SLMessage.checkArgument(args, 1);
			if(current_dict != null)
				env.printlnConsole("Looking at dictionary: " + current_dict.getName());
			else
				env.printlnConsole("Not looking at any dictionary");
			return current_dict;
			
		default:
			break;
		}
		throw new SLMessage("Unknown operation: " + insn);
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
	@SLExport(type = "method", name = "bprint", targs = {"boolean"},
			delegate = "print", delegateParams = {boolean.class})
	@SLExport(type = "method", name = "cprint", targs = {"char"},
			delegate = "print", delegateParams = {char.class})
	@SLExport(type = "method", name = "dprint", targs = {"double"},
			delegate = "print", delegateParams = {double.class})
	@SLExport(type = "method", name = "fprint", targs = {"float"},
			delegate = "print", delegateParams = {float.class})
	@SLExport(type = "method", name = "iprint", targs = {"int"},
			delegate = "print", delegateParams = {int.class})
	@SLExport(type = "method", name = "lprint", targs = {"long"},
			delegate = "print", delegateParams = {long.class})
	public static PrintStream out = System.out;
	
	@SLExport(type = "field", name = "current_dict")
	public static volatile SLDictionaryLoaded current_dict;
}
