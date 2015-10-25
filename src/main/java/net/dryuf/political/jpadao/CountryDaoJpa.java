package net.dryuf.political.jpadao;

import net.dryuf.political.Country;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CountryDaoJpa extends net.dryuf.dao.DryufDaoContext<Country, String> implements net.dryuf.political.dao.CountryDao
{

	public				CountryDaoJpa()
	{
		super(Country.class);
	}

}
