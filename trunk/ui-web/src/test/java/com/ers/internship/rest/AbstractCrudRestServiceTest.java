package com.ers.internship.rest;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.historization.HistorizedItem;

import junit.framework.Assert;

/**
 * 
 * @author Snejina Yanakieva
 *
 * @param <IdType> the type of the id of the entity
 * @param <EntityType> the type of the entity
 */
public abstract class AbstractCrudRestServiceTest<IdType, EntityType extends HistorizedItem<IdType>> 
					extends RestServiceTest {

	protected static final int TEST_ENTITIES_COUNT = 1;

	protected List<EntityType> validEntities;
	protected List<EntityType> invalidEntities;

	
	/**
	 * 
	 * @return the relative create URI starting with "/"
	 */
	protected abstract String getRelativeCreateURI();

	/**
	 * 
	 * @return the relative update URI starting with "/"
	 */
	protected abstract String getRelativeUpdateURI();

	/**
	 * 
	 * @return the relative loadById URI starting with "/"
	 */
	protected abstract String getRelativeLoadByIdURI(IdType id, Calendar atDate);

	/**
	 * 
	 * @return the relative deleteById URI starting with "/"
	 */
	protected abstract String getRelativeDeleteByIdURI(IdType id);

	/**
	 * 
	 * @return a list with generated valid entities. Should not return
	 * the same references each time
	 */
	protected abstract List<EntityType> generateValidEntities();

	/**
	 * 
	 * @return a list with generated invalid entities. Should not return
	 * the same references each time
	 */
	protected abstract List<EntityType> generateInvalidEntities();
	
	/**
	 * Asserts that two entities are equal.
	 * 
	 * @param message The message to be shown if the entities are NOT equal
	 * @param expected the expected entity
	 * @param actual the actual entity
	 */
	protected abstract void assertEquals(String message, EntityType expected, EntityType actual);
	
	/**
	 * Updates the entity's valid from/to properties and at least one
	 * additional property.
	 * @param entity the entity to be updated
	 */
	protected abstract void updateEntity(EntityType entity);


	/**
	 * 
	 * @return the entity class
	 */
	protected abstract Class<EntityType> getType();
	
	/**
	 * @return Returns the CrudDao the rest service works with.
	 */
	protected abstract CrudDao<IdType, EntityType> getDao();
	
	/**
	 * 
	 * @return an instance of the tested RestServiceImpl
	 */
	protected abstract Object getService();

	

	private void cleanupEntities() {
		CrudDao<IdType, EntityType> dao = getDao();

		for (EntityType validEntity : validEntities) {
			dao.delete(validEntity.getID());
		}

		for (EntityType invalidEntity : invalidEntities) {
			dao.delete(invalidEntity.getID());
		}
	}

	protected void saveEntities(List<EntityType> validEntities) {
		CrudDao<IdType, EntityType> dao = getDao();

		for (EntityType entity : validEntities) {
			dao.save(entity);
		}
	}

	@Before
	public void before() {
		validEntities = generateValidEntities();
		invalidEntities = generateInvalidEntities();
		cleanupEntities();
	}

	@After
	public void after() {
		cleanupEntities();
	}

	@Test
	public void createTest() {
		CrudDao<IdType, EntityType> dao = getDao();
		Response response;
		EntityType savedEntity;
		client.replacePath(getRelativeCreateURI());

		for (EntityType validEntity : validEntities) {
			Calendar validEntityDate = Calendar.getInstance();
			validEntityDate.setTimeInMillis(validEntity.getValidFrom().getTime());
			response = client.post(validEntity);
			Assert.assertEquals(
					"Received status code " + response.getStatus() + " when trying to create a valid entity!",
					Status.CREATED.getStatusCode(), response.getStatus());
			savedEntity = dao.loadById(validEntity.getID(), validEntityDate);
			Assert.assertNotNull("Service stated that the item is created but it was not!", savedEntity);
			this.assertEquals("The expected and the actually created entities are different!",
					validEntity, savedEntity);
		}

		for (EntityType invalidEntity : invalidEntities) {
			response = client.post(invalidEntity);
			Assert.assertEquals(
					"Received status code " + response.getStatus() 
					+ " when trying to create an invalid entity! Request uri : " + client.getCurrentURI(),
					Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		}
	}

	@Test
	public void updateTest() {		
		saveEntities(validEntities);
		
		client.replacePath(getRelativeUpdateURI());
		CrudDao<IdType, EntityType> dao = getDao();
		EntityType actual;
		Response response;
		Calendar updatedEntityDate = Calendar.getInstance();
		
		for (EntityType expected : validEntities) {
			updateEntity(expected);
			response = client.post(expected);
			
			Assert.assertEquals("Service could not update an entity! Received "
					+ "response code " + response.getStatus(),
					Status.OK.getStatusCode(), response.getStatus());
			
			updatedEntityDate.setTimeInMillis(expected.getValidFrom().getTime());
			actual = dao.loadById(expected.getID(), updatedEntityDate);
			
			Assert.assertNotNull("Service stated that the item was updated "
					+ "but it could not be found!", actual);
			this.assertEquals("Service stated that the item was updated but "
					+ "it does not match the new value", expected, actual);			
		}
	}

	@Test
	public void loadByIdTest() {
		saveEntities(validEntities);

		Response response;
		EntityType actualEntity;
		Calendar entityValidDate;

		for (EntityType expectedEntity : validEntities) {
			entityValidDate = Calendar.getInstance();
			entityValidDate.setTimeInMillis(expectedEntity.getValidFrom().getTime());
			
			String relativePath = getRelativeLoadByIdURI(expectedEntity.getID(), entityValidDate);
			client.replacePath(relativePath);
			response = client.get();

			Assert.assertEquals("Service could not load an existing entity! Received " +
					"response code " + response.getStatus(), Status.OK.getStatusCode(),
					response.getStatus());
			
			try {
				actualEntity = response.readEntity(getType());
				this.assertEquals("Service returned unexpected entity (loadById)!",
						expectedEntity, actualEntity);
			} catch (IllegalStateException | MessageProcessingException e) {
				Assert.fail("Received object in loadById could not be converted to a valid entity!");
			}

			
		}
	}
		
	@Test
	public void deleteByIdTest() {
		CrudDao<IdType, EntityType> dao = getDao();

		saveEntities(validEntities);

		Response response;
		Calendar expectedEntityDate;
		
		for (EntityType entity : validEntities) {
			expectedEntityDate = Calendar.getInstance();
			expectedEntityDate.setTimeInMillis(entity.getValidFrom().getTime());
			client.replacePath(getRelativeDeleteByIdURI(entity.getID()));
			response = client.delete();
			Assert.assertEquals("Service returned response code " + response.getStatus()
					+ " when trying to delete an existing entity!", Status.OK.getStatusCode(),
					response.getStatus());
			Assert.assertNull("Service returned " + response.getStatus()
					+ " but could not delete the existing entity!",
					dao.loadById(entity.getID(), expectedEntityDate));
		}
	}
}
