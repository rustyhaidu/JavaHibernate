package edu.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import edu.hibernate.model.Contact;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final SessionFactory sessionFactory = buildSessionFactory();
    public static void main( String[] args )
    {
        Contact contact =new Contact.ContactBuilder("Chris", "Ramocciotti")
        		.withEmail("rama@teamtreehouse.com")
        		.withPhone(777554423L)
        		.build();
        
		int id = save(contact);
		
		//Display a list of contacts
		System.out.println("%n%nBefore update%n%n");
		for (Contact c : fetchAllContacts()){
			System.out.println(c);
		}
		//fetchAllContacts().stream().forEach(System.out::println);
		
		// Get the persisted contact
		Contact c = findContactById(id);
		// Update the contact
		c.setFirstName("Beatrix");
		// Persist the changes
		System.out.println("%n%nUpdating... %n%n");
		update(c);
		System.out.println("%n%nUpdate Complete... %n%n");
		// Display a list of contacts after the update
		System.out.println("%n%nAfter update%n%n");
		fetchAllContacts().stream().forEach(System.out::println);
		
		// Get the contact with id of 1
        c = findContactById(1);

        // Delete the contact
        System.out.printf("%nDeleting...%n");
        delete(c);
        System.out.printf("%nDeleted!%n");
        System.out.printf("%nAfter delete%n");
        fetchAllContacts().stream().forEach(System.out::println);
		
    }    
  
    private static Contact findContactById(int id){
    	// Open a Session
    	Session session = sessionFactory.openSession();
    	// Retrieve the persistant object (or null if not found)
    	Contact contact =  session.get(Contact.class, id);
    	// Close the session
    	session.close();
    	return contact;
    }
    
    private static void update(Contact contact){
    	// Open a session
    	Session session = sessionFactory.openSession();
    	// Begin a transactino
    	session.beginTransaction();
    	// Use the session to update the contact
    	session.update(contact);
    	// commit the transaction
    	session.getTransaction().commit();
    	// close the session
    	session.close();    	
    }
    
    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts(){
    	//Open a session
    	Session session = sessionFactory.openSession();
		
		
		//Create Criteria Object
    	Criteria criteria = session.createCriteria(Contact.class);
    	
    	//Get a list of Contact objects according to the Criteria object
    	
		List<Contact> contacts = criteria.list();
    	//Close the Session
    	session.close();
    	return contacts;
    }
    
	public static int save(Contact contact) {
		//Open a session
        Session session = sessionFactory.openSession();
        //Begin a Transaction
        session.beginTransaction();
        //Use the session to save the contact
        int id = (Integer) session.save(contact);
       // System.out.println(session.save(contact).getClass().getName());
        //Commit the transaction
        session.getTransaction().commit();        
        //Close the session
        session.close();
        return id;
	}
	 private static void delete(Contact contact) {
	        // Open a session
	        Session session = sessionFactory.openSession();

	        // Begin a transaction
	        session.beginTransaction();

	        // Use the session to update the contact
	        session.delete(contact);

	        // Commit the transaction
	        session.getTransaction().commit();

	        // Close the session
	        session.close();
	}
	
	private static SessionFactory buildSessionFactory() {
		//create a standardServiceRegistry
		final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure("./resources/hibernate.cfg.xml").build();
		return new MetadataSources(registry).buildMetadata().buildSessionFactory();		
	}
}
