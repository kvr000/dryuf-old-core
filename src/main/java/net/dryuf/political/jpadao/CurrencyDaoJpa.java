package net.dryuf.political.jpadao;

import net.dryuf.political.Currency;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CurrencyDaoJpa extends net.dryuf.dao.DryufDaoContext<Currency, String> implements net.dryuf.political.dao.CurrencyDao
{

	public				CurrencyDaoJpa()
	{
		super(Currency.class);
	}

}
