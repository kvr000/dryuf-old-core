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

package net.dryuf.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import net.dryuf.core.CallerContext;
import net.dryuf.core.EntityHolder;
import net.dryuf.transaction.TransactionHandler;


public interface DynamicDao<ET, PKT>
{
	public Class<ET>		getEntityClass();
	public ET			refresh(ET obj);
	public ET			loadByPk(PKT pk);
	public List<ET>			listAll();
	public void			insert(ET obj);
	public void			insertTxNew(ET obj);
	public ET			update(ET obj);
	public void			remove(ET obj);
	public boolean			removeByPk(PKT pk);
	public PKT			openRelation(EntityHolder<ET> holder, String relation);
	public PKT			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<ET> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<ET> holder);
	public ET			createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<ET>		retrieveDynamic(EntityHolder<?> composition, PKT pk);
	public ET			updateDynamic(EntityHolder<ET> roleObject, PKT pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, PKT pk);
	public long			listDynamic(List<EntityHolder<ET>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);

	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(Callable<R> code) throws Exception;
	public <R> R			runTransactionedSafe(Callable<R> code);
	public <R> R			runTransactionedNew(Callable<R> code) throws Exception;
	public <R> R			runTransactionedNewSafe(Callable<R> code);
}
