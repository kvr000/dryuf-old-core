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

package net.dryuf.dao.hsql;

import java.sql.SQLException;

import org.hsqldb.error.ErrorCode;

import net.dryuf.dao.DaoAccessException;
import net.dryuf.dao.DaoPrimaryKeyConstraintException;
import net.dryuf.dao.DaoUniqueConstraintException;
import net.dryuf.dao.DaoBadNullConstraintException;
import net.dryuf.dao.DaoForeignKeyConstraintException;


public class JpaExceptionTranslatorHsql extends java.lang.Object
	implements net.dryuf.dao.JpaExceptionTranslator
{
	public void			translateJpaException(RuntimeException ex)
	{
		RuntimeException newex = translateDaoExceptionIfPossible(ex);
		throw newex == null ? ex : newex;
	}

	public DaoAccessException	translateDaoExceptionIfPossible(RuntimeException ex)
	{
		for (Throwable cause = ex; cause != null; cause = cause.getCause()) {
			if (cause instanceof SQLException) {
				return getSqlException(ex, (SQLException)cause);
			}
		}
		return null;
	}

	public DaoAccessException	getSqlException(RuntimeException ex, SQLException sqlex)
	{
		int errorCode = sqlex.getErrorCode();
		switch (-errorCode) {
		case ErrorCode.CONSTRAINT:
		case ErrorCode.X_23000:
			return new DaoPrimaryKeyConstraintException(ex);

		case ErrorCode.X_23505:
			if (sqlex.getMessage().matches(".*; SYS_PK_\\w+ table: .*"))
				return new DaoPrimaryKeyConstraintException(ex);
			return new DaoUniqueConstraintException(ex);

		case ErrorCode.X_23502:
			return new DaoBadNullConstraintException(ex);

		case ErrorCode.X_23503 :
			return new DaoForeignKeyConstraintException(ex);
		}
		return null;
	}
}
