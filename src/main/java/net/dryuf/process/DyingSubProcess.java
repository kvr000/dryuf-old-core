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

package net.dryuf.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.dryuf.config.ValueConfig;
import net.dryuf.core.CallerContext;
import net.dryuf.textual.TextualManager;
import net.dryuf.textual.LongTextual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * SubProcess implementation which dies very quickly.
 *
 * This class is mainly for testing purposes.
 */
public class DyingSubProcess extends AbstractSyncSubProcess
{
	public				DyingSubProcess()
	{
	}

	public void                     init()
	{
		super.init();
		timeout = config.getTextualDefault("timeout", TextualManager.createTextual(LongTextual.class, callerContext), 0L);
	}

	public void			loop()
	{
		try {
			Thread.sleep(timeout);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private long		        timeout;
}
