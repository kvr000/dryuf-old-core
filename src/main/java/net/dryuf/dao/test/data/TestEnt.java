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

package net.dryuf.dao.test.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;


@Entity
@Table(name="TestEnt")
public class TestEnt extends java.lang.Object
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="testId")
	public Long			testId;

	public Long			getTestId()
	{
		return testId;
	}

	public void			setTestId(Long testId_)
	{
		this.testId = testId_;
	}

	@Column(name="name")
	protected String		name = "";

	@Column(name="uniq")
	protected String		uniq = null;

	@Column(name="nonull")
	protected String		nonull = "";

	public void			setName(String name_)
	{
		this.name = name_;
	}

	public String			getName()
	{
		return this.name;
	}

	public void			setUniq(String uniq_)
	{
		this.uniq = uniq_;
	}

	public String			getUniq()
	{
		return this.uniq;
	}

	public void			setNonull(String nonull_)
	{
		this.nonull = nonull_;
	}

	public String			getNonull()
	{
		return this.nonull;
	}
};
