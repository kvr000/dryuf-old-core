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

package net.dryuf.dao.test;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;


@Entity
@Table(name = "DryufJpaChild")
public class DryufJpaChild implements java.io.Serializable
{
	private static final long	serialVersionUID = 1L;

	@Embeddable
	public static class Pk implements java.io.Serializable
	{
		private static final long	serialVersionUID = 1L;

		@Column(name = "mainId")
		protected String		mainId;

		public String			getMainId()
		{
			return this.mainId;
		}

		public void			setMainId(String mainId)
		{
			this.mainId = mainId;
		}

		@Column(name = "childId")
		protected String		childId;

		public String			getChildId()
		{
			return this.childId;
		}

		public void			setChildId(String childId)
		{
			this.childId = childId;
		}

		@Override
		public int			hashCode()
		{
			return mainId.hashCode()*37+childId.hashCode()*37;
		}

		@Override
		public boolean			equals(Object o)
		{
			if (!(o instanceof Pk))
				return false;
			Pk s = (Pk)o;
			return s.mainId.equals(mainId) && childId.equals(childId);
		}
	}

	@EmbeddedId
	Pk				pk;

	public Pk			getPk()
	{
		return pk;
	}

	public void			setPk(Pk pk)
	{
		this.pk = pk;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "mainId", referencedColumnName = "mainId") })
	protected DryufJpaMain drufJpaMain;

	public DryufJpaMain getDryufJpaMain()
	{
		return drufJpaMain;
	}

	public void setDryufJpaMain(DryufJpaMain drufJpaMain)
	{
		this.drufJpaMain = drufJpaMain;
	}
}
