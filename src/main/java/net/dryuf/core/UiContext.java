/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.core;

import java.util.Map;
import java.util.function.Function;

import net.dryuf.trans.meta.NoDynamic;


/**
 * {@code UiContext} represents the user localization behaviour.
 */
public interface UiContext
{
	String				getDefaultLanguage();

	String				getLanguage();
	void				setLanguage(String language);
	boolean				checkLanguage(String language);

	String[]			listLanguages();

	int				getLocalizeDebug();
	void				setLocalizeDebug(int level);

	boolean				getTiming();
	void				setTiming(boolean timing);

	String				getLocalizePath();

	void				setLocalizeContextPath(String path);
	String				getLocalizeContextPath();

	String				localize(String class_name, String text);
	@NoDynamic
	String				localize(Class<?> clazz, String text);
	String				localizeArgs(String class_name, String text, Object[] objects);
	@NoDynamic
	String				localizeArgs(Class<?> clazz, String text, Object[] args);
	String				localizeArgsEscape(Function<String, String> escaper, String class_name, String text, Object[] objects);
	@NoDynamic
	String				localizeArgsEscape(Function<String, String> escaper, Class<?> clazz, String text, String[] args);

	String				readLocalizedFile(String filename);

	Map<String, String>		getClassLocalization(String className);
}
