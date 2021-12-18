package com.ers.internship.rest;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.historization.HistorizedItem;

import junit.framework.Assert;

/**
 * 
 * @author Snejina Yanakieva
 *
 * @param <IdType> the type of the EntityType's ID
 * @param <EntityType> the type of the entity managed by the tested rest service
 */
public abstract class AbstractSearchingRestServiceTest<IdType, EntityType extends HistorizedItem<IdType>>
			extends AbstractCrudRestServiceTest<IdType, EntityType>{
	
	protected abstract String getRelativeLoadAllURI();
	
	protected abstract boolean isEqual(EntityType a, EntityType b);
	
	@Override
	protected abstract SearchingDao<IdType, EntityType> getDao();
	
	@Test
	public void loadAllTest() throws NoSuchFieldException, SecurityException {
		saveEntities(validEntities);
		
		client.replacePath(getRelativeLoadAllURI());
		Response response = client.get();
		
		Assert.assertEquals("Service returned status code " + response.getStatus()
				+ " when trying to get all entities!", Status.OK.getStatusCode(), response.getStatus());
		
		List<EntityType> actualEntities;
		
		try {
			actualEntities = response.readEntity(getListType());

			Assert.assertNotNull("Load all returned OK status code, but a null entity list!", 
					actualEntities);
			Assert.assertTrue("Expected list size" + validEntities.size() + " is bigger than the actual"
					+ actualEntities.size(), validEntities.size() <= actualEntities.size());
			
			for (EntityType expectedEntity : validEntities) {
				boolean expectedEntityIsReturned = false;
				
				for (EntityType actualEntity : actualEntities) {
					if (this.isEqual(expectedEntity, actualEntity)) {
						expectedEntityIsReturned = true;
						break;
					}
				}
				
				Assert.assertTrue("An expected entity is not returned by loadAll!", 
						expectedEntityIsReturned);
			}
		}
		catch(IllegalStateException exc) {
			Assert.fail("Could not convert the result from loadAll to "
					+ "a list of entities!");
		}
	}	
	
	private GenericType<List<EntityType>> getListType() {
		Type listType = null;
		
		try {
			Method generateValidEntitiesMethod = getClass().getDeclaredMethod("generateValidEntities", 
					new Class<?>[0]);
			listType = generateValidEntitiesMethod.getGenericReturnType();
		} catch (Exception e) {
			Assert.fail("Failed getting the generic type of List<EntityType> !");
		}
		
		return new GenericType<>(listType);
	}
	
	protected static boolean checkIfEqual(Object a, Object b) {
		if (a != null) {
			return a.equals(b);
		}
		else {
			return a == b;
		}
	}
}
