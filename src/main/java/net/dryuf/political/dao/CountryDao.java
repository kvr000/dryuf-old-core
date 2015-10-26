package net.dryuf.political.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.political.Country;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface CountryDao extends net.dryuf.dao.DynamicDao<Country, String>
{
	public Country			refresh(Country obj);
	public Country			loadByPk(String pk);
	public List<Country>		listAll();
	public void			insert(Country obj);
	public void			insertTxNew(Country obj);
	public Country			update(Country obj);
	public void			remove(Country obj);
	public boolean			removeByPk(String pk);

	public String			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<Country> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<Country> holder);
	public Country			createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<Country>	retrieveDynamic(EntityHolder<?> composition, String pk);
	public Country			updateDynamic(EntityHolder<Country> roleObject, String pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, String pk);
	public long			listDynamic(List<EntityHolder<Country>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
