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

package net.dryuf.dao.mysql;

import java.sql.SQLException;

import com.mysql.jdbc.MysqlErrorNumbers;

import net.dryuf.dao.DaoAccessException;
import net.dryuf.dao.DaoPrimaryKeyConstraintException;
import net.dryuf.dao.DaoUniqueConstraintException;
import net.dryuf.dao.DaoBadNullConstraintException;
import net.dryuf.dao.DaoForeignKeyConstraintException;


public class JpaExceptionTranslatorMysql extends java.lang.Object
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
		switch (sqlex.getErrorCode()) {
		case MysqlErrorNumbers.ER_DUP_KEY:
		case MysqlErrorNumbers.ER_DUP_ENTRY:
		case MysqlErrorNumbers.ER_DUP_ENTRY_WITH_KEY_NAME:
			return new DaoPrimaryKeyConstraintException(ex);

		case MysqlErrorNumbers.ER_DUP_ENTRY_AUTOINCREMENT_CASE:
		case MysqlErrorNumbers.ER_DUP_UNIQUE:
			return new DaoUniqueConstraintException(ex);

		case MysqlErrorNumbers.ER_BAD_NULL_ERROR:
			return new DaoBadNullConstraintException(ex);

		case MysqlErrorNumbers.ER_NO_REFERENCED_ROW:
		case MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2:
		case MysqlErrorNumbers.ER_ROW_IS_REFERENCED:
		case MysqlErrorNumbers.ER_ROW_IS_REFERENCED_2:
			return new DaoForeignKeyConstraintException(ex);
		}
		return null;
	}
}
