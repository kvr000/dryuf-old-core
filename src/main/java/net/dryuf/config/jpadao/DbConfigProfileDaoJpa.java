package net.dryuf.config.jpadao;

import net.dryuf.config.DbConfigProfile;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DbConfigProfileDaoJpa extends net.dryuf.dao.DryufDaoContext<DbConfigProfile, String> implements net.dryuf.config.dao.DbConfigProfileDao
{

	public				DbConfigProfileDaoJpa()
	{
		super(DbConfigProfile.class);
	}

}
