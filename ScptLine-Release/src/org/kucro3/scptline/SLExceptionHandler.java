package org.kucro3.scptline;

import org.kucro3.scptline.exception.SLException;

public interface SLExceptionHandler {
	public abstract boolean handle(SLEnvironment env, SLException e);
}
