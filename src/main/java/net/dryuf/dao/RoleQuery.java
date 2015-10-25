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

import java.util.LinkedList;
import java.util.List;


public class RoleQuery extends java.lang.Object
{
	public				RoleQuery()
	{
		this.columns = new StringBuilder();
		this.table = new StringBuilder();
		this.where = new StringBuilder();
		this.sort = new StringBuilder();
		this.whereBinds = new LinkedList<Object>();
		this.columnsBinds = new LinkedList<Object>();
	}

	public void			setColumns(String columns)
	{
		this.columns = new StringBuilder(columns);
	}

	public void			setTable(String table)
	{
		this.table = new StringBuilder(table);
	}

	public void			setWhere(String where)
	{
		this.where = new StringBuilder(where);
	}

	public void			setSort(String sort)
	{
		this.sort = new StringBuilder(sort);
	}

	public void			appendColumn(String columnDef)
	{
		this.columns.append(", ").append(columnDef);
	}

	public void			appendColumns(List<String> columnDefs)
	{
		for (String columnDef: columnDefs)
			this.columns.append(", ").append(columnDef);
	}

	public void			appendTable(String tableName)
	{
		this.table.append(tableName);
	}

	public void			appendWhere(String condition)
	{
		this.where.append(condition);
	}

	public void			appendSort(String sort)
	{
		this.sort.append(sort);
	}

	public void			appendWhereBinds(List<Object> bindValues)
	{
		this.whereBinds.addAll(bindValues);
	}

	public void			appendWhereBind(Object bindValue)
	{
		this.whereBinds.add(bindValue);
	}

	public void			appendColumnsBinds(List<Object> bindValues)
	{
		this.columnsBinds.addAll(bindValues);
	}

	public void			appendColumnsBind(Object bindValue)
	{
		this.columnsBinds.add(bindValue);
	}

	public String			createQuery()
	{
		return columns+" "+table+" "+where;
	}

	public List<Object>		getWhereBinds()
	{
		return whereBinds;
	}

	public List<Object>		getBinds()
	{
		columnsBinds.addAll(whereBinds);
		return columnsBinds;
	}

	protected StringBuilder		columns;

	public StringBuilder		getColumns()
	{
		return this.columns;
	}

	protected StringBuilder		table;

	public StringBuilder		getTable()
	{
		return this.table;
	}

	protected StringBuilder		where;

	public StringBuilder		getWhere()
	{
		return this.where;
	}

	protected StringBuilder		sort;

	public StringBuilder		getSort()
	{
		return this.sort;
	}

	protected LinkedList<Object>	columnsBinds;

	protected LinkedList<Object>	whereBinds;
};
